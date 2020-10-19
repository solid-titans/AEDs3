package menu.sistema.graficos;

/**
 * Classe para gerenciar impressão de texto personalizado 
 * usando ASCIInterface e ANSILibrary
 * @author  MysteRys337 ( Gustavo Lopes )
 * @version 0.0.1
 */
public class CustomPrint{

    private ASCIInterface graficos;
    private ANSILibrary   destaqueTitulo;
    private ANSILibrary   destaqueComplemento;

    public ASCIInterface getInterface() {
        return this.graficos;
    }

    public ANSILibrary getDestaqueTitulo() {
		return this.destaqueTitulo;
	}

    public ANSILibrary getDestaqueComplemento() {
		return this.destaqueComplemento;
    }

    public void setGraficos(ASCIInterface graficos) {
        this.graficos = graficos;
    }

    public void setDestaqueTitulo(ANSILibrary destaqueTitulo) {
		this.destaqueTitulo = destaqueTitulo;
	}

    public void setDestaqueComplemento(ANSILibrary destaqueComplemento) {
		this.destaqueComplemento = destaqueComplemento;
    }

    /**
     * Construtor 1: nenhum parâmetro
     */
    public CustomPrint() {
        this.graficos            = new ASCIInterface();
        this.destaqueTitulo      = new ANSILibrary();
        this.destaqueComplemento = new ANSILibrary();
    }
    
    /**
     * Construtor 2: Todas as interfaces graficas configuradas
     * @param graficos            é a ASCIInterface que vai configurar as caixas
     * @param destaqueTitulo      é o layout para colorir o título
     * @param destaqueComplemento é o layout para colorir o complemento
     */
    public CustomPrint(ASCIInterface graficos, ANSILibrary destaqueTitulo, ANSILibrary destaqueComplemento) {
        this.graficos            = graficos;
        this.destaqueTitulo      = destaqueTitulo;
        this.destaqueComplemento = destaqueComplemento;
    }

    /**
     * Função de imprimir
     * @param entrada é o que tem que ser imprimido
     * @return
     */
    public String imprimir(String entrada) {      
        String resposta   = "";
        String substring  = "";

        short indexFinal  = -1;


        for(short i = 0; i < entrada.length(); i++) {

            if(entrada.charAt(i) == '[') {
                indexFinal = find(']',entrada,i);
                if(indexFinal != -1) {

                    substring = entrada.substring(i + 1, indexFinal);
                    resposta += graficos.caixa(substring) + "\n";
                    i += indexFinal - i;
                }

            }
            else if(entrada.charAt(i) == '(') {
                indexFinal = find(')',entrada,i);
                if(indexFinal != -1) {

                    substring = entrada.substring(i + 1, indexFinal);
                    resposta += separarString(substring, destaqueTitulo);
                    i += indexFinal - i;
                }

            }
            else if(entrada.charAt(i) == '{') {
                indexFinal = find('}',entrada,i);
                if(indexFinal != -1) {

                    substring = entrada.substring(i + 1, indexFinal);
                    resposta += separarString(substring, destaqueComplemento);
                    i += indexFinal - i;
                }

            } 
            else if(entrada.charAt(i) == '*') {
                    i++;
                    resposta += entrada.charAt(i);
                    
            }else {
                resposta += entrada.charAt(i);
            }
        }
        return resposta;
    }

    /**
     * Limpar a tela do terminal
     * @implNote o programa verifica se o sistema operacional é um Windows ou do tipo Unix 
     */
    public void limparTela(){

        String OS = System.getProperty("os.name").toLowerCase();
        //String clear;

        if(OS.equals("linux") || OS.equals("mac")) {

            ANSILibrary.limparTelaUnix();
        }
        else {

            try {
                Runtime.getRuntime().exec("cls");
            }
            catch (Exception e) {
                System.err.println("Deu ruim");
            }
        }
    }

    private String separarString(String entrada,ANSILibrary destaque) {

        String resposta  = "";
        String [] barraN = null;

        if(entrada.contains("\n")) {
            barraN = entrada.split("\n");
            for(byte i = 0; i < barraN.length;i++) 
                resposta += destaque.imprimir(barraN[i]) + "\n";

        } else {
            resposta = destaque.imprimir(entrada);
        }
        return resposta;
    }

    private short find(char search,String s,short index) {
        short resultado = -1;

        while(index < s.length()) {

            if(s.charAt(index) == search) {
                resultado = index;
                index     = (short)s.length();
            }
            index++;
        }

        return resultado;
    }

}
