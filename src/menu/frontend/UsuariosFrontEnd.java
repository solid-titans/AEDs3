package menu.frontend;

import menu.frontend.graficos.*;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.backend.misc.Regex;
import menu.frontend.genericos.FrontEnd;

/**
 * Classe para administrar casos específicos da admnistração visual dos Usuários
 * 
 * @author MysteRys337 (Gustavo Lopes)
 */
public class UsuariosFrontEnd extends FrontEnd {
 
    private final byte  TAM_MIN_SENHA;// Tamanho padrao 05 
    private final byte  TAM_MAX_SENHA;// Tamanho padrao 64

    public UsuariosFrontEnd(CustomPrint myPrint, byte TAM_MIN_SENHA, byte TAM_MAX_SENHA, CustomInput myInput, String name) {
        super(myPrint, myInput, name);
        this.TAM_MIN_SENHA = TAM_MIN_SENHA;
        this.TAM_MAX_SENHA = TAM_MAX_SENHA;
    }

    public UsuariosFrontEnd(CustomPrint myPrint, CustomInput myInput, String name) {
        super(myPrint, myInput, name);
        this.TAM_MIN_SENHA = 5;
        this.TAM_MAX_SENHA = 64;
    }
    /**
     * Função para dar n tentativas ao usuário de inserir a senha
     * 
     * @param senhaDoUsuario é a senha que usuário precisa inserir
     * @param tentativas     é a quantidade de tentativas que o loop irá permitir
     * @return um codigo de protocolo referente ao resultado da verificacao
     */
    public CodigoDeProtocolo inserirSenha(UsuarioInterface usuarios, String senhaDoUsuario, int tentativas) {
        String entradaDoUsuario = "";
        CodigoDeProtocolo sucesso = CodigoDeProtocolo.ERRO;

        boolean acertouSenha = false;

        do {
            entradaDoUsuario = myInput.inserir("Insira a senha", "\nNumero de tentativas : " + tentativas,
                                                   TAM_MIN_SENHA, TAM_MAX_SENHA, false);

            if (entradaDoUsuario.equals("")) {
                sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
                return sucesso;
            }

            myPrint.limparTela();
            acertouSenha = usuarios.isSenha(senhaDoUsuario, entradaDoUsuario);

            if (acertouSenha == false) {

                tentativas--;
                System.err.println("Erro! As senhas não são iguais!");
                myInput.esperarUsuario();
                myPrint.limparTela();

            } else {
                sucesso = CodigoDeProtocolo.SUCESSO;
            }

        } while (acertouSenha == false && tentativas > 0);

        return sucesso;
    }

    /**
     * Função para dar o usuário a oportunidade de inserir uma senha
     * 
     * @return a senha que o usuário inseriu
     */
    public String novaSenha() {
        String senha = "";
        String confirmarSenha = "";
        byte forcaDaSenha = -1;

        boolean senhasIguais = false;

        do {
            senha = myInput.inserir("Criando senha", TAM_MIN_SENHA, TAM_MAX_SENHA, true);
            if (senha.equals("")) {
                return "";
            }
            confirmarSenha = myInput.inserir("Confirmar senha", TAM_MIN_SENHA, TAM_MAX_SENHA, false);
            if (confirmarSenha.equals("")) {
                return "";
            }

            myPrint.limparTela();
            forcaDaSenha = Regex.verificarSenha(senha);
            senhasIguais = senha.equals(confirmarSenha);

            if (senhasIguais == false) {

                System.err.println("ERRO!\nAs duas senhas inseridas não são iguais! Tente novamente\n");
            }
            if (forcaDaSenha <= 2 || senhasIguais == false) {

                System.err.println("ERRO!   Força da sua senha: " + forcaDaSenha);
                System.out.println("Considere as recomendações abaixo para uma boa senha:\n");
                System.out.print("*   -> Ter mais de 8 dígitos\n" + "*   -> Ter algum caractere em minusculo\n"
                        + "*   -> Ter algum caractere em maiusculo\n"
                        + "*   -> Possuir algum caractere especial(Exemplo: *?#)\n"
                        + "*   -> Possuir pelo menos 1 digito\n\n"
                        + "Obs: Recomendamos no mínimo uma senha de força 3.\n"
                        + "Pressione \"Enter\" para continuar...");

                myInput.lerString();
                myPrint.limparTela();
            }
        } while (senhasIguais == false || forcaDaSenha <= 2);

        return senha;
    }

}