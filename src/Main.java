import menu.Menu;
import menu.sistema.graficos.*;

public class Main
{
    public static void main(String[] args) {
        //Menu m = new Menu();
        //m.Inicio();
        ASCIInterface a = new ASCIInterface();
        a.setBorda(8);
        a.setJanela(69);
        System.out.println(a.caixa(5,"Se liga ai Leonel"));
    }

}
