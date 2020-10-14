package menu;

import menu.sistema.graficos.*;
import menu.sistema.*;
import produtos.Pergunta;
import menu.pergunta.PerguntasFrontEnd;

/**
 * Classe para gerenciar as escolhas do usuário e a comunicação com a CrudAPI
 * @author  MysteRys337
 * @version 0.0.1
 */
public class Menu {

    private ASCIInterface              graficos;

    private static int         idUsuario;                  //Id do Usuario que usar o sistema
    private static CrudAPI            minhaAPI;                   //Gerenciador do Crud e para direcionar as decisões do usuário
    private static CodigoDeProtocolo  requisicao;                 //Variavel para 

    /**
     * Configura a id do usuário que vai acessar o programa
     * @param id
     */
    public static void setId(int id) {
        idUsuario = id;
    }

    public Menu() {

        idUsuario      = -1;
        minhaAPI       = new CrudAPI();
        graficos       = new ASCIInterface();
    }

    /**
     * Função que dará acesso inicial ao programa
     */
    public void Inicio() {

        char opcao;

        ASCIInterface.limparTela();
        do {

            System.out.println(graficos.caixa("PERGUNTAS 1.0"));
            opcao = Selecao.Acesso();

            requisicao = CodigoDeProtocolo.NULL;
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
                    System.out.println("Obrigado por usar o Pergunta 1.0\nTenha um bom dia!");
                    break;

                case '1': // Acessando o sistema usando credenciais existentes
                    requisicao = CodigoDeProtocolo.ACESSOAOSISTEMA;
                    break;

                case '2': // Criando um novo usuario
                    requisicao = CodigoDeProtocolo.CRIARNOVOUSUARIO;
                    break;

                case '3': // Resentando a senha
                    requisicao = CodigoDeProtocolo.CRIARSENHATEMPORARIA;
                    break;

                //Operacao Invalida
                default:
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if(requisicao != CodigoDeProtocolo.NULL) {
                requisicao = minhaAPI.verificarRequisicaoEmAcesso(requisicao);
                if(requisicao == CodigoDeProtocolo.MUDARUSUARIO) {
                    graficos.setBorda(63);
                    acessoGarantido();
                    
                }

            }
            
        ASCIInterface.limparTela();
        } while (opcao != '0');
    }

    /**
     * Função onde o usuário terá acesso ao programa em si
     */
    private void acessoGarantido() {

        String opcao = "";

        byte menuIndex    = 1;
        byte notificacoes = 0;

        //Loop do menu
        do {
            ASCIInterface.limparTela();

            System.out.println(graficos.caixa("PERGUNTAS 1.0"));
            opcao = Selecao.imprimirTela(menuIndex,notificacoes);

            requisicao = CodigoDeProtocolo.NULL;

            //Fazendo a mudança do menu
            /*
            *   Eis aqui uma explicacao de como o sistema de menu funciona
            *   Primeiro o programa faz a leitura do teclado , 
            *   apos isso, ele soma o valor da variavel 'menuIndex' 
            *   com o que foi lido. O resultado disso era uma String 
            *   de 2 caracteres, sendo o primeiro a opcao escolhida 
            *   pelo usuario, e a segunda o menu na qual ele esta atualmente.
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
                    graficos.setBorda(1);
                    break;

                case "11": // Indo para a tela de criacao de perguntas
                    menuIndex = 2;
                    break;

                case "21": // Indo para a tela de consultar/responder perguntas
                    requisicao = CodigoDeProtocolo.CONSULTARPERGUNTAS;
                    break;

                case "31": // Verificar suas notificacoes
                    requisicao = CodigoDeProtocolo.OLHARNOTIFICACOES;
                    break;

                case "41": 
                    requisicao = CodigoDeProtocolo.NOVASENHA;
                    break;                       

                //Menu Criacao de Perguntas

                case "02": // Voltando a tela de Inicio
                    menuIndex = 1;
                    break;

                case "12": // Listando as perguntas do usuario atual
                    requisicao = CodigoDeProtocolo.LISTARPERGUNTAS;
                    break;

                case "22": // Incluindo uma nova pergunta
                    requisicao = CodigoDeProtocolo.NOVAPERGUNTA;
                    break;

                case "32": // Alterando uma pergunta atual
                    requisicao = CodigoDeProtocolo.ALTERARPERGUNTA;
                    break;

                case "42": // Arquivando as perguntas
                    requisicao = CodigoDeProtocolo.ARQUIVARPERGUNTA;
                    break;

                //Operacao Invalida

                default:
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if(requisicao != CodigoDeProtocolo.NULL) {
                requisicao = minhaAPI.verificarRequisicaoDoUsuario(requisicao,idUsuario);

            }

        }while (!opcao.equals("01"));

    }

    public static CodigoDeProtocolo navegarPergunta(Pergunta p,String nome) {

        Selecao.graficos.setBorda(220);
        
        String opcao = "";
        
        byte menuIndex = 3;

        do {
            ASCIInterface.limparTela();

            System.out.println(PerguntasFrontEnd.toString(p, nome));

            opcao = Selecao.imprimirTela(menuIndex,(byte)-1);

            requisicao = CodigoDeProtocolo.NULL;

            switch(opcao) {
                case "03":
                    System.out.println("Ok! Voltando ao menu...");
                    break;

                case "13":
                    menuIndex = 4;
                    break;
                
                case "23":
                    menuIndex = 5;
                    break;
                
                case "33":
                    menuIndex = 6;
                    break;

                case "04":
                    menuIndex = 3;
                    break;

                case "14":
                    requisicao = CodigoDeProtocolo.LISTARRESPOSTAS;
                    break;
                
                case "24":
                    requisicao = CodigoDeProtocolo.INCLUIRRESPOSTA;
                    break;
                
                case "34":
                    requisicao = CodigoDeProtocolo.ALTERARRESPOSTA;
                    break;
                
                case "44":
                    requisicao = CodigoDeProtocolo.ARQUIVARRESPOSTA;
                    break;

                case "05":
                    menuIndex = 3;
                    break;

                default:
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if(requisicao != CodigoDeProtocolo.NULL) {
                requisicao = minhaAPI.verificarRequisicaoEmPergunta(requisicao,p.getId(),idUsuario);

            }

        } while(!opcao.equals("03"));


        return requisicao;
    }

}