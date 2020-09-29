import crud.Crud;
import produtos.*;
import menu.*;

public class Main
{
    //Cria o CRUD
    private static Crud<Usuario> usuarios    = null;
    private static ASCIInterface graficos    = new ASCIInterface(); // Interface grafica feita em ASCII
    private static Acesso acesso             = new Acesso();

    //Menu 0 : Tela de Acesso
    private static String acesso() {
        return "ACESSO\n\n" +
               "1) Acesso ao sistema\n" +
               "2) Novo usuario\n" +
               "3) Esqueci minha senha\n\n"+
               "0) Sair\n\n" +
               "Opção: ";
    }

    public static void main(String[] args) {

        try {
            usuarios  = new Crud<>("Usuarios",  Usuario.class.getConstructor());
        } catch(Exception e) { e.printStackTrace(); }

        //limpar a tela
        graficos.limparTela();

        Menu menu;
        char opcao;
        boolean sucesso         = false;

        int id                  = -1;

        //Loop do menu
        do {

            //Limpar a String 'opcao'
            opcao = 'B';

            //Imprimir uma caixa com o titulo
            System.out.println(graficos.caixa(5,"PERGUNTAS 1.0"));

            System.out.print(acesso());

            //Fazendo leitura do teclado
            opcao = Sistema.lerChar();

            //limpando a tela
            graficos.limparTela();

            //System.out.println(opcao);

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
                        id = acesso.acessoAoSistema(usuarios);

                        if(id != -1) {
                            System.out.println("\nSucesso! Login aprovado!\nSeja bem vindo usuário!\n");
                            Sistema.lerChar();

                            menu = new Menu(usuarios,id);
                            menu.gerenciamento();
                    
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
                        sucesso = acesso.criandoUsuario(usuarios);
                
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
                        sucesso = acesso.senhaTemporaria(usuarios);

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

        }while (opcao != '0');
    
    }

}
