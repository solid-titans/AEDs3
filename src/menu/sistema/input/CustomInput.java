package menu.sistema.input;

import menu.sistema.abstracts.input.CustomInputInterface;
import menu.sistema.graficos.*;

/**
 * Classe que mistura a Input.java e as classes em menu.sistema.graficos, para
 * criar sistemas de entradas customizadas
 * 
 * @author MysteRys337 (Gustavo Lopes)
 */
public class CustomInput extends Input implements CustomInputInterface {

    // Tipos de destaque
    private CustomPrint myPrint;

    public CustomInput(CustomPrint myPrint) {
        super();
        this.myPrint = myPrint;
    }


    /**
     * Cria uma interface de interação com o usuário para pegar o input do teclado
     * 
     * @param titulo     é o título dentro da caixa gerada pelo padrão de interface
     * @param limMin     é o tamanho mínimo da entrada que será feita pelo usuário
     * @param limMax     é o tamanho máximo da entrada que será feita pelo usuário
     * @param printRestr é o booleano que define se as limitacoes da String devem
     *                   ser imprimidas
     * @return a String que o usuário inseriu
     */
    public String inserir(String titulo, int limMin, int limMax, boolean printRestr) {
        String entradaDoUsuario = "";

        do {
            System.out.print(myPrint.imprimir("["+titulo+"]" + "(OBS: Deixe o espaço em branco e pressione \'Enter\' para cancelar o processo)") + "\n");
            System.out.print((printRestr) ? imprimirTamanhoMinMax(limMin, limMax) + "\n\n-> " : "-> ");

            entradaDoUsuario = lerString();

            myPrint.limparTela();
            if (temTamanhoAdequado(entradaDoUsuario, limMin, limMax) && !entradaDoUsuario.equals("")) {

                System.err.println("Erro! entrada inválida!\nPressione \'Enter\' para continuar");
                esperarUsuario();
                myPrint.limparTela();

            }
        } while (temTamanhoAdequado(entradaDoUsuario, limMin, limMax) && !entradaDoUsuario.equals(""));

        return entradaDoUsuario;
    }

    /**
     * Cria uma interface de interação com o usuário para pegar o input do teclado
     * 
     * @param titulo     é o título dentro da caixa gerada pelo padrão de interface
     * @param observacao é a String adicional que contenha qualquer observação que
     *                   seja pertinente informar ao usuário
     * @param limMin     é o tamanho mínimo da entrada que será feita pelo usuário
     * @param limMax     é o tamanho máximo da entrada que será feita pelo usuário
     * @param printRestr é o booleano que define se as limitacoes da String devem
     *                   ser imprimidas
     * @return
     */
    public String inserir(String titulo, String observacao, int limMin, int limMax, boolean printRestr) {
        String entradaDoUsuario = "";

        do {
            System.out.print(myPrint.imprimir("["+titulo+"]" + "{" + observacao + "}" +   
                            "\n" + "(OBS: Deixe o espaço em branco e pressione \'Enter\' para cancelar o processo)") + "\n");

            System.out.print((printRestr == true) ? imprimirTamanhoMinMax(limMin, limMax) + "\n\n-> " : "\n-> ");

            entradaDoUsuario = lerString();

            myPrint.limparTela();

            if (temTamanhoAdequado(entradaDoUsuario, limMin, limMax) && !entradaDoUsuario.equals("")) {

                System.err.println("Erro! entrada inválida!\nPressione \'Enter\' para continuar");
                esperarUsuario();
                myPrint.limparTela();

            }

        } while (temTamanhoAdequado(entradaDoUsuario, limMin, limMax) && !entradaDoUsuario.equals(""));

        return entradaDoUsuario;
    }

    /**
     * Função para conferir se uma String está entre um espaço de menor até maior
     * 
     * @param s     é a String a ser conferida
     * @param menor é o menor tamanho que a String pode ter
     * @param maior é o maior tamanho que a String pode ter
     * @return true or false se a String está no tamanho adequado
     */
    private boolean temTamanhoAdequado(String s, int menor, int maior) {
        return !(s.length() >= menor && s.length() <= maior);
    }

    /**
     * Função para imprimir o tamanho maximo e minimo da entrada
     * 
     * @param menor é o tamanho mínimo da entrada
     * @param maior é o tamanho máximo da entrada
     * @return é a String que será imprimida
     */
    private String imprimirTamanhoMinMax(int menor, int maior) {
        return myPrint.imprimir("{Tamanho mínimo: " + menor + "}") + ";" + 
               myPrint.imprimir("{Tamanho máximo: " + maior + "}");
    }
}
