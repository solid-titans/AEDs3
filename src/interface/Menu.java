import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Menu {

    //Id do Usuario que usar o sistema
    public static int IdUsuario             = - 1;

    public static ASCIInterface a           = new ASCIInterface(); // Interface grafica feita em ASCII

    //Variaveis para leitura do teclado
    public static InputStream is            = System.in;
    public static InputStreamReader isr     = new InputStreamReader(is);
    public static BufferedReader br         = new BufferedReader(isr);

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
    public static String inicio(byte notificacoes) {
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

    public static void main(String[] args) {

        //Variaveis do menu
        String opcao;
        byte menuIndex = 0;
        byte notificacoes = 0;

        boolean sucesso = false;

        //Loop do menu
        do {

            //Limpar a String 'opcao'
            opcao = ""; 

            //Imprimir uma caixa com o titulo
            System.out.println(a.caixa((short)5,"PERGUNTAS 1.0"));

            //Imprimindo o menu a ser exibido para o usuario
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

            //System.out.println(opcao);

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
                        System.out.println("Obrigado por usar o programa!\nTenha um excelente dia!");
                        break;

                    case "10": // Acessando o sistema usando credenciais existentes
                        sucesso = acessoAoSistema();

                        if(sucesso) {
                            System.out.println("\nSucesso! Login aprovado!\nSeja bem vindo usuário!\n")
                            menuIndex = 1;
                        }
                        else {
                            System.err.println("\nERRO! Login não aprovado!\nTente novamente!\n");
                        }
                        break;
                    case "20": // Criando um novo usuario 
                        sucesso = criandoUsuario();

                        if(sucesso) {
                            System.out.println("\nSucesso! Novo usuário criado! Agora faça login!\n")
                            menuIndex = 1;
                        }
                        else {
                            System.err.println("\nERRO! Criação de usuário deu errado!\nTente novamente!\n");
                        }
                        break;

                    case "30": // Resentando a senha
                        sucesso = novaSenha();
                        if (sucesso) {
                            
                            System.out.println("\nSucesso! A senha da sua conta foi alterada!\nVoltando ao menu...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de mudança de senha!\nTente novamente!\n");
                        }
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

    //Interface 03: Criação de nova senha
    public static boolean novaSenha() {

        String email          = "";
        String novaSenha      = "";
        String confirmarSenha = "";

        boolean senhasIguais  = false;
        boolean resp = false;

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

                //Leitura da senha
                /*
                *   O usuario precisa fazer a inserção da senha
                *   duas vezes para conferir se o mesmo sabe
                *   qual e a senha
                */
                try {
                    novaSenha = br.readLine();
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

            resp = true;

        }
        else {
            System.err.println("\nO email que foi inserido não é valido\nOu não consta em nosso banco de dados!\n Tente novamente!\n");
        }

        return resp;
    }

    //Funcao booleana expressando se o usuario tem acesso ou não ao sistema
    /* 
    *   Primeiro o usuario tenta inserir o e-mail, se o mesmo consta no banco
    *   de dados, então ele passa para a verificação de senha, se a senha
    *   não for válida, então o usuario não ganha acesso.
    */
    public static boolean acessoAoSistema() {

        String email = "";
        String senha = "";
        boolean resp = false;

        byte tentativas = 5;
        
        do { 
            System.out.println(a.caixa((short)5,"Bem vindo usuário!"));
            System.out.println("ACESSO AO SISTEMA\n\nE-mail: ");

            try {
                email = br.readLine();
            }  
            catch(IOException e) {
                System.err.println("Erro na leitura do buffer!");
            }

            a.limparTela();
            if(verificarEmail(email)) {

                System.out.println(a.caixa((short)5,"Insira sua senha:"));
                System.out.println("\nACESSO AO SISTEMA\nSenha: ");

                try {
                    senha = br.readLine();
                }  
                catch(IOException e) {
                    System.err.println("Erro na leitura do buffer!");
                }

                a.limparTela();
                if ( 1 != 1 ) {
                        
                    System.out.println("Ops! Parece que essa não é a senha!\nTente novamente!\nN° de tentativas: " + tentativas + "\n");
                }
                else {
                        
                    resp = true;
                }
            }
            else {
                
                tentativas--;
                System.out.println("\nO email inserido não é válido ou não existe no banco de dados\nTeste novamente!\nN° de tentativas: " + tentativas + "\n");
            }
        } while( tentativas != 0 );

        
        return resp;
    }

    public static boolean criandoUsuario() {

        String email     = "";
        String usuario   = "";
        String senha     = "";

        String confirmar = "";
        boolean resp     = false;

        do {
            System.out.println(a.caixa((short)5,"Vamos criar um novo usuário!"));
            System.out.println("NOVO USUÁRIO\n\nEmail: ");

            try {
                email = br.readLine();
            }  
            catch(IOException e) {
                System.err.println("Erro na leitura do buffer!");
            }
            
            a.limparTela();
            if ( 1 != 1 ) {

                System.out.println("Esse e-mail já está registrado em nosso sistema\nTente outro e-mail!\n");

            }
        } while ( 1 != 1);
    
        if(verificarEmail(email)) {

            System.out.println(a.caixa((short)5,"Digite um nome e senha!"));
            System.out.println("NOVO USUÁRIO\n\nUsuário: ");

            try {
                usuario = br.readLine();
                a.limparTela();
                System.out.println(a.caixa((short)5,"Digite um nome e senha!"));
                System.out.println("NOVO USUÁRIO\n\nSenha: ");
                usuario = br.readLine();
            }  
            catch(IOException e) {
                System.err.println("Erro na leitura do buffer!");
            }

            a.limparTela();
            System.out.println("Vamos então verificar os seus dados!" +
                               "Email: "           + email     + "\n" + 
                               "Nome de usuário: " + usuario   + "\n" +
                               "Senha: "           + senha     + "\n");
            
            System.out.println("\nEstá tudo de acordo?");

            try {
                confirmar = br.readline();
            }  
            catch(IOException e) {
                System.err.println("Erro na leitura do buffer!");
            }

            a.limparTela();
            if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) { 
                
                System.out.println("Certo! Vamos então criar o usuario "+ usuario +"para você!");
                resp = true;
                System.out.println("Sucesso! Usuário criado! Voltando ao menu!\n")
            }
            else {

                System.out.println("Processo cancelado!\nVoltando para o menu...\n");
            }

            
        }
        else {
            System.err.println("O email inserido é inválido!\nVoltando ao menu...\n\n");
        }

        return resp;

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
