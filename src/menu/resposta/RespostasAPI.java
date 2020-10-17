package menu.resposta;

import menu.sistema.controle.CodigoDeProtocolo;
import menu.sistema.graficos.*;
import menu.sistema.input.CustomInput;
import produtos.CelulaResposta;
import produtos.Resposta;

/**
 * Classe para gerenciar as Respostas
 * @author MysteRys337 (Gustavo Lopes)
 */
public class RespostasAPI {

    private final byte        TAM_MIN_RESPOSTA; //Tamanho minimo para as respostas
    private final byte        TAM_MAX_RESPOSTA; //Tamanho maximo para as respostas

    private RespostasFrontEnd respostasFrontEnd;

    private CustomInput       customInput;

    public RespostasAPI(byte TAM_MIN_RESPOSTA, byte TAM_MAX_RESPOSTA, RespostasFrontEnd respostasFrontEnd,CustomInput customInput) {
        this.TAM_MIN_RESPOSTA  = TAM_MIN_RESPOSTA;
        this.TAM_MAX_RESPOSTA  = TAM_MAX_RESPOSTA;

        this.respostasFrontEnd = respostasFrontEnd;
    }

    /**
     * Função para listar todas as respostas com base na id da Pergunta
     * @param idPergunta é a ID da pergunta que será usado como base na pesquisa
     * @return uma CelulaResposta com os resultados da operação
     */
    public CelulaResposta listarRespostasDoGeral(int idPergunta) {

        CelulaResposta resultado = new CelulaResposta();
        Resposta[]     array     = APIControle.getRespostaArrayGeral(idPergunta);

        if ( array == null ) {
            System.err.println("Ops.. parece que você não tem nenhuma pergunta...\n");
        
        } else {
            System.out.println(respostasFrontEnd.listarGeral(array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }

        return resultado;
    }

    /**
     * Função para listar todas as respostas com base na id da Pergunta e na ID do usuario
     * @param idPergunta é a ID da pergunta que será usado como base na pesquisa
     * @param idUsuario é a ID do usuario que será usado como base na pesquisa
     * @return uma CelulaResposta com os resultados da operação
     */
    public CelulaResposta listarRespostasDoUsuario(int idPergunta, int idUsuario) {

        CelulaResposta resultado = new CelulaResposta();
        Resposta[] array  = APIControle.getRespostaArrayUser(idUsuario);

        if ( array == null ) {
            System.err.println("Ops.. parece que você não tem nenhuma pergunta...\n");
        
        } else {
            System.out.println(respostasFrontEnd.listar(array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }

        return resultado;
    }

    /**
     * Função para criar uma resposta para o banco de dados
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario é a ID do usuario que fez a requisição
     * @return
     */
    public CelulaResposta criarResposta(int idPergunta, int idUsuario) {
        String            resposta             = "";

        CelulaResposta    resultado            = new CelulaResposta();
        CodigoDeProtocolo confirmarOperacao    = CodigoDeProtocolo.ERRO;

        resposta = customInput.inserir("Insira a sua resposta",TAM_MIN_RESPOSTA,TAM_MAX_RESPOSTA,true);
        if(resposta.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setResposta(new Resposta(idPergunta, idUsuario, resposta));

        confirmarOperacao = respostasFrontEnd.verificar(resultado.getResposta());
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para alterar uma das respostas do usuário
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario é a ID do usuario que fez a requisição
     * @return
    */
    public CelulaResposta alterarResposta(int idPergunta, int idUsuario) {
        Resposta              respostaAlterada     = null;

        String                resposta             = "";

        CodigoDeProtocolo     confirmarOperacao    = CodigoDeProtocolo.ERRO;
        CelulaResposta        resultado            = new CelulaResposta();

        respostaAlterada = escolherResposta(idPergunta,idUsuario).getResposta();
        if(respostaAlterada == null) {
            return resultado;
        }

        if(!respostaAlterada.getAtiva()) {
            System.out.println("A resposta já estava ativada!");
            return resultado;
        }

        resposta = customInput.inserir("Insira a sua resposta",TAM_MIN_RESPOSTA,TAM_MAX_RESPOSTA,true);

        respostaAlterada.setResposta(resposta);

        confirmarOperacao = respostasFrontEnd.verificar(respostaAlterada);
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para arquivar uma das respostas do usuário
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario é a ID do usuario que fez a requisição
     * @return
    */
    public CelulaResposta arquivarResposta(int idPergunta, int idUsuario) {

        Resposta              respostaAlterada  = null;

        CodigoDeProtocolo     confirmarOperacao    = CodigoDeProtocolo.ERRO;
        CelulaResposta        resultado            = new CelulaResposta();

        respostaAlterada = escolherResposta(idPergunta,idUsuario).getResposta();
        if(respostaAlterada == null) {
            return resultado;
        }

        if(!respostaAlterada.getAtiva()) {
            System.out.println("A resposta já estava ativada!");
            return resultado;
        }

        confirmarOperacao = respostasFrontEnd.verificar(respostaAlterada);
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return null;
        }

        respostaAlterada.setAtiva(false);
        resultado.setResposta(respostaAlterada);
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para gerenciar a escolha de uma resposta com base na ID de um usuário e uma pergunta
     * @param idUsuario é o número que corresponde a ID do usuário que gostaria de escolher uma de suas próprias respostas
     * @return a Resposta que foi propriamente escolhida
     */
    private CelulaResposta escolherResposta(int idPergunta, int idUsuario) {
        int id                   = -1;
        Resposta[] array         = null;

        CelulaResposta resultado = new CelulaResposta();

        array = APIControle.getRespostaArrayUser(idUsuario);       

        if ( array != null ) {
            id   = respostasFrontEnd.escolherResposta(array);
            if(id != -1)
                if(id == -3) {
                    resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
                }
                else {
                    resultado.setResposta(APIControle.acharResposta(id));
                }

        } else {
            System.err.println("ERRO! nenhuma pergunta encontrada!");
        }

        return resultado; 
    }
    
}
