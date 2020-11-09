package menu.api;

import menu.backend.cruds.*;
import menu.backend.misc.CodigoDeProtocolo;
import produtos.CelulaResposta;

public class APIControle {

	UsuariosAPI    usuariosAPI;
	PerguntasAPI   perguntasAPI;
	RespostasAPI   respostasAPI;
	VotosAPI       votosAPI;
	ComentariosAPI comentariosAPI;

	UsuariosCRUD    usuariosCRUD;
	PerguntasCRUD   perguntasCRUD;
	RespostasCRUD   respostasCRUD;
	VotosCRUD       votosCRUD;
	ComentariosCRUD comentariosCRUD;

	public APIControle(UsuariosAPI usuariosAPI, PerguntasAPI perguntasAPI, RespostasAPI respostasAPI, VotosAPI votosAPI, ComentariosAPI comentariosAPI,
			UsuariosCRUD usuariosCRUD, PerguntasCRUD perguntasCRUD, RespostasCRUD respostasCRUD, VotosCRUD votosCRUD, ComentariosCRUD comentariosCRUD) {

		this.usuariosAPI  = usuariosAPI;
		this.perguntasAPI = perguntasAPI;
		this.respostasAPI = respostasAPI;
		this.votosAPI     = votosAPI;
		this.comentariosAPI = comentariosAPI;

		this.usuariosCRUD  = usuariosCRUD;
		this.perguntasCRUD = perguntasCRUD;
		this.respostasCRUD = respostasCRUD;
		this.votosCRUD	   = votosCRUD;
		this.comentariosCRUD = comentariosCRUD;
	}

	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver na tela de
	 * Acesso
	 * 
	 * @param cdp é o Codigo referente a ação que o usuário quer
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo
	 *         usuário
	 */
	public CelulaResposta requisicaoEmAcesso(CodigoDeProtocolo cdp) {
		CelulaResposta cr  = new CelulaResposta();

		String nomeUsuario = "";

		switch (cdp) {

			case ACESSOAOSISTEMA: // Pedir para acessar o sistema
				cr = usuariosAPI.acessarAoSistema(usuariosCRUD);
				if(cr.getCdp() == CodigoDeProtocolo.MUDARUSUARIO) 
					nomeUsuario = usuariosCRUD.achar(cr.getUsuario().getId()).getNome(); //Recuperar o nome do usuário

				break;

			case CRIARNOVOUSUARIO: // Criar um novo usuário no banco de dados
				cr = usuariosAPI.criarNovoUsuario(usuariosCRUD);
				if (cr.getUsuario() != null && cr.getCdp() != CodigoDeProtocolo.OPERACAOCANCELADA)
					usuariosCRUD.inserir(cr.getUsuario());

				break;

			case CRIARSENHATEMPORARIA:
				cr = usuariosAPI.criarSenhaTemporaria(usuariosCRUD);
				if (cr.getUsuario() != null)
					usuariosCRUD.atualizar(cr.getUsuario());

				break;
			default:
				break;

		}

		CodigoDeProtocolo.verificarCodigo(cr.getCdp(),nomeUsuario);

		return cr;
	}

	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver logado no
	 * programa
	 * 
	 * @param cdp       é o Codigo referente a ação que o usuário quer
	 * @param idUsuario é o codigo do usuário que fez a requisição
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo
	 *         usuário
	 */
	public CelulaResposta requisicaoEmInicio(CodigoDeProtocolo cdp, int idUsuario) {
		CelulaResposta cr = new CelulaResposta();

		switch (cdp) {

			case CONSULTARPERGUNTAS: // Indo para a tela de consultar/responder perguntas
				cr = perguntasAPI.consultarPergunta(perguntasCRUD, idUsuario);
				if (cr.getCdp() == CodigoDeProtocolo.IRPARAPERGUNTA) {
					cr.setUsuario(usuariosCRUD.achar(cr.getPergunta().getIdUsuario()));
				}

				break;

			case OLHARNOTIFICACOES: // Verificar suas notificacoes
				System.out.println("Olha so as notificacoes");
				break;

			case NOVASENHA:
				cr = usuariosAPI.criarNovaSenha(usuariosCRUD, idUsuario);
				if (cr.getUsuario() != null)
					usuariosCRUD.atualizar(cr.getUsuario());

				break;

			case LISTARPERGUNTAS: // Listando as perguntas do usuario atual
				cr = perguntasAPI.listarPerguntas(perguntasCRUD, idUsuario);
				break;

			case NOVAPERGUNTA: // Incluindo uma nova pergunta
				cr = perguntasAPI.criarPergunta(idUsuario);
				if (cr.getPergunta() != null)
					perguntasCRUD.inserir(cr.getPergunta(), idUsuario);

				break;

			case ALTERARPERGUNTA: // Alterando uma pergunta atual
				cr = perguntasAPI.alterarPergunta(perguntasCRUD, idUsuario);
				if (cr.getPergunta() != null)
					perguntasCRUD.atualizar(cr.getPergunta());

				break;

			case ARQUIVARPERGUNTA: // Arquivando as perguntas
				cr = perguntasAPI.arquivarPergunta(perguntasCRUD, idUsuario);
				if (cr.getPergunta() != null)
					perguntasCRUD.desativar(cr.getPergunta());

				break;
			default:
				break;

		}

		CodigoDeProtocolo.verificarCodigo(cr.getCdp());

		return cr;
	}

