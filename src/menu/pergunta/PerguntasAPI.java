package menu.pergunta;

import produtos.*;
import menu.sistema.*;
import menu.sistema.graficos.*;

/**
 * Classe para gerenciar todas as funções de controle de perguntas
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class PerguntasAPI {

    public static ASCIInterface graficos = new ASCIInterface(199, 231 , 232, 184); //interface grafica

    private static final byte TAM_MIN_TITULO     = 3;                       //Tamanho minimo para as perguntas
    private static final byte TAM_MAX_TITULO     = 25;                      //Tamanho maximo para as perguntas

    private static final byte TAM_MIN_PERGUNTA   = 3;                       //Tamanho minimo para as perguntas
    private static final byte TAM_MAX_PERGUNTA   = 100;                     //Tamanho maximo para as perguntas

    /**
     * Função que serve para listar as perguntas de um usuário com base na ID do usuário
     * @param idUsuario é o numero que corresponde a ID do usuário que gostaria de ver as perguntas
     * @return um Codigo de Protocolo que representa o resultado da operação
     */
    public static CelulaResposta listarPerguntas(int idUsuario) {

        CelulaResposta resultado = new CelulaResposta();
        Pergunta[]     array     = CrudAPI.getPerguntaArray(idUsuario);

        if ( array == null ) {
            System.err.println("Ops.. parece que você não tem nenhuma pergunta...\n");
        
        } else {
            System.out.println(PerguntasFrontEnd.listarPerguntas(array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }

        return resultado;
    }

    /**
     * Função para criar uma nova pergunta e registrar na ID do usuario que foi recebido
     * @param idUsuario é o número que corresponde a ID do usuario que quer registrar uma pergunta
     * @return a Pergunta que foi registrada
     */
    public static CelulaResposta criarPergunta(int idUsuario) {

        Pergunta           novaPergunta          = null;
        CodigoDeProtocolo  confirmarOperacao     = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado = new CelulaResposta();

        novaPergunta  = inserirDadosDaPergunta(idUsuario);
        if(novaPergunta == null) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        confirmarOperacao = PerguntasFrontEnd.verificar(novaPergunta);
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(confirmarOperacao);
            return resultado;
        }

        resultado.setPergunta(novaPergunta);
        resultado.setCdp(confirmarOperacao);

        return resultado;
    }

    /**
     * Função para alternar uma pergunta já existente para o usuário
     * @param idUsuario é o numero correspondente a ID do usuário que gostaria de trocar a pergunta
     * @return a Pergunta que será alterada
     */
    public static CelulaResposta alterarPergunta(int idUsuario) {

        Pergunta             perguntaAlterada    = null;
        int                  idPerguntaAlterada  = -1;
        CodigoDeProtocolo    confirmar           = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado = new CelulaResposta();

        perguntaAlterada = escolherPergunta(idUsuario).getPergunta();
        if(perguntaAlterada == null) {
            return resultado;
        }

        if(!perguntaAlterada.getAtiva()) {
            System.out.println("A pergunta está desativada!");
            return resultado;
        }

        idPerguntaAlterada = perguntaAlterada.getId();

        perguntaAlterada   = inserirDadosDaPergunta(idUsuario);
        perguntaAlterada.setId(idPerguntaAlterada);

        confirmar = PerguntasFrontEnd.verificar(perguntaAlterada);
        if(confirmar == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(confirmar);
            return resultado;
        }

        resultado.setPergunta(perguntaAlterada);
        resultado.setCdp(confirmar);

        return resultado;
    }

    /**
     * Função para arquivar uma pergunta do usuario 
     * @param idUsuario é o numero que corresponde a ID do usuário que gostaria de arquivar uma pergunta
     * @return a Pergunta que será registrada
     */
    public static CelulaResposta arquivarPergunta(int idUsuario) {

        Pergunta              perguntaAlterada  = null;
        CodigoDeProtocolo     confirmarOperacao = CodigoDeProtocolo.ERRO;

        CelulaResposta        resultado         = new CelulaResposta();

        perguntaAlterada = escolherPergunta(idUsuario).getPergunta();
        if(perguntaAlterada == null) {
            return resultado;
        }

        if(!perguntaAlterada.getAtiva()) {
            System.out.println("A pergunta já estava ativada!");
            return resultado;
        }

        confirmarOperacao = PerguntasFrontEnd.verificar(perguntaAlterada);
        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(confirmarOperacao);
            return resultado;
        }

        perguntaAlterada.setAtiva(false);
        resultado.setPergunta(perguntaAlterada);
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função que irá permitir o usuário encontrar uma perguntas com base na palavras-chave
     * @param idUsuario é o número da ID do usuário que está acessando essa função
     * @return a Pergunta que o usuário escolheu
     */
    public static CelulaResposta consultarPerguntaPelaPalavraChave(int idUsuario) {

        String         entrada     = "";

        Pergunta[]     lista       = null;
        CelulaResposta resultado   = new CelulaResposta();
        
        int            idPergunta  = -1;
    
        entrada = Sistema.inserir(graficos,"Busque as perguntas por palavra chave separadas por espaço em branco",
                                           "Ex: política Brasil eleições",TAM_MIN_PERGUNTA,TAM_MAX_PERGUNTA,true);
        if(entrada.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        entrada = Pergunta.consertarPalavrasChave(entrada);

        lista   = CrudAPI.getPerguntasPalavrasChave(entrada.split(" "),idUsuario);
        ASCIInterface.limparTela();
    
        if(lista.length > 0) {
    
            System.out.println("Um total de " + lista.length + " foi/foram encontrado(s)");
    
            idPergunta = PerguntasFrontEnd.escolherPergunta(lista);
            if(idPergunta != -1) {
                resultado.setPergunta(CrudAPI.acharPergunta(idPergunta));
                resultado.setCdp(CodigoDeProtocolo.IRPARAPERGUNTA);

            }

            ASCIInterface.limparTela();

        } else {
            System.out.println("Atenção! Nenhuma pergunta encontrada com as palavras-chave inserida!");
        }
    
        return resultado;
    }

    /**
     * Função genérica para inserir dados de uma pergunta
     * @param idUsuario é o numero que corresponde a ID do usuário que será dono da pergunta inserida
     * @return a Pergunta que foi formada
     */
    private static Pergunta inserirDadosDaPergunta(int idUsuario) {

        String titulo         = "";
        String pergunta       = "";
        String palavras_chave = "";

        Pergunta p            = null;

        titulo         = Sistema.inserir(graficos,"Insira o título da pergunta",                                             
                                         TAM_MIN_TITULO,TAM_MAX_TITULO,true);
        if(titulo.equals("")){
            return null;
        }
        pergunta       = Sistema.inserir(graficos,"Insira a pergunta",                                                       
                                         TAM_MIN_PERGUNTA,TAM_MAX_PERGUNTA,true);
        if(pergunta.equals("")){
            return null;
        }
        palavras_chave = Sistema.inserir(graficos,"Insira as palavras-chave dessa pergunta","Exemplo: Brasil política saude",
                                         TAM_MIN_PERGUNTA,TAM_MAX_PERGUNTA,true);
        if(palavras_chave.equals("")){
            return null;
        }

        p = new Pergunta(idUsuario,titulo,pergunta,palavras_chave);

        return p;
    }

    /**
     * Função para gerenciar a escolha de pergunta com base na ID de um usuário
     * @param idUsuario é o número que corresponde a ID do usuário que gostaria de escolher uma de suas próprias perguntas
     * @return a Pergunta que foi propriamente escolhida
     */
    private static CelulaResposta escolherPergunta(int idUsuario) {
        int id                   = -1;
        Pergunta[] array         = null;

        CelulaResposta resultado = new CelulaResposta();

        array = CrudAPI.getPerguntaArray(idUsuario);         

        if ( array != null ) {
            id   = PerguntasFrontEnd.escolherPergunta(array);
            if(id != -1)
                if(id == -3) {
                    resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
                }
                else {
                    resultado.setPergunta(CrudAPI.acharPergunta(id));
                }

        } else {
            System.err.println("ERRO! nenhuma pergunta encontrada!");
        }

        return resultado; 
    }

}
