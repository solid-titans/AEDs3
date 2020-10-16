package menu.sistema.misc;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe para gerenciar funções que trabalham com regex
 * @author MysteRys337(Gustavo Lopes)
 */
public class Regex {

    /**
     * Recebe uma senha e retorna a força dela de 0 a 5
     * @param s e a senha que quer ser verificada
     * @return força da senha de 0 a 5 (em byte)
     */
    public static byte verificarSenha(String s) {
        byte forca = 5;

        if (!s.equals("")) {//Se a String recebida estiver vazia
            if (s.length() < 8) 
                forca--;
            
            if(!s.matches(".*[a-z].*"))    //Verificando pela existencia de caracteres em minúsculo
                forca--;
            
            if(!s.matches(".*[A-Z].*"))    //Verificando pela existencia de caracteres em maiúsculo
                forca--;
            
            if(s.matches("[a-zA-Z0-9 ]*")) //Verificando se a String possui algum caractere especial
                forca--;
            
            if(!s.matches(".*\\d.*"))      //Verificando se possui algum numero
                forca--;
            
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

}
