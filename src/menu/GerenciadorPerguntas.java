package menu;

import java.util.Date;
import crud.Crud;
import produtos.*;

public class GerenciadorPerguntas {

    public static ASCIInterface graficos           = new ASCIInterface(); // Interface grafica feita em ASCII
    public Crud<CelulaIDs> ids;
    public Crud<Pergunta> perguntas;


        public GerenciadorPerguntas() 
        {
            try {
                this.ids = new Crud<>("Ids",  CelulaIDs.class.getConstructor());
                this.perguntas = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
            } catch (Exception e) {}

        }

         //Menu 2
    
        //Opção 1 : Tela de listagem de perguntas
        
        public String listarPerguntas(int IdUsuario) {

            Pergunta[] array = null;
            String      resp = "";
            byte    contador = 1;

            array = getPerguntaArray(IdUsuario);
            if( array == null ) {

                resp = "\nOps.. parece que você não tem nenhum pergunta.";
            }
            else {

                resp += graficos.caixa(5,"MINHAS PERGUNTAS");

                for (byte i = 0; i < array.length; i++,contador++) {
                    if(array[i].getAtiva() == false) {
                        resp += "\n(Arquivada)";
                    }
                    resp += "\n" + contador + ".\n";
                    resp += array[i].getData() + "\n" + array[i].getPergunta() + "\n";

                }

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
                    p = new Pergunta(IdUsuario,new Date().getTime(),pergunta);                

                    idResp = perguntas.create(p);
                    p.setId(idResp);
                    
                    novoParId(IdUsuario,idResp);
                                     
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

            array = getPerguntaArray(IdUsuario);

            if (array != null) {

                System.out.println(listarPerguntas(IdUsuario));
    
                System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
                entrada = Sistema.lerEntrada();
        
                graficos.limparTela();

                if ( entrada.length() != 0 && !entrada.equals("0") && array.length > Integer.parseInt(entrada) - 1) {

                    indexSelecionado = array[Integer.parseInt(entrada) - 1].getId();

                    System.out.println(indexSelecionado);
        
                    System.out.println("\nOk... vamos encontrar a sua pergunta.");
        
                    try {
                        p = perguntas.read(indexSelecionado);
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
        
                                perguntas.update(p,p.getId());
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

            array = getPerguntaArray(IdUsuario);

            System.out.println(graficos.caixa(5,"Vamos alterar uma pergunta"));

            if (array != null ) {

                System.out.println(listarPerguntas(IdUsuario));
        
                System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
                entrada = Sistema.lerEntrada();

                graficos.limparTela();
        
                if (entrada.length() != 0 && !entrada.equals("0") && array.length > Integer.parseInt(entrada) - 1) {

                    indexSelecionado = array[Integer.parseInt(entrada) - 1].getId();
        
                    System.out.println("Ok... vamos encontrar a sua pergunta.");
        
                    try {
                        p = perguntas.read(indexSelecionado);
                    } catch(Exception e ) {}
        
                    if( p != null  &&  p.getAtiva() != false) {
        
                        System.out.println("Sucesso! Pergunta encontrada!\nVamos imprimir essa pergunta:\n\n"+ p.getPergunta()  + "\n\nConfirme se essa é a pergunta?(s/n): ");
        
                        entrada = Sistema.lerEntrada();

                        graficos.limparTela();
        
                        graficos.limparTela();
                        if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {
        
                            System.out.println("Certo! Vamos desativar essa pergunta");
                            p.setAtiva(false);
        
                            perguntas.update(p,p.getId());

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
        

    private void novoParId(int IdUsuario, int IdPergunta ) {

        CelulaIDs tmp   = null;
        String    idTmp = "";

        try {
            tmp = ids.read(IdUsuario);
        }
    catch(Exception e) {}

            if ( tmp != null) {

                idTmp = tmp.chaveSecundaria() + "-" + String.valueOf(IdPergunta);
                tmp.setChaveSecundaria(idTmp);
                ids.update(tmp,tmp.getId());

            }
            else {
                ids.create(new CelulaIDs(IdUsuario,String.valueOf(IdPergunta)));
            }
        }

    private Pergunta[] getPerguntaArray(int IdUsuario) {
        
        Pergunta[] resp = null;
        CelulaIDs  tmp  = null;
        String[] split  = null;

        try {
            
            tmp = ids.read(IdUsuario);
            if ( tmp != null) {

                split = tmp.chaveSecundaria().split("-"); 
                resp = new Pergunta[split.length];

                for (byte i = 0 ; i < split.length; i++) {

                    resp[i] = perguntas.read(Integer.parseInt(split[i]));
                }
                
            }

        }
        catch(Exception e) {}
        return resp;
    }

}
   
