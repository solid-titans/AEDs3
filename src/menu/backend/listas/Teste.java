package menu.backend.listas;

public class Teste {
    public static void main(String[] args) {
        ListaIDs lista = new ListaIDs("Dados/db");

        lista.create(0, 0);
        lista.create(1, 2);
        lista.create(0, 3);
        lista.create(2, 10);
        lista.create(1, 6);
        lista.create(2, 20);
        lista.create(1, 4);
        lista.create(0, 2);
        lista.create(0, 1);
        lista.create(1, 5);
        lista.create(2, 50);

        lista.delete(1, 5);
        lista.delete(1, 4);

        int[] resposta = lista.read(1);
        for (int i : resposta) {
            System.out.println(i);
        }

    }
}
