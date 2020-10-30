import java.io.File;

public class Teste {
    public static void main(String[] args) {
        GZip gzip          = new GZip();
        File entrada       = new File("Godot_v3.2.3-stable_x11.64");
        File comprimido    = new File("Comprimido.txt");
        File descomprimido = new File("Descomprimido.txt");

        gzip.comprimir(entrada, comprimido);

        gzip.descomprimir(comprimido, descomprimido);
    }
}
