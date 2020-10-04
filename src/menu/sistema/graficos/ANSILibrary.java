
/*
*   Classe com configurações para imprensão 
*   personalizada no terminal
*
*   Criada por: MysteRys337
*   Versão : 0.0.1
*/

package menu.sistema.graficos;

public class ANSILibrary{

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

    }
    
    public ANSILibrary(int cor) { 
    
		setCor(cor);
        fundo    = -1;
        destaque = "";
    }
    
    public ANSILibrary(int cor,int fundo) { 
    
    	setCor(cor);
    	setFundo(fundo);
        destaque = "";
    }
    
    public ANSILibrary(int cor,int fundo,String destaque) { 
    
    	setCor(cor);
    	setFundo(fundo);
        setDestaque(destaque);
    }

    //  Funções de 'set'
    /*
    *	As funções 'set' que estão em privado existem
    *	para tratar a entrada de numeros que não podem serem
    *	considerados números
    */
    public void setFundo(int fundo) {
    	if (ehCorValida(fundo)) 
			setFundo((short)fundo);
		else 
            System.err.println("(COR)Erro! Padrão que foi enviado é inválido!");
    }
    
    private void setFundo(short fundo) {
    	this.fundo = fundo;
    }

	public void setCor(int cor) {
		if (ehCorValida(cor)) 
			setCor((short)cor);
		else 
            System.err.println("(COR)Erro! Padrão que foi enviado é inválido!");
	}
	
    private void setCor(short cor) {

        this.cor = cor;
    }

    public void setDestaque(String destaque) {

        if (acharPadrao(destaque)) {

            this.destaque = destaque;
        }
        else {

            System.err.println("(DESTAQUE)Erro! O padrão que foi enviado é inválido!");
        }
    }
    
    //Funções 'get'
    public String getDestaque() {
    	return destaque;
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

        if(entrada.matches("^\u001B\\[\\d{1,2}m")) {

            resp = true;
        }

        return resp;        
    }
    
    private boolean ehCorValida(int n) {
    	return (n > 0 && n < 256);
    }

    public static void limparTelaUnix() {
        System.out.print("\033[H\033[2J");   
        System.out.flush(); 
    }

}
