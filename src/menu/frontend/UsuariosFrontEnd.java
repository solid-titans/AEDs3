package menu.frontend;

import menu.frontend.graficos.*;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.backend.misc.Regex;
import menu.frontend.abstracts.FrontEnd;
import produtos.abstracts.RegistroVisual;

/**
 * Classe para administrar casos específicos da admnistração visual dos Usuários
 * 
 * @author MysteRys337 (Gustavo Lopes)
 */
public class UsuariosFrontEnd implements FrontEnd {

    // Variaveis de controle de grafico
    private CustomPrint myPrint;
 
    private final byte  TAM_MIN_SENHA;// Tamanho padrao 05 
    private final byte  TAM_MAX_SENHA;// Tamanho padrao 64

    private CustomInput customInput;

    public UsuariosFrontEnd(CustomPrint myPrint, byte TAM_MIN_SENHA, byte TAM_MAX_SENHA, CustomInput customInput) {
        this.TAM_MIN_SENHA = TAM_MIN_SENHA;
        this.TAM_MAX_SENHA = TAM_MAX_SENHA;

        this.myPrint       = myPrint;

        this.customInput   = customInput;
    }

    public UsuariosFrontEnd(CustomPrint myPrint, CustomInput customInput) {
        this.TAM_MIN_SENHA = 5;
        this.TAM_MAX_SENHA = 64;

        this.myPrint       = myPrint;

        this.customInput   = customInput;
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
            entradaDoUsuario = customInput.inserir("Insira a senha", "\nNumero de tentativas : " + tentativas,
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
                customInput.esperarUsuario();
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
            senha = customInput.inserir("Criando senha", TAM_MIN_SENHA, TAM_MAX_SENHA, true);
            if (senha.equals("")) {
                return "";
            }
            confirmarSenha = customInput.inserir("Confirmar senha", TAM_MIN_SENHA, TAM_MAX_SENHA, false);
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

                customInput.lerString();
                myPrint.limparTela();
            }
        } while (senhasIguais == false || forcaDaSenha <= 2);
        ;

        return senha;
    }

    /**
     * Função para verificar se o usuário a ser registrado é o desejado
     * 
     * @param novoUsuario é o usuário a ser conferido
     * @return um codigo de protocolo referente ao resultado da verificacao
     */
    public CodigoDeProtocolo verificar(RegistroVisual novoUsuario) {
        CodigoDeProtocolo sucesso = CodigoDeProtocolo.ERRO;
        String confirmar = "";

        System.out.print(myPrint.imprimir("{Vamos então conferir o cadastro:}\n[Dados do usuário]") + "\n" + 
                         myPrint.imprimir(novoUsuario.imprimir()));

        confirmar = customInput.lerString("\nEstá tudo de acordo?(s/n) : ");

        myPrint.limparTela();
        if (confirmar.equals("") || confirmar.toLowerCase().equals("s")) {

            sucesso = CodigoDeProtocolo.SUCESSO;
            System.out.println("Usuário confirmou a operação");

        } else {
            sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
            System.err.println("Processo cancelado!\nVoltando para o menu...");
        }

        return sucesso;
    }

}