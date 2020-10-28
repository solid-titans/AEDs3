package menu.frontend;

import menu.backend.input.*;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.abstracts.FrontEnd;
import menu.frontend.graficos.CustomPrint;
import produtos.abstracts.RegistroVisual;

public class VotosFrontEnd implements FrontEnd {

    CustomPrint myPrint;
    Input       input;

    public VotosFrontEnd(CustomPrint myPrint, Input input) {
        this.myPrint = myPrint;
        this.input   = input;
    }

    @Override
    public CodigoDeProtocolo verificar(RegistroVisual objeto) {
        String confirmar = "";
        CodigoDeProtocolo sucesso = CodigoDeProtocolo.ERRO;

        System.out.println(myPrint.imprimir("[Vamos conferir o seu voto]"));
        System.out.print(myPrint.imprimir(objeto.imprimir()));

        confirmar = input.lerString("\nEssa é a sua pergunta?(s/n) : ");

        myPrint.limparTela();

        if (confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {

            sucesso = CodigoDeProtocolo.SUCESSO;
        } else {
            sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
            System.out.println("Processo cancelado!\nVoltando para o menu...\n");
        }

        return sucesso;
    }
}