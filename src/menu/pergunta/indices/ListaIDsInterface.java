package menu.pergunta.indices;

public interface ListaIDsInterface {
    public boolean create(int idUsuario, int idPergunta);
    public int[]   read(int idUsuario);
    public boolean delete(int idPergunta);

}
