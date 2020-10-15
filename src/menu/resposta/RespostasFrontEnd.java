package menu.resposta;

import menu.sistema.*;
import menu.sistema.graficos.*;
import produtos.Resposta;

public class RespostasFrontEnd {

		//Atributos
        private static ANSILibrary destaqueData             = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);

        /**
         * Função para retornar todas as respostas em um array em formato String
         * @param array é o array de respostas que foi enviado
         * @return a String correspondente a listagem das respostas
         */
        public static String listarRespostas(Resposta[] array) {

            String  resp     = "";

            byte    contador = 1;

            resp += RespostaAPI.graficos.caixa(3,"Respostas");

           
            for (Resposta i : array) {
                if(i.getAtiva() == false) 
                    resp += "\n(Arquivada)";

                resp += "\n" + destaqueData.imprimir(contador + ".") + "\n";
                resp += toString(i) + "\n";
                contador++;
                
            }
    
            return resp;
        }

        /**
         * Função para retornar todas as respostas em um array em formato String
         * @param array é o array de respostas que foi enviado
         * @return a String correspondente a listagem das respostas
         */
        public static String listarRespostasGeral(Resposta[] array) {

            String  resp     = "";
            String  nome     = "";
            
            byte    contador = 1;

            resp += RespostaAPI.graficos.caixa(3,"Respostas");

           
            for (Resposta i : array) {
                if(i.getAtiva() == false) 
                    continue;

                nome = CrudAPI.acharUsuario(i.getIdUsuario()).getNome();

                resp += "\n" + destaqueData.imprimir(contador + ".") + "\n";
                resp += toString(i,nome) + "\n";
                contador++;
                
            }
    
            return resp;
        }


        /**
         * Função para fazer a confirmação com o usuário se essa é a resposta que será registrada/alterada/arquivada
         * @param r é a resposta que foi recebida seja qual for a operação
         * @return um codigo de protocolo referente ao resultado da verificacao
         */
        public static CodigoDeProtocolo verificar(Resposta r) {

            String              confirmar   = "";
            CodigoDeProtocolo   sucesso     = CodigoDeProtocolo.ERRO;

            System.out.println(RespostaAPI.graficos.caixa("Vamos conferir a sua pergunta") + "\n");
            System.out.print(toString(r) + 
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
         * Função intermediaria para escolher uma resposta em um determinado array
         * @param array que é o array de respostas na qual o usuário terá que fazer uma escolha
         * @return o Id da resposta que o usuário escolheu
         */
        public static int escolherResposta(Resposta[] array) {

            byte     entrada          = -1;
            int      indexSelecionado = -1;
    
            System.out.print(listarRespostas(array) + 
                            "\nEscolha uma das respostas: \nObs: Pressione \'0\' para voltar ao menu\n-> ");
    
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
         * Função retornar uma String com o conteúdo da resposta r formatado
         * @param r é a resposta que foi recebida
         * @return uma String com o conteúdo da resposta
         */
        public static String toString(Resposta r) {
            return destaqueData.imprimir(r.getData()) + "\n"         + 
                   "\n" + RespostaAPI.graficos.caixa(r.getResposta());
        }

        /**
         * Função retornar uma String com o conteúdo da resposta r formatado
         * @param r é a resposta que foi recebida
         * @return uma String com o conteúdo da Resposta
         */
        public static String toString(Resposta r,String nome) {
            return "\n" + RespostaAPI.graficos.caixa(r.getResposta())                            +
                   "Respondida em " + destaqueData.imprimir(r.getData()) + " por " + nome + "\n" +
                   "Nota: " + r.getNota() + "\n";
        }
}