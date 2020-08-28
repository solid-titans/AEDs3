// Exemplo de classe para ser usada no Crud

class Livro {
    private String nomeLivro;
    private String nomeAutor;
    private float preco;

    // Criando um livro
    public Livro(String nome, String autor, float preco) {
        this.nomeLivro = nome;
        this.nomeAutor = autor;
        this.preco     = preco;
    }

    // Get nome do Livro
    public String nomeLivro() {
        return this.nomeLivro;
    }

    // Get nome do Autor
    public String nomeAutor() {
        return this.nomeAutor;
    }

    // Get preco do Livro
    public float getPreco() {
        return this.preco;
    }
}