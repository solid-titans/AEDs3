package menu;

import menu.pergunta.*;
import menu.sistema.graficos.*;
import menu.sistema.Sistema;
import menu.usuario.*;

public class Menu {

    //Id do Usuario que usar o sistema
    private int IdUsuario             = - 1;
    private ADMPerguntas minhasPerguntas = new ADMPerguntas();
    private ADMUsuario   meusUsuarios    = new ADMUsuario();

    public static ASCIInterface graficos   = new ASCIInterface(); // Interface grafica feita em ASCII

    public Menu() {

        IdUsuario       = -1;
        minhasPerguntas = new ADMPerguntas();
        meusUsuarios    = new ADMUsuario();
    }

    public void Inicio() {

        char opcao;

        boolean sucesso   = false;
        int idSucesso     = -1;

        do {

            opcao = Selecao.Acesso();
            //Fazendo a interação com o acesso
            /*
            *   O acesso ao sistema funciona a partir de um
            *   switch, com o char 'opcao', cada case representa
            *   a opcao escolhida pelo usuario
            *
            *   Lista de opções:
            *
            *   0 - Sair
            *   1 - Acessando o Sistema
            *   2 - Criacao de usuario
            *   3 - Senha temporaria
            *
            *   Obs: Se o usuario apenas apertou 'enter' deixando a String
            *   vazia, o programa contara isso como um erro.
            */
            switch(opcao) {

            //Menu Acesso

                case '0': //Saindo do programa
                    System.out.println("Obrigado por usar o programa!\nTenha um excelente dia!\n");
                    break;

                case '1': // Acessando o sistema usando credenciais existentes
                    idSucesso = meusUsuarios.acessoAoSistema();

                    if(idSucesso != -1) {

                        IdUsuario = idSucesso;
                        System.out.println("\nSucesso! Login aprovado!\nSeja bem vindo usuário!\n\nPressione \"Enter\" para continuar...");
                        Sistema.lerChar();

                        graficos.limparTela();

                        AcessoGarantido();
                
                        opcao = '0';
                    }
                    else {
                        System.err.println("\nERRO! Login não aprovado!\nTente novamente!\n");
                    }
                    System.out.print("Pressione \"Enter\" para continuar...");

                    Sistema.lerEntrada();
                    graficos.limparTela();
                    break;

                case '2': // Criando um novo usuario
                    sucesso = meusUsuarios.criandoUsuario();
            
                    if(sucesso) {
                        System.out.println("\nSucesso! Novo usuário criado! Agora faça login!\n");
                    }
                    else {
                        System.err.println("\nERRO! Criação de usuário deu errado!\nTente novamente!\n");
                    }

                    System.out.print("Pressione \"Enter\" para continuar...");

                    Sistema.lerEntrada();
                    graficos.limparTela();
                    break;

                case '3': // Resentando a senha
                    sucesso = meusUsuarios.senhaTemporaria();

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

                //Operacao Invalida

                default:
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

        } while (opcao != '0');
    }

    private void AcessoGarantido() {

        String opcao = "";

        byte menuIndex    = 1;
        byte notificacoes = 0;
    
        boolean sucesso   = false;
        int idSucesso     = -1;

        //Loop do menu
        do {

            opcao = Selecao.Inicio(menuIndex,notificacoes);
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
                    sucesso = meusUsuarios.novaSenha(IdUsuario);

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
                    System.out.print(minhasPerguntas.listarPerguntas(IdUsuario) + "\n\nPressione qualquer tecla para continuar...");
                    Sistema.lerEntrada();
                    graficos.limparTela();
                    break;

                case "22": // Incluindo uma nova pergunta
                    idSucesso = minhasPerguntas.novaPergunta(IdUsuario);

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
                    idSucesso = minhasPerguntas.alterarPergunta(IdUsuario);

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
                    idSucesso = minhasPerguntas.arquivarPergunta(IdUsuario);

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

}