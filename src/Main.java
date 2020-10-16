import java.io.File;

import menu.Menu;
import seguranca.GFG;

public class Main {
    private static int NUMMAXINTERACOESHASH = 1000;

    public static void main(String[] args) {
        // Verificar se o directorio de dados existe, caso nao exista crie-o
        File directorioDados = new File("Dados");
        if(!directorioDados.exists()) directorioDados.mkdir();

        GFG seguranca = new GFG(NUMMAXINTERACOESHASH);

        Menu m = new Menu();
        m.Inicio();
    }

}
