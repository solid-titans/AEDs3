package menu.usuario;

import crud.Crud;
import produtos.*;

public class UsuarioCRUD{
    
    private static Crud<Usuario> usuarios    = null;
    
    public UsuarioCRUD() {
        try {
            usuarios  = new Crud<>("Usuarios",  Usuario.class.getConstructor());
        } catch(Exception e) { e.printStackTrace(); }
    }
        
    public int novoUsuario(Usuario u) {

        return usuarios.create(u);
    }

    public Usuario acharUsuario(String email) {

        Usuario resp = null;

        try {
            resp = usuarios.read(email);
        } catch(Exception e) {}

        return resp;
    }

    public Usuario acharUsuario(int IdUsuario) {

        Usuario resp = null;

        try {
            resp = usuarios.read(IdUsuario);
        } catch(Exception e) {}

        return resp;
    }

    public void atualizarUsuario(Usuario u) {
        usuarios.update(u, u.getId());  
    }


}