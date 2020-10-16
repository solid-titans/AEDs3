package menu.sistema.controle;

import produtos.*;
import seguranca.GFG;

import java.util.ArrayList;

import crud.*;
import menu.pergunta.indices.*;

public class CrudManager {


	//GFG para hashear a senha
	public GFG hasheador;
	
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

	public CrudManager() {

		this.hasheador = new GFG(1000);

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
    public Usuario acharUsuario(String email) {
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
    public Usuario acharUsuario(int idUsuario) {
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
	public Pergunta acharPergunta(int id) {
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
	public Pergunta[] getPerguntaArray(int idUsuario) {
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
	public Pergunta[] getPerguntasPalavrasChave(String[] palavrasChave,int idUsuario) {
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
