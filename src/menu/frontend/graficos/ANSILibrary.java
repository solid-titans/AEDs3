package menu.frontend.graficos;

/**
 * Classe para gerenciamento de códigos de saída ANSI pro terminal
 * @author MysteRys337 ( Gustavo Lopes )
 */
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

    /**
     * Construtor 1: nenhum dado inserido
     */
    public ANSILibrary() { 

        fundo = cor = -1;
        destaque = "";

    }
    
    /**
     * Construtor 2 : a cor do texto foi inserida
     * @param cor é o inteiro que corresponde a cor do texto
     */
    public ANSILibrary(int cor) { 
    
		setCor(cor);
        fundo    = -1;
        destaque = "";
    }
    
    /**
     * Construtor 3 : a cor do texto e do fundo foi inserida
     * @param cor é o inteiro que corresponde a cor do texto
     * @param fundo é o inteiro que corresponde a cor do fundo do texto
     */
    public ANSILibrary(int cor,int fundo) { 
    
    	setCor(cor);
    	setFundo(fundo);
        destaque = "";
    }
    
    /**
     * Construtor 4 : Todos os parámetros foram configurados
     * @param cor é o inteiro que corresponde a cor do texto
     * @param fundo é o inteiro que corresponde a cor do fundo do texto
     * @param destaque é a String que corresponde ao tipo de destaque do texto
     */
    public ANSILibrary(int cor,int fundo,String destaque) { 
    
    	setCor(cor);
    	setFundo(fundo);
        setDestaque(destaque);
    }

    //  Funções de 'set'
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

    /**
     * Imprimir o texto com as configurações já definidas
     * @param saida é o texto especial que deva ser imprimido
     * @return a String com os padrões ANSI inseridos
     */
    public String imprimir(String saida) {

        return getFundo() + getCor() + destaque + saida + ANSI_RESETAR;
    }

    /**
     * Verificar se o que o usuário inseriu como destaque corresponde a um padrão de texto real
     * @param entrada é o código ANSI que o usuário inseriu
     * @return true ou false se a entrada corresponde ao padrão esperado
     */
    private boolean acharPadrao(String entrada) {

        boolean resp = false;

        if(entrada.matches("^\u001B\\[\\d{1,2}m")) {

            resp = true;
        }

        return resp;        
    }
    
    /**
     * Verificar se a cor inserida é valida, se ela está no escopo previsto
     * @param n é a cor inserida
     * @return true ou false se n está entre 0 e 256
     */
    private boolean ehCorValida(int n) {
    	return (n > 0 && n < 256);
    }

    /**
     * Função para limpar a tela de terminais em sistemas Unix(Linux e MAC)
     */
    public static void limparTelaUnix() {
        System.out.print("\033[H\033[2J");   
        System.out.flush(); 
    }

}
