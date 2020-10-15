package menu.sistema;

import java.util.ArrayList;

import produtos.*;
import crud.*;
import menu.usuario.UsuarioAPI;
import menu.pergunta.PerguntasAPI;
import menu.pergunta.indices.*;
import menu.resposta.RespostaAPI;
import seguranca.GFG;

public class CrudAPI {

	// Path dos Cruds
	private final String            path = "Dados";

	//Banco de Dados

		//Usuarios
		private static ListaIDs         perguntasUsuario;
		private static Crud<Usuario>    usuarios;

		//Perguntas
    	private static Crud<Pergunta>   perguntas;
		private static ListaInvertida   listaDeChaves;

		//Respostas
		private static Crud<Resposta>   respostas;
		private static ListaIDs         respostasUsuario;
		private static ListaIDs 		perguntasRespostas;

	//GFG para hashear a senha
	public static GFG hasheador = new GFG(1000);
	
	/**
	 * 	Construtor 1: iniciar todos os bancos de dados que o programa precisa
	 */
    public CrudAPI() {
        
        try {
			//Usuarios
            usuarios           = new Crud<>("Usuarios",  Usuario.class.getConstructor());
			perguntasUsuario   = new ListaIDs(this.path + "/" + "perguntasUsuarioIDs");
			
			//Perguntas
			perguntas          = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
			listaDeChaves      = new ListaInvertida(10,"Dados/indice.dict","Dados/indice.dictBlock");
			
			//Respostas
			respostas          = new Crud<>("Respostas",  Resposta.class.getConstructor());
			respostasUsuario   = new ListaIDs(this.path + "/" + "respostasUsuarioIDs");
			perguntasRespostas = new ListaIDs(this.path + "/" + "perguntasRespostasIDs");

            
        } catch(Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver na tela de Acesso
	 * @param cdp é o Codigo referente a ação que o usuário quer
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo usuário
	 */
	public CelulaResposta verificarRequisicaoEmAcesso(CodigoDeProtocolo cdp ) {
		CelulaResposta cr = new CelulaResposta();

		switch(cdp) {

			case ACESSOAOSISTEMA: //Pedir para acessar o sistema
				cr = UsuarioAPI.acessarAoSistema();
				break;

			case CRIARNOVOUSUARIO: // Criar um novo usuário no banco de dados
				cr = UsuarioAPI.criarNovoUsuario();
				if(cr.getUsuario() != null) 
					inserirNovoUsuarioNoCrud(cr.getUsuario());

				break;

			case CRIARSENHATEMPORARIA: 
				cr = UsuarioAPI.criarSenhaTemporaria();
				if( cr.getUsuario() != null) 
					atualizarCredenciaisDoUsuario(cr.getUsuario());

				break;   
			default:
				break;                    

		}
		
		CodigoDeProtocolo.verificarCodigo(cr.getCdp());

		return cr;
	}

	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver logado no programa
	 * @param cdp é o Codigo referente a ação que o usuário quer
	 * @param idUsuario é o codigo do usuário que fez a requisição
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo usuário
	 */
	public CelulaResposta verificarRequisicaoDoUsuario(CodigoDeProtocolo cdp, int idUsuario) {
		CelulaResposta cr = new CelulaResposta();

		switch(cdp) {

			case CONSULTARPERGUNTAS: // Indo para a tela de consultar/responder perguntas
				cr = PerguntasAPI.consultarPerguntaPelaPalavraChave(idUsuario);
				if (cr.getCdp() == CodigoDeProtocolo.IRPARAPERGUNTA) {
					Usuario u = acharUsuario(cr.getPergunta().getIdUsuario());
					if(u != null)
						cr.setUsuario(u);
				}

				break;

			case OLHARNOTIFICACOES: // Verificar suas notificacoes
				System.out.println("Olha so as notificacoes");
				break;

			case NOVASENHA: 
				cr = UsuarioAPI.criarNovaSenha(idUsuario);
				if(cr.getUsuario() != null) 
					atualizarCredenciaisDoUsuario(cr.getUsuario());

				break;                       

			case LISTARPERGUNTAS: // Listando as perguntas do usuario atual
				cr = PerguntasAPI.listarPerguntas(idUsuario);
				break;

			case NOVAPERGUNTA: // Incluindo uma nova pergunta
				cr = PerguntasAPI.criarPergunta(idUsuario);
				if(cr.getPergunta() != null) 
					novaPergunta(cr.getPergunta(), idUsuario);

				break;

			case ALTERARPERGUNTA: // Alterando uma pergunta atual
				cr = PerguntasAPI.alterarPergunta(idUsuario);
				if(cr.getPergunta() != null) 
					atualizarPergunta(cr.getPergunta());	

				break;

			case ARQUIVARPERGUNTA: // Arquivando as perguntas
				cr = PerguntasAPI.arquivarPergunta(idUsuario);
				if(cr.getPergunta() != null) 
					desativarPergunta(cr.getPergunta());

				break;
			default:
				break;

		}

		CodigoDeProtocolo.verificarCodigo(cr.getCdp());

		return cr;
	}

	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver na tela de Consultar Pergunta
	 * @param cdp é o Codigo referente a ação que o usuário quer
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo usuário
	 */
	public CelulaResposta verificarRequisicaoEmPergunta(CodigoDeProtocolo cdp, int idPergunta,int idUsuario) {
		CelulaResposta cr = new CelulaResposta();

		switch(cdp) {

			case LISTARCOMENTARIOSGERAL:
				System.out.println("Comentarios...");
				break;

			case LISTARRESPOSTASGERAL:
				cr = RespostaAPI.listarRespostasDoGeral(idPergunta);
				break;

			case LISTARRESPOSTASUSUARIO: //Pedir para acessar o sistema
				cr = RespostaAPI.listarRespostasDoUsuario(idPergunta,idUsuario);
				break;

			case INCLUIRRESPOSTA: // Criar um novo usuário no banco de dados
				cr = RespostaAPI.criarResposta(idPergunta,idUsuario);
				if(cr.getResposta() != null) 
					novaResposta(cr.getResposta(), idPergunta);

				break;

			case ALTERARRESPOSTA: 
				cr = RespostaAPI.alterarResposta(idPergunta,idUsuario);
				if( cr.getResposta() != null) 
					atualizarResposta(cr.getResposta());

				break;    

			case ARQUIVARRESPOSTA: 
				cr = RespostaAPI.arquivarResposta(idPergunta,idUsuario);
				if( cr.getResposta() != null) 
					atualizarResposta(cr.getResposta());

				break;
				       
			default:
				break;              
		}
		
		CodigoDeProtocolo.verificarCodigo(cr.getCdp());

		return cr;
	}

	/*
	*	Funções do CRUD de usuário
	*/

	/**
	 * Função para inserir um novo usuário no CRUD
	 * @param u é o usuário que é para ser registrado
	 * @return um numero inteiro correspondendo ao ID do novo usuário
	 */
    public int inserirNovoUsuarioNoCrud(Usuario u) { return usuarios.create(u); }

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
	public void atualizarCredenciaisDoUsuario(Usuario u) { usuarios.update(u, u.getId()); }
	
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
		perguntasUsuario.create(idUsuario,resp);
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
		
		idsPerguntas = perguntasUsuario.read(idUsuario);

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
				idArray = listaDeChaves.read(palavrasChave[i]);

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
				listaDeChaves.create(s,p.getId());

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
				listaDeChaves.delete(s,p.getId());

		} catch(Exception e) {e.printStackTrace();}

	}

