package menu.backend.listas.abstracts;

public interface ListaIDsInterface {
    public boolean create(int idUsuario, int idPergunta);
    public int[]   read(int idUsuario);
    public boolean delete(int idUsuario,int idPergunta);

}
