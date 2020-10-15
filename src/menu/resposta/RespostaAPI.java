package menu.resposta;

import menu.sistema.CodigoDeProtocolo;
import menu.sistema.CrudAPI;
import menu.sistema.graficos.*;
import menu.sistema.Sistema;
import produtos.Resposta;

/**
 * Classe para gerenciar as Respostas
 * @author MysteRys337 (Gustavo Lopes)
 */
public class RespostaAPI {

    public static ASCIInterface graficos = new ASCIInterface(199, 231 , 232, 184); //interface grafica

    private static final byte TAM_MIN_RESPOSTA     = 3;                            //Tamanho minimo para as respostas
    private static final byte TAM_MAX_RESPOSTA     = 100;                          //Tamanho maximo para as respostas

    /**
     * Função para listar todas as respostas com base na id da Pergunta
     * @param idPergunta é a ID da pergunta que será usado como base na pesquisa
     * @return
     */
    public static CodigoDeProtocolo listarRespostasDoGeral(int idPergunta) {

        CodigoDeProtocolo resp = CodigoDeProtocolo.ERRO;
        Resposta[] array  = CrudAPI.getRespostaArrayGeral(idPergunta);

        if ( array == null ) {
            System.err.println("Ops.. parece que você não tem nenhuma pergunta...\n");
        
        } else {
            System.out.println(RespostasFrontEnd.listarRespostasGeral(array));
            resp = CodigoDeProtocolo.SUCESSO;
        }

        return resp;
    }

    /**
     * Função para listar todas as respostas com base na id da Pergunta e na ID do usuario
     * @param idPergunta é a ID da pergunta que será usado como base na pesquisa
     * @param idUsuario é a ID do usuario que será usado como base na pesquisa
     * @return
     */
    public static CodigoDeProtocolo listarRespostasDoUsuario(int idPergunta, int idUsuario) {

        CodigoDeProtocolo resp = CodigoDeProtocolo.ERRO;
        Resposta[] array  = CrudAPI.getRespostaArrayUser(idPergunta,idUsuario);

        if ( array == null ) {
            System.err.println("Ops.. parece que você não tem nenhuma pergunta...\n");
        
        } else {
            System.out.println(RespostasFrontEnd.listarRespostas(array));
            resp = CodigoDeProtocolo.SUCESSO;
        }

        return resp;
    }

    /**
     * Função para criar uma resposta para o banco de dados
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario é a ID do usuario que fez a requisição
     * @return
     */
    public static Resposta criarResposta(int idPergunta, int idUsuario) {

        Resposta respostaCriada             = null;
        String resposta                     = "";

        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;

        resposta = Sistema.inserir(graficos,"Insira a sua resposta",TAM_MIN_RESPOSTA,TAM_MAX_RESPOSTA,true);
        if(resposta.equals("")) {
            CrudAPI.resultado = CodigoDeProtocolo.OPERACAOCANCELADA;
            return null;
        }

        respostaCriada = new Resposta(idPergunta, idUsuario, resposta);

        confirmarOperacao = RespostasFrontEnd.verificar(respostaCriada);
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            CrudAPI.resultado = CodigoDeProtocolo.OPERACAOCANCELADA;
            return null;
        }

        return respostaCriada;
    }

    /**
     * Função para alterar uma das respostas do usuário
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario é a ID do usuario que fez a requisição
     * @return
    */
    public static Resposta alterarResposta(int idPergunta, int idUsuario) {

        Resposta              respostaAlterada  = null;
        CodigoDeProtocolo     confirmarOperacao = CodigoDeProtocolo.ERRO;

        String                resposta          = "";

        respostaAlterada = escolherResposta(idPergunta,idUsuario);
        if(respostaAlterada == null) {
            return null;
        }

        if(!respostaAlterada.getAtiva()) {
            System.out.println("A resposta já estava ativada!");
            return null;
        }

        resposta = Sistema.inserir(graficos,"Insira a sua resposta",TAM_MIN_RESPOSTA,TAM_MAX_RESPOSTA,true);

        respostaAlterada.setResposta(resposta);

        confirmarOperacao = RespostasFrontEnd.verificar(respostaAlterada);
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            CrudAPI.resultado = CodigoDeProtocolo.OPERACAOCANCELADA;
            return null;
        }

        return respostaAlterada;
    }

    /**
     * Função para arquivar uma das respostas do usuário
     * @param idPergunta é a id da pergunta que foi escolhida pelo usuário
     * @param idUsuario é a ID do usuario que fez a requisição
     * @return
    */
    public static Resposta arquivarResposta(int idPergunta, int idUsuario) {

        Resposta              respostaAlterada  = null;
        CodigoDeProtocolo     confirmarOperacao = CodigoDeProtocolo.ERRO;

        respostaAlterada = escolherResposta(idPergunta,idUsuario);
        if(respostaAlterada == null) {
            return null;
        }

        if(!respostaAlterada.getAtiva()) {
            System.out.println("A resposta já estava ativada!");
            return null;
        }

        confirmarOperacao = RespostasFrontEnd.verificar(respostaAlterada);
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            CrudAPI.resultado = CodigoDeProtocolo.OPERACAOCANCELADA;
            return null;
        }

        respostaAlterada.setAtiva(false);

        return respostaAlterada;
    }

    /**
     * Função para gerenciar a escolha de uma resposta com base na ID de um usuário e uma pergunta
     * @param idUsuario é o número que corresponde a ID do usuário que gostaria de escolher uma de suas próprias respostas
     * @return a Resposta que foi propriamente escolhida
     */
    private static Resposta escolherResposta(int idPergunta, int idUsuario) {

        Resposta resp         = null;
        int id                = -1;
        Resposta[] array      = null;

        array = CrudAPI.getRespostaArrayUser(idPergunta,idUsuario);       

        if ( array != null ) {
            id   = RespostasFrontEnd.escolherResposta(array);
            if(id != -1)
                if(id == -3) {
                    CrudAPI.resultado = CodigoDeProtocolo.OPERACAOCANCELADA;
                }
                else {
                    resp = CrudAPI.acharResposta(id);
                }

        } else {
            System.err.println("ERRO! nenhuma pergunta encontrada!");
        }

        return resp; 
    }

    
}
