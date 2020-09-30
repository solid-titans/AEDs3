package menu.pergunta;

import produtos.*;
import menu.sistema.graficos.*;
import menu.sistema.Sistema;

public class ADMPerguntas {

    public static ASCIInterface graficos           = new ASCIInterface(); // Interface grafica feita em ASCII
    private PerguntasCRUD perguntasCRUD;


        public ADMPerguntas() 
        {
            perguntasCRUD = new PerguntasCRUD();
        }

         //Menu 2
    
        //Opção 1 : Tela de listagem de perguntas
        public String listarPerguntas(int IdUsuario) {

            String resp = "";

            Pergunta[] array = null;
            array = perguntasCRUD.getPerguntaArray(IdUsuario);
            if ( array == null ) {
                resp = "Ops.. parece que você não tem nenhuma pergunta...\n";
            }
            else {
                resp = listarPerguntas(array);
            }

            return resp;
        }
        
        private String listarPerguntas(Pergunta[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += graficos.caixa(3,"MINHAS PERGUNTAS");

            for (byte i = 0; i < array.length; i++,contador++) {
                if(array[i].getAtiva() == false) {
                    resp += "\n(Arquivada)";
                }
                resp += "\n" + contador + ".\n";
                resp += array[i].getData() + "\n" + array[i].getPergunta() + "\n";

            }
    
            return resp;
        }
    
        //Opção 2: Criação de pergunta
        /*
        *   Primeiro o usuario precisa inserir a pergunta
        *   se a pergunta não for vazia, ele 
        */
        public int novaPergunta(int IdUsuario) {
    
            int idResp       = -1;
            String pergunta  = "";
            String confirmar = "";
    
            Pergunta p = null;
     
            System.out.println(graficos.caixa(5,"Vamos criar uma nova pergunta"));
            System.out.print("\nPor favor insira a sua pergunta: ");
    
            pergunta = Sistema.lerEntrada();
    
            graficos.limparTela();
    
            if(!pergunta.equals("")) {
    
                System.out.println(graficos.caixa(5,"Vamos conferir a sua pergunta"));
                System.out.print("\nPergunta: " + pergunta + "\n\nEssa é a sua pergunta?(s/n) : ");
    
                confirmar = Sistema.lerEntrada();
    
                graficos.limparTela();
    
                if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {
    
                    System.out.println("Certo! a sua pergunta foi criada!");                  
                    p = new Pergunta(IdUsuario,pergunta);                

                    idResp = perguntasCRUD.novaPergunta(p,IdUsuario);
                    p.setId(idResp);
                                     
                }
                else {
    
                    System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                }
    
            }
            else {
                System.err.println("ERRO! a pergunta inserida está vazia! Tente novamente!");
            }
    
            return idResp;
        }
    
        //Opção 3: Alterar a pergunta
        public int alterarPergunta(int IdUsuario) {
    
            int IdResposta       = -1;
            int indexSelecionado = -1;
            String entrada       = "";
            String pergunta      = "";
    
            Pergunta p = null;
            Pergunta[] array = null;
    
            System.out.println(graficos.caixa(5,"Vamos alterar uma pergunta"));

            array = perguntasCRUD.getPerguntaArray(IdUsuario);

            if (array != null) {

                System.out.println(listarPerguntas(array));
    
                System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
                entrada = Sistema.lerEntrada();
        
                graficos.limparTela();

                if ( entrada.length() != 0 && !entrada.equals("0") && array.length > Integer.parseInt(entrada) - 1) {

                    indexSelecionado = array[Integer.parseInt(entrada) - 1].getId();
        
                    System.out.println("\nOk... vamos encontrar a sua pergunta.");
        
                    try {
                        p = perguntasCRUD.acharPergunta(indexSelecionado);
                    } catch(Exception e ) {}
        
                    if( p != null && p.getAtiva() != false) {
        
                        System.out.print("Sucesso! Pergunta encontrada!\nVamos alterar a sua pergunta, pressione enter para continuar...");
                        Sistema.lerEntrada();

                        graficos.limparTela();
        
                        System.out.println(graficos.caixa(5,"Vamos criar uma nova pergunta"));
                        System.out.print("\nPor favor insira a sua pergunta: ");
        
                        pergunta = Sistema.lerEntrada();

                        graficos.limparTela();
                        
                        if(!pergunta.equals("")) {
        
                            System.out.println(graficos.caixa(5,"Vamos conferir a sua pergunta"));
                            System.out.print("\nPergunta: " + pergunta + "\n\nEssa é a sua pergunta?(s/n) : ");
        
                            entrada = Sistema.lerEntrada();
        
                            graficos.limparTela();
        
                            if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {
        
                                System.out.println("Certo! a sua pergunta foi criada!");
                                p.setPergunta(pergunta);
                                perguntasCRUD.atualizarPergunta(p);
                                IdResposta = p.getId();
                            }   
                        }
                        else {
        
                             System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                        }
        
                    }
                    else {
        
                        System.err.println("ERRO! O valor inserido não é válido!\nVoltando ao menu...");
                    }
        
                }
                else {
        
                    System.out.println("Ok! Voltando menu...");
                }

            }
            else {
                System.out.println("Ops, parece que você não tem nenhuma pergunta registrada!\n");
            }
    
            return IdResposta;
        }
    
        // Opção 4 : Arquivar pergunta  
        
        public int arquivarPergunta(int IdUsuario) {
    
            int IdResposta       = -1;
            int indexSelecionado = -1;
            String entrada = "";

            Pergunta[] array = null;
            Pergunta p = null;

            array = perguntasCRUD.getPerguntaArray(IdUsuario);

            System.out.println(graficos.caixa(5,"Vamos alterar uma pergunta"));

            if (array != null ) {

                System.out.println(listarPerguntas(array));
        
                System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
                entrada = Sistema.lerEntrada();

                graficos.limparTela();
        
                if (entrada.length() != 0 && !entrada.equals("0") && array.length > Integer.parseInt(entrada) - 1) {

                    indexSelecionado = array[Integer.parseInt(entrada) - 1].getId();
        
                    System.out.println("Ok... vamos encontrar a sua pergunta.");
        
                    try {
                        p = perguntasCRUD.acharPergunta(indexSelecionado);
                    } catch(Exception e ) {}
        
                    if( p != null  &&  p.getAtiva() != false) {
        
                        System.out.println("Sucesso! Pergunta encontrada!\nVamos imprimir essa pergunta:\n\n"+ p.getPergunta()  + "\n\nConfirme se essa é a pergunta?(s/n): ");
        
                        entrada = Sistema.lerEntrada();

                        graficos.limparTela();
        
                        graficos.limparTela();
                        if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {
        
                            System.out.println("Certo! Vamos desativar essa pergunta");
                            p.setAtiva(false);
        
                            perguntasCRUD.atualizarPergunta(p);

                            IdResposta = p.getId();
                            
                        }
                        else {
        
                            System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                        }
                    }
                    else {
                        System.err.println("Erro! Pergunta não encontrada, ou já está desativada!");
                    }        
                }
                else {
        
                    System.out.println("Ok! Voltando menu...");
                }
            }
            else {
                System.out.println("Erro! Não tem nenhuma pergunta");
            }
    
            return IdResposta;
        }

}
   
