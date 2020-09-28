package menu;

import produtos.*;
import crud.*;
import seguranca.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Menu {
    
    public Crud<Usuario> usuarios;
    public Crud<Pergunta> perguntas;

    //Id do Usuario que usar o sistema
    public static int IdUsuario             = - 1;

    public static ASCIInterface a           = new ASCIInterface(); // Interface grafica feita em ASCII

    //Variaveis para leitura do teclado
    public static InputStream is            = System.in;
    public static InputStreamReader isr     = new InputStreamReader(is);
    public static BufferedReader br         = new BufferedReader(isr);

    //String com as interfaces dos diferentes menu

    //Menu 0 : Tela de Acesso
    public String acesso() {
        return "ACESSO\n\n" +
               "1) Acesso ao sistema\n" +
               "2) Novo usuario\n" +
               "3) Esqueci minha senha\n\n"+
               "0) Sair\n\n" +
               "Opção: ";
    }

    //Menu 1 : Tela de Inicio
    public String inicio(byte notificacoes) {
        return "INÍCIO\n\n" +
               "1) Criação de perguntas\n" +
               "2) Consultar/responder perguntas\n" +
               "3) Notificações: " + notificacoes + "\n\n"+
               "4) Redefinir senha: \n\n" + 
               "0) Sair\n\n" +
               "Opção: ";
    }

    //Menu 2 : Tela de criacaoDePerguntas
    public String criacaoDePerguntas() {
        return "INÍCIO > CRIAÇÃO DE PERGUNTAS\n\n" +
               "1) Listar\n" +
               "2) Incluir\n" +
               "3) Alterar\n"+
               "4) Arquivar\n\n" +
               "0) Retornar ao menu anterior\n\n"+
               "Opção: ";
    }

    // Construtor do Menu
    public Menu(Crud<Usuario> usuarios,Crud<Pergunta> perguntas)
    {
        this.usuarios = usuarios;
        this.perguntas = perguntas;
    }

    public void gerenciamento() {

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
            opcao = Sistema.lerEntrada(br);

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

                        Sistema.lerEntrada(br);
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

                        Sistema.lerEntrada(br);
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

                        Sistema.lerEntrada(br);
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

                        Sistema.lerEntrada(br);
                        a.limparTela();
                        break;                       

                    //Menu Criacao de Perguntas

                    case "02": // Voltando a tela de Inicio
                        menuIndex = 1;
                        break;

                    case "12": // Listando as perguntas do usuario atual
                        //System.out.print(listagem() + "\n\nPressione qualquer tecla para continuar...");
                        Sistema.lerEntrada(br);
                        a.limparTela();
                        break;

                    case "22": // Incluindo uma nova pergunta
                        //idSucesso = novaPergunta();

                        if (idSucesso != -1) {

                            System.out.println("\nSucesso! Sua pergunta foi registrada...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de criar pergunta!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        Sistema.lerEntrada(br);
                        a.limparTela();
                        break;

                    case "32": // Alterando uma pergunta atual
                        //idSucesso = alterarPergunta();

                        if (idSucesso != -1) {

                            System.out.println("\nSucesso! Sua pergunta foi alterada com sucesso...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de alterar pergunta!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        Sistema.lerEntrada(br);
                        a.limparTela();
                        break;

                    case "42": // Arquivando as perguntas
                        //idSucesso = arquivarPergunta();

                        if (idSucesso != -1) {

                            System.out.println("\nSucesso! Sua pergunta foi alterada com sucesso...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de alterar pergunta!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        Sistema.lerEntrada(br);
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
    public boolean acessoAoSistema() {

        String email = "";
        String senha = "";
        boolean resp = false;

        byte tentativas = 3;

        try{

            do {
                System.out.println(a.caixa(5,"Bem vindo usuário!"));
                System.out.println("ACESSO AO SISTEMA\n\nE-mail: ");

                email = Sistema.lerEntrada(br);
            
                a.limparTela();
                if(usuarios.read(email) != null) {

                    System.out.println(a.caixa(5,"Insira sua senha:"));
                    System.out.print("\nACESSO AO SISTEMA\n\nSenha: ");
    
                    senha = new GFG().senhaHasheada(Sistema.lerEntrada(br));
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
    public boolean criandoUsuario() {

        String email     = "";
        String usuario   = "";
        String senha     = "";

        String confirmar = "";
        boolean resp     = false;

        try{

            do {
                System.out.println(a.caixa(5,"Vamos criar um novo usuário!"));
                System.out.println("NOVO USUÁRIO\n\nEmail: ");

                
                email = Sistema.lerEntrada(br);
                
                a.limparTela();
            
                if (usuarios.read(email) == null) { //conferir se o email ja esta no banco de dados

                    System.out.println("Esse e-mail já está registrado em nosso sistema\nTente outro e-mail!\n");

                }

            } while (usuarios.read(email) == null); //enquanto o email nao estiver no banco de dados
        
        } catch (Exception r) {/*System.err.println("Deu erro na leitura do email");*/}

        if(Sistema.verificarEmail(email)) {

            do {
                System.out.println(a.caixa(5,"Digite um nome e senha!"));
                System.out.print("NOVO USUÁRIO\n\nUsuário: ");

                usuario = Sistema.lerEntrada(br);

                if ( usuario.length() < 3) {

                    a.limparTela();
                    System.out.println(a.caixa(5,"Usuário inválido!"));
                    System.out.print("Tenha um usuário com no mínimo 3 caracteres!\n" +
                                         "Pressione \"enter\" para continuar...");
                    Sistema.lerEntrada(br);
                    a.limparTela();
                }

            } while( usuario.length() < 3 );

            do {
                
                System.out.print("\nSenha: ");
                senha = Sistema.lerEntrada(br);
                
                if (Sistema.verificarSenha(senha) <= 2)
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
                    Sistema.lerEntrada(br);
                    a.limparTela();
                    System.out.println(a.caixa(5,"Digite outra senha!"));

                }

            } while (Sistema.verificarSenha(senha) <= 2);

            a.limparTela();
            System.out.println(a.caixa(5,"Vamos então verificar os seus dados!"));
            System.out.print("\n" +
                               "Email:           " + email     + "\n" +
                               "Nome de usuário: " + usuario   + "\n" +
                               "Senha:           " + senha     + "\n" +
                               "\nEstá tudo de acordo?(s/n) : "       );

            confirmar = Sistema.lerEntrada(br);

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
    public boolean senhaTemporaria() {

        Usuario user = new Usuario();

        String email          = "";
        String senhaTemp      = "";

        //boolean senhasIguais  = false;
        boolean resp = false;

        //byte forca = -1;

        System.out.println(a.caixa(5,"Vamos resetar sua senha"));
        System.out.println("Por favor insira o seu email");

        //Fazendo leitura do teclado
        try {
            email = Sistema.lerEntrada(br);

            a.limparTela();

            if(Sistema.verificarEmail(email) && usuarios.read(email).getEmail().equals(email)) {

                System.out.println("\nSucesso! uma mensagem com uma senha temporaria foi enviado\nao seu email!\n");

                senhaTemp = Sistema.gerarSenha();

                user = usuarios.read(email);

                user.setSenha(new GFG().senhaHasheada(senhaTemp));
                usuarios.update(user, user.getId());            

                resp = true;

                Sistema.escreverEmail(senhaTemp,user.getNome());

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
    public boolean novaSenha() {

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

            confirmarSenha = Sistema.lerEntrada(br);

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
                    novaSenha = Sistema.lerEntrada(br);
                    System.out.println("\nInsira novamente a senha: ");
                    confirmarSenha = Sistema.lerEntrada(br);

                    forca = Sistema.verificarSenha(novaSenha);
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

                        Sistema.lerEntrada(br);
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

}