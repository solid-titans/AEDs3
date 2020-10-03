/*
*   Classe para a criação de interface gráfica em
*   terminal, usando ASCII e ANSI
*   Criada por: Gustavo Lopes Rodrigues
*   Versão : 0.0.2
*/
package menu.sistema.graficos;

public class ASCIInterface {

    //Classe
    private final StringManipulation s = new StringManipulation();

    //Definindo a altura e largura maxima dos elementos
    private final short ALTURA_MAX       = 1000;
    private final short LARGURA_MAX      = 1000;

    private final byte LARGURA_SEGURANCA;
    private final byte ALTURA_SEGURANCA;

    private final byte LARGURA_PADRAO;

    //Declarando os elementos que guardam as informações de texto
    /*
    *   A ANSILibrary foi uma classe criada por mim para que
    *   eu possa ter controle da cor,cor de fundo e destaque
    *   do texto que eu quero imprimir, eu apenas envio uma
    *   cor entre 0 e 255 e a classe já configura o resto para
    *   imprimir.
    *   OBS:
    *   Para mais detalhes sobre a classe, consule ANSILibrary.java
    */
    private ANSILibrary borda;
    private ANSILibrary janela;
    private ANSILibrary texto_primario;
    private ANSILibrary texto_secundario;

    public ASCIInterface () {

        //Iniciando todos os objetos
        borda             = new ANSILibrary(1,1);                   
        janela            = new ANSILibrary(255,255);  
        texto_primario    = new ANSILibrary(232,255);
        texto_secundario  = new ANSILibrary(1,255);

        //Configurar as dimensões
        LARGURA_PADRAO    = 30;
        LARGURA_SEGURANCA = 5;
        ALTURA_SEGURANCA  = 4;

    }

    public ASCIInterface(int larguraPadrao, int larguraSeguranca, int alturaSeguranca) {
    
        //Iniciando todos os objetos
        borda             = new ANSILibrary(1,1);                   
        janela            = new ANSILibrary(255,255);  
        texto_primario    = new ANSILibrary(232,255);
        texto_secundario  = new ANSILibrary(1,255);

        //Configurar as dimensões
        LARGURA_PADRAO    = (byte)larguraPadrao;
        LARGURA_SEGURANCA = (byte)larguraSeguranca; 
        ALTURA_SEGURANCA  = (byte)alturaSeguranca;

    }

    public ASCIInterface(int corBorda, int corJanela, int corTexto,int corTextoSecundario) {
    
        //Iniciando todos os objetos
        borda             = new ANSILibrary(corBorda,corBorda);                   
        janela            = new ANSILibrary(corJanela,corJanela);  
        texto_primario    = new ANSILibrary(corTexto,corJanela);
        texto_secundario  = new ANSILibrary(corTextoSecundario,corJanela);
        
        //Configurar as dimensões
        LARGURA_PADRAO    = 30;
        LARGURA_SEGURANCA = 5; 
        ALTURA_SEGURANCA  = 4;

    }

    //Funções 'set'
    public void setBorda(int cor) {
        borda.setCor(cor);
        borda.setFundo(cor);
    }

    public void setJanela(int cor) {
        janela.setCor(cor);
        janela.setFundo(cor);
        texto_primario.setFundo(cor);
        texto_secundario.setFundo(cor);
    }

    public void setTextoPrimario(int cor) {
        texto_primario.setCor(cor);
    }

    public void setTextoSecundario(short cor) {
        texto_secundario.setCor(cor);
    }

    //Instanciando uma caixa
    /** 
    *   A caixa é um espaço retangular ou quadricular
    *   Onde tem uma borda e o um único texto.
    *
    *   Existe duas tipos de caixas:
    *
    *  -> A que recebe como parâmetro as duas medidas
    *   (altura e largura) e a String que será inserida
    *
    *  -> A que recebe apenas a altura e String, a largura
    *   é inferida como sendo a largura da String.
    *
    *  -> A que recebe apenas o String, a largura e altura
    *   é tomado como referência a String
    *   
    *   OBS: Por enquanto, nos dois casos, a String estará
    *   centralizada na caixa.
    */

    //Caixa 1: largura,altura e texto.
    public String caixa(int largura, int altura, String texto) {

        String resp    = "";
        String[] array = null;
        largura += LARGURA_SEGURANCA + 1;

        if(texto.length() > largura) {

            array = texto.split(" ");
            altura  += ALTURA_SEGURANCA;
            
            if(encaixa(largura,altura) && s.calcularTamanho(array, largura) < altura) {
                resp = caixa((short)largura,(short)altura,array);
            }
        }
        else {
            if ( texto.length() < largura && encaixa(largura,altura) ) {
                resp = caixa((short)largura,(short)altura,texto);
            }
            else {
                resp = texto;
            }
        }
        return resp;
    }

