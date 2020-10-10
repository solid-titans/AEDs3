
package menu.sistema;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.InputStream;
import java.io.InputStreamReader;

import menu.sistema.graficos.*;

/**
 * Classe para gerenciar funções generalizadas do programa
 * @author MysteRys337(Gustavo Lopes)
 */
public class Sistema {
    
    //Variaveis para leitura do teclado
    public static InputStream is            = System.in;
    public static InputStreamReader isr     = new InputStreamReader(is);
    public static BufferedReader br         = new BufferedReader(isr);


    /**
     * Recebe uma senha e retorna a força dela de 0 a 5
     * @param s e a senha que quer ser verificada
     * @return força da senha de 0 a 5 (em byte)
     */
    public static byte verificarSenha(String s) {
        byte forca = 5;

        if (!s.equals("")) {//Se a String recebida estiver vazia
            if (s.length() < 8) {
                forca--;
            }
            if(!s.matches(".*[a-z].*")) { //Verificando pela existencia de caracteres em minúsculo
                forca--;
            }
            if(!s.matches(".*[A-Z].*")) { //Verificando pela existencia de caracteres em maiúsculo
                forca--;
            }
            if(s.matches("[a-zA-Z0-9 ]*")) { //Verificando se a String possui algum caractere especial
                forca--;
            }
            if(!s.matches(".*\\d.*")) { //Verificando se possui algum numero
                forca--;
            }
        } else { //Se a String estiver vazia
            forca = 0;
            System.out.println("Erro! Nenhuma senha inserida");
        }
        return forca;
    }
  
    /**
     * Verifica se um email é valido
     * @param email e a String correspondendo ao email a ser verificado
     * @return true ou false se o email é valido
     */
    public static boolean verificarEmail(String email) {

        //Cria o padrao regex
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]{3,20}+(?:\\.[a-zA-Z0-9_+&*-]{3,20}+)*@(?:[a-zA-Z0-9-]{3,20}+\\.)+[a-zA-Z]{2,7}$", Pattern.CASE_INSENSITIVE);

        //Recebe o email recebido para ser comparado ao padrao regex
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }
  
    /**
     * Gera uma senha aleatoria e segura
     * @return uma String correspondendo a senha gerada
     */
    public static String gerarSenha() {
  
          String s = "";
          Random r = new Random();
          do { 
              for (byte i = 0 ; i < 8 ; i++) {
                  char a = (char)(r.nextInt(122 - 48) + 48);
                  s += a;
              }
              if ( verificarSenha(s) <= 2 ) {
                  s = "";
              }
          } while(s.length() == 0);
          
      return s;
    }
  
    /**
     * Escreve um 'email' (um arquivo .txt) no diretorio do projeto, com uma senha temporaria
     * @param senha corresponde a senha temporaria do usuario
     * @param usuario o nome do usuario que pediu a troca de senha
     */
    public static void escreverEmail(String senha,String usuario) {
  
        String saida = "";
        RandomAccessFile r;
  
        try {
              //Mensagem a ser enviada ao usuário
              saida ="Prezado "+ usuario +",tudo bem?\n" + 
                     "Parece que você pediu para mudar de senha e para isso\n" +
                     "estamos te mandando uma nova senha\n\n" +
                     "Essa é a sua nova senha: \n" +
                     senha +
                     "\nNa proxima vez que você logar, você tera\n" +
                     "a possibilidade de mudar de senha\n\n" +
                     "Muito obrigado e um excelente dia!";
  
              r = new RandomAccessFile("email.txt", "rw");     
                  
              r.writeUTF(saida);     
  
        } catch( Exception e) {
            System.err.println("Deu errado");
        }
    }
  
    /**
     * Função que faz a leitura do input de uma String pelo teclado
     * @return a String lida
     */
    public static String lerEntrada() {
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
    public static char lerChar() {
        String entrada = "";
        char resp = '\0';

        entrada = lerEntrada();

        if(!entrada.equals("")) {
          resp = entrada.charAt(0);
        }
        return resp;       
    }
    
    /**
     * Função que faz a leitura do input de uma String pelo teclado e transforma para um numero inteiro
     * @return um numero inteiro digitado pelo usuário
     */
    public static int lerInt() {
    
    	int resp = -1;
    	String entrada = "";
    	
    	entrada = lerEntrada();
    	
    	if(!entrada.equals("") && entrada.matches("[0-9]+")) {
          resp = Integer.parseInt(entrada);
      }

      return resp;
    
    }

    /**
     * Função que faz a leitura de um input de uma String pelo teclado e transforma em um byte(numero entre -128 e 127)
     * @return um byte digitado pelo usuario
     */
    public static byte lerByte() {

      int  aux  = -1;
      byte resp = -1;

      aux = lerInt();

      if ( aux >= -128 && aux <= 127)
        resp = (byte)aux;

      return resp;
    }

    /**
     * Cria uma interface de interação com o usuário para pegar o input do teclado
     * @param graficos é o padrão de interface que será utilizado
     * @param titulo é o título dentro da caixa gerada pelo padrão de interface
     * @param tamanhoMinimoDaEntrada é o tamanho mínimo da entrada que será feita pelo usuário
     * @return a String que o usuário inseriu
     */
    public static String inserir(ASCIInterface graficos,String titulo,int tamanhoMinimoDaEntrada) {
      String entradaDoUsuario = "";

      do {         
          System.out.print(graficos.caixa(titulo) + "\n-> ");
          entradaDoUsuario = lerEntrada();

          ASCIInterface.limparTela();
          if(entradaDoUsuario.length() < tamanhoMinimoDaEntrada) {
              System.err.println("Erro! entrada inválida!\nPressione \'Enter\' para continuar");
              lerEntrada();
          }
      } while(entradaDoUsuario.length() < tamanhoMinimoDaEntrada);

      return entradaDoUsuario;
    }

    /**
     * Cria uma interface de interação com o usuário para pegar o input do teclado
     * @param graficos é o padrão de interface que será utilizado
     * @param titulo é o título dentro da caixa gerada pelo padrão de interface
     * @param observacao é a String adicional que contenha qualquer observação que seja pertinente informar ao usuário
     * @param tamanhoMinimoDaEntrada é o tamanho mínimo da entrada que será feita pelo usuário
     * @return
     */
    public static String inserir(ASCIInterface graficos,String titulo,String observacao, int tamanhoMinimoDaEntrada) {
        String entradaDoUsuario = "";

        do { 
            System.out.print(graficos.caixa(titulo) + observacao + "\n\n-> ");

            entradaDoUsuario = lerEntrada();

            ASCIInterface.limparTela();

            if(entradaDoUsuario.length() < tamanhoMinimoDaEntrada) {
                System.err.println("Erro! entrada inválida!\nPressione \'Enter\' para continuar");
                lerEntrada();
                ASCIInterface.limparTela();
            }

        } while(entradaDoUsuario.length() < tamanhoMinimoDaEntrada);

        return entradaDoUsuario;
    }

    /**
    * Função para esperar feedback do usuário para continuar no programa
    */
    public static void esperarUsuario() {
        System.out.print("\nPressione \'Enter\' para continuar...");
        Sistema.lerEntrada();
    }
}
