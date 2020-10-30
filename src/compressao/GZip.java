
/** Implementação do compressor GZip por Lucas Santiago
 *  V1.0
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZip {

    public void comprimir(File entrada, File saida) {
        try{
            GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(saida));
            FileInputStream  in  = new FileInputStream(entrada); 

            byte[] buffer = new byte[1024];
            int    tamanho;

            while((tamanho = in.read(buffer)) != -1) {
                out.write(buffer, 0, tamanho);
            }

            out.close();
            in.close();

        } catch(Exception e) { e.printStackTrace(); }       
    }

    public void descomprimir(File entrada, File saida) {
        try {
            GZIPInputStream  in  = new GZIPInputStream(new FileInputStream(entrada));
            FileOutputStream out = new FileOutputStream(saida);

            byte[] buffer = new byte[1024];
            int tamanho;

            while((tamanho = in.read(buffer)) != -1) {
                out.write(buffer, 0, tamanho);
            }

            out.close();
            in.close();

        } catch(Exception e) { e.printStackTrace(); }
    }
}