	/**
	 * Função para verificar o pedido do usuário enquanto ele estiver na tela de
	 * Consultar Pergunta
	 * 
	 * @param cdp é o Codigo referente a ação que o usuário quer
	 * @return é o Codigo correspondente ao resultado da operação realizada pelo
	 *         usuário
	 */
	public CelulaResposta requisicaoEmPerguntas(CodigoDeProtocolo cdp, int idPergunta, int idUsuario) {
		CelulaResposta cr = new CelulaResposta();

		switch (cdp) {

			case COMENTARIOSPERGUNTA:
			//System.out.println("Deu certo!");
				cr = comentariosAPI.listarComentariosDaPergunta(comentariosCRUD,idPergunta, idUsuario);
				break;

			case LISTARRESPOSTASGERAL:
				cr = respostasAPI.listarRespostasDoGeral(usuariosCRUD,respostasCRUD,votosCRUD,idPergunta);
				break;

			case LISTARRESPOSTASUSUARIO: // Pedir para acessar o sistema
				cr = respostasAPI.listarRespostasDoUsuario(respostasCRUD,idPergunta, idUsuario);
				break;

			case INCLUIRRESPOSTA: // Criar um novo usuário no banco de dados
				cr = respostasAPI.criarResposta(idPergunta, idUsuario);
				if (cr.getResposta() != null)
					respostasCRUD.inserir(cr.getResposta(), idPergunta);

				break;

			case ALTERARRESPOSTA:
				cr = respostasAPI.alterarResposta(respostasCRUD,idPergunta, idUsuario);
				if (cr.getResposta() != null)
					respostasCRUD.atualizar(cr.getResposta());

				break;

			case ARQUIVARRESPOSTA:
				cr = respostasAPI.arquivarResposta(respostasCRUD,idPergunta, idUsuario);
				if (cr.getResposta() != null)
					respostasCRUD.deletar(cr.getResposta(),idPergunta);

				break;

			case VOTAREMPERGUNTA:
				cr = votosAPI.votarPR(votosCRUD,idPergunta,idUsuario,false);
				if (cr.getCdp() == CodigoDeProtocolo.SUCESSO) {
					votosCRUD.inserir(cr.getVoto());
					perguntasCRUD.atualizar(perguntasCRUD.achar(idPergunta),cr.getVoto().getVoto());
				}

				break;
				
			case VOTAREMRESPOSTA:
				CelulaResposta respostaEscolhida = respostasAPI.escolherResposta(respostasCRUD, idPergunta, -1);				
				if(respostaEscolhida.getCdp().equals(CodigoDeProtocolo.SUCESSO))
					cr = votosAPI.votarPR(votosCRUD,respostaEscolhida.getResposta().getId(),idUsuario,true);
				if (cr.getCdp() == CodigoDeProtocolo.SUCESSO) { 
					votosCRUD.inserir(cr.getVoto());
					respostasCRUD.atualizar(respostaEscolhida.getResposta(),cr.getVoto().getVoto());
				}

				break;

			case COMENTARPERGUNTA:
				cr = comentariosAPI.comentarPR(comentariosCRUD,idPergunta,idUsuario);
				if (cr.getCdp() == CodigoDeProtocolo.SUCESSO) {
					comentariosCRUD.inserirPergunta(cr.getComentario(), idPergunta);
				}

				break;
				
			case COMENTARRESPOSTA:
				CelulaResposta respostaEscolhidaC = respostasAPI.escolherResposta(respostasCRUD, idPergunta, -1);				
				if(respostaEscolhidaC.getCdp().equals(CodigoDeProtocolo.SUCESSO))
					cr = comentariosAPI.comentarPR(comentariosCRUD,respostaEscolhidaC.getResposta().getId(),idUsuario);
				if (cr.getCdp() == CodigoDeProtocolo.SUCESSO) { 
					comentariosCRUD.inserirResposta(cr.getComentario(), cr.getResposta().getId());
				}

				break;

			default:
				break;
		}

		CodigoDeProtocolo.verificarCodigo(cr.getCdp());

		return cr;
	}

	public String recuperarNota(String regex) {
		return votosCRUD.recuperarNota(regex);
	}
}
