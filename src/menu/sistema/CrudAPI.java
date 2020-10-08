package menu.sistema;

import java.util.ArrayList;

import produtos.*;
import crud.*;
import menu.usuario.UsuarioAPI;
import menu.Menu;
import menu.pergunta.PerguntasAPI;
import menu.pergunta.indices.*;

public class CrudAPI {

	// Path dos Cruds
	private final String path = "Dados";

	//Cruds
    private static ListaIDs       ids;
    private static Crud<Pergunta> perguntas;
    private static Crud<Usuario>  usuarios;
    private static ListaInvertida lista;
    
    public CrudAPI() {
        
        try {
            usuarios   = new Crud<>("Usuarios",  Usuario.class.getConstructor());
            ids        = new ListaIDs(this.path + "/" + "IDs");
			perguntas  = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
            lista 	   = new ListaInvertida(10,"Dados/indice.dict","Dados/indice.dictBlock");
            
        } catch(Exception e) { e.printStackTrace(); }
	}
	
	public CodigoDeProtocolo verificarRequisicaoEmAcesso(CodigoDeProtocolo cdp ) {

		CodigoDeProtocolo resultado = CodigoDeProtocolo.ERRO;
		int id 					  = -1;
		Usuario tmp			      = null;

		switch(cdp) {

			case ACESSOAOSISTEMA: // Indo para a tela de consultar/responder perguntas
				id = UsuarioAPI.acessarAoSistema();
				if(id != -1) {
					Menu.setId(id);
					resultado = CodigoDeProtocolo.MUDARUSUARIO;
				}

				break;

			case CRIARNOVOUSUARIO: // Verificar suas notificacoes
				tmp = UsuarioAPI.criarNovoUsuario();
				if(tmp != null) {
					resultado = CodigoDeProtocolo.SUCESSO;
					inserirNovoUsuarioNoCrud(tmp);
				}
				break;

			case CRIARSENHATEMPORARIA: 
				tmp = UsuarioAPI.criarSenhaTemporaria();
				if( tmp != null) {
					atualizarCredenciaisDoUsuario(tmp);
					resultado = CodigoDeProtocolo.SUCESSO;
				}

				break;                       

			default:
				System.err.println("Erro! Entrada inválida, tente novamente.");
				break;
			}

		return resultado;
	}

	public CodigoDeProtocolo verificarRequisicaoDoUsuario(CodigoDeProtocolo cdp, int idUsuario) {

		CodigoDeProtocolo resultado = CodigoDeProtocolo.ERRO;
		Usuario usuarioAtual 	  = null;
		Pergunta pergunta         = null;

		switch(cdp) {

			case CONSULTARPERGUNTAS: // Indo para a tela de consultar/responder perguntas
				resultado = PerguntasAPI.consultarPerguntas(idUsuario);
				break;

			case OLHARNOTIFICACOES: // Verificar suas notificacoes
				System.out.println("Olha so as notificacoes");
				break;

			case NOVASENHA: 
				usuarioAtual = UsuarioAPI.criarNovaSenha(idUsuario);
				if(usuarioAtual != null) {
					atualizarCredenciaisDoUsuario(usuarioAtual);
					resultado = CodigoDeProtocolo.SUCESSO;
				}

				break;                       

			case LISTARPERGUNTAS: // Listando as perguntas do usuario atual
				resultado = PerguntasAPI.listarPerguntas(idUsuario);
				break;

			case NOVAPERGUNTA: // Incluindo uma nova pergunta
				pergunta = PerguntasAPI.criarPergunta(idUsuario);
				if(pergunta != null) {
					novaPergunta(pergunta, idUsuario);
					resultado = CodigoDeProtocolo.SUCESSO;
				}
				break;

			case ALTERARPERGUNTA: // Alterando uma pergunta atual
				pergunta = PerguntasAPI.alterarPergunta(idUsuario);
				if(pergunta != null) {
					atualizarPergunta(pergunta);
					resultado = CodigoDeProtocolo.SUCESSO;
				}

				break;

			case ARQUIVARPERGUNTA: // Arquivando as perguntas
				pergunta = PerguntasAPI.arquivarPergunta(idUsuario);
				if(pergunta != null) {
					desativarPergunta(pergunta);
					resultado = CodigoDeProtocolo.SUCESSO;
				}

				break;

			//Operacao Invalida

			default:
				System.err.println("Erro! Entrada inválida, tente novamente.");
				break;
			}

		return resultado;
	}

    public int inserirNovoUsuarioNoCrud(Usuario u) {

        return usuarios.create(u);
    }

