import produtos.*;
import crud.*;
import crud.lixo.Lixo;
import menu.*;
import seguranca.*;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    
    //Cria o CRUD
    public static Crud<Usuario> usuarios    = null;
    public static Crud<Pergunta> perguntas  = null;

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
               "4) Redefinir senha: \n\n" + 
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

    public static void main(String[] args) {

        try {
    
            usuarios = new Crud<>("Usuarios", Usuario.class.getConstructor());
            perguntas = new Crud<>("Perguntas", Pergunta.class.getConstructor());
        
        } catch(Exception e) { e.printStackTrace(); }

        a.limparTela();

        //Variaveis do menu
        String opcao;
        byte menuIndex    = 0;
        byte notificacoes = 0;

        boolean sucesso   = false;
        int idSucesso     = -1;

        //Loop do menu
        do {

            //Limpar a String 'opcao'
            opcao = "";

            //Imprimir uma caixa com o titulo
            System.out.println(a.caixa(5,"PERGUNTAS 1.0"));

            //Imprimindo o menu a ser exibido para o usuario
            switch(menuIndex) 
            {

                //Acesso
                case 0:
                    System.out.print(acesso());
                    break;
                //Inicio
                case 1:
                    System.out.print(inicio(notificacoes));
                    break;

                //Criacao de perguntas
                case 2:
                    System.out.print(criacaoDePerguntas());
                    break;

                //Caso a variavel tenha alguma variavel diferente
                default:
                    System.err.println("Erro! Alguma coisa deu muito errado");
                    break;
            }

            //System.out.println(menuIndex);
            //Fazendo leitura do teclado
            opcao = lerEntrada();

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
            switch(opcao) {

                //Menu Acesso

                    case "00": //Saindo do programa
                        System.out.println("Obrigado por usar o programa!\nTenha um excelente dia!\n");
                        break;

                    case "10": // Acessando o sistema usando credenciais existentes
                        sucesso = acessoAoSistema();

                        if(sucesso) {
                            System.out.println("\nSucesso! Login aprovado!\nSeja bem vindo usuário!\n");
                            menuIndex = 1;
                        }
                        else {
                            System.err.println("\nERRO! Login não aprovado!\nTente novamente!\n");
                        }
                        System.out.print("Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();
                        break;

                    case "20": // Criando um novo usuario
                        sucesso = criandoUsuario();
                
                        if(sucesso) {
                            System.out.println("\nSucesso! Novo usuário criado! Agora faça login!\n");
                        }
                        else {
                            System.err.println("\nERRO! Criação de usuário deu errado!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();
                        break;

                    case "30": // Resentando a senha
                        sucesso = senhaTemporaria();

                        if (sucesso) {

                            System.out.println("\nSucesso! A senha da sua conta foi alterada!\nVoltando ao menu...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de mudança de senha!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();
                        break;

                    //Menu Inicio

                    case "01": // Saindo do programa
                        System.out.println("Obrigado por usar o programa!\nTenha um excelente dia\n");
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

                    case "41": 
                        sucesso = novaSenha();

                        if (sucesso) {

                            System.out.println("\nSucesso! A senha da sua conta foi alterada!\nVoltando ao menu...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de mudança de senha!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();
                        break;                       

                    //Menu Criacao de Perguntas

                    case "02": // Voltando a tela de Inicio
                        menuIndex = 1;
                        break;

                    case "12": // Listando as perguntas do usuario atual
                        System.out.print(listagem() + "\n\nPressione qualquer tecla para continuar...");
                        lerEntrada();
                        a.limparTela();
                        break;

                    case "22": // Incluindo uma nova pergunta
                        idSucesso = novaPergunta();

                        if (idSucesso != -1) {

                            System.out.println("\nSucesso! Sua pergunta foi registrada...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de criar pergunta!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();
                        break;

                    case "32": // Alterando uma pergunta atual
                        idSucesso = alterarPergunta();

                        if (idSucesso != -1) {

                            System.out.println("\nSucesso! Sua pergunta foi alterada com sucesso...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de alterar pergunta!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();
                        break;

                    case "42": // Arquivando as perguntas
                        idSucesso = arquivarPergunta();

                        if (idSucesso != -1) {

                            System.out.println("\nSucesso! Sua pergunta foi alterada com sucesso...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de alterar pergunta!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();
                        break;

                    //Operacao Invalida

                    default:
                        System.err.println("Erro! Entrada inválida, tente novamente.");
                        break;
                }

        }while (!opcao.equals("00") && !opcao.equals("01"));


    }

    //Menu 0

    //Opção 1: Acessando o sistema com um usuario já registrado
    /*
    *   Primeiro o usuario tenta inserir o e-mail, se o mesmo consta no banco
    *   de dados, então ele passa para a verificação de senha, se a senha
    *   não for válida, então o usuario não ganha acesso.
    */
    public static boolean acessoAoSistema() {

        String email = "";
        String senha = "";
        boolean resp = false;

        byte tentativas = 3;

        try{

            do {
                System.out.println(a.caixa(5,"Bem vindo usuário!"));
                System.out.println("ACESSO AO SISTEMA\n\nE-mail: ");

                email = lerEntrada();
            
                a.limparTela();
                if(usuarios.read(email) != null) {

                    System.out.println(a.caixa(5,"Insira sua senha:"));
                    System.out.print("\nACESSO AO SISTEMA\n\nSenha: ");
    
                    senha = new GFG().senhaHasheada(lerEntrada());
                    Usuario user = usuarios.read(email);

                    a.limparTela();
                    if (!user.getSenha().equals(senha)) { //conferir a senha no banco de dados

                        tentativas--;
                        System.out.println("Ops! Parece que essa não é a senha!\nTente novamente!\nN° de tentativas: " + tentativas + "\n");

                    }
                    else {

                        IdUsuario = user.getId();
                        resp = true;
                    }
                }
                else {

                    System.out.println("\nO email inserido não é válido ou não existe no banco de dados\nTeste novamente!\nN° de tentativas: " + tentativas + "\n");
                }
            } while( tentativas != 0  && resp == false);

        } catch (Exception e) {System.err.println("Erro ao tentar acessar o Sistema!");}

        return resp;
    }

    // Opção 2 : criar um novo usuario no CRUD
    /*
    *   Primeiro o usuario precisa digitar um email
    *   Caso o email ja esteja no banco de dados entao
    *   a leitura sera feita novamente. Apos isso, o
    *   email precisa ter confirmação por regex, ou
    *   seja, a formatação da String tem que estar
    *   de acordo com o formato de um email.
    *   Caso dê certo, então agora é hora de criar o
    *   usuario e pedir o nome e senha.
    *
    *   Por fim, o usuario irá confirmar se as credenciais estão certas
    *   se for confirmado, o CRUD irá registrar esse novo usuario, caso
    *   contrario, a operação será cancelada.
    */
    public static boolean criandoUsuario() {

        String email     = "";
        String usuario   = "";
        String senha     = "";

        String confirmar = "";
        boolean resp     = false;

        try{

            do {
                System.out.println(a.caixa(5,"Vamos criar um novo usuário!"));
                System.out.println("NOVO USUÁRIO\n\nEmail: ");

                
                email = lerEntrada();
                
                a.limparTela();
            
                if (usuarios.read(email) == null) { //conferir se o email ja esta no banco de dados

                    System.out.println("Esse e-mail já está registrado em nosso sistema\nTente outro e-mail!\n");

                }

            } while (usuarios.read(email) == null); //enquanto o email nao estiver no banco de dados
        
        } catch (Exception r) {/*System.err.println("Deu erro na leitura do email");*/}

        if(verificarEmail(email)) {

            do {
                System.out.println(a.caixa(5,"Digite um nome e senha!"));
                System.out.print("NOVO USUÁRIO\n\nUsuário: ");

                usuario = lerEntrada();

                if ( usuario.length() < 3) {

                    a.limparTela();
                    System.out.println(a.caixa(5,"Usuário inválido!"));
                    System.out.print("Tenha um usuário com no mínimo 3 caracteres!\n" +
                                         "Pressione \"enter\" para continuar...");
                    lerEntrada();
                    a.limparTela();
                }

            } while( usuario.length() < 3 );

            do {
                
                System.out.print("\nSenha: ");
                senha = lerEntrada();
                
                if (verificarSenha(senha) <= 2)
                {
                    a.limparTela();
                    System.out.println(a.caixa(5,"Não recomendamos que use esta senha!"));
                    System.out.println("Considere as recomendações abaixo para uma boa senha:\n" +
                                       "*   -> Ter mais de 8 dígitos\n" +
                                       "*   -> Ter algum caractere em minusculo\n" +
                                       "*   -> Ter algum caractere em maiusculo\n" +
                                       "*   -> Possuir algum caractere especial(Exemplo: *?#)\n" +
                                       "*   -> Possuir pelo menos 1 digito\n\n" +
                                       "Obs: Recomendamos no mínimo uma senha de força 3." + 
                                       "\nPressione enter para continuar...");
                    lerEntrada();
                    a.limparTela();
                    System.out.println(a.caixa(5,"Digite outra senha!"));

                }

            } while (verificarSenha(senha) <= 2);

            a.limparTela();
            System.out.println(a.caixa(5,"Vamos então verificar os seus dados!"));
            System.out.print("\n" +
                               "Email:           " + email     + "\n" +
                               "Nome de usuário: " + usuario   + "\n" +
                               "Senha:           " + senha     + "\n" +
                               "\nEstá tudo de acordo?(s/n) : "       );

            confirmar = lerEntrada();

            a.limparTela();
            if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {

                System.out.println("Certo! Vamos então criar o usuario "+ usuario +" para você!");
                String senhah = new GFG().senhaHasheada(senha);
                Usuario user = new Usuario(usuario,email,senhah);
                int id = usuarios.create(user);

                if(id != -1)
                {
                    resp = true;    
                }
                
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
    
    //Opção 3: Mandar senha temporaria
    /*
    *   O programa irá pedir ao usuário que o mesmo
    *   insira o email usado pelo mesmo para registrar
    *   ao sistema. Se o email for encontrado, então
    *   um 'email' será enviado para o usuário, com
    *   a sua nova senha temporária.
    */
    public static boolean senhaTemporaria() {

        Usuario user = new Usuario();

        String email          = "";
        String senhaTemp      = "";

        boolean senhasIguais  = false;
        boolean resp = false;

        byte forca = -1;

        System.out.println(a.caixa(5,"Vamos resetar sua senha"));
        System.out.println("Por favor insira o seu email");

        //Fazendo leitura do teclado
        try {
            email = lerEntrada();

            a.limparTela();

            if(verificarEmail(email) && usuarios.read(email).getEmail().equals(email)) {

                System.out.println("\nSucesso! uma mensagem com uma senha temporaria foi enviado\nao seu email!\n");

                senhaTemp = gerarSenha();

                user = usuarios.read(email);

                user.setSenha(new GFG().senhaHasheada(senhaTemp));
                usuarios.update(user, user.getId());            

                resp = true;

                escreverEmail(senhaTemp,user.getNome());

                System.out.println("Obs: Procure por um .txt, na pasta do projeto ;)");
                
            }
            else {
                System.err.println("\nO email que foi inserido não é valido\nOu não consta em nosso banco de dados!\n Tente novamente!\n");
            }
    
        }catch(Exception e) {}

        return resp;
    }

    //Menu 1

    //Opção 4 : Criação de uma novaSenha
    /*
    *   Caso o usuário esteja instatisfeito
    *   com a sua senha atual, ele pode pedir os sistema
    *   para atualizar a senha. Para isso o usuário terá que
    *   inserir a senha atual e então colocar a nova senha duas
    *   vezes para ter certeza que ele está ciente em qual é a senha
    */
    public static boolean novaSenha() {

        Usuario user = null;

        String novaSenha      = "";
        String confirmarSenha = "";

        boolean senhasIguais  = false;
        boolean resp          = false;

        byte forca            = -1;

        System.out.println(a.caixa(5,"Vamos resetar sua senha"));
        System.out.println("Por favor insira a sua senha atual");

        //Fazendo leitura do teclado
        try {

            user = usuarios.read(IdUsuario);

            confirmarSenha = lerEntrada();

            a.limparTela();

            if(user.getSenha().equals(new GFG().senhaHasheada(confirmarSenha))) {

                do {
                    System.out.println(a.caixa(5,"Redefinindo senha!"));

                    System.out.println("Nova senha: ");

                    //Leitura da senha
                    /*
                    *   O usuario precisa fazer a inserção da senha
                    *   duas vezes para conferir se o mesmo sabe
                    *   qual e a senha
                    */
                    novaSenha = lerEntrada();
                    System.out.println("\nInsira novamente a senha: ");
                    confirmarSenha = lerEntrada();

                    forca = verificarSenha(novaSenha);
                    senhasIguais = novaSenha.equals(confirmarSenha);

                    a.limparTela();
                    if ( senhasIguais == false ) {

                        System.err.println("ERRO!\nAs duas senhas inseridas não são iguais! Tente novamente\n");
                    }
                    if ( forca <= 2 || senhasIguais == false) {

                        System.err.println("ERRO!   Força da sua senha: " +  forca);
                        System.out.println("Considere as recomendações abaixo para uma boa senha:\n");
                        System.out.print("*   -> Ter mais de 8 dígitos\n" +
                                         "*   -> Ter algum caractere em minusculo\n" +
                                         "*   -> Ter algum caractere em maiusculo\n" +
                                         "*   -> Possuir algum caractere especial(Exemplo: *?#)\n" +
                                         "*   -> Possuir pelo menos 1 digito\n\n" +
                                         "Obs: Recomendamos no mínimo uma senha de força 3.\n" +                                      
                                         "Pressione \"Enter\" para continuar...");

                        lerEntrada();
                        a.limparTela();

                    }
                } while( senhasIguais == false || forca <= 2);

                user.setSenha(new GFG().senhaHasheada(novaSenha));
                usuarios.update(user, user.getId());            

                resp = true;

            }
            else {
                System.err.println("\nEssa não é a senha atual! Tente novamente!\n");
            }
    
        }catch(Exception e) {}

        return resp;
    }

    //Menu 2

    //Opção 1 : Tela de listagem de perguntas
    public static String listagem() {

        String resp = "\nLista de perguntas: \n\n";

        return resp;
    }

    //Opção 2: Criação de pergunta
    /*
    *   Primeiro o usuario precisa inserir a pergunta
    *   se a pergunta não for vazia, ele 
    */
    public static int novaPergunta() {

        int idResp       = -1;
        String pergunta  = "";
        String confirmar = "";

        Pergunta p = null;
 
        System.out.println(a.caixa(5,"Vamos criar uma nova pergunta"));
        System.out.print("\nPor favor insira a sua pergunta: ");

        pergunta = lerEntrada();

        a.limparTela();

        if(!pergunta.equals("")) {

            System.out.println(a.caixa(5,"Vamos conferir a sua pergunta"));
            System.out.print("\nPergunta: " + pergunta + "\n\nEssa é a sua pergunta?(s/n) : ");

            confirmar = lerEntrada();

            a.limparTela();

            if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {

                System.out.println("Certo! a sua pergunta foi criada!");
                p = new Pergunta(IdUsuario,new Date().getTime(),pergunta);

                idResp = perguntas.create(p);
                p.setId(idResp);
                
            }
            else {

                System.out.println("Processo cancelado!\nVoltando para o menu...\n");
            }

        }
        else {
            System.err.println("ERRO! a pergunta inserida está vazia! Tente novamente!");
        }

        return idResp;
    }

    //Opção 3: Alterar a pergunta
    public static int alterarPergunta() {

        int idResposta = -1;
        String entrada = "";
        String pergunta  = "";

        Pergunta p = null;

        System.out.println(a.caixa(5,"Vamos alterar uma pergunta"));
        listagem();

        System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
        entrada = lerEntrada();

        if ( entrada.length() != 0 && !entrada.equals("0")) {

            System.out.println("Ok... vamos encontrar a sua pergunta.");

            try {
                p = perguntas.read(Integer.parseInt(entrada) - 1);
            } catch(Exception e ) {}

            if( p == null ) {

                System.out.print("Sucesso! Pergunta encontrada!\nVamos alterar a sua pergunta, pressione enter para continuar...");
                lerEntrada();

                System.out.println(a.caixa(5,"Vamos criar uma nova pergunta"));
                System.out.print("\nPor favor insira a sua pergunta: ");

                pergunta = lerEntrada();
                
                if(!pergunta.equals("")) {

                    System.out.println(a.caixa(5,"Vamos conferir a sua pergunta"));
                    System.out.print("\nPergunta: " + pergunta + "\n\nEssa é a sua pergunta?(s/n) : ");

                    entrada = lerEntrada();

                    a.limparTela();

                    if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {

                        System.out.println("Certo! a sua pergunta foi criada!");
                        p.setPergunta(pergunta);

                        perguntas.update(p,p.getId());
                    }   
                }
                else {

                     System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                }

            }
            else {

                System.err.println("ERRO! O valor inserido não é válido!\nVoltando ao menu...");
            }

        }
        else {

            System.out.println("Ok! Voltando menu...");
        }

        return idResposta;
    }

    // Opção 4 : Arquivar pergunta
    /*
    *   
    */
    public static int arquivarPergunta() {

        int idResposta = -1;
        String entrada = "";
        String pergunta  = "";

        Pergunta p = null;

        System.out.println(a.caixa(5,"Vamos alterar uma pergunta"));
        listagem();

        System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
        entrada = lerEntrada();

        if ( entrada.length() != 0 && !entrada.equals("0")) {

            System.out.println("Ok... vamos encontrar a sua pergunta.");

            try {
                p = perguntas.read(Integer.parseInt(entrada) - 1);
            } catch(Exception e ) {}

            if( p == null ) {

                System.out.println("Sucesso! Pergunta encontrada!\nVamos imprimir essa pergunta:");

                System.out.print("\nConfirme se essa é a pergunta?(s/n): ");

                entrada = lerEntrada();

                a.limparTela();
                if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {

                    System.out.println("Certo! Vamos então criar o usuario "+ usuario +" para você!");
                    p.setAtiva(false);

                    perguntas.update(p,p.getId());
                    
                }
                else {

                    System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                }

        }
        else {

            System.out.println("Ok! Voltando menu...");
        }

        return idResposta;
    }
    // Funções secundarias

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
	public static byte verificarSenha(String s) {

      //Variavel que vai retornar com a qualidade da senha
	    byte forca = 5;

      //Se a String recebida estiver vazia
	    if (!s.equals("")) {

          //Verificando se a String possui um tamanho menor que 8
    	    if (s.length() < 8) {

    	        forca--;
    	        //System.out.println("Sua senha esta curta!\nRecomendamos uma senha entre 8 e 12 digitos\n");
    	    }

          //Verificando pela existencia de caracteres em minúsculo
    	    if(!s.matches(".*[a-z].*")) {

    	        forca--;
    	        //System.out.println("Tente inserir pelo menos um caractere minusculo\n");
    	    }

          //Verificando pela existencia de caracteres em maiúsculo
    	    if(!s.matches(".*[A-Z].*")) {

    	        forca--;
    	        //System.out.println("Tente inserir pelo menos um caractere maiusculo\n");
    	    }

          //Verificando se a String possui algum caractere especial
    	    if(s.matches("[a-zA-Z0-9 ]*")) {

    	        forca--;
    	        //System.out.println("Coloque um caractere especial na sua senha\n");
    	    }

          //Verificando se possui algum numero
    	    if(!s.matches(".*\\d.*")) {

    	        forca--;
                //System.out.println("Tente inserir pelo menos um numero na sua senha\n");
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

    //Criar senha aleatoria
    /*
    *   Essa função é usada no contexto de criação
    *   de redefinição de senha, quando ele pedir
    *   pela troca da senha, sera enviado ao 'email'
    *   uma senha aleatoria, e essa senha aleatoria
    *   é criada nessa função, usando a classe 'Random'
    */
    public static String gerarSenha() {

        String s = "";
        Random r = new Random();
        do { 
            for (byte i = 0 ; i < 8 ; i++) {
                char a = (char)(r.nextInt(122 - 48) + 48);
                s += a;
            }
            if ( verificarSenha(s) <= 2 ) {
                s = "";
            }
        } while(s.length() == 0);
        
    return s;
  }

  //Operação de escrever email
  /*
  * Essa função serve para simular a operação
  * de enviar a nova senha pelo email, nesse caso
  * o email na verdade é um arquivo .txt com a senha
  * acompanhada de um texto.
  */
  public static void escreverEmail(String senha,String usuario) {

      String saida = "";
      RandomAccessFile r;

      try {
            saida ="Prezado "+ usuario +",tudo bem?\n" + 
                   "Parece que você pediu para mudar de senha e para isso\n" +
                   "estamos te mandando uma nova senha\n\n" +
                   "Essa é a sua nova senha: \n" +
                   senha +
                   "\nNa proxima vez que você logar, você tera\n" +
                   "a possibilidade de mudar de senha\n\n" +
                   "Muito obrigado e um excelente dia!";

            r = new RandomAccessFile("email.txt", "rw");     
                
            r.writeUTF(saida);     

      } catch( Exception e) {
          System.err.println("Deu errado");
      }
  }

    public static String lerEntrada() {
        String resp = "";
        try {
            resp = br.readLine();
        } catch(IOException e ) {System.err.println("Erro na leitura!");}
        return resp;
    }
}
