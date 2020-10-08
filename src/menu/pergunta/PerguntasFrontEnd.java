// Classe de interface gráfica da administração das perguntas
/*
*	Criada por: Gustavo Lopes(MysteRys337)
*	Versão : 	0.0.1
*/
package menu.pergunta;

import produtos.*;
import menu.sistema.graficos.*;
import menu.sistema.Sistema;

class PerguntasFrontEnd {
	
		//Atributos
        private static ANSILibrary   destaque = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);

        //Função que retornar todas as perguntas inseridas em uma String
        public static String listarPerguntas(Pergunta[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += PerguntasAPI.graficos.caixa(3,"PERGUNTAS");

           
            for (Pergunta i : array) {
                if(i.getAtiva() == false) {
                    resp += "\n(Arquivada)";
                }

                resp += "\n" + destaque.imprimir(contador + ".") + "\n";
                resp += toString(i);
                contador++;
                
            }
    
            return resp;
        }

        //Função que retornar todas as perguntas inseridas em uma String
        public static String listarPerguntasSimplificado(Pergunta[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += PerguntasAPI.graficos.caixa(3,"PERGUNTAS");

           
            for (Pergunta i : array) {
                if(i.getAtiva() == false) {
                    resp += "\n(Arquivada)";
                }

                resp += "\n" + destaque.imprimir(contador + ".") + "\n";
                resp += toStringSimplificada(i);
                contador++;
                
            }
    
            return resp;
        }

        public static boolean verificar(Pergunta novaPergunta) {

            String confirmar             = "";
            boolean confirmarVerificacao = false;

            System.out.println(PerguntasAPI.graficos.caixa("Vamos conferir a sua pergunta") + "\n");
            System.out.print(toString(novaPergunta) + 
                            "\nEssa é a sua pergunta?(s/n) : ");

            confirmar = Sistema.lerEntrada();

            ASCIInterface.limparTela();

            if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {

                confirmarVerificacao = true;
            }
            else {

                System.out.println("Processo cancelado!\nVoltando para o menu...\n");
            }

            return confirmarVerificacao;
        }

		//Função intermediária: escolher uma pergunta
		/*
		*	Essa função serve como intermediário para que
		*	seja enviado uma ID de usuario, listar as perguntas
		*	do tal usuário e então escolher entre uma delas
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
                    System.out.println(indexSelecionado);
            }
            else {
                System.err.println("ERRO! Entrada inválida!");
            }
            
            return indexSelecionado; 
        }

        public static String toString(Pergunta p) {
            return destaque.imprimir(p.getData()) + "\n"                        + 
                   p.getTitulo()                                                +
                   "\n" + PerguntasAPI.graficos.caixa(p.getPergunta())          + 
                   "Palavras-chave: " + destaque.imprimir(p.getPalavrasChave()) + "\n";
        }

        public static String toString(Pergunta p,String nome) {
            return p.getTitulo()                                                           +
                   "\n" + PerguntasAPI.graficos.caixa(p.getPergunta())                     +            
                   "Pergunta criada por \'"+nome+"\' em "+ destaque.imprimir(p.getData())  +
                   "\nPalavras-chave: " + destaque.imprimir(p.getPalavrasChave())    + "\n";
        }

        public static String toStringSimplificada(Pergunta p) {
            return "Título: " + destaque.imprimir(p.getTitulo()) + "\n";
        }

}
   


