package menu;

import menu.sistema.Sistema;
import menu.sistema.graficos.*;

class Selecao {
    
    private static ASCIInterface graficos = new ASCIInterface(); // Interface grafica feita em ASCII

    //String com as interfaces dos diferentes menu

    private static String acessoString() {
        return "ACESSO\n\n" +
               "1) Acesso ao sistema\n" +
               "2) Novo usuario\n" +
               "3) Esqueci minha senha\n\n"+
               "0) Sair\n\n" +
               "Opção: ";
    }

    //Menu 1 : Tela de Inicio
    public static String inicioString(byte notificacoes) {
        return "INÍCIO\n\n" +
               "1) Criação de perguntas\n" +
               "2) Consultar/responder perguntas\n" +
               "3) Notificações: " + notificacoes + "\n\n"+
               "4) Redefinir senha: \n\n" + 
               "0) Sair\n\n" +
               "Opção: ";
    }

    //Menu 2 : Tela de criacaoDePerguntas
    public static String criacaoDePerguntasString() {
        return "INÍCIO > CRIAÇÃO DE PERGUNTAS\n\n" +
               "1) Listar\n" +
               "2) Incluir\n" +
               "3) Alterar\n"+
               "4) Arquivar\n\n" +
               "0) Retornar ao menu anterior\n\n"+
               "Opção: ";
    }

    //Tela Inicial do programa
    public static char Acesso() {

        //limpar a tela
        graficos.limparTela();

        char opcao = 'B';

        //Imprimir uma caixa com o titulo
        System.out.println(graficos.caixa(5,"PERGUNTAS 1.0"));

        System.out.print(acessoString());

        //Fazendo leitura do teclado
        opcao = Sistema.lerChar();

        //limpando a tela
        graficos.limparTela();   

        return opcao;
    }

    //Tela quando o usuario acessa o programa com sucesso
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
            graficos.limparTela();

            return opcao;
    }

}