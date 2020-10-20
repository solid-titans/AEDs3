package menu.frontend.graficos;

/**
 * Criação de interface gráfica no terminal usando ASCII e ANSI 
 * @author MysteRys337 (Gustavo Lopes)
 */
public class ASCIInterface {

    //Definindo a altura e largura maxima dos elementos
    private final short ALTURA_MAX       = 1000;
    private final short LARGURA_MAX      = 1000;

    //Espaço que fica entre o texto 
    private short LARGURA_SEGURANCA;
    private short ALTURA_SEGURANCA;

    //Espaço antes que o texto se divida em linhas
    private short LARGURA_PADRAO;

    //Padrões de cores de cada elemento da interface
    private ANSILibrary borda;
    private ANSILibrary janela;
    private ANSILibrary texto_primario;
    private ANSILibrary texto_secundario;

    /**
     * Construtor 1: Criar uma interface com configurações genéricas
     */
    public ASCIInterface () {

        //Iniciando todos os objetos
        borda             = new ANSILibrary(1,1);                   
        janela            = new ANSILibrary(255,255);  
        texto_primario    = new ANSILibrary(232,255);
        texto_secundario  = new ANSILibrary(1,255);

        //Configurar as dimensões
        LARGURA_PADRAO    = 40;
        LARGURA_SEGURANCA = 5;
        ALTURA_SEGURANCA  = 4;

    }

    /**
     * Construtor 2: Criar uma interface mas já configurando os limites de largura e altura
     * @param larguraPadrao é o inteiro correspondendo ao tamanho máximo antes que o texto se divida em linhas
     * @param larguraSeguranca é o inteiro que corresponde ao espaço que ficará entre a primeira linha e a borda na largura
     * @param alturaSeguranca é o inteiro que corresponde ao espaço que ficará entre a primeira linha e a borda na altura
     */
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

    /**
     * Construtor 3: Criar uma interface mas já com os padrões de cor já definidos
     * @param corBorda é o int correspondendo a cor da borda das janelas
     * @param corJanela é o int correspondendo a cor de fundo das janelas
     * @param corTexto é o int correspondendo a cor dos textos principais nas interfaces
     * @param corTextoSecundario é o int correspondendo a cor dos textos secundarios nas interfaces
     */
    public ASCIInterface(int corBorda, int corJanela, int corTexto,int corTextoSecundario) {
    
        //Iniciando todos os objetos
        borda             = new ANSILibrary(corBorda,corBorda);                   
        janela            = new ANSILibrary(corJanela,corJanela);  
        texto_primario    = new ANSILibrary(corTexto,corJanela);
        texto_secundario  = new ANSILibrary(corTextoSecundario,corJanela);
        
        //Configurar as dimensões
        LARGURA_PADRAO    = 40;
        LARGURA_SEGURANCA = 5; 
        ALTURA_SEGURANCA  = 4;

    }

    //Funções 'set'
    public void setLarguraPadrao(int largura) {
        LARGURA_PADRAO = (short)largura;
    }

    public void setLarguraSeguranca(int largura) {
        LARGURA_SEGURANCA = (short)largura;
    }

    public void setAlturaSeguranca(int altura) {
        ALTURA_SEGURANCA = (short)altura;
    }

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

    /**
     * Função de criar caixa 1: largura e altura especificado
     * @param largura é o tamanho em inteiro da largura da caixa
     * @param altura é o tamanho em inteiro da altura da caixa
     * @param texto é a String que contém o texto que será inserido na caixa
     * @return a String que contem a caixa a ser imprimida
     */
    public String caixa(int largura, int altura, String texto) {

        String resp    = "";
        String[] array = null;
        largura += LARGURA_SEGURANCA + 1;

        if(texto.length() > largura) {

            array = texto.split(" ");
            altura  += ALTURA_SEGURANCA;
            
            if(encaixa(largura,altura) && calcularTamanho(array, largura) < altura) {
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

    /**
     * Função de criar caixa 2: altura especificada
     * @param altura é o tamanho em inteiro da altura da caixa
     * @param texto é a String que contém o texto que será inserido na caixa
     * @implNote a largura da caixa será a largura do texto
     * @return a String que contem a caixa a ser imprimida
     */
    public String caixa(int altura, String texto) {

        String resp    = "";
        int largura    = -1;
        String[] array = null;

        if(texto.length() > LARGURA_PADRAO) {
            array    = texto.split(" ");
            largura  = LARGURA_PADRAO + LARGURA_SEGURANCA + 1;
            altura  += ALTURA_SEGURANCA;
            
            if(encaixa(largura,altura) && calcularTamanho(array, largura) < altura) {
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

    /**
     * Função de criar caixa 2: Nenhuma dimensão especificada
     * @param texto é a String que contém o texto que será inserido na caixa
     * @implNote a largura e a altura da caixa será controlada a partir das necessidades da string
     * @return a String que contem a caixa a ser imprimida
     */
    public String caixa(String texto) {

        String   resp  = "";
        String[] array = null;
        int largura = -1;
        int altura  = -1;


        if(texto.length() > LARGURA_PADRAO) {
            array = texto.split(" ");
            largura = LARGURA_PADRAO + LARGURA_SEGURANCA + 1;
            altura  = calcularTamanho(array, largura) + ALTURA_SEGURANCA;
            
            if(encaixa(largura,altura)) {
                resp = caixa((short)largura,(short)altura,array);
            }
        }
        else { 
            largura = texto.length() + LARGURA_SEGURANCA ;
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
                    if ( !textoInserido && i >= altura/2 && j > largura - texto.length() - j - 2) {
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
                    if ( textoIndex < texto.length  && i > 1 && j + texto[textoIndex].length() < largura -1 ) {
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

    /** Calcular quanto tamanho vertical(de linhas) é necessario para 
     ** colocar um array
     * 
     * @param array é o array de Strings com o conteúdo que o usuário quer inserir
     * @param largura é o tamanho máximo de linhas 
     * @return numero de linhas necessaria para colocar um array
     */
    private int calcularTamanho(String[] array,int largura) {
        
        int tamanhoString = 0;
        int numDeArrays   = 1;
        
        for ( String i : array) {
            if (tamanhoString + i.length() < largura) {
                tamanhoString += i.length() + 1;
            }
            else {
                tamanhoString = i.length() + 1;
                numDeArrays++;
                
            }
        }
        return numDeArrays;
    }

    /**
     * Função que verifica se a largura e altura especificado estavam dentro do esperado
     * @param largura é o inteiro que corresponde a largura da caixa
     * @param altura é o inteiro que corresponde a altura da caixa
     * @return true ou false se as especificações estão dentro do conforme
     */
    private boolean encaixa(int largura, int altura) {
        return (largura > 0 && altura > 2 && largura < LARGURA_MAX && altura < ALTURA_MAX);
    }
}
