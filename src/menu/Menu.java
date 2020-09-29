package menu;

import produtos.*;
import crud.*;
import seguranca.*;

public class Menu {
    
    public Crud<Usuario> usuarios;
    public Crud<Pergunta> perguntas;

    public GerenciarP gerenciador = new GerenciarP();

    //Id do Usuario que usar o sistema
    public static int IdUsuario             = - 1;

    public static ASCIInterface graficos   = new ASCIInterface(); // Interface grafica feita em ASCII

    //String com as interfaces dos diferentes menu

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
    public Menu(Crud<Usuario> usuarios,int idUsuario)
    {
        this.IdUsuario = idUsuario;
        this.usuarios  = usuarios;
        try {
            this.perguntas = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
        } catch(Exception e) {} 
    }

    public void gerenciamento() {

        graficos.limparTela();

        //Variaveis do menu
        String opcao;
        byte menuIndex    = 1;
        byte notificacoes = 0;

        boolean sucesso   = false;
        int idSucesso     = -1;

        //Loop do menu
        do {

            //Limpar a String 'opcao'
            opcao = "";

            //Imprimir uma caixa com o titulo
            System.out.println(graficos.caixa(5,"PERGUNTAS 1.0"));

            //Imprimindo o menu a ser exibido para o usuario
            switch(menuIndex) 
            {

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
            opcao = Sistema.lerEntrada();

            //Se o usuario não tenha apenas apertado 'enter' (String vazia)
            if ( opcao.length() != 0 ) {
                opcao = opcao.charAt(0) + String.valueOf((int)menuIndex);
            }

            //limpando a tela
            graficos.limparTela();

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
            *   1 - Inicio
            *   2 - Criacao de Perguntas
            *
            *   Obs: Se o usuario apenas apertou 'enter' deixando a String
            *   vazia, o programa contara isso como um erro.
            */
            switch(opcao) {

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

                        Sistema.lerEntrada();
                        graficos.limparTela();
                        break;                       

                    //Menu Criacao de Perguntas

                    case "02": // Voltando a tela de Inicio
                        menuIndex = 1;
                        break;

                    case "12": // Listando as perguntas do usuario atual
                        System.out.print(gerenciador.listarPerguntas(IdUsuario,perguntas) + "\n\nPressione qualquer tecla para continuar...");
                        Sistema.lerEntrada();
                        graficos.limparTela();
                        break;

                    case "22": // Incluindo uma nova pergunta
                        idSucesso = gerenciador.novaPergunta(IdUsuario,perguntas);

                        if (idSucesso != -1) {

                            System.out.println("\nSucesso! Sua pergunta foi registrada...");
                        }
                        else {
                            System.out.println("\nERRO! Não foi possível fazer a operação de criar pergunta!\nTente novamente!\n");
                        }

                        System.out.print("Pressione \"Enter\" para continuar...");

                        Sistema.lerEntrada();
                        graficos.limparTela();
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

                        Sistema.lerEntrada();
                        graficos.limparTela();
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

                        Sistema.lerEntrada();
                        graficos.limparTela();
                        break;

                    //Operacao Invalida

                    default:
                        System.err.println("Erro! Entrada inválida, tente novamente.");
                        break;
                }

        }while (!opcao.equals("01"));


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

        System.out.println(graficos.caixa(5,"Vamos resetar sua senha"));
        System.out.print("\nPor favor insira a sua senha atual.\nSenha: ");

        //Fazendo leitura do teclado
        try {

            user = usuarios.read(IdUsuario);

            confirmarSenha = Sistema.lerEntrada();

            graficos.limparTela();

            if(user.getSenha().equals(new GFG().senhaHasheada(confirmarSenha))) {

                do {
                    System.out.println(graficos.caixa(5,"Redefinindo senha!"));

                    System.out.println("Nova senha: ");

                    //Leitura da senha
                    /*
                    *   O usuario precisa fazer a inserção da senha
                    *   duas vezes para conferir se o mesmo sabe
                    *   qual e a senha
                    */
                    novaSenha = Sistema.lerEntrada();
                    System.out.println("\nInsira novamente a senha: ");
                    confirmarSenha = Sistema.lerEntrada();

                    forca = Sistema.verificarSenha(novaSenha);
                    senhasIguais = novaSenha.equals(confirmarSenha);

                    graficos.limparTela();
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

                        Sistema.lerEntrada();
                        graficos.limparTela();

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