    public static Usuario acharUsuario(String email) {

        Usuario resp = null;

        try {
            resp = usuarios.read(email);
        } catch(Exception e) {}

        return resp;
    }

    public static Usuario acharUsuario(int idUsuario) {

        Usuario resp = null;

        try {
            resp = usuarios.read(idUsuario);
        } catch(Exception e) {e.printStackTrace();}

        return resp;
    }

    public void atualizarCredenciaisDoUsuario(Usuario u) {
        usuarios.update(u, u.getId());  
    }
	//Encontrar uma pergunta no CRUD a partir do ID 
	public static Pergunta acharPergunta(int id) {

		Pergunta p = null;
		try {
			p = perguntas.read(id);
		} catch(Exception e ) { e.printStackTrace(); }
		return p;
	}
	
	/** Adicionar uma nova pergunta ao usuário
	 * 
	 * @param p         Objeto que contem a pergunta feita pelo usuario
	 * @param IdUsuario Id do usuario que fez a pergunta
	 * @return          Retornar a id do objeto no banco de dados
	 */
	public int novaPergunta(Pergunta p, int idUsuario) {
		int resp = -1;

		resp = perguntas.create(p);
		ids.create(idUsuario,resp);
		try {
			lista.create(p.getPalavrasChave(),resp);

		} catch(Exception e) {e.printStackTrace();}
		return resp;
	}

	//Atualizar alguma pergunta
	public void atualizarPergunta(Pergunta novo) {

		Pergunta antiga = acharPergunta(novo.getId());
		String[] split = antiga.getPalavrasChave().split(" ");

		try {
			for (byte i = 0;i < split.length; i++) {
				lista.delete(split[i],novo.getId());
			}

		} catch(Exception e) {e.printStackTrace();}

		split = novo.getPalavrasChave().split(" ");

		try {
			for (byte i = 0;i < split.length; i++) {
				lista.create(split[i],novo.getId());
			}

		} catch(Exception e) {e.printStackTrace();}
		perguntas.update(novo,novo.getId());
	}

	public void desativarPergunta(Pergunta p) {

		String[] split = p.getPalavrasChave().split(" ");

		try {
			for (byte i = 0;i < split.length; i++) {
				lista.delete(split[i],p.getId());
			}

		} catch(Exception e) {e.printStackTrace();}
		perguntas.update(p,p.getId());
	}

	//Armazenar as perguntas do usuário em um array e voltar
	/*
	*	A partir do ID Usuario, o programa irá acessar o CRUD
	*	de IDs e pegar a String de IDs de perguntas que o usuário
	*	tem. se o resultado der positivo, então ele irá pegar o 
	*	resultado do 'Split', a partir do '-' para pesquisar
	*	dentro do CRUD de perguntas as perguntas do usuario
	*/
	public static Pergunta[] getPerguntaArray(int idUsuario) {
		    
		Pergunta[] resp  = null;
		int[] idsPerguntas = ids.read(idUsuario);

        if(idsPerguntas == null)
			return  null;
		else {
			resp = new Pergunta[idsPerguntas.length];
		}

		int contador = 0;
        for (int i : idsPerguntas) {
            try {
                Pergunta temp = perguntas.read(i);
                if(temp == null)
					continue;

                resp[contador] = temp; 
                contador++;
            }catch(Exception e) { e.printStackTrace(); }
        }


		return resp;
	}

	public void inserirPalavrasChave(String palavrasChave, int idPergunta) {

		String[] split = palavrasChave.split(" ");

		try {
			for(String i : split) {
				lista.create(i,idPergunta);
			}

		}
		catch(Exception e) {e.printStackTrace();}

	}

	public static Pergunta[] getPerguntasPalavrasChave(String[] palavrasChave,int idUsuario) {

		ArrayList<Pergunta> array = new ArrayList<>();
		Pergunta[] resp 		  = null;

		int[] idArray = null;

		try { 
			for (byte i = 0; i < palavrasChave.length; i++) {
				idArray = lista.read(palavrasChave[i]);

				for (byte j = 0; j < idArray.length; j++) {
	
					Pergunta tmp = perguntas.read(idArray[j]);

					if(tmp.getIdUsuario() != idUsuario)
						array.add(perguntas.read(idArray[j]));
				}
			}
		} catch(Exception e) {e.printStackTrace();}

		resp = new Pergunta[array.size()];

		for (byte i = 0; i < array.size(); i++) {
			resp[i] = array.get(i);
		}

		return resp;
	}

}
