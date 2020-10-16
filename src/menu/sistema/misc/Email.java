package menu.sistema.misc;

import java.io.RandomAccessFile;

public class Email {

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
}
