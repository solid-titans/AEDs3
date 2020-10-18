package menu.sistema.controle;

import menu.usuario.UsuariosAPI;
import produtos.CelulaResposta;
import menu.pergunta.PerguntasAPI;
import menu.resposta.RespostasAPI;
import menu.sistema.controle.crudmanagers.*;

public class APIControle {

	UsuariosAPI   usuariosAPI;
	PerguntasAPI  perguntasAPI;
	RespostasAPI  respostasAPI;

	UsuariosCRUD  usuariosCRUD;
	PerguntasCRUD perguntasCRUD;
	RespostasCRUD respostasCRUD;

	public APIControle(UsuariosAPI usuariosAPI, PerguntasAPI perguntasAPI, RespostasAPI respostasAPI, UsuariosCRUD usuariosCRUD, PerguntasCRUD perguntasCRUD, RespostasCRUD respostasCRUD) {

		this.usuariosAPI   = usuariosAPI;
		this.perguntasAPI  = perguntasAPI;
		this.respostasAPI  = respostasAPI;

		this.usuariosCRUD  = usuariosCRUD;
		this.perguntasCRUD = perguntasCRUD;
		this.respostasCRUD = respostasCRUD;
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
				cr = usuariosAPI.acessarAoSistema();
				break;

			case CRIARNOVOUSUARIO: // Criar um novo usuário no banco de dados
				cr = usuariosAPI.criarNovoUsuario(usuariosCRUD);
				if(cr.getUsuario() != null) 
					usuariosCRUD.inserir(cr.getUsuario());

				break;

			case CRIARSENHATEMPORARIA: 
				cr = usuariosAPI.criarSenhaTemporaria();
				if( cr.getUsuario() != null) 
					usuariosCRUD.atualizar(cr.getUsuario());

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
				cr = perguntasAPI.consultarPerguntaPelaPalavraChave(idUsuario);
				if (cr.getCdp() == CodigoDeProtocolo.IRPARAPERGUNTA) {
					cr.setUsuario(usuariosCRUD.achar(cr.getPergunta().getIdUsuario()));
				}

				break;

			case OLHARNOTIFICACOES: // Verificar suas notificacoes
				System.out.println("Olha so as notificacoes");
				break;

			case NOVASENHA: 
				cr = usuariosAPI.criarNovaSenha(idUsuario);
				if(cr.getUsuario() != null) 
					usuariosCRUD.atualizar(cr.getUsuario());

				break;                       

			case LISTARPERGUNTAS: // Listando as perguntas do usuario atual
				cr = perguntasAPI.listarPerguntas(idUsuario);
				break;

			case NOVAPERGUNTA: // Incluindo uma nova pergunta
				cr = perguntasAPI.criarPergunta(idUsuario);
				if(cr.getPergunta() != null) 
					perguntasCRUD.inserir(cr.getPergunta(), idUsuario);

				break;

			case ALTERARPERGUNTA: // Alterando uma pergunta atual
				cr = perguntasAPI.alterarPergunta(idUsuario);
				if(cr.getPergunta() != null) 
					perguntasCRUD.atualizar(cr.getPergunta());	

				break;

			case ARQUIVARPERGUNTA: // Arquivando as perguntas
				cr = perguntasAPI.arquivarPergunta(idUsuario);
				if(cr.getPergunta() != null) 
					perguntasCRUD.desativar(cr.getPergunta());

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
				cr = respostasAPI.listarRespostasDoGeral(idPergunta);
				break;

			case LISTARRESPOSTASUSUARIO: //Pedir para acessar o sistema
				cr = respostasAPI.listarRespostasDoUsuario(idPergunta,idUsuario);
				break;

			case INCLUIRRESPOSTA: // Criar um novo usuário no banco de dados
				cr = respostasAPI.criarResposta(idPergunta,idUsuario);
				if(cr.getResposta() != null) 
					respostasCRUD.inserir(cr.getResposta(), idPergunta);

				break;

			case ALTERARRESPOSTA: 
				cr = respostasAPI.alterarResposta(idPergunta,idUsuario);
				if( cr.getResposta() != null) 
					respostasCRUD.atualizar(cr.getResposta());

				break;    

			case ARQUIVARRESPOSTA: 
				cr = respostasAPI.arquivarResposta(idPergunta,idUsuario);
				if( cr.getResposta() != null) 
					respostasCRUD.atualizar(cr.getResposta());

				break;
				       
			default:
				break;              
		}
		
		CodigoDeProtocolo.verificarCodigo(cr.getCdp());

		return cr;
	}
}
