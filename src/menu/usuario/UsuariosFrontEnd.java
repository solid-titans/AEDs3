package menu.usuario;

import produtos.*;
import menu.sistema.Sistema;
import menu.sistema.graficos.*;
import seguranca.GFG;

/**
 * Classe para administrar casos específicos da admnistração visual dos Usuários
 * @author MysteRys337 (Gustavo Lopes)
 */
public class UsuariosFrontEnd {
    
    //Variaveis de controle de grafico
    private static ANSILibrary   destaque  = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
    private static ANSILibrary   destaque2 = new ANSILibrary(15, 27, ANSILibrary.TEXTO_SUBLINHADO);
    
    private static final byte   TAMANHO_MINIMO_SENHA   = 3;
    private static final byte   TAMANHO_MAXIMO_SENHA   = 50;

    /**
     * Função para dar n tentativas ao usuário de inserir a senha
     * @param senhaDoUsuario é a senha que usuário precisa inserir
     * @param tentativas é a quantidade de tentativas que o loop irá permitir
     * @return true or false se o usuário conseguiu acessar o sistema
     */
    public static boolean inserirSenha(String senhaDoUsuario,int tentativas) {
        String entradaDoUsuario = "";
        boolean sucesso         = false;
  
        do {         
            entradaDoUsuario = new GFG().senhaHasheada(Sistema.inserir(UsuarioAPI.graficos,"Insira a senha","\nNumero de tentativas : " + tentativas,
                                                                                                        TAMANHO_MINIMO_SENHA,TAMANHO_MAXIMO_SENHA,false));
  
            ASCIInterface.limparTela();
            if(!entradaDoUsuario.equals(senhaDoUsuario)) {
                tentativas--;
                System.err.println("Erro! As senhas não são iguais!");
                Sistema.esperarUsuario();
                ASCIInterface.limparTela();

            }
            else {
                sucesso = true;
            }
        } while(!entradaDoUsuario.equals(senhaDoUsuario) && tentativas > 0);
  
        return sucesso;
    }

    /**
     * Função para dar o usuário a oportunidade de inserir uma senha
     * @return a senha que o usuário inseriu
     */
    public static String novaSenha() {
        String senha          = "";
        String confirmarSenha = "";
        byte forcaDaSenha     = -1;

        boolean senhasIguais  = false;

        do {
            senha          = Sistema.inserir(UsuarioAPI.graficos, "Criando senha",TAMANHO_MINIMO_SENHA,TAMANHO_MAXIMO_SENHA,true);
            confirmarSenha = Sistema.inserir(UsuarioAPI.graficos, "Confirmar senha",TAMANHO_MINIMO_SENHA,TAMANHO_MAXIMO_SENHA,false);

            ASCIInterface.limparTela();
            forcaDaSenha = Sistema.verificarSenha(senha);
            senhasIguais = senha.equals(confirmarSenha);

            if ( senhasIguais == false ) {

                System.err.println("ERRO!\nAs duas senhas inseridas não são iguais! Tente novamente\n");
            }
            if ( forcaDaSenha <= 2 || senhasIguais == false) {

                System.err.println("ERRO!   Força da sua senha: " +  forcaDaSenha);
                System.out.println("Considere as recomendações abaixo para uma boa senha:\n");
                System.out.print("*   -> Ter mais de 8 dígitos\n" +
                                "*   -> Ter algum caractere em minusculo\n" +
                                "*   -> Ter algum caractere em maiusculo\n" +
                                "*   -> Possuir algum caractere especial(Exemplo: *?#)\n" +
                                "*   -> Possuir pelo menos 1 digito\n\n" +
                                "Obs: Recomendamos no mínimo uma senha de força 3.\n" +                                      
                                "Pressione \"Enter\" para continuar...");

                Sistema.lerEntrada();
                ASCIInterface.limparTela();
            }
        } while( senhasIguais == false || forcaDaSenha <= 2);;

        return senha;        
    }

    /**
     * Função para verificar se o usuário a ser registrado é o desejado
     * @param novoUsuario é o usuário a ser conferido
     * @return true or false se o usuário confirmou a operação
     */
    public static boolean verificar(Usuario novoUsuario) {
        boolean sucesso = false;
        String  confirmar = "";

        System.out.print(UsuarioAPI.graficos.caixa("Vamos então verificar os seus dados!") + "\n" + 
                                           imprimirUsuario(novoUsuario)                  +
                                           "\nEstá tudo de acordo?(s/n) : "       );

        confirmar = Sistema.lerEntrada();

        ASCIInterface.limparTela();
        if(confirmar.equals("") || confirmar.toLowerCase().equals("s")) {

            sucesso = true;
            System.out.println("Usuário confirmou a operação");

        } else {
            System.err.println("Processo cancelado!\nVoltando para o menu...");
        }

        return sucesso;
    }

    /**
     * Função para imprimir o usuário de forma customizada
     * @param u é o usuario a ser imprimido
     * @return a String que corresponde ao usuário
     */
    public static String imprimirUsuario(Usuario u) {
        return   "\n" +
        destaque.imprimir("Email:           ") + destaque2.imprimir(u.getEmail())     + "\n\n" +
        destaque.imprimir("Nome de usuário: ") + destaque2.imprimir(u.getNome())      + "\n\n" +
        destaque.imprimir("Senha:           ") + destaque2.imprimir(u.getSenha())     + "\n\n";
    }

}