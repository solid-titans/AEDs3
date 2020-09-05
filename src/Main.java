import produtos.*;
import crud.*;

public class Main {
    public static void main(String[] args) {
        // Criando Crud
        Crud<Livro> livros = null;
        try{
            livros = new Crud<>("Arquivo", Livro.class.getConstructor());
        
        } catch(Exception e) { e.printStackTrace(); }
            
        Livro livro1 = new Livro("Livro1", "Autor1", 10f);
        Livro livro2 = new Livro("Livro2", "Autor2", 100f);
        Livro livro3 = new Livro("Livro3", "Autor3", 3f);
        livros.create(livro1);
        livros.create(livro2);
        livros.create(livro3);

        System.out.println(livros.read(0));
        System.out.println(livros.read(1));

        livros.update(livro3, 1);

        System.out.println(livros.read(1));

        livros.delete(1);

        if(livros.read(1) == null)
            System.out.println("Livro ID 1 apagado com sucesso!");

    }
}