	//Respostas 
	/**
	 * Armazena uma nova resposta nos bancos de dados
	 * @param r é a resposta a ser armazenada
	 * @param idPergunta é a pergunta na qual a resposta corresponde
	 * @return
	 */
	public int novaResposta(Resposta r, int idPergunta) {
		int idResposta = -1;

		idResposta = respostas.create(r);
		r.setId(idResposta);

		respostasUsuario.create(r.getIdUsuario(),r.getId());
		perguntasRespostas.create(idPergunta,r.getId());

		return idResposta;
	}

	/**
	 * Função para achar uma resposta no banco de dados a partir da ID
	 * @param id que é a ID da resposta
	 * @return a resposta correspondente a ID (caso ela exista)
	 */
	public static Resposta acharResposta(int id) {
		Resposta r = null;
		try {
			r = respostas.read(id);
		} catch(Exception e ) { e.printStackTrace(); }
		return r;

	}

	/**
	 * Função para atualizar uma resposta
	 * @param r é a pergunta a ser atualizada
	 */
	public void atualizarResposta(Resposta r) { respostas.update(r,r.getId());}

	/**
	 * Função para pegar um array de respostas com base na Id de um usuário no banco de dados
	 * @param idPergunta da pergunta
	 * @param idUsuario do usuário que requisitou o processo
	 * @return array de respostas do idUsuario(se ele tiver registrado)
	 */
	public static Resposta[] getRespostaArrayUser(int idUsuario) {
		Resposta[]   resp  		     = null;
		int[] 	     idsRespostas    = null;
		
		idsRespostas = respostasUsuario.read(idUsuario);

        if(idsRespostas == null)
			return  null;

		resp = new Resposta[idsRespostas.length];

		int contador = 0;
        for (int i : idsRespostas) {
            try {
                Resposta temp = respostas.read(i);
                if(temp == null)
					continue;

				resp[contador] = temp; 
				contador++;
            }catch(Exception e) { e.printStackTrace(); }
		}

		return resp;
	}

	/**
	 * Função para pegar um array de respostas com base na Id de uma pergunta no banco de dados
	 * @param idPergunta da pergunta
	 * @return array de respostas do idUsuario(se ele tiver registrado)
	 */
	public static Resposta[] getRespostaArrayGeral(int idPergunta) {
		Resposta[]   resp  		     = null;
		int[] 	     idsRespostas    = null;
		
		idsRespostas = perguntasRespostas.read(idPergunta);

        if(idsRespostas == null)
			return  null;

		resp = new Resposta[idsRespostas.length];

		int contador = 0;
        for (int i : idsRespostas) {
            try {
                Resposta temp = respostas.read(i);
                if(temp == null)
					continue;
				if(temp.getIdPergunta() != idPergunta) {
					System.out.println("ue");
				}

				resp[contador] = temp; 
				contador++;
            }catch(Exception e) { e.printStackTrace(); }
		}

		return resp;
	}
}
