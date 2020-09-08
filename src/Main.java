import produtos.Livro;
import crud.*;

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

        //livros.update(livro3, 1);

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
}