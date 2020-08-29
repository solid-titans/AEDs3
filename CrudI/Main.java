public class Main {
    public static void main(String[] args) {
        // Criando Crud
        Crud<Livro> livros = null;
        try{
            livros = new Crud<>("Arquivo", Livro.class.getConstructor());
        
        } catch(Exception e) { e.printStackTrace(); }
            
        Livro livro1 = new Livro("Livro1", "Autor1", 10f);
        System.out.println(livro1.toString());
        livros.create(livro1);
    }
}