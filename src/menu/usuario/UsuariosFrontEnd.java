package menu.usuario;

import produtos.*;
import menu.sistema.controle.CodigoDeProtocolo;
import menu.sistema.graficos.*;
import menu.sistema.input.CustomInput;
import menu.sistema.input.Input;
import menu.sistema.misc.Regex;
import menu.sistema.abstracts.frontend.FrontEnd;
import menu.sistema.abstracts.frontend.RegistroVisual;

/**
 * Classe para administrar casos específicos da admnistração visual dos Usuários
 * @author MysteRys337 (Gustavo Lopes)
 */
public class UsuariosFrontEnd implements FrontEnd {
    
    //Variaveis de controle de grafico
    private ASCIInterface graficos;
    
    private final byte    TAM_MIN_SENHA;
    private final byte    TAM_MAX_SENHA;

    private CustomInput   customInput;

    public UsuariosFrontEnd(ASCIInterface graficos, byte TAM_MIN_SENHA, byte TAM_MAX_SENHA,CustomInput customInput) {
        this.TAM_MIN_SENHA = TAM_MIN_SENHA;
        this.TAM_MAX_SENHA = TAM_MAX_SENHA;

        this.graficos      = graficos;

        this.customInput   = customInput;
    }

    /**
     * Função para dar n tentativas ao usuário de inserir a senha
     * @param senhaDoUsuario é a senha que usuário precisa inserir
     * @param tentativas é a quantidade de tentativas que o loop irá permitir
     * @return um codigo de protocolo referente ao resultado da verificacao
     */
    public CodigoDeProtocolo inserirSenha(String senhaDoUsuario,int tentativas) {
        String            entradaDoUsuario   = "";
        CodigoDeProtocolo sucesso            = CodigoDeProtocolo.ERRO;
  
        do {         
            entradaDoUsuario = customInput.inserir("Insira a senha","\nNumero de tentativas : " + tentativas,TAM_MIN_SENHA,TAM_MAX_SENHA,false);
            if(entradaDoUsuario.equals("")) {
                sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
                return sucesso;
            }
  
            graficos.limparTela();

            if(!APIControle.hasheador.verificarHash(senhaDoUsuario,entradaDoUsuario)) {

                tentativas--;
                System.err.println("Erro! As senhas não são iguais!");
                customInput.esperarUsuario();
                graficos.limparTela();

            } else {
                sucesso = CodigoDeProtocolo.SUCESSO;
            }

        } while(!APIControle.hasheador.verificarHash(senhaDoUsuario,entradaDoUsuario) && tentativas > 0);
  
        return sucesso;
    }

    /**
     * Função para dar o usuário a oportunidade de inserir uma senha
     * @return a senha que o usuário inseriu
     */
    public String novaSenha() {
        String senha          = "";
        String confirmarSenha = "";
        byte forcaDaSenha     = -1;

        boolean senhasIguais  = false;

        do {
            senha = customInput.inserir("Criando senha",TAM_MIN_SENHA,TAM_MAX_SENHA,true);
            if(senha.equals("")) {
                return "";
            }
            confirmarSenha = customInput.inserir("Confirmar senha",TAM_MIN_SENHA,TAM_MAX_SENHA,false);
            if(confirmarSenha.equals("")) {
                return "";
            }            

            graficos.limparTela();
            forcaDaSenha = Regex.verificarSenha(senha);
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

                customInput.lerString();
                graficos.limparTela();
            }
        } while( senhasIguais == false || forcaDaSenha <= 2);;

        return senha;        
    }

    /**
     * Função para verificar se o usuário a ser registrado é o desejado
     * @param novoUsuario é o usuário a ser conferido
     * @return um codigo de protocolo referente ao resultado da verificacao
     */
    public CodigoDeProtocolo verificar(RegistroVisual novoUsuario) {
        CodigoDeProtocolo sucesso     = CodigoDeProtocolo.ERRO;
        String            confirmar   = "";

        System.out.print(graficos.caixa("Vamos então verificar os seus dados!") + "\n" + 
                                        novoUsuario.imprimir()                  +
                                        "\nEstá tudo de acordo?(s/n) : "       );

        confirmar = customInput.lerString();

        graficos.limparTela();
        if(confirmar.equals("") || confirmar.toLowerCase().equals("s")) {

            sucesso = CodigoDeProtocolo.SUCESSO;
            System.out.println("Usuário confirmou a operação");

        } else {
            sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
            System.err.println("Processo cancelado!\nVoltando para o menu...");
        }

        return sucesso;
    }

}