package menu;

import menu.sistema.graficos.*;
import menu.sistema.*;

/**
 * Classe para gerenciar as escolhas do usuário e a comunicação com a CrudAPI
 * @author  MysteRys337
 * @version 0.0.1
 */
public class Menu {

    private static int         idUsuario;                  //Id do Usuario que usar o sistema
    private CrudAPI            minhaAPI;                   //Gerenciador do Crud e para direcionar as decisões do usuário
    private CodigoDeProtocolo  requisicao;                 //Variavel para 

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
    }

    /**
     * Função que dará acesso inicial ao programa
     */
    public void Inicio() {

        char opcao;

        do {
            ASCIInterface.limparTela();

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
                    requisicao = CodigoDeProtocolo.NULL;
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
                    requisicao = CodigoDeProtocolo.NULL;
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if(requisicao != CodigoDeProtocolo.NULL) {
                requisicao = minhaAPI.verificarRequisicaoEmAcesso(requisicao);
                if(requisicao == CodigoDeProtocolo.MUDARUSUARIO) {
                    Selecao.graficos.setBorda(63);
                    acessoGarantido();
                    
                }

            }

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
            opcao = Selecao.Inicio(menuIndex,notificacoes);

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
                    requisicao = CodigoDeProtocolo.NULL;
                    System.out.println("Obrigado por usar o programa!\nTenha um excelente dia\n");
                    Selecao.graficos.setBorda(1);
                    break;

                case "11": // Indo para a tela de criacao de perguntas
                    requisicao = CodigoDeProtocolo.NULL;
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
                    requisicao = CodigoDeProtocolo.NULL;
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
                    requisicao = CodigoDeProtocolo.NULL;
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if(requisicao != CodigoDeProtocolo.NULL) {
                requisicao = minhaAPI.verificarRequisicaoDoUsuario(requisicao,idUsuario);

            }

        }while (!opcao.equals("01"));

    }

}