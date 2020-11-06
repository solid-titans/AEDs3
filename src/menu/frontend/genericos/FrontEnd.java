package menu.frontend.genericos;

import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.genericos.abstracts.FrontEndInterface;
import menu.frontend.graficos.CustomPrint;
import produtos.abstracts.RegistroVisual;

public class FrontEnd implements FrontEndInterface {

    // Variaveis de controle de grafico
    protected CustomPrint myPrint;
    protected CustomInput myInput;

    protected final String name;

    public FrontEnd(CustomPrint myPrint, CustomInput myInput, String name) {
        this.myPrint       = myPrint;
        this.myInput       = myInput;
        this.name          = name;

    }

    public CodigoDeProtocolo verificar(RegistroVisual objeto) {

        String entrada = "";
        CodigoDeProtocolo sucesso = CodigoDeProtocolo.ERRO;

        System.out.println(myPrint.imprimir("[Conferindo " + this.name  + "]"));
        System.out.print(myPrint.imprimir(objeto.imprimir()));

        entrada = myInput.lerString("\nOs dados estão corretos?(s/n) : ");

        myPrint.limparTela();

        if (confirmado(entrada)) {
            sucesso = CodigoDeProtocolo.SUCESSO;

        } else {
            sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
            System.out.println("Processo cancelado!\nVoltando para o menu...\n");
        }

        return sucesso;
    }

    private boolean confirmado(String s) {
        return s.length() == 0 || s.toLowerCase().equals("s");
    }
}
