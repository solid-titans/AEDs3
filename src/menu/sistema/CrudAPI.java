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
	private final String            path = "Dados";

	//Cruds
    private static ListaIDs         ids;
    private static Crud<Pergunta>   perguntas;
    private static Crud<Usuario>    usuarios;
	private static ListaInvertida   lista;
	
	//Codigo de protocolo geral
	public static CodigoDeProtocolo resultado = CodigoDeProtocolo.NULL;
	
	/**
	 * 	Construtor 1: iniciar todos os bancos de dados que o programa precisa
	 */
    public CrudAPI() {
        
        try {
            usuarios   = new Crud<>("Usuarios",  Usuario.class.getConstructor());
            ids        = new ListaIDs(this.path + "/" + "IDs");
			perguntas  = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
            lista 	   = new ListaInvertida(10,"Dados/indice.dict","Dados/indice.dictBlock");
            
        } catch(Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver na tela de Acesso
	 * @param cdp é o Codigo referente a ação que o usuário quer
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo usuário
	 */
	public CodigoDeProtocolo verificarRequisicaoEmAcesso(CodigoDeProtocolo cdp ) {
		int                id			= -1;
		Usuario            tmp			= null;

		resultado 	= CodigoDeProtocolo.ERRO;

		switch(cdp) {

			case ACESSOAOSISTEMA: //Pedir para acessar o sistema
				id = UsuarioAPI.acessarAoSistema();
				if(id != -1) {
					Menu.setId(id);
					resultado = CodigoDeProtocolo.MUDARUSUARIO;
				}

				break;

			case CRIARNOVOUSUARIO: // Criar um novo usuário no banco de dados
				tmp = UsuarioAPI.criarNovoUsuario();
				if(tmp != null) {
					inserirNovoUsuarioNoCrud(tmp);
					resultado = CodigoDeProtocolo.SUCESSO;
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
		
		CodigoDeProtocolo.verificarCodigo(resultado);

		return resultado;
	}

	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver logado no programa
	 * @param cdp é o Codigo referente a ação que o usuário quer
	 * @param idUsuario é o codigo do usuário que fez a requisição
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo usuário
	 */
	public CodigoDeProtocolo verificarRequisicaoDoUsuario(CodigoDeProtocolo cdp, int idUsuario) {
		Usuario           usuarioAtual 	 = null;
		Pergunta          pergunta       = null;

		resultado 	 = CodigoDeProtocolo.ERRO;

		switch(cdp) {

			case CONSULTARPERGUNTAS: // Indo para a tela de consultar/responder perguntas
				PerguntasAPI.consultarPerguntas(idUsuario);
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

		CodigoDeProtocolo.verificarCodigo(resultado);

		return resultado;
	}

	/*
	*	Funções do CRUD de usuário
	*/

	/**
	 * Função para inserir um novo usuário no CRUD
	 * @param u é o usuário que é para ser registrado
	 * @return um numero inteiro correspondendo ao ID do novo usuário
	 */
    public int inserirNovoUsuarioNoCrud(Usuario u) {
		return usuarios.create(u);	
    }

	/**
	 * Função para encontrar um usuário a partir do email
	 * @param email é a String contendo o email
	 * @return o usuário caso ele foi encontrado
	 */
    public static Usuario acharUsuario(String email) {

        Usuario resp = null;

        try {
			resp = usuarios.read(email);
			
		} catch(Exception e) {}

        return resp;
    }

	/**
	 * Função para encontrar um usuário a partir do ID
	 * @param idUsuario é o numero inteiro que corresponde ao ID do usuário a ser procurado
	 * @return o usuário caso ele foi encontrado
	 */
    public static Usuario acharUsuario(int idUsuario) {

        Usuario resp = null;

        try {
			resp = usuarios.read(idUsuario);
			
        } catch(Exception e) {e.printStackTrace();}

        return resp;
    }

	/**
	 * Atualizar as credenciais de um usuário no CRUD
	 * @param u é o usuário a ser atualizado
	 */
    public void atualizarCredenciaisDoUsuario(Usuario u) {
        usuarios.update(u, u.getId());  
	}
	
	/*
	*	Funções do CRUD de pergunta
	*/

	/** 
	 * Função para adicionar uma nova pergunta ao usuário
	 * @param p         Objeto que contem a pergunta feita pelo usuario
	 * @param IdUsuario Id do usuario que fez a pergunta
	 * @return          Retornar a id da pergunta que foi registrada
	 */
	public int novaPergunta(Pergunta p, int idUsuario) {
		int resp = -1;

		resp = perguntas.create(p);
		p.setId(resp);
		ids.create(idUsuario,resp);
		inserirPalavrasChave(p);
		return resp;
	}

	/**
	 * Função para achar uma pergunta no banco de dados a partir da ID
	 * @param id que é a ID da pergunta
	 * @return a pergunta correspondente a ID (caso ela exista)
	 */
	public static Pergunta acharPergunta(int id) {

		Pergunta p = null;
		try {
			p = perguntas.read(id);
		} catch(Exception e ) { e.printStackTrace(); }
		return p;
	}

	/**
	 * Função para atualizar uma pergunta no banco de dados
	 * @param novo é a pergunta que será atualizada
	 */
	public void atualizarPergunta(Pergunta novo) {

		Pergunta antiga = acharPergunta(novo.getId());

		removerPalavrasChave(antiga);
		inserirPalavrasChave(novo);

		perguntas.update(novo,novo.getId());
	}

	/**
	 * Função para desativar uma pergunta
	 * @param p é a pergunta a ser desativada
	 */
	public void desativarPergunta(Pergunta p) {

		removerPalavrasChave(p);
		perguntas.update(p,p.getId());
	}

	/**
	 * Função para pegar um array de perguntas com base na Id de um usuário no banco de dados
	 * @param idUsuario que for mandada
	 * @return array de perguntas do idUsuario(se ele tiver registrado)
	 */
	public static Pergunta[] getPerguntaArray(int idUsuario) {
		    
		Pergunta[]   resp  		     = null;
		int[] 	     idsPerguntas    = null;
		
		idsPerguntas = ids.read(idUsuario);

        if(idsPerguntas == null)
			return  null;
		
		resp = new Pergunta[idsPerguntas.length];

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

	/**
	 * Função para recuperar as perguntas com base nas palavras-chave recebida
	 * @param palavrasChave que deverá ser usada como base para recuperar as palavras-chave
	 * @param idUsuario que é a ID do usuário que fez o pedido para pesquisar
	 * @return um array de perguntas com as palavrasChave enviada
	 */
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

	/**
	 * Função para inserir as palavras-chave da pergunta P 
	 * @param p é a Pergunta de onde será retirada as palavras-chave
	 */
	public void inserirPalavrasChave(Pergunta p) {
	
		String[] palavras_chave = p.getPalavrasChave().split(" ");

		try {
			for ( String s : palavras_chave )
				lista.create(s,p.getId());

		} catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Função para remover todas as palavras-chave da pergunta registrada na lista invertida
	 * @param p é a Pergunta de onde será retirada as palavras-chave
	 */
	public void removerPalavrasChave(Pergunta p) {
		String[] palavras_chave = p.getPalavrasChave().split(" ");

		try {
			for ( String s : palavras_chave) 
				lista.delete(s,p.getId());

		} catch(Exception e) {e.printStackTrace();}

	}

}
