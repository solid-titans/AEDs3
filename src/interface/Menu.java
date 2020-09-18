import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Menu {

    public static ASCIInterface a = new ASCIInterface(); // Interface grafica feita em ASCII

    //Variaveis para leitura do teclado
    public static InputStream is = System.in;
    public static InputStreamReader isr = new InputStreamReader(is);
    public static BufferedReader br = new BufferedReader(isr);

    public static void main(String[] args) {

        //Variaveis do menu
        String opcao;
        short menuIndex = 0;
        short notificacoes = 0;

        //Loop do menu
        do {

            //Limpar a String 'opcao'
            opcao = ""; 

            //Imprimir uma caixa com o titulo
            System.out.println(a.caixa((short)5,"PERGUNTAS 1.0"));

            //Identificar qual menu deve ser imprimido
            switch(menuIndex) {

                //Acesso
                case 0:
                    System.out.println(acesso());
                    break;
                //Inicio
                case 1: 
                    System.out.println(inicio(notificacoes));
                    break;

                //Criacao de perguntas
                case 2:
                    System.out.println(criacaoDePerguntas());
                    break;

                //Listagem
                case 12:
                    System.out.println(listagem());
                    break;

                //Caso a variavel tenha alguma variavel diferente
                default:
                    System.err.println("Erro! Alguma coisa deu muito errado");
                    break;
            }

            //Fazendo leitura do teclado
            try {
                opcao = br.readLine();
            }
            catch(IOException e) {
                System.err.println("Erro na leitura do buffer!");
            }

            //Se o usuario não tenha apenas apertado 'enter' (String vazia)
            if ( opcao.length() != 0 ) {
                opcao = opcao.charAt(0) + String.valueOf((int)menuIndex);
            }

            //limpando a tela
            a.limparTela();

            System.out.println(opcao);

            //Fazendo a mudança do menu
            /*
            *   Eis aqui uma explicacao de como o sistema de menu funciona
            *   Primeiro o programa faz a leitura do teclado na linha 
            *   55(opcao = br.readLine();), apos isso, ele soma o valor
            *   da variavel 'menuIndex' com o que foi lido. O resultado
            *   Disso era uma String de 2 caracteres, sendo o primeiro
            *   a opcao escolhida pelo usuario, e a segunda o menu na 
            *   qual ele esta atualmente.
            *
            *   Lista de menus:
            *
            *   0 - Acesso
            *   1 - Inicio
            *   2 - Criacao de Perguntas
            * 
            *   Obs: Se o usuario apenas apertou 'enter' deixando a String
            *   vazia, o programa contara isso como um erro.
            */
            if ( menuIndex != 12) {
                switch(opcao) {

                    //Menu Acesso
    
                    case "00": //Saindo do programa
                        System.out.println("Obrigado por usar o programa!\nTenha um excelente dia");
                        break;
                    case "10": // Acessando o sistema usando credenciais existentes
                        System.out.println("Acesso ao sistema ta ai");
                        menuIndex = 1;
                        break;
                    case "20": // Criando um novo usuario 
                        System.out.println("Bora criar um novo usuario");
                        break;
                    case "30": // Resentando a senha
                        novaSenha();
                        break;

                    //Menu Inicio

                    case "01": // Saindo do programa
                        System.out.println("Obrigado por usar o programa!\nTenha um excelente dia");
                        break;
                    case "11": // Indo para a tela de criacao de perguntas
                        menuIndex = 2;
                        break;
                    case "21": // Indo para a tela de consultar/responder perguntas
                        System.out.println("Vamos consultar/responder perguntas");
                        break;
                    case "31": // Verificar suas notificacoes
                        System.out.println("Olha so as notificacoes");
                        break;

                    //Menu Criacao de Perguntas

                    case "02": // Voltando a tela de Inicio
                        menuIndex = 1;
                        break;
                    case "12": // Listando as perguntas do usuario atual
                        menuIndex = 12;
                        break;
                    case "22": // Incluindo uma nova pergunta
                        System.out.println("Vamos Incluir!");
                        break;
                    case "32": // Alterando uma pergunta atual
                        System.out.println("Vamos Alterar");
                        break;
                    case "42": // Arquivando as perguntas
                        System.out.println("Arquivar");
                        break;

                    //Operacao Invalida

                    default:
                        System.err.println("Erro! Entrada inválida, tente novamente.");
                        break;
                }
            }
            else {
                menuIndex = 2;
            }

        }while (!opcao.equals("00") && !opcao.equals("01"));
        

    }

    //String com as interfaces dos diferentes menu

    //Menu 0 : Tela de Acesso
    public static String acesso() {
        return "ACESSO\n\n" +
               "1) Acesso ao sistema\n" +
               "2) Novo usuario\n" +
               "3) Esqueci minha senha\n\n"+
               "0) Sair\n\n" + 
               "Opção: ";
    }

    //Menu 1 : Tela de Inicio
    public static String inicio(short notificacoes) {
        return "INÍCIO\n\n" +
               "1) Criação de perguntas\n" +
               "2) Consultar/responder perguntas\n" +
               "3) Notificações: " + notificacoes + "\n\n"+
               "0) Sair\n\n" + 
               "Opção: ";
    }

    //Menu 2 : Tela de criacaoDePerguntas
    public static String criacaoDePerguntas() {
        return "INÍCIO > CRIAÇÃO DE PERGUNTAS\n\n" +
               "1) Listar\n" +
               "2) Incluir\n" +
               "3) Alterar\n"+
               "4) Arquivar\n\n" + 
               "0) Retornar ao menu anterior\n\n"+
               "Opção: ";
    }

    //Menu 21 : Tela de listagem de perguntas
    public static String listagem() {
        return "Minhas perguntas\n\n"+
               "Pressione qualquer tecla para continuar...";
    }

    //Interface 03: Criação de nova senha
    public static void novaSenha() {

        String email          = "";
        String novaSenha      = "";
        String confirmarSenha = "";

        boolean senhasIguais  = false;

        int forca = -1;

        System.out.println(a.caixa((short)5,"Vamos resetar sua senha"));
        System.out.println("Por favor insira o seu email");

        //Fazendo leitura do teclado
        try {
            email = br.readLine();
        }
        catch(IOException e) {
            System.err.println("Erro na leitura do buffer!");
        }

        a.limparTela();

        if(verificarEmail(email)) {

            System.out.println("Sucesso! uma mensagem de redifinição de senha foi enviada\nao seu email!");

            do {

                System.out.println("Nova senha: ");

                try {
                    novaSenha = br.readLine();
                }
                catch(IOException e) {
                    System.err.println("Erro na leitura do buffer!");
                }
                System.out.println("Confirme a nova senha: ");

                try {
                    confirmarSenha = br.readLine();
                }
                catch(IOException e) {
                    System.err.println("Erro na leitura do buffer!");
                }

                forca = verificarSenha(novaSenha);
                senhasIguais = novaSenha.equals(confirmarSenha);

                a.limparTela();
                if ( senhasIguais == false ) {

                    System.err.println("ERRO!\nAs duas senhas inseridas não são iguais! Tente novamente\n");
                }
                if ( forca <= 2 || senhasIguais == false) {

                    System.err.println("ERRO!\nForça da sua senha: " +  forca);
                    System.out.println("Considere as recomendações abaixo para uma boa senha:\n");
                    System.out.println("*   -> Ter mais de 8 dígitos\n" + 
                                       "*   -> Ter algum caractere em minusculo\n" + 
                                       "*   -> Ter algum caractere em maiusculo\n" +
                                       "*   -> Possuir algum caractere especial(Exemplo: *?#)\n" + 
                                       "*   -> Possuir pelo menos 1 digito\n\n" +
                                       "Obs: Recomendamos no mínimo uma senha de força 3.");
                }
            } while( senhasIguais == false || forca <= 2);
        }
        else {
            System.err.println("O email que foi inserido não é valido! Tente novamente!");
        }
        System.out.println("\nSucesso! a senha da sua conta foi alterada!\nVoltando ao menu...");
        
    }


    //Verificar se uma senha(String) é forte
    /*
    * Para verificar se uma senha e forte ela
    * precisa atender a algumas demandas:
    *   -> Ter mais de 8 dígitos
    *   -> Ter algum caractere em minusculo
    *   -> Ter algum caractere em maiusculo
    *   -> Possuir algum caractere especial(Exemplo: *?#)
    *   -> Possuir pelo menos 1 digito
    * 
    * O objetivo da funcao e verificar se todas essas
    * demandas são atendidas
    */
	public static int verificarSenha(String s) {
	    
      //Variavel que vai retornar com a qualidade da senha
	    int forca = 5;

      //Se a String recebida estiver vazia
	    if (!s.equals("")) {
        
          //Verificando se a String possui um tamanho menor que 8
    	    if (s.length() < 8) {
    	        
    	        forca--;
    	        System.out.println("Sua senha esta curta!\nRecomendamos uma senha entre 8 e 12 digitos\n");
    	    }

          //Verificando pela existencia de caracteres em minúsculo
    	    if(!s.matches(".*[a-z].*")) {
    	        
    	        forca--;
    	        System.out.println("Tente inserir pelo menos um caractere minusculo\n");
    	    }

          //Verificando pela existencia de caracteres em maiúsculo
    	    if(!s.matches(".*[A-Z].*")) {
    	        
    	        forca--;
    	        System.out.println("Tente inserir pelo menos um caractere maiusculo\n");
    	    }

          //Verificando se a String possui algum caractere especial
    	    if(s.matches("[a-zA-Z0-9 ]*")) {
    	        
    	        forca--;
    	        System.out.println("Coloque um caractere especial na sua senha\n");
    	    }

          //Verificando se possui algum numero
    	    if(!s.matches(".*\\d.*")) {

    	        forca--;
              System.out.println("Tente inserir pelo menos um numero na sua senha\n");
            }
	    }
      //Se a String estiver vazia
	    else {
	        
	        forca = 0;
	        System.out.println("Erro! Nenhuma senha inserida");
	    }
	    
	    return forca;
	}

    //Verificar se o e-mail é valido
    /*
    *   Recebe um email como String e atraves
    *   de regex e as classes Pattern e Matcher
    *   verifica se o email enviado eh valido
    */
    public static boolean verificarEmail(String email) {

        //Cria o padrao regex
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]{3,20}+(?:\\.[a-zA-Z0-9_+&*-]{3,20}+)*@(?:[a-zA-Z0-9-]{3,20}+\\.)+[a-zA-Z]{2,7}$", Pattern.CASE_INSENSITIVE);

        //Recebe o email recebido para ser comparado ao padrao regex
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }
}
