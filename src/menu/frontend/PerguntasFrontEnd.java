package menu.frontend;

import produtos.Pergunta;
import menu.backend.input.Input;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.abstracts.FrontEndplus;
import produtos.abstracts.*;
import menu.frontend.graficos.*;

/**
 * Classe para administrar casos específicos da admnistração visual das
 * Perguntas
 * 
 * @author MysteRys337 (Gustavo Lopes)
 */
public class PerguntasFrontEnd implements FrontEndplus {

    // Atributos
    private CustomPrint myPrint;

    private Input input;

    public PerguntasFrontEnd(CustomPrint myPrint, Input input) {

        this.myPrint      = myPrint;
        this.input        = input;
    }

    /**
     * Função para retornar todas as perguntas em um array em formato String
     * 
     * @param array é o array de perguntas que foi enviado
     * @return a String correspondente a listagem das perguntas
     */
    public String listar(RegistroVisualplus[] array) {

        String resp = "";
        byte contador = 1;

        resp += myPrint.imprimir("[PERGUNTAS]");

        for (RegistroVisualplus i : array) {
            if (i.getAtiva() == false) {
                resp += "\n(Arquivada)";
            }

            resp += "\n" + myPrint.imprimir(contador + "." + i.imprimir() + "\n");
            contador++;

        }
        
        return resp;
    }

    /**
     * Função para retornar todas as perguntas em um array em formato String de
     * forma simplificada
     * 
     * @param array é o array de perguntas que foi enviado
     * @return a String correspondente a listagem das perguntas
     */
    public String listarSimplificado(RegistroVisualplus[] array) {

        String resp = "";
        byte contador = 1;

        resp += myPrint.imprimir("[PERGUNTAS]");

        for (RegistroVisualplus i : array) {
            if (i.getAtiva() == false) {
                resp += "\n(Arquivada)";
            }

            resp += myPrint.imprimir("\n" + contador + "." + i.imprimirSimplificado() + "\n");
            contador++;

        }

        return resp;
    }

    /**
     * Função para fazer a confirmação com o usuário se essa é a pergunta que será
     * registrada/alterada/arquivada
     * 
     * @param p é a pergunta que foi recebida seja qual for a operação
     * @return um codigo de protocolo referente ao resultado da verificacao
     */
    public CodigoDeProtocolo verificar(RegistroVisual objeto) {

        String confirmar = "";
        CodigoDeProtocolo sucesso = CodigoDeProtocolo.ERRO;

        System.out.println(myPrint.imprimir("[Vamos conferir a sua pergunta]"));
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

    /**
     * Função intermediaria para escolher uma pergunta em um determinado array
     * 
     * @param array que é o array de perguntas na qual o usuário terá que fazer uma
     *              escolha
     * @return o Id da pergunta que o usuário escolheu
     */
    public int escolherPergunta(Pergunta[] array) {

        byte entrada = -1;
        int indexSelecionado = -1;

        System.out.print(listarSimplificado(array));

        entrada = input.lerByte("\nEscolha uma das perguntas: \nObs: Pressione \'0\' para voltar ao menu\n-> ");
        myPrint.limparTela();

        if (array.length > entrada - 1 && entrada - 1 >= 0) {

            indexSelecionado = array[entrada - 1].getId();
        } else {
            if (entrada != 0)
                System.err.println("ERRO! Entrada inválida!");
            else
                indexSelecionado = -3; // Sinal para o programa que o usuário cancelou o processo
        }

        return indexSelecionado;
    }

}