    //Caixa 2: altura e texto(a largura é inferida como sendo o tamanho de 'texto' + a largura de seguranca)
    public String caixa(int altura, String texto) {

        String resp    = "";
        int largura    = -1;
        String[] array = null;

        if(texto.length() > LARGURA_PADRAO) {
            array    = texto.split(" ");
            largura  = LARGURA_PADRAO + LARGURA_SEGURANCA + 1;
            altura  += ALTURA_SEGURANCA;
            
            if(encaixa(largura,altura) && s.calcularTamanho(array, largura) < altura) {
                resp = caixa((short)largura,(short)altura,array);
            }
        }
        else { 
            largura = texto.length() + LARGURA_SEGURANCA;
            if (encaixa(largura,altura) ) {
                resp = caixa((short)largura,(short)altura,texto);
            }
            else {
                System.err.println("ERRO! O texto enviado é maior do que a caixa");
            }
        }

        return resp;
    }

    //Caixa 3: apenas texto(a largura é inferida como sendo o tamanho de 'texto' + a largura de seguranca)
    //                      e a altura é inferida como sendo a altura de seguranca)
    public String caixa(String texto) {

        String   resp  = "";
        String[] array = null;
        int largura = -1;
        int altura  = -1;


        if(texto.length() > LARGURA_PADRAO) {
            array = texto.split(" ");
            largura = LARGURA_PADRAO + LARGURA_SEGURANCA + 1;
            altura  = s.calcularTamanho(array, largura) + ALTURA_SEGURANCA;
            
            if(encaixa(largura,altura)) {
                resp = caixa((short)largura,(short)altura,array);
            }
        }
        else { 
            largura = texto.length() + LARGURA_SEGURANCA + 1;
            altura = ALTURA_SEGURANCA + 1;

            if (encaixa(largura,altura) ) {
                resp = caixa((short)largura,(short)altura,texto);
            }
            else {
                resp = texto;
            }
        }

        return resp;
    }

    //Funções para Imprimir uma caixa
    /**
     *  @ recebem uma largura, altura e um texto para formar uma caixa
     *  @ gera uma String que contém caixa
     */
    
    //Função para criar a caixa efetivamente e manda-la devolta como String
    private String caixa(short largura, short altura, String texto) {
        String caixa = "";
        boolean textoInserido = false;

        for (short i = 0 ; i < altura; i++) {
            for (short j = 0; j < largura; j++) {

                if(i == 0 || i == altura -1 || j == 0 || j == largura -1) {
                    caixa += borda.imprimir("■");
                }
                else {
                    if ( !textoInserido && i == altura/2 && j > largura - texto.length() - j - 1) {
                        caixa += texto_primario.imprimir(texto);
                        j += texto.length() - 1;
                        textoInserido = true;
                    }
                    else {
                        caixa += janela.imprimir("▒");
                    }
                }
            }
            caixa += "\n";
         }
        return caixa;
    }

    //Função para imprimir a caixa de texto efetivamente e manda-la devolta como String
    private String caixa(short largura,short altura,String[] texto) {

        String caixa = "";
        byte textoIndex = 0;

        for (short i = 0 ; i < altura; i++) {
            for (short j = 0; j < largura; j++) {

                if(i == 0 || i == altura -1 || j == 0 || j == largura -1) {
                    caixa += borda.imprimir("■");
                }
                else {
                    if ( textoIndex < texto.length  && i > 1 && j > 1 && j + texto[textoIndex].length() < largura ) {
                        texto[textoIndex] += " ";
                        caixa += texto_primario.imprimir(texto[textoIndex]);
                        j += texto[textoIndex].length() -1 ;
                        textoIndex++;
                    }
                    else {
                        caixa += janela.imprimir("▒");
                    }
                }
            }
            caixa += "\n";
         }
        return caixa;
    }

    //Limpar a tela
    /*
    *   Para uma melhor navegação pelas interfaces
    *   essa função ajudara na limpeza da tela
    *   
    *   Em MAC e LINUX, a limpeza é feita usando
    *   o código ANSI.
    *
    *   Em WINDOWS ele tentará executar o código
    *   'cls' no terminal
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

    //Função que returna 'true' ou false, se as medidas da caixa são validas
    private boolean encaixa(int largura, int altura) {
        return (largura > 0 && altura > 2 && largura < LARGURA_MAX && altura < ALTURA_MAX);
    }
}
