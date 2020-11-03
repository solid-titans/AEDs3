package menu.backend.cruds;

import crud.Crud;
import menu.backend.cruds.abstracts.RespostasInterface;
import menu.backend.listas.ListaIDs;
import produtos.Resposta;

public class RespostasCRUD implements RespostasInterface {

	// Path dos Cruds
	private final String path;

	private static Crud<Resposta> respostas;
	private static ListaIDs respostasUsuario;
	private static ListaIDs perguntasRespostas;

	public RespostasCRUD(String path) {

		this.path = path;
		try {

			respostas = new Crud<>("Respostas", Resposta.class.getConstructor());
			respostasUsuario = new ListaIDs(this.path + "/" + "respostasUsuarioIDs");
			perguntasRespostas = new ListaIDs(this.path + "/" + "perguntasRespostasIDs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Armazena uma nova resposta nos bancos de dados
	 * 
	 * @param r          é a resposta a ser armazenada
	 * @param idPergunta é a pergunta na qual a resposta corresponde
	 * @return
	 */
	public int inserir(Resposta resposta, int idPergunta) {

		int idResposta = -1;

		idResposta = respostas.create(resposta);
		resposta.setId(idResposta);

		respostasUsuario.create(resposta.getIdUsuario(), resposta.getId());
		perguntasRespostas.create(idPergunta, resposta.getId());

		return idResposta;
	}

	/**
	 * Função para achar uma resposta no banco de dados a partir da ID
	 * 
	 * @param id que é a ID da resposta
	 * @return a resposta correspondente a ID (caso ela exista)
	 */
	public Resposta achar(int id) {

		Resposta r = null;
		try {
			r = respostas.read(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;

	}

	/**
	 * Função para atualizar uma resposta
	 * 
	 * @param r é a pergunta a ser atualizada
	 */
	public void atualizar(Resposta resposta) {
		respostas.update(resposta, resposta.getId());
	}

	public void atualizar(Resposta resposta,boolean nota) {
		short notaResposta = resposta.getNota();

		if(nota == true) {
			notaResposta++;
		}
		else {
			notaResposta--;
		}
		resposta.setNota(notaResposta);
		respostas.update(resposta, resposta.getId());
	}

	/**
	 * Deletar uma resposta do banco de dados
	 * @param resposta é a resposta a ser deletada
	 * @param idPergunta é o id da pergunta associado a essa resposta
	 */
	public void deletar(Resposta resposta, int idPergunta) {
		respostas.update(resposta, resposta.getId());
		respostasUsuario.delete(resposta.getIdUsuario(), resposta.getId());
		perguntasRespostas.delete(idPergunta, resposta.getId());
	}

	/**
	 * Função para pegar um array de respostas com base na Id de um usuário no banco
	 * de dados
	 * 
	 * @param idPergunta da pergunta
	 * @param idUsuario  do usuário que requisitou o processo
	 * @return array de respostas do idUsuario(se ele tiver registrado)
	 */
	public Resposta[] getRespostaArrayUser(int idUsuario) {

		Resposta[] resp = null;
		int[] idsRespostas = null;

		idsRespostas = respostasUsuario.read(idUsuario);

		if (idsRespostas == null)
			return null;

		resp = new Resposta[idsRespostas.length];

		int contador = 0;
		for (int i : idsRespostas) {
			try {
				Resposta temp = respostas.read(i);
				if (temp == null)
					continue;

				resp[contador] = temp;
				contador++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resp;
	}

	/**
	 * Função para pegar um array de respostas com base na Id de uma pergunta no
	 * banco de dados
	 * 
	 * @param idPergunta da pergunta
	 * @return array de respostas do idUsuario(se ele tiver registrado)
	 */
	public Resposta[] getRespostaArrayGeral(int idPergunta) {

		Resposta[] resp = null;
		int[] idsRespostas = null;

		idsRespostas = perguntasRespostas.read(idPergunta);

		if (idsRespostas == null)
			return null;

		resp = new Resposta[idsRespostas.length];

		int contador = 0;
		for (int i : idsRespostas) {
			try {
				Resposta temp = respostas.read(i);
				if (temp == null)
					continue;

				resp[contador] = temp;
				contador++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resp;
	}
	
}
