public class Livro {
    private String nomeLivro;
    private String nomeAutor;
    private float preco;

    // Construtor de um livro vazio
    public Livro() {
        this("", "", -1f);
    }

    // Construtor de um Livro
    public Livro(String nome, String autor, float preco) {
        this.nomeLivro = nome;
        this.nomeAutor = autor;
        this.preco     = preco;
    }

    // Get nome do Livro
    public String getNome() {
        return this.nomeLivro;
    }

    // Get nome do Livro
    public String getAutor() {
        return this.nomeAutor;
    }

    // Get nome do Livro
    public float getPreco() {
        return this.preco;
    }
}