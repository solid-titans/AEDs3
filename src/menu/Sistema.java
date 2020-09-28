package menu;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.RandomAccessFile;

// Funções secundarias

public class Sistema {
    
    //Verificar se uma senha(String) é forte
    /*
    * Para verificar se uma senha e forte ela
    * precisa atender a algumas demandas:
    *   -> Ter mais de 8 dígitos
    *   -> Ter algum caractere em minusculo
    *   -> Ter algum caractere em maiusculo
    *   -> Possuir algum caractere especial(Exemplo: *?#)
    *   -> Possuir pelo menos 1 digito
    *
    * O objetivo da funcao e verificar se todas essas
    * demandas são atendidas
    */

	public static byte verificarSenha(String s) {

        //Variavel que vai retornar com a qualidade da senha
          byte forca = 5;
  
        //Se a String recebida estiver vazia
          if (!s.equals("")) {
  
            //Verificando se a String possui um tamanho menor que 8
              if (s.length() < 8) {
  
                  forca--;
                  //System.out.println("Sua senha esta curta!\nRecomendamos uma senha entre 8 e 12 digitos\n");
              }
  
            //Verificando pela existencia de caracteres em minúsculo
              if(!s.matches(".*[a-z].*")) {
  
                  forca--;
                  //System.out.println("Tente inserir pelo menos um caractere minusculo\n");
              }
  
            //Verificando pela existencia de caracteres em maiúsculo
              if(!s.matches(".*[A-Z].*")) {
  
                  forca--;
                  //System.out.println("Tente inserir pelo menos um caractere maiusculo\n");
              }
  
            //Verificando se a String possui algum caractere especial
              if(s.matches("[a-zA-Z0-9 ]*")) {
  
                  forca--;
                  //System.out.println("Coloque um caractere especial na sua senha\n");
              }
  
            //Verificando se possui algum numero
              if(!s.matches(".*\\d.*")) {
  
                  forca--;
                  //System.out.println("Tente inserir pelo menos um numero na sua senha\n");
              }
          }
        //Se a String estiver vazia
          else {
  
              forca = 0;
              System.out.println("Erro! Nenhuma senha inserida");
          }
  
          return forca;
      }
  
      //Verificar se o e-mail é valido
      /*
      *   Recebe um email como String e atraves
      *   de regex e as classes Pattern e Matcher
      *   verifica se o email enviado eh valido
      */
      public static boolean verificarEmail(String email) {
  
          //Cria o padrao regex
          Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]{3,20}+(?:\\.[a-zA-Z0-9_+&*-]{3,20}+)*@(?:[a-zA-Z0-9-]{3,20}+\\.)+[a-zA-Z]{2,7}$", Pattern.CASE_INSENSITIVE);
  
          //Recebe o email recebido para ser comparado ao padrao regex
          Matcher matcher = pattern.matcher(email);
  
          return matcher.find();
      }
  
      //Criar senha aleatoria
      /*
      *   Essa função é usada no contexto de criação
      *   de redefinição de senha, quando ele pedir
      *   pela troca da senha, sera enviado ao 'email'
      *   uma senha aleatoria, e essa senha aleatoria
      *   é criada nessa função, usando a classe 'Random'
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
  
    //Operação de escrever email
    /*
    * Essa função serve para simular a operação
    * de enviar a nova senha pelo email, nesse caso
    * o email na verdade é um arquivo .txt com a senha
    * acompanhada de um texto.
    */
    public static void escreverEmail(String senha,String usuario) {
  
        String saida = "";
        RandomAccessFile r;
  
        try {
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
  
      public static String lerEntrada(BufferedReader br) {
          String resp = "";
          try {
              resp = br.readLine();
          } catch(IOException e ) {System.err.println("Erro na leitura!");}
          return resp;
      }
}