package produtos.abstracts;

public interface RegistroVisualplus extends RegistroVisual {
    public boolean getAtiva();
    public String imprimirSimplificado();
    public String imprimir(String nome);
    public int    getIdUsuario();
}
