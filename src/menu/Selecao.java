package menu;

import menu.sistema.Sistema;
import menu.sistema.graficos.*;

/**
 * Classe para armazenar todos os menus de seleção do programa
 * @author MysteRys337( Gustavo Lopes )
 */
public class Selecao {
    
    public static ASCIInterface graficos = new ASCIInterface(); // Interface grafica feita em ASCII

    //String com as interfaces dos diferentes menu

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
        return "INÍCIO\n\n" +
               "1) Criação de perguntas\n" +
               "2) Consultar/responder perguntas\n" +
               "3) Notificações: " + notificacoes + "\n\n"+
               "4) Redefinir senha: \n\n" + 
               "0) Sair\n\n" +
               "Opção: ";
    }

    /**
     * Função com a tela de criação de Perguntas
     * @return uma String com todas as opções do menu de criação de perguntas
     */
    public static String criacaoDePerguntasString() {
        return "INÍCIO > CRIAÇÃO DE PERGUNTAS\n\n" +
               "1) Listar\n" +
               "2) Incluir\n" +
               "3) Alterar\n"+
               "4) Arquivar\n\n" +
               "0) Retornar ao menu anterior\n\n"+
               "Opção: ";
    }

    /**
     * Função com a tela de navegação pela pergunta selecionada pelo usuário
     * @return uma String com todas as opções do menu de interação com pergunta
     */
    public static String navegarPelaPergunta() {
        return "Escolha uma das opções abaixo: \n\n"  +
               "1) Responder\n"                       +
               "2) Comentar\n"                        +
               "3) Avaliar\n\n"                       +
               "0) Retornar\n\n"                      +
               "Opção: ";
    }

    /**
     * Função com interação a tela de acesso ao sistema
     * @return um char com a opção escolhida pelo usuário
     */
    public static char Acesso() {

        //limpar a tela
        ASCIInterface.limparTela();

        char opcao = 'B';

        //Imprimir uma caixa com o titulo
        System.out.println(graficos.caixa("PERGUNTAS 1.0"));
        System.out.print(acessoString());

        //Fazendo leitura do teclado
        opcao = Sistema.lerChar();

        //limpando a tela
        ASCIInterface.limparTela();   

        return opcao;
    }

    /**
     * Função com interação a tela inicial do programa
     * @param menuIndex é o menu atual na qual o usuário está dentro do programa
     * @param notificacoes é o numero de notificações que o atual usuário tem
     * @return uma String que corresponde a opção escolhida pelo usuário
     */
    public static String Inicio(byte menuIndex, byte notificacoes) {    

            String opcao = "";

            //Imprimir uma caixa com o titulo
            System.out.println(graficos.caixa(5,"PERGUNTAS 1.0"));

            //Imprimindo o menu a ser exibido para o usuario
            switch(menuIndex) 
            {

                //Inicio
                case 1:
                    System.out.print(inicioString(notificacoes));
                    break;

                //Criacao de perguntas
                case 2:
                    System.out.print(criacaoDePerguntasString());
                    break;

                //Caso a variavel tenha alguma variavel diferente
                default:
                    System.err.println("Erro! Alguma coisa deu muito errado");
                    break;
            }

            //Fazendo leitura do teclado
            opcao = Sistema.lerEntrada();

            //Se o usuario não tenha apenas apertado 'enter' (String vazia)
            if ( opcao.length() != 0 ) {
                opcao = opcao.charAt(0) + String.valueOf((int)menuIndex);
            }

            //limpando a tela
            ASCIInterface.limparTela();

            return opcao;
    }

    /**
     * Função de interação com a pergunta selecionada pelo usuário atual
     * @param perguntaSelecionada é a string contendo informações da pergunta selecionada
     * @return o byte correspondendo a opção escolhida pelo usuário
     */
    public static byte interagirComPergunta(String perguntaSelecionada) {

        byte opcao = -1;

        System.out.println(graficos.caixa("Vamos imprimir a pergunta selecionada!"));
        System.out.println(perguntaSelecionada);

        System.out.println(navegarPelaPergunta());

        opcao = Sistema.lerByte();
    
        //limpando a tela
        ASCIInterface.limparTela();

        return opcao;
    }

}