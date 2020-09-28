import crud.Crud;
import produtos.*;
import menu.Menu;

public class Main
{

    //Cria o CRUD
    public static Crud<Usuario> usuarios    = null;
    public static Crud<Pergunta> perguntas  = null;

    public static void main(String[] args) {

        try {
    
            usuarios = new Crud<>("Usuarios", Usuario.class.getConstructor());
            perguntas = new Crud<>("Perguntas", Pergunta.class.getConstructor());
        
        } catch(Exception e) { e.printStackTrace(); }

        Menu menu = new Menu(usuarios, perguntas);
        menu.gerenciamento();
        
    
    }

}
