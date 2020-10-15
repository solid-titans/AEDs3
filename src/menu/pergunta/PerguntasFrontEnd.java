package menu.pergunta;

import produtos.*;
import menu.sistema.graficos.*;
import menu.sistema.CodigoDeProtocolo;
import menu.sistema.Sistema;

/**
 * Classe para administrar casos específicos da admnistração visual das Perguntas
 * @author MysteRys337 (Gustavo Lopes)
 */
public class PerguntasFrontEnd {
	
		//Atributos
        private static ANSILibrary destaqueData             = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
        private static ANSILibrary destaqueTitulo           = new ANSILibrary(15, 27, ANSILibrary.TEXTO_SUBLINHADO);
        private static ANSILibrary destaquePalavrasChave    = new ANSILibrary(135, 154, ANSILibrary.TEXTO_SUBLINHADO);

        /**
         * Função para retornar todas as perguntas em um array em formato String
         * @param array é o array de perguntas que foi enviado
         * @return a String correspondente a listagem das perguntas
         */
        public static String listarPerguntas(Pergunta[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += PerguntasAPI.graficos.caixa(3,"PERGUNTAS");

           
            for (Pergunta i : array) {
                if(i.getAtiva() == false) {
                    resp += "\n(Arquivada)";
                }

                resp += "\n" + destaqueData.imprimir(contador + ".") + "\n";
                resp += toString(i) + "\n";
                contador++;
                
            }
    
            return resp;
        }

        /**
         * Função para retornar todas as perguntas em um array em formato String de forma simplificada
         * @param array é o array de perguntas que foi enviado
         * @return a String correspondente a listagem das perguntas
         */
        public static String listarPerguntasSimplificado(Pergunta[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += PerguntasAPI.graficos.caixa(3,"PERGUNTAS");

           
            for (Pergunta i : array) {
                if(i.getAtiva() == false) {
                    resp += "\n(Arquivada)";
                }

                resp += "\n" + destaqueData.imprimir(contador + ".") + "\n";
                resp += toStringSimplificada(i);
                contador++;
                
            }
    
            return resp;
        }

        /**
         * Função para fazer a confirmação com o usuário se essa é a pergunta que será registrada/alterada/arquivada
         * @param p é a pergunta que foi recebida seja qual for a operação
         * @return um codigo de protocolo referente ao resultado da verificacao
         */
        public static CodigoDeProtocolo verificar(Pergunta p) {

            String              confirmar   = "";
            CodigoDeProtocolo   sucesso     = CodigoDeProtocolo.ERRO;

            System.out.println(PerguntasAPI.graficos.caixa("Vamos conferir a sua pergunta") + "\n");
            System.out.print(toString(p) + 
                            "\nEssa é a sua pergunta?(s/n) : ");

            confirmar = Sistema.lerEntrada();

            ASCIInterface.limparTela();

            if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {

                sucesso = CodigoDeProtocolo.SUCESSO;
            } else {
                sucesso = CodigoDeProtocolo.OPERACAOCANCELADA;
                System.out.println("Processo cancelado!\nVoltando para o menu...\n");
            }

            return sucesso;
        }

        /**
         * Função intermediaria para escolher uma pergunta em um determinado array
         * @param array que é o array de perguntas na qual o usuário terá que fazer uma escolha
         * @return o Id da pergunta que o usuário escolheu
         */
        public static int escolherPergunta(Pergunta[] array) {

            byte     entrada          = -1;
            int      indexSelecionado = -1;
    
            System.out.print(PerguntasFrontEnd.listarPerguntasSimplificado(array) + 
                              "\nEscolha uma das perguntas: \nObs: Pressione \'0\' para voltar ao menu\n-> ");
    
            entrada = Sistema.lerByte();
            ASCIInterface.limparTela();     
    
            if (array.length > entrada - 1 && entrada - 1 >= 0) {

                    indexSelecionado = array[entrada -1].getId();
            }
            else {
                if ( entrada != 0 )
                    System.err.println("ERRO! Entrada inválida!");
                else
                    indexSelecionado = -3; //Sinal para o programa que o usuário cancelou o processo
            }
            
            return indexSelecionado; 
        }

        /**
         * Função retornar uma String com o conteúdo da pergunta P formatado
         * @param p é a pergunta que foi recebida
         * @return uma String com o conteúdo da Pergunta
         */
        public static String toString(Pergunta p) {
            return destaqueData.imprimir(p.getData()) + "\n"                                 + 
                   destaqueTitulo.imprimir(p.getTitulo())                                    +
                   "\n" + PerguntasAPI.graficos.caixa(p.getPergunta())                       + 
                   "Palavras-chave: " + destaquePalavrasChave.imprimir(p.getPalavrasChave()) + "\n";
        }

        /**
         * Função para retornar uma String com o conteúdo da pergunta P formatado
         * @param p é a pergunta que foi recebida
         * @param nome é o nome do usuário que registrou a pergunta
         * @return uma String com o conteúdo da Pergunta
         */
        public static String toString(Pergunta p,String nome) {
            return p.getTitulo()                                                               +
                   "\n" + PerguntasAPI.graficos.caixa(p.getPergunta())                         +            
                   "Pergunta criada por \'"+nome+"\' em "+ destaqueData.imprimir(p.getData())  +
                   "\nPalavras-chave: " + destaquePalavrasChave.imprimir(p.getPalavrasChave()) + "\n" +
                   "Nota: " + p.getNota() + "\n";
        }

        /**
         * Função para retornar uma String com o conteúdo da pergunta P formatado de forma simplificada
         * @param p é a pergunta que foi recebida
         * @return uma String com o conteúdo da Pergunta
         */
        public static String toStringSimplificada(Pergunta p) {
            return "Título:         " + destaqueTitulo.imprimir(p.getTitulo()) + "\n" +
                   "Palavras-chave: " + destaquePalavrasChave.imprimir(p.getPalavrasChave()) + "\n";
        }

}
   


