package menu.sistema.controle.crudmanagers;

import produtos.*;
import seguranca.GFG;

import crud.*;

public class UsuariosCRUD {


	//GFG para hashear a senha
	public GFG hasheador;

	//Usuarios
	private static Crud<Usuario>    usuarios;

	public UsuariosCRUD() {

		this.hasheador = new GFG(1000);

		try {
			//Usuarios
            usuarios           = new Crud<>("Usuarios",  Usuario.class.getConstructor());
            
        } catch(Exception e) { e.printStackTrace(); }
	}
    
	/*
	*	Funções do CRUD de usuário
	*/

	/**
	 * Função para inserir um novo usuário no CRUD
	 * @param u é o usuário que é para ser registrado
	 * @return um numero inteiro correspondendo ao ID do novo usuário
	 */
    public int inserir(Usuario u) { 
		return usuarios.create(u); 
	}

	/**
	 * Função para encontrar um usuário a partir do email
	 * @param email é a String contendo o email
	 * @return o usuário caso ele foi encontrado
	 */
    public Usuario achar(String email) {
        Usuario resp = null;

        try {
			resp = usuarios.read(email);
			
		} catch(Exception e) {}

        return resp;
	}
	
	/**
	 * Função para encontrar um usuário a partir do ID
	 * @param idUsuario é o numero inteiro que corresponde ao ID do usuário a ser procurado
	 * @return o usuário caso ele foi encontrado
	 */
    public Usuario achar(int idUsuario) {
        Usuario resp = null;

        try {
			resp = usuarios.read(idUsuario);
			
        } catch(Exception e) {e.printStackTrace();}

        return resp;
	}
	
	/**
	 * Atualizar as credenciais de um usuário no CRUD
	 * @param u é o usuário a ser atualizado
	 */
	public void atualizar(Usuario u) { 
		usuarios.update(u, u.getId()); 
	}

}
