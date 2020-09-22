package menu;

public class ASCIInterface {

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

    public String caixa(short largura, short altura, String texto) {

        String caixa = "";
        boolean textoInserido = false;

        if ( texto.length() < largura || largura != 0 || altura != 0) {
            for (short i = 0 ; i < altura; i++) {
                for (short j = 0; j < largura; j++) {

                    if(i == 0 || i == altura -1 || j == 0 || j == largura -1) {
                        caixa += borda.imprimir("■");
                    }
                    else if ( j == 0 || j == largura -1) {
                        caixa += borda.imprimir("█");
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
        }
        else {
            System.err.println("ERRO! O texto enviado é maior do que a caixa");
        }
        return caixa;
    }

    public String caixa(short altura, String texto) {

        short largura = (short)texto.length();
        largura += 4;
        String caixa = "";
        boolean textoInserido = false;

        if ( altura != 0 || largura >= 0) {
            for (short i = 0 ; i < altura; i++) {
                for (short j = 0; j < largura; j++) {

                    if(i == 0 || i == altura -1 || j == 0 || j == largura -1) {
                        caixa += borda.imprimir("■");
                    }
                    else if ( j == 0 || j == largura -1) {
                        caixa += borda.imprimir("█");
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
        }
        else {
            System.err.println("ERRO! O texto enviado é maior do que a caixa");
        }
        return caixa;
    }

    public void limparTela(){

        String OS = System.getProperty("os.name").toLowerCase();

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
}