package menu.pergunta;

import produtos.*;
import menu.*;
import menu.sistema.*;
import menu.sistema.graficos.*;

public class PerguntasAPI {

    public static ASCIInterface graficos = new ASCIInterface(199, 231 , 232, 184);
    private static final byte TAMANHO_MINIMO_PERGUNTA   = 3;

    public static Pergunta criarPergunta(int idUsuario) {

        Pergunta novaPergunta  = null;
        boolean  confirmarAcao = false;

        novaPergunta  = inserirDados(idUsuario);

        confirmarAcao = PerguntasFrontEnd.verificar(novaPergunta);
        if(!confirmarAcao) {
            return null;
        }

        return novaPergunta;
    }

    public static Pergunta alterarPergunta(int idUsuario) {

        Pergunta perguntaAlterada  = null;
        int idPerguntaAlterada     = -1;
        boolean  confirmarAcao = false;

        perguntaAlterada = escolherPergunta(idUsuario);
        if(perguntaAlterada == null) {
            return null;
        }

        if(!perguntaAlterada.getAtiva()) {
            System.out.println("A pergunta está desativada!");
            return null;
        }

        confirmarAcao = PerguntasFrontEnd.verificar(perguntaAlterada);
        if(!confirmarAcao) {
            return null;
        }

        idPerguntaAlterada = perguntaAlterada.getId();

        perguntaAlterada   = inserirDados(idUsuario);
        perguntaAlterada.setId(idPerguntaAlterada);

        return perguntaAlterada;
    }

    public static Pergunta arquivarPergunta(int idUsuario) {

        Pergunta perguntaAlterada  = null;
        boolean  confirmarAcao = false;

        perguntaAlterada = escolherPergunta(idUsuario);
        if(perguntaAlterada == null) {
            return null;
        }

        if(!perguntaAlterada.getAtiva()) {
            System.out.println("A pergunta já estava ativada!");
            return null;
        }

        confirmarAcao = PerguntasFrontEnd.verificar(perguntaAlterada);
        if(!confirmarAcao) {
            return null;
        }

        perguntaAlterada.setAtiva(false);

        return perguntaAlterada;
    }

    private static Pergunta escolherPergunta(int idUsuario) {

        Pergunta resp         = null;
        int id                = -1;
        Pergunta[] array      = null;

        array = CrudAPI.getPerguntaArray(idUsuario);         

        if ( array != null ) {
            id   = PerguntasFrontEnd.escolherPergunta(array);
            if(id != -1)
                resp = CrudAPI.acharPergunta(id);

        } else {
            System.err.println("ERRO! nenhuma pergunta encontrada!");
        }

        return resp; 
    }

    public static CodigoDeProtocolo consultarPerguntas(int idUsuario) {

        Pergunta tmp              = null;
        String   nome             = "";
        String   perguntaEmString = "";
        byte opcao                = -1;

        CodigoDeProtocolo sucesso = CodigoDeProtocolo.ERRO;

        tmp = encontrarPergunta(idUsuario);
    
        if ( tmp != null) {
            nome = CrudAPI.acharUsuario(tmp.getIdUsuario()).getNome();
            perguntaEmString = PerguntasFrontEnd.toString(tmp,nome);

            opcao = Selecao.interagirComPergunta(perguntaEmString);

            switch(opcao) {

                case 0:
                    System.out.println("Ok.. vamos voltar ao menu");
                    break;

                case 1:
                    System.out.println("Responder o usuario");
                    break;

                case 2:
                    System.out.println("Comentar o usuario");
                    break;

                case 3:
                    System.out.println("Avaliar a pergunta");
                    break;

                default:
                    System.out.println("Erro! Entrada inválida!");
                    break;
            }

        }

        return sucesso;
    }

    //Função para consultar as perguntas no sistema a partir das palavras-chave
    public static Pergunta encontrarPergunta(int idUsuario) {

        String entrada    = "";
        Pergunta[] lista  = null;
        Pergunta resp     = null;
        int idPergunta    = -1;
    
        entrada = Sistema.inserir(graficos,"Busque as perguntas por palavra chave separadas por espaço em branco",
                                           "Ex: política Brasil eleições",TAMANHO_MINIMO_PERGUNTA);

        entrada = Pergunta.consertarPalavrasChave(entrada);

        lista   = CrudAPI.getPerguntasPalavrasChave(entrada.split(" "),idUsuario);
        ASCIInterface.limparTela();
    
        if(lista.length > 0) {
    
            System.out.println("Um total de " + lista.length + " foi/foram encontrado(s)");
    
            idPergunta = PerguntasFrontEnd.escolherPergunta(lista);
            if(idPergunta != -1)
                resp = CrudAPI.acharPergunta(idPergunta);

            ASCIInterface.limparTela();
        }
        else {
            System.out.println("Atenção! Nenhuma pergunta encontrada com as palavras-chave inserida!");
        }
    
        return resp;
    }

    public static CodigoDeProtocolo listarPerguntas(int idUsuario) {

        CodigoDeProtocolo resp = CodigoDeProtocolo.ERRO;
        Pergunta[] array  = CrudAPI.getPerguntaArray(idUsuario);

        if ( array == null ) {
            System.err.println("Ops.. parece que você não tem nenhuma pergunta...\n");
        
        } else {
            System.out.println(PerguntasFrontEnd.listarPerguntas(array));
            resp = CodigoDeProtocolo.SUCESSO;
        }

        return resp;
    }

    private static Pergunta inserirDados(int idUsuario) {

        String titulo         = "";
        String pergunta       = "";
        String palavras_chave = "";

        Pergunta p            = null;

        titulo         = Sistema.inserir(graficos,"Insira o título da pergunta",                                             TAMANHO_MINIMO_PERGUNTA);
        pergunta       = Sistema.inserir(graficos,"Insira a pergunta",                                                       TAMANHO_MINIMO_PERGUNTA);
        palavras_chave = Sistema.inserir(graficos,"Insira as palavras-chave dessa pergunta","Exemplo: Brasil política saude",TAMANHO_MINIMO_PERGUNTA);

        p = new Pergunta(idUsuario,titulo,pergunta,palavras_chave);

        return p;
    }

}
