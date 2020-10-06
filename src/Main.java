import java.io.File;

import menu.Menu;

public class Main {
    public static void main(String[] args) {
        // Verificar se o directorio de dados existe, caso nao exista crie-o
        File directorioDados = new File("Dados");
        if(!directorioDados.exists()) directorioDados.mkdir();

        Menu m = new Menu();
        m.Inicio();
    }

}
