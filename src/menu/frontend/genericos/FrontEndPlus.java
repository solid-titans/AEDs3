package menu.frontend.genericos;

import menu.backend.input.CustomInput;
import menu.frontend.genericos.abstracts.FrontEndplusInterface;
import menu.frontend.graficos.CustomPrint;
import produtos.abstracts.RegistroVisualplus;

public class FrontEndPlus extends FrontEnd implements FrontEndplusInterface {

    public FrontEndPlus(CustomPrint myPrint, CustomInput myInput,String name) {
        super(myPrint, myInput, name);
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

        resp += myPrint.imprimir("[" + (this.name + "s").toUpperCase() +  "]");

        for (RegistroVisualplus i : array) {
            if (i.getAtiva() == false)
                resp += "\n(Arquivada)";

            resp += "\n" + myPrint.imprimir("(" + contador + ".)" + i.imprimir() + "\n");
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

        resp += myPrint.imprimir("[" + (this.name + "s").toUpperCase() +  "]");

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
     * Função intermediaria para escolher uma registro em um determinado array
     * 
     * @param array que é o array de respostas na qual o usuário terá que fazer uma
     *              escolha
     * @return o Id da resposta que o usuário escolheu 
     *         Obs: retorna -1 se o usuário escolheu algo inválido
     *         Obs: retorna -3 se o usuário cancelou o processo
     */
    public int escolher(RegistroVisualplus[] array) {

        int entrada = -1;
        int indexSelecionado = -1;

        System.out.println(listarSimplificado(array));

        entrada = myInput.lerInt("\nDigite o número d" + this.lastChar(this.name) + " " + this.name + 
                                  ":\nObs: Pressione \'0\' para voltar ao menu\n-> ");
        myPrint.limparTela();

        if (isInRange(array.length, entrada)) {

            indexSelecionado = array[entrada - 1].getId();
        } else {
            if (entrada != 0)
                System.err.println("ERRO! Entrada inválida!");
            else
                indexSelecionado = -3; // Sinal para o programa que o usuário cancelou o processo
        }

        return indexSelecionado;
    }

    /**
     * Verifica se entrada é um número válido dentro de um array
     * @param n é o tamanho do array
     * @param entrada é a entrada do usuário
     * @return true ou false se a entrada é válida
     */
    private boolean isInRange(int n, int entrada) {
        return n > entrada - 1 && entrada - 1 >= 0;
    }

    /**
     * Retorna o ultimo caractere de uma String
     * @param s é a String 
     * @return o ultimo caractere da string 
     */
    private char lastChar(String s) {
        return this.name.charAt(this.name.length() - 1);
    }

}
