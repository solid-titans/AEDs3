package menu.resposta;

import menu.sistema.abstracts.api.crudmanagers.UsuarioInterface;
import menu.sistema.abstracts.frontend.RegistroVisual;
import menu.sistema.abstracts.frontend.RegistroVisualResposta;
import menu.sistema.abstracts.frontend.RegistroVisualplus;
import menu.sistema.abstracts.frontend.RespostaFrontEndInterface;
import menu.sistema.controle.CodigoDeProtocolo;
import menu.sistema.graficos.*;
import menu.sistema.input.Input;
import produtos.Resposta;

public class RespostasFrontEnd implements RespostaFrontEndInterface {

    // Atributos
    private CustomPrint myPrint;

    private Input input;

    public RespostasFrontEnd(CustomPrint myPrint, Input input) {
        this.myPrint      = myPrint;
        this.input = input;
    }

    /**
     * Função para retornar todas as respostas em um array em formato String
     * 
     * @param array é o array de respostas que foi enviado
     * @return a String correspondente a listagem das respostas
     */
    public String listar(RegistroVisualplus[] array) {

        String resp = "";
        byte contador = 1;

        resp += myPrint.imprimir("[RESPOSTAS]");

        for (RegistroVisualplus i : array) {
            if (i.getAtiva() == false)
                resp += "\n(Arquivada)";

            resp += "\n" + myPrint.imprimir("(" + contador + ".)" + i.imprimir() + "\n");
            contador++;

        }

        return resp;
    }

    /**
     * Função para retornar todas as respostas em um array em formato String
     * 
     * @param array é o array de respostas que foi enviado
     * @return a String correspondente a listagem das respostas
     */
    public String listarGeral(UsuarioInterface usuarios, RegistroVisualResposta[] array) {

        String resp = "";
        String nome = "";

        byte contador = 1;

        resp += myPrint.imprimir("[RESPOSTAS]");

        for (RegistroVisualResposta i : array) {
            if (i.getAtiva() == false)
                continue;

            nome = usuarios.achar(i.getIdUsuario()).getNome();

            resp += "\n" + myPrint.imprimir(contador + "." + i.imprimir(nome) + "\n");
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

        resp += myPrint.imprimir("[RESPOSTAS]");

        for (RegistroVisualplus i : array) {
            if (i.getAtiva() == false) {
                resp += "\n(Arquivada)";
            }

            resp += "\n" + myPrint.imprimir(contador + "." + i.imprimirSimplificado() + "\n");
            contador++;

        }

        return resp;
    }

    /**
     * Função para fazer a confirmação com o usuário se essa é a resposta que será
     * registrada/alterada/arquivada
     * 
     * @param r é a resposta que foi recebida seja qual for a operação
     * @return um codigo de protocolo referente ao resultado da verificacao
     */
    public CodigoDeProtocolo verificar(RegistroVisual r) {

        String confirmar = "";
        CodigoDeProtocolo sucesso = CodigoDeProtocolo.ERRO;

        System.out.println(myPrint.imprimir("[Vamos conferir a sua resposta]") + "\n");
        System.out.print(myPrint.imprimir(r.imprimir()) + "\nEssa é a sua resposta?(s/n) : ");

        confirmar = input.lerString();

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
     * Função intermediaria para escolher uma resposta em um determinado array
     * 
     * @param array que é o array de respostas na qual o usuário terá que fazer uma
     *              escolha
     * @return o Id da resposta que o usuário escolheu
     */
    public int escolherResposta(Resposta[] array) {

        byte entrada = -1;
        int indexSelecionado = -1;

        listar(array);
        System.out.println("\nEscolha uma das respostas: \nObs: Pressione \'0\' para voltar ao menu\n-> ");
        entrada = input.lerByte();
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