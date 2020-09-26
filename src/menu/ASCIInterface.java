/*
*   Classe para a criação de interface gráfica em
*   terminal, usando ASCII e ANSI
*   Criada por: Gustavo Lopes Rodrigues
*   Versão : 0.0.2
*/
package menu;

import java.io.IOException;

public class ASCIInterface {

    //Definindo a altura e largura maxima dos elementos
    private final short ALTURA_MAX = 1000;
    private final short LARGURA_MAX = 1000;

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
        borda = new ANSILibrary();
        janela = new ANSILibrary();
        texto_primario = new ANSILibrary();
        texto_secundario = new ANSILibrary();

        //Configurando borda
        borda.setFundo((short)1);
        borda.setCor((short)1);

        //Configurando a janela
        janela.setFundo((short)255);
        janela.setCor((short)255);
        
        //Configurando texto_primario
        texto_primario.setFundo((short)255);
        texto_primario.setCor((short)232);
        
        //Configurando texto_secundario
        texto_secundario.setFundo((short)255);
        texto_secundario.setCor((short)1);
    }

    //Instanciando uma caixa
    /*
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
    *   OBS: Por enquanto, nos dois casos, a String estará
    *   centralizada na caixa.
    */

    //Caixa 1: largura,altura e texto.
    public String caixa(int largura, int altura, String texto) {

        String resp = "";
        if ( texto.length() < largura && encaixa(largura,altura) ) {
            resp = caixa((short)largura,(short)altura,texto);
        }
        else {
            System.err.println("ERRO! O texto enviado é maior do que a caixa");
        }

        return resp;
    }

    //Caixa 2: altura e texto(a largura é inferida como sendo o tamanho de 'texto')
    public String caixa(int altura, String texto) {

        String resp = "";
        int largura = texto.length() + 4;
        if (encaixa(largura,altura) ) {
            resp = caixa((short)largura,(short)altura,texto);
        }
        else {
            System.err.println("ERRO! O texto enviado é maior do que a caixa");
        }

        return resp;
    }
    
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
                    if ( !textoInserido && i == altura/2 && j >= largura - texto.length() - j) {
                        caixa += texto_primario.imprimir(texto);
                        j += texto.length() -1 ;
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
    public static void limparTela(){

        String OS = System.getProperty("os.name").toLowerCase();
        String clear;

        if(OS.equals("linux") || OS.equals("mac")) {

            ANSILibrary.limparTelaUnix();
        }
        else {

            try {
                Runtime.getRuntime().exec("cls");
            }
            catch (Exception e) {
                System.out.println("Deu ruim");
            }
        }
    }

    //Função que returna 'true' ou false, se as medidas da caixa são validas
    private boolean encaixa(int largura, int altura) {

        return (largura > 0 && altura > 2 && largura < LARGURA_MAX && altura < ALTURA_MAX);
    }
}