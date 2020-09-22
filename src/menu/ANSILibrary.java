/*
*   Classe com configurações para imprensão 
*   personalizada no terminal
*
*   Criada por: MysteRys337
*   Versão : 0.0.1
*/

package menu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ANSILibrary{

    //Utilizando REGEX para controlar a entrada de parametros
    Pattern p;
    Matcher m;

    //  Variavel para resetar 
    public final String ANSI_RESETAR                = "\u001B[0m";

    //  Tipo de destaque ao texto
    public static final String TEXTO_ITALICO	    = "\u001B[3m";
    public static final String TEXTO_SUBLINHADO     = "\u001B[4m";
    public static final String TEXTO_NEGRITO        = "\u001B[1m";

    //  Variaveis para configurar o texto
    private short fundo;
    private short cor;
    private String destaque;

    //Construtor
    public ANSILibrary() { 

        fundo = cor = -1;
        destaque = "";

        //Configurando o padrão que será utilizado pelo programa
        //para verificar expressões.
        p = Pattern.compile("^\u001B\\[\\d{1,2}m", Pattern.CASE_INSENSITIVE);
    }

    //  Funções de 'set'
    /*
    *   Todas as funções de set
    *   possuem esse laço condicional
    *   para verificar se o usuario
    *   entrou com uma expressao
    *   válida
    */
    public void setFundo(short fundo) {
        
        if (fundo > 0 && fundo < 256) {

            this.fundo = fundo;
        }
        else {

            System.err.println("(FUNDO)Erro! Padrão que foi enviado é inválido!");
        }
    }

    public void setCor(short cor) {

        if (cor > 0 && cor < 256) {

            this.cor = cor;
        }
        else {

            System.err.println("(COR)Erro! Padrão que foi enviado é inválido!");
        }
    }

    public void setDestaque(String destaque) {

        if (acharPadrao(destaque)) {

            this.destaque = destaque;
        }
        else {

            System.err.println("(DESTAQUE)Erro! O padrão que foi enviado é inválido!");
        }
    }

    public String getFundo() {
        String resp = "";
        if ( fundo != -1 ) {
            resp = "\u001b[48;5;" + fundo + "m";
        }
        return resp;
    }

    public String getCor() {
        String resp = "";
        if ( cor != -1 ) {
            resp = "\u001b[38;5;" + cor + "m";
        }
        return resp;
    }

    //  Imprimir
    /*
    *   Pega os dados nas variaveis 'fundo', 'cor'
    *   e 'destaque', e as combina com a String
    *   'saida', que foi inserida pelo usuario
    *   para resultar o texto personalizado.
    *
    *   O uso da "ANSI_RESETAR" serve para
    *   dizer ao "System.out.print" que a 
    *   impressão nessas configurações acabou.
    */
    public String imprimir(String saida) {

        return getFundo() + getCor() + destaque + saida + ANSI_RESETAR;
    }

    //  Verificando a entrada do usuario
    /*
    *   Como o usuario pode enviar literalmente
    *   qualquer String para ser usada como
    *   parâmetro de customização de texto,
    *   essa função serve para verificar
    *   se o que o usuário inseriu é um
    *   parâmetro válido.
    */
    private boolean acharPadrao(String entrada) {

        boolean resp = false;

        m = p.matcher(entrada);

        if(m.find()) {

            resp = true;
        }

        return resp;        
    }

    public static void limparTelaUnix() {
        System.out.print("\033[H\033[2J");   
        System.out.flush(); 
    }



}