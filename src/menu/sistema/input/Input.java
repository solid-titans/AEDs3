package menu.sistema.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import menu.sistema.abstracts.input.InputInterface;

/**
 * Classe para simplificar a entrada de dados pelo teclado
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class Input implements InputInterface {

    //Variaveis para leitura do teclado
    public InputStream         is;
    public InputStreamReader   isr;
    public BufferedReader      br;

    public Input() {
      this.is  = System.in;
      this.isr = new InputStreamReader(is);
      this.br  = new BufferedReader(isr);
    }

   /**
     * Função que faz a leitura do input de uma String pelo teclado
     * @return a String lida
     */
    public String lerString() {
        String resp = "";
        try {
            resp = br.readLine();
        } catch(IOException e ) {System.err.println("Erro na leitura!");}
        return resp;
    }

    /**
     * Função que faz a leitura do input de uma String pelo teclado e pega o primeiro caractere(Char)
     * @return o primeiro caractere da String colocada
     */
    public char lerChar() {
        String entrada = "";
        char resp = '\0';

        entrada = lerString();

        if(!entrada.equals("")) {
          resp = entrada.charAt(0);
        }
        return resp;       
    }
    
    /**
     * Função que faz a leitura do input de uma String pelo teclado e transforma para um numero inteiro
     * @return um numero inteiro digitado pelo usuário
     */
    public int lerInt() {
    
    	int resp = -1;
    	String entrada = "";
    	
    	entrada = lerString();
    	
    	if(!entrada.equals("") && entrada.matches("[0-9]+")) {
          resp = Integer.parseInt(entrada);
      }

      return resp;
    
    }

    /**
     * Função que faz a leitura de um input de uma String pelo teclado e transforma em um byte(numero entre -128 e 127)
     * @return um byte digitado pelo usuario
     */
    public byte lerByte() {

      int  aux  = -1;
      byte resp = -1;

      aux = lerInt();

      if ( aux >= -128 && aux <= 127)
        resp = (byte)aux;

      return resp;
    }

    /**
    * Função para esperar feedback do usuário para continuar no programa
    */
    public void esperarUsuario() {
        System.out.print("\nPressione \'Enter\' para continuar...");
        lerString();
    }
}
