import produtos.Livro;
import produtos.Usuario;
import crud.*;

//import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Criando Crud
        Crud<Livro> livros = null;
        try{
            livros = new Crud<>("Livros", Livro.class.getConstructor());
        
        } catch(Exception e) { e.printStackTrace(); }
            
        Livro livro1 = new Livro("Livro1", "Autor1", 10f);
        Livro livro2 = new Livro("Livro2", "Autor2", 100f);
        Livro livro3 = new Livro("Livro3", "Autor3", 3f);
        livros.create(livro1);
        livros.create(livro2);
        livros.create(livro3);

        try {
            System.out.println(livros.read("Livro1"));

        } catch (Exception e) {
            System.out.println("Livro1 não encontrado! ");

        }

        try {
            System.out.println(livros.read("Livro2"));

        } catch (Exception e) {
            System.out.println("Livro2 não encontrado! ");

        }

        livros.update(livro3, 1);

        try {
            System.out.println(livros.read("Livro1"));

        } catch (Exception e) {
            System.out.println("Livro1 não encontrado! ");

        }

        livros.delete("Livro1");

        try {
            livros.read("Livro1");

        } catch (Exception e) {
            System.out.println("Livro ID 1 apagado com sucesso!");

        }

    }
    
    /*public static Scanner leitor;
    public static void main(String[] args) {
        leitor = new Scanner(System.in);

        // Exibir o menu na tela
        int resp = menuPrincipal();

        Crud<Livro>   livros   = null;
        Crud<Usuario> usuarios = null;

        
        // Criando Crud
        if(resp == 1) {
            try {
                livros   = new Crud<>("Livros", Livro.class.getConstructor());

            } catch (Exception e) { e.printStackTrace(); }

        } else {
            try{
                usuarios = new Crud<>("Usuarios", Usuario.class.getConstructor());
            
            } catch(Exception e) { e.printStackTrace(); }
            
        }
        
    }

    // Exibir menu de interação na tela
    public static int menuPrincipal() {
        int entrada;

        do {
            limparTela();
    
            System.out.println("Bem-vindo ao meu Crud");
            System.out.println("Escolha qual crud fazer: ");
            System.out.println("1 - Livros");
            System.out.println("2 - Usuarios");
    
            System.out.println("Resposta: ");
            entrada = leitor.nextInt();

        } while(entrada > 0 && entrada < 3); 
        
        return entrada;
    }

    // Criar novo livro
    public static void novosLivros() {
        limparTela();

        System.out.println("Acões do Crud");
        System.out.println("1 - Criar novo livro");
        System.out.println("2 - Ler um livro");
        System.out.println("3 - Atualizar livro");
        System.out.println("4 - Apagar registro");
        System.out.println("Resposta: ");

    }

    // Limpar tela
    public static void limparTela() {
        for(int i = 0; i < 100; i++) {
            System.out.println("");
        }
    }*/
}