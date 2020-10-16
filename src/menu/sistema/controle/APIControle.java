package menu.sistema.controle;

import java.util.ArrayList;

import produtos.*;
import crud.*;
import menu.usuario.UsuarioAPI;
import menu.pergunta.PerguntasAPI;
import menu.pergunta.indices.*;
import menu.resposta.RespostaAPI;
import seguranca.GFG;

public class APIControle {
	
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
}
