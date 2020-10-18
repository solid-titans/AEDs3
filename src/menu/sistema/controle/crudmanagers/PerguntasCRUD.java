package menu.sistema.controle.crudmanagers;

import java.util.ArrayList;

import crud.Crud;
import menu.pergunta.indices.ListaIDs;
import menu.pergunta.indices.ListaInvertida;
import menu.sistema.abstracts.api.crudmanagers.PerguntaInferface;
import produtos.Pergunta;

public class PerguntasCRUD implements PerguntaInferface {
    
	// Path dos Cruds
    private final String            path;
    
    private static Crud<Pergunta>   perguntas;
    private static ListaInvertida   listaDeChaves;
    private static ListaIDs         perguntasUsuario;

	public PerguntasCRUD(String path) {

		this.path = path;
		try {
			//Usuarios
			perguntasUsuario   = new ListaIDs(this.path + "/" + "perguntasUsuarioIDs");
			
			//Perguntas
			perguntas          = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
			listaDeChaves      = new ListaInvertida(10,"Dados/indice.dict","Dados/indice.dictBlock");
            
        } catch(Exception e) { e.printStackTrace(); }
	}


	/** 
	 * Função para adicionar uma nova pergunta ao usuário
	 * @param p         Objeto que contem a pergunta feita pelo usuario
	 * @param IdUsuario Id do usuario que fez a pergunta
	 * @return          Retornar a id da pergunta que foi registrada
	 */
	public int inserir(Pergunta pergunta, int idUsuario) {
		int resp = -1;

		resp = perguntas.create(pergunta);
		pergunta.setId(resp);
		perguntasUsuario.create(idUsuario,resp);
		inserirPalavrasChave(pergunta);
		return resp;
	}

	/**
	 * Função para achar uma pergunta no banco de dados a partir da ID
	 * @param id que é a ID da pergunta
	 * @return a pergunta correspondente a ID (caso ela exista)
	 */
	public Pergunta achar(int id) {
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
	public void atualizar(Pergunta novo) {
		Pergunta antiga = achar(novo.getId());

		removerPalavrasChave(antiga);
		inserirPalavrasChave(novo);

		perguntas.update(novo,novo.getId());
	}

	/**
	 * Função para desativar uma pergunta
	 * @param pergunta é a pergunta a ser desativada
	 */
	public void desativar(Pergunta pergunta) {
		removerPalavrasChave(pergunta);
		perguntas.update(pergunta,pergunta.getId());

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
	 * @param pergunta é a Pergunta de onde será retirada as palavras-chave
	 */
	private void inserirPalavrasChave(Pergunta pergunta) {
		String[] palavras_chave = pergunta.getPalavrasChave().split(" ");

		try {
			for ( String s : palavras_chave )
				listaDeChaves.create(s,pergunta.getId());

		} catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Função para remover todas as palavras-chave da pergunta registrada na lista invertida
	 * @param p é a Pergunta de onde será retirada as palavras-chave
	 */
	private void removerPalavrasChave(Pergunta pergunta) {
		String[] palavras_chave = pergunta.getPalavrasChave().split(" ");

		try {
			for ( String s : palavras_chave) 
				listaDeChaves.delete(s,pergunta.getId());

		} catch(Exception e) {e.printStackTrace();}

    }
    
}
