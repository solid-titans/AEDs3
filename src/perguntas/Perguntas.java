package perguntas;

import java.util.Date;
import crud.indices.*;
import crud.Crud;
import menu.*;
import produtos.*;

public class Perguntas {

    public ArvoreBMais_String_Int arvB;
    public Crud<Pergunta> perguntas;
    public static ASCIInterface a           = new ASCIInterface(); // Interface grafica feita em ASCII


        public Perguntas(Crud<Pergunta> perguntas)
        {
            this.perguntas = perguntas;
            
            try {
            this.arvB = new ArvoreBMais_String_Int(5, "teste" + ".blus");
            } catch (Exception e) {}

        }


         //Menu 2
    
        //Opção 1 : Tela de listagem de perguntas
        public String listagem() {
    
            String resp = "\nLista de perguntas: \n\n";
    
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
     
            System.out.println(a.caixa(5,"Vamos criar uma nova pergunta"));
            System.out.print("\nPor favor insira a sua pergunta: ");
    
            pergunta = Sistema.lerEntrada();
    
            a.limparTela();
    
            if(!pergunta.equals("")) {
    
                System.out.println(a.caixa(5,"Vamos conferir a sua pergunta"));
                System.out.print("\nPergunta: " + pergunta + "\n\nEssa é a sua pergunta?(s/n) : ");
    
                confirmar = Sistema.lerEntrada();
    
                a.limparTela();
    
                if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {
    
                    System.out.println("Certo! a sua pergunta foi criada!");
                    
                    System.out.println(IdUsuario);
                    //System.out.println(new Date().getTime());
                    System.out.println(pergunta);
                    
                    
                    p = new Pergunta(IdUsuario,-1,pergunta);
                    



                    idResp = perguntas.create(p);
                    
                    try {
                    arvB.create(String.valueOf(IdUsuario), idResp);
                    } catch (Exception e) {}
                    
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
        public int alterarPergunta() {
    
            int idResposta = -1;
            String entrada = "";
            String pergunta  = "";
    
            Pergunta p = null;
    
            System.out.println(a.caixa(5,"Vamos alterar uma pergunta"));
            listagem();
    
            System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
            entrada = Sistema.lerEntrada();
    
            if ( entrada.length() != 0 && !entrada.equals("0")) {
    
                System.out.println("Ok... vamos encontrar a sua pergunta.");
    
                try {
                    p = perguntas.read(Integer.parseInt(entrada) - 1);
                } catch(Exception e ) {}
    
                if( p == null ) {
    
                    System.out.print("Sucesso! Pergunta encontrada!\nVamos alterar a sua pergunta, pressione enter para continuar...");
                    Sistema.lerEntrada();
    
                    System.out.println(a.caixa(5,"Vamos criar uma nova pergunta"));
                    System.out.print("\nPor favor insira a sua pergunta: ");
    
                    pergunta = Sistema.lerEntrada();
                    
                    if(!pergunta.equals("")) {
    
                        System.out.println(a.caixa(5,"Vamos conferir a sua pergunta"));
                        System.out.print("\nPergunta: " + pergunta + "\n\nEssa é a sua pergunta?(s/n) : ");
    
                        entrada = Sistema.lerEntrada();
    
                        a.limparTela();
    
                        if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {
    
                            System.out.println("Certo! a sua pergunta foi criada!");
                            p.setPergunta(pergunta);
    
                            perguntas.update(p,p.getId());
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
    
            return idResposta;
        }
    
        // Opção 4 : Arquivar pergunta
        /*
        *   
        
        public int arquivarPergunta() {
    
            int idResposta = -1;
            String entrada = "";
            String pergunta  = "";
    
            Pergunta p = null;
    
            System.out.println(a.caixa(5,"Vamos alterar uma pergunta"));
            listagem();
    
            System.out.print("\nInsira o numero da pergunta que você quer alterar:\nObs: Pressione \'0\' para voltar ao menu\n-> ");
            entrada = Sistema.lerEntrada();
    
            if ( entrada.length() != 0 && !entrada.equals("0")) {
    
                System.out.println("Ok... vamos encontrar a sua pergunta.");
    
                try {
                    p = perguntas.read(Integer.parseInt(entrada) - 1);
                } catch(Exception e ) {}
    
                if( p == null ) {
    
                    System.out.println("Sucesso! Pergunta encontrada!\nVamos imprimir essa pergunta:");
    
                    System.out.print("\nConfirme se essa é a pergunta?(s/n): ");
    
                    entrada = Sistema.lerEntrada();
    
                    a.limparTela();
                    if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {
    
                        System.out.println("Certo! Vamos então criar o usuario "+ usuario +" para você!");
                        p.setAtiva(false);
    
                        perguntas.update(p,p.getId());
                        
                    }
                    else {
    
                        System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                    }
    
            }
            else {
    
                System.out.println("Ok! Voltando menu...");
            }
    
            return idResposta;
            }
        }
        */
}
   