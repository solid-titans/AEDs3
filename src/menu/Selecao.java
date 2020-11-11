package menu;

import menu.backend.input.Input;
import menu.frontend.graficos.CustomPrint;

/**
 * Classe para armazenar todos os menus de seleção do programa
 * @author MysteRys337( Gustavo Lopes )
 */
public class Selecao {
    
    private CustomPrint myPrint;
    private Input         input;

    public Selecao(CustomPrint myPrint,Input input) {
        this.myPrint  = myPrint;
        this.input    = input;
    }

    public void mudarCorBorda(int cor) {
        myPrint.getInterface().setBorda(cor);
    }

    /**
     * Função com o menu de acesso
     * @return uma String com todas as opções do menu de acesso
     */
    private static String acessoString() {
        return "ACESSO\n\n" +
               "1) Acesso ao sistema\n" +
               "2) Novo usuario\n" +
               "3) Esqueci minha senha\n\n"+
               "0) Sair\n\n" +
               "Opção: ";
    }

    /**
     * Função com a tela de início od programa
     * @param notificacoes é o numero de notificações que o usuário possui
     * @return uma String com todas as opções do menu inicial
     */
    public static String inicioString(byte notificacoes) {
        return "INÍCIO\n\n"                                 +
               "1) Criação de perguntas\n"                  +
               "2) Consultar/responder perguntas\n"         +
               "3) Notificações: " + notificacoes + "\n\n"  +
               "4) Redefinir senha: \n\n"                   + 
               "0) Sair\n\n"                                +
               "Opção: ";
    }

    /**
     * Função com a tela de criação de Perguntas
     * @return uma String com todas as opções do menu de criação de perguntas
     */
    public static String menuDePerguntasString() {
        return "INÍCIO > CRIAÇÃO DE PERGUNTAS\n\n"  +
               "1) Listar\n"                        +
               "2) Incluir\n"                       +
               "3) Alterar\n"                       +
               "4) Arquivar\n\n"                    +
               "0) Retornar ao menu anterior\n\n"   +
               "Opção: ";
    }

    /**
     * Função com a tela de navegação pela pergunta selecionada pelo usuário
     * @return uma String com todas as opções do menu de interação com pergunta
     */
    public static String navegarPelaPerguntaString() {
        return "INÍCIO > PERGUNTAS \n\n"               +
               "Escolha uma das opções abaixo: \n\n"   +
               "1) Listar respostas\n"                 +
               "2) Listar comentarios da pergunta\n\n" +
               "3) Responder\n"                        +
               "4) Comentar\n"                         +
               "5) Avaliar\n\n"                        +
               "0) Retornar\n\n"                       +
               "Opção: ";
    }

    /**
     * Função com a tela de criação de respostas
     * @return uma String com todas as opções do menu de criação de respostas
     */
    public static String menuDeRespostasString() {
        return "INÍCIO > PERGUNTAS > CRIAÇÃO DE RESPOSTAS\n\n"  +
               "1) Listar suas respostas\n"                     +
               "2) Incluir uma resposta\n"                      +
               "3) Alterar uma resposta\n"                      +
               "4) Arquivar uma resposta\n\n"                   +
               "0) Retornar ao menu anterior\n\n"               +
               "Opção: ";
    }

    /**
     * Função com a tela de criação de comentarios
     * @return uma String com todas as opções do menu de criação de comentarios
     */
    public static String menuDeComentariosString() {
        return "1) Comentar pergunta\n"                   +
               "2) Comentar uma resposta da pergunta\n\n" +
               "0) Retornar ao menu anterior\n\n"         +
               "Opção: ";
    }

    /**
     * Função com a tela de criação de comentarios
     * @return uma String com todas as opções do menu de criação de comentarios
     */
    public static String menuDeVotosString() {
        return "1) Avaliar pergunta\n"                +
               "2) Avaliar respostas da pergunta\n\n" +
               "0) Retornar ao menu anterior\n\n"     +
               "Opção: ";
    }


    /**
     * Função com interação a tela de acesso ao sistema
     * @return um char com a opção escolhida pelo usuário
     */
    public char Acesso() {

        char opcao = '\0';

        System.out.print(acessoString());

        //Fazendo leitura do teclado
        opcao = input.lerChar();

        //limpando a tela
        myPrint.limparTela();   

        return opcao;
    }

    /**
     * Função com interação a tela inicial do programa
     * @param menuIndex é o menu atual na qual o usuário está dentro do programa
     * @param notificacoes é o numero de notificações que o atual usuário tem
     * @return uma String que corresponde a opção escolhida pelo usuário
     */
    public String imprimirTela(byte menuIndex, byte notificacoes) {    

            String opcao = "";

            //Imprimindo o menu a ser exibido para o usuario
            switch(menuIndex) 
            {

                //Inicio
                case 1:
                    System.out.print(inicioString(notificacoes));
                    break;

                //Criacao de perguntas
                case 2:
                    System.out.print(menuDePerguntasString());
                    break;

                //Navegação de perguntas
                case 3:
                    System.out.print(navegarPelaPerguntaString());
                    break;

                //Menu de respostas
                case 4:
                    System.out.print(menuDeRespostasString());
                    break;

                //Menu de comentarios
                case 5:
                    System.out.print(menuDeComentariosString());
                    break;
            
                //Menu de avaliação
                case 6:
                    System.out.print(menuDeVotosString());
                    break;

                //Caso a variavel tenha alguma variavel diferente
                default:
                    System.err.println("Erro! Alguma coisa deu muito errado");
                    break;
            }

            //Fazendo leitura do teclado
            opcao = input.lerString();

            //Se o usuario não tenha apenas apertado 'enter' (String vazia)
            if ( opcao.length() != 0 ) {
                opcao = opcao.charAt(0) + String.valueOf((int)menuIndex);
            }

            //limpando a tela
            myPrint.limparTela();

            return opcao;
    }

}