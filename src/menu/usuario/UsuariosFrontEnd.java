package menu.usuario;

import produtos.*;
import menu.sistema.Sistema;
import menu.sistema.graficos.*;

public class UsuariosFrontEnd {
    
    private static ANSILibrary   destaque  = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
    private static ANSILibrary   destaque2 = new ANSILibrary(15, 27, ANSILibrary.TEXTO_SUBLINHADO);

    public static String novaSenha(String titulo) {
        String senha          = "";
        String confirmarSenha = "";
        byte tentativas       = 3;
        byte forcaDaSenha     = -1;

        boolean senhasIguais  = false;

        do {

            senha          = Sistema.inserir(UsuarioAPI.graficos, titulo, "Tentativas : " + tentativas,3);

            confirmarSenha = Sistema.inserir(UsuarioAPI.graficos, "Confirmar senha",3);

            ASCIInterface.limparTela();
            forcaDaSenha = Sistema.verificarSenha(senha);
            senhasIguais = senha.equals(confirmarSenha);

            if ( senhasIguais == false ) {

                System.err.println("ERRO!\nAs duas senhas inseridas não são iguais! Tente novamente\n");
                tentativas--;
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
                tentativas--;
                ASCIInterface.limparTela();

            }

        } while( senhasIguais == false || forcaDaSenha <= 2);;

        return senha;        
    }

    public static boolean verificar(Usuario novoUsuario) {

        boolean sucesso = false;
        String  confirmar = "";

        System.out.println(UsuarioAPI.graficos.caixa("Vamos então verificar os seus dados!") + "\n" + 
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

    public static String imprimirUsuario(Usuario u) {
        return   "\n" +
        destaque.imprimir("Email:           ") + destaque2.imprimir(u.getEmail())     + "\n\n" +
        destaque.imprimir("Nome de usuário: ") + destaque2.imprimir(u.getNome())      + "\n\n" +
        destaque.imprimir("Senha:           ") + destaque2.imprimir(u.getSenha())     + "\n\n";
    }

}