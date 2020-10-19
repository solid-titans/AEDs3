package menu.sistema.abstracts.api.crudmanagers;

import produtos.Usuario;

public interface UsuarioInterface {
    public int     inserir(Usuario u);
    public Usuario achar(int id);
    public Usuario achar(String email);
    public void    atualizar(Usuario u);
    public boolean isSenha(String senhaInserida, String senhaRegistrada);
}
