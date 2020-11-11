package menu.api;

import menu.backend.cruds.abstracts.ComentariosInterface;
import menu.backend.cruds.abstracts.RespostasInterface;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.RespostasFrontEnd;
import produtos.CelulaResposta;
import produtos.Resposta;

/**
 * Classe para gerenciar as Respostas
 * 
 * @author MysteRys337 (Gustavo Lopes)
 */
public class RespostasAPI {

    private final byte TAM_MIN_RESPOSTA; // Tamanho minimo para as respostas
    private final byte TAM_MAX_RESPOSTA; // Tamanho maximo para as respostas

    private RespostasFrontEnd frontEnd;

    private CustomInput customInput;

    public RespostasAPI(byte TAM_MIN_RESPOSTA, byte TAM_MAX_RESPOSTA, RespostasFrontEnd frontEnd,
            CustomInput customInput) {
        this.TAM_MIN_RESPOSTA = TAM_MIN_RESPOSTA;
        this.TAM_MAX_RESPOSTA = TAM_MAX_RESPOSTA;

        this.frontEnd = frontEnd;
    }

    public RespostasAPI(RespostasFrontEnd frontEnd, CustomInput customInput) {
        this.TAM_MIN_RESPOSTA = 1;
        this.TAM_MAX_RESPOSTA = 120;

        this.frontEnd    = frontEnd;
        this.customInput = customInput;
    }

    /**
     * Função para listar todas as respostas com base na id da Pergunta
     * 
     * @param idPergunta é a ID da pergunta que será usado como base na pesquisa
     * @return uma CelulaResposta com os resultados da operação
     */
    public CelulaResposta listarRespostasDoGeral(int idUsuario, UsuarioInterface usuarios, RespostasInterface respostas, ComentariosInterface comentarios, VotosInterface votos,
            int idPergunta) {

        CelulaResposta resultado = new CelulaResposta();
        Resposta[] array = respostas.getRespostaArrayGeral(idPergunta);

        if (array == null) {
            System.err.println("\n\n                   ¯\\_(ツ)_/¯");
            System.err.println("Ops.. parece que ninguém submeteu uma resposta a essa pergunta...\n");

        } else {
            System.out.println(frontEnd.listarGeral(idUsuario,usuarios, votos,comentarios, array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }

        return resultado;
    }

    /**
     * Função para listar todas as respostas com base na id da Pergunta e na ID do
     * usuario
     * 
     * @param idPergunta é a ID da pergunta que será usado como base na pesquisa
     * @param idUsuario  é a ID do usuario que será usado como base na pesquisa
     * @return uma CelulaResposta com os resultados da operação
     */
    public CelulaResposta listarRespostasDoUsuario(RespostasInterface respostas, int idPergunta, int idUsuario) {

        CelulaResposta resultado = new CelulaResposta();
        Resposta[] array = respostas.getRespostaArrayUser(idUsuario);

        if (array == null) {
            System.err.println("\n\n                   ¯\\_(ツ)_/¯");
            System.err.println("Ops.. parece que ninguém submeteu uma resposta a essa pergunta...\n");

        } else {
            System.out.println(frontEnd.listar(array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }

        return resultado;
    }

    /**
     * Função para criar uma resposta para o banco de dados
     * 
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario  é a ID do usuario que fez a requisição
     * @return
     */
    public CelulaResposta criarResposta(int idPergunta, int idUsuario) {
        String resposta = "";

        CelulaResposta resultado = new CelulaResposta();
        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;

        resposta = customInput.inserir("Insira a sua resposta", TAM_MIN_RESPOSTA, TAM_MAX_RESPOSTA, true);
        if (resposta.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setResposta(new Resposta(idPergunta, idUsuario, resposta));

        confirmarOperacao = frontEnd.verificar(resultado.getResposta());
        if (confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para alterar uma das respostas do usuário
     * 
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario  é a ID do usuario que fez a requisição
     * @return
     */
    public CelulaResposta alterarResposta(RespostasInterface respostas, int idPergunta, int idUsuario) {
        Resposta respostaAlterada = null;

        String resposta = "";

        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;
        CelulaResposta resultado = new CelulaResposta();

        respostaAlterada = escolherResposta(respostas, idPergunta, idUsuario).getResposta();
        if (respostaAlterada == null) {
            return resultado;
        }

        if (!respostaAlterada.getAtiva()) {
            System.out.println("A resposta já estava ativada!");
            return resultado;
        }

        resposta = customInput.inserir("Insira a sua resposta", TAM_MIN_RESPOSTA, TAM_MAX_RESPOSTA, true);

        respostaAlterada.setResposta(resposta);

        confirmarOperacao = frontEnd.verificar(respostaAlterada);
        if (confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        resultado.setResposta(respostaAlterada);

        return resultado;
    }

    /**
     * Função para arquivar uma das respostas do usuário
     * 
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario  é a ID do usuario que fez a requisição
     * @return
     */
    public CelulaResposta arquivarResposta(RespostasInterface respostas, int idPergunta, int idUsuario) {

        Resposta respostaAlterada = null;

        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;
        CelulaResposta resultado = new CelulaResposta();

        respostaAlterada = escolherResposta(respostas, idPergunta, idUsuario).getResposta();
        if (respostaAlterada == null) {
            return resultado;
        }

        if (!respostaAlterada.getAtiva()) {
            System.out.println("A resposta já estava ativada!");
            return resultado;
        }

        confirmarOperacao = frontEnd.verificar(respostaAlterada);
        if (confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        respostaAlterada.setAtiva(false);
        resultado.setResposta(respostaAlterada);
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para gerenciar a escolha de uma resposta com base na ID de um usuário
     * e uma pergunta
     * 
     * @param idUsuario é o número que corresponde a ID do usuário que gostaria de
     *                  escolher uma de suas próprias respostas
     * @return a Resposta que foi propriamente escolhida
     */
    public CelulaResposta escolherResposta(RespostasInterface respostas, int idPergunta, int idUsuario) {
        int id = -1;
        Resposta[] array = null;

        CelulaResposta resultado = new CelulaResposta();

        array = (idUsuario != -1 ? respostas.getRespostaArrayUser(idUsuario) : respostas.getRespostaArrayGeral(idPergunta));

        if (array != null) {
            id = frontEnd.escolher(array);
            if (id != -1)
                if (id == -3) {
                    resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
                } else {
                    resultado.setResposta(respostas.achar(id));
                    resultado.setCdp(CodigoDeProtocolo.SUCESSO);
                }

        } else {
            System.err.println("ERRO! nenhuma resposta encontrada!");
        }

        return resultado;
    }

}
