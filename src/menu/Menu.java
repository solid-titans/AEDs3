package menu;

import menu.frontend.graficos.CustomPrint;
import menu.api.APIControle;
import menu.backend.input.Input;
import menu.backend.misc.CodigoDeProtocolo;
import produtos.CelulaResposta;
import produtos.Pergunta;
import produtos.Usuario;

/**
 *
 * Classe para gerenciar as escolhas do usuário e a comunicação com a CrudAPI
 * 
 * @author MysteRys337
 * @version 0.0.1
 */
public class Menu {

    private CustomPrint myPrint; // Interface gráfica

    private int         idUsuario; // Id do Usuario que usar o sistema
    private APIControle minhaAPI; // Gerenciador do Crud e para direcionar as decisões do usuário
    private Selecao     selecao;
    private Input       input;

    /**
     * Configura a id do usuário que vai acessar o programa
     * 
     * @param id
     */
    public Menu(CustomPrint myPrint, APIControle minhaAPI, Selecao selecao) {

        this.idUsuario = -1;         // Inicia como '-1' pois nenhum usuário estará logado de imediato
        this.minhaAPI  = minhaAPI;
        this.myPrint   = myPrint;
        this.selecao   = selecao;
        this.input     = new Input();
    }

    /**
     * Função que dará acesso inicial ao programa
     */
    public void Inicio() {

        char opcao;
        CelulaResposta resultadoVerificacao = new CelulaResposta();

        CodigoDeProtocolo opcaoEscolhida;

        myPrint.limparTela();
        do {

            System.out.println(myPrint.imprimir("[PERGUNTAS 1.0]"));
            opcao = selecao.Acesso();

            opcaoEscolhida = CodigoDeProtocolo.NULL;
            // Fazendo a interação com o acesso
            /*
             * O acesso ao sistema funciona a partir de um switch, com o char 'opcao', cada
             * case representa a opcao escolhida pelo usuario
             *
             * Lista de opções:
             *
             * 0 - Sair 1 - Acessando o Sistema 2 - Criacao de usuario 3 - Senha temporaria
             *
             * Obs: Se o usuario apenas apertou 'enter' deixando a String vazia, o programa
             * contara isso como um erro.
             */
            switch (opcao) {

                // Menu Acesso

                case '0': // Saindo do programa
                    System.out.println("Obrigado por usar o Pergunta 1.0\nTenha um bom dia!");
                    break;

                case '1': // Acessando o sistema usando credenciais existentes
                    opcaoEscolhida = CodigoDeProtocolo.ACESSOAOSISTEMA;
                    break;

                case '2': // Criando um novo usuario
                    opcaoEscolhida = CodigoDeProtocolo.CRIARNOVOUSUARIO;
                    break;

                case '3': // Resentando a senha
                    opcaoEscolhida = CodigoDeProtocolo.CRIARSENHATEMPORARIA;
                    break;

                // Operacao Invalida
                default:
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if (opcaoEscolhida != CodigoDeProtocolo.NULL) {

                resultadoVerificacao = minhaAPI.requisicaoEmAcesso(opcaoEscolhida);
                input.esperarUsuario();
                if (resultadoVerificacao.getCdp() == CodigoDeProtocolo.MUDARUSUARIO) {

                    myPrint.getInterface().setBorda(113);
                    idUsuario = resultadoVerificacao.getUsuario().getId();
                    acessoGarantido();

                }

            }

            myPrint.limparTela();
        } while (opcao != '0');
    }

    /**
     * Função onde o usuário terá acesso ao programa em si
     */
    private void acessoGarantido() {

        String opcao = "";

        byte menuIndex = 1;
        byte notificacoes = 0;

        CelulaResposta resultadoVerificacao = new CelulaResposta();

        CodigoDeProtocolo opcaoEscolhida;

        // Loop do menu
        do {
            myPrint.limparTela();

            System.out.println(myPrint.imprimir("[PERGUNTAS 1.0]"));
            opcao = selecao.imprimirTela(menuIndex, notificacoes);

            opcaoEscolhida = CodigoDeProtocolo.NULL;

            // Fazendo a mudança do menu
            /*
             * Eis aqui uma explicacao de como o sistema de menu funciona Primeiro o
             * programa faz a leitura do teclado , apos isso, ele soma o valor da variavel
             * 'menuIndex' com o que foi lido. O resultado disso era uma String de 2
             * caracteres, sendo o primeiro a opcao escolhida pelo usuario, e a segunda o
             * menu na qual ele esta atualmente.
             *
             * Lista de menus:
             *
             * 1 - Inicio 2 - Criacao de Perguntas
             *
             * Obs: Se o usuario apenas apertou 'enter' deixando a String vazia, o programa
             * contara isso como um erro.
             */
            switch (opcao) {

                // Menu Inicio

                case "01": // Saindo do programa
                    System.out.println("\n+-----------------------------------------------------+");
                    System.out.println("|          Obrigado por usar o programa!              |");
                    System.out.println("|              Tenha um excelente dia                 |");
                    System.out.println("+-----------------------------------------------------+\n");

                    input.esperarUsuario();
                    idUsuario = -1;
                    myPrint.getInterface().setBorda(27);
                    break;

                case "11": // Indo para a tela de criacao de perguntas
                    menuIndex = 2;
                    break;

                case "21": // Indo para a tela de consultar/responder perguntas
                    opcaoEscolhida = CodigoDeProtocolo.CONSULTARPERGUNTAS;
                    break;

                case "31": // Verificar suas notificacoes
                    opcaoEscolhida = CodigoDeProtocolo.OLHARNOTIFICACOES;
                    break;

                case "41":
                    opcaoEscolhida = CodigoDeProtocolo.NOVASENHA;
                    break;

                // Menu Criacao de Perguntas

                case "02": // Voltando a tela de Inicio
                    menuIndex = 1;
                    break;

                case "12": // Listando as perguntas do usuario atual
                    opcaoEscolhida = CodigoDeProtocolo.LISTARPERGUNTAS;
                    break;

                case "22": // Incluindo uma nova pergunta
                    opcaoEscolhida = CodigoDeProtocolo.NOVAPERGUNTA;
                    break;

                case "32": // Alterando uma pergunta atual
                    opcaoEscolhida = CodigoDeProtocolo.ALTERARPERGUNTA;
                    break;

                case "42": // Arquivando as perguntas
                    opcaoEscolhida = CodigoDeProtocolo.ARQUIVARPERGUNTA;
                    break;

                // Operacao Invalida

                default:
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if (opcaoEscolhida != CodigoDeProtocolo.NULL) {
                resultadoVerificacao = minhaAPI.requisicaoEmInicio(opcaoEscolhida, idUsuario);
                input.esperarUsuario();

                if (resultadoVerificacao.getCdp() == CodigoDeProtocolo.IRPARAPERGUNTA) 
                    navegarPergunta(resultadoVerificacao.getPergunta(), resultadoVerificacao.getUsuario());

            }

        } while (!opcao.equals("01"));

    }

    public CodigoDeProtocolo navegarPergunta(Pergunta pergunta, Usuario u) {

        selecao.mudarCorBorda(220);

        String opcao = "";

        byte menuIndex = 3;

        CelulaResposta resultadoVerificacao = new CelulaResposta();
        CodigoDeProtocolo opcaoEscolhida;

        do {
            myPrint.limparTela();

            System.out.println(myPrint.imprimir(pergunta.imprimir(u.getNome())));

            //Imprimir o voto do usuario na pergunta, se a pergunta em si não for do usuário
            if ( pergunta.getIdUsuario() != idUsuario )
                System.out.println("\n" + minhaAPI.recuperarNota(idUsuario + "|P|" + pergunta.getId()) + "\n");

            opcao = selecao.imprimirTela(menuIndex, (byte) -1);

            opcaoEscolhida = CodigoDeProtocolo.NULL;

            switch (opcao) {
                case "03":
                    System.out.println("Ok! Voltando ao menu...");
                    break;

                case "13":
                    opcaoEscolhida = CodigoDeProtocolo.LISTARRESPOSTASGERAL;
                    break;

                case "23": 
                    opcaoEscolhida = CodigoDeProtocolo.COMENTARIOSPERGUNTA;
                    break;

                case "33": // Menu de respostas
                    if (pergunta.getIdUsuario() == idUsuario) {
                        myPrint.limparTela();
                        System.out.println("Você não pode responder a sua própria pergunta!");
                        input.esperarUsuario();

                    } else { 
                        menuIndex = 4;
                    }
                    
                    break;

                case "43": // Menu de comentarios
                    menuIndex = 5;
                    break;

                case "53": // Menu de votos
                    menuIndex = 6;
                    break;

                case "04": // Voltar ao menu de navegar pela resposta escolhida 
                    menuIndex = 3;
                    break;

                case "14":
                    opcaoEscolhida = CodigoDeProtocolo.LISTARRESPOSTASUSUARIO;
                    break;

                case "24":
                    opcaoEscolhida = CodigoDeProtocolo.INCLUIRRESPOSTA;
                    break;

                case "34":
                    opcaoEscolhida = CodigoDeProtocolo.ALTERARRESPOSTA;
                    break;

                case "44":
                    opcaoEscolhida = CodigoDeProtocolo.ARQUIVARRESPOSTA;
                    break;

                case "05":
                    menuIndex = 3;
                    break;

                case "15":
                    opcaoEscolhida = CodigoDeProtocolo.COMENTARPERGUNTA;
                    break;
                  
                case "25":
                    opcaoEscolhida = CodigoDeProtocolo.COMENTARRESPOSTA;
                    break;

                case "06":
                    menuIndex = 3;
                    break;

                case "16":
                    if (pergunta.getIdUsuario() == idUsuario) {
                        myPrint.limparTela();
                        System.out.println("Você não pode votar na sua própria pergunta!");
                        input.esperarUsuario();

                    } else { 
                        opcaoEscolhida = CodigoDeProtocolo.VOTAREMPERGUNTA;
                    }
                    break;

                case "26":
                    opcaoEscolhida = CodigoDeProtocolo.VOTAREMRESPOSTA;
                    break;
                    
                default:
                    System.err.println("Erro! Entrada inválida, tente novamente.");
                    break;
            }

            if (opcaoEscolhida != CodigoDeProtocolo.NULL) {
                resultadoVerificacao = minhaAPI.requisicaoEmPerguntas(opcaoEscolhida, pergunta.getId(),idUsuario);
                input.esperarUsuario();

            }

        } while (!opcao.equals("03"));

        myPrint.getInterface().setBorda(113);
        return opcaoEscolhida;
    }

}