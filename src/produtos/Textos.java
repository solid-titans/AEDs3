/**
 * Registro de Strings para fazer perguntas e respostas
 * 
 */

package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import crud.Registro;

public class Textos implements Registro {
    private int id;
    private String texto;

    public Textos() {
        this.id    = -1;
        this.texto = ""; 
    }

    /**
     * Setar a ID no objeto de textos
     */
    @Override
    public void setId(int id) {
        this.id = id;

    }

    /**
     * Pegar a ID do objeto
     */
    @Override
    public int getId() {
        return this.id;

    }

    /**
     * Chave de pesquisa
     */
    @Override
    public String chaveSecundaria() {
        return this.texto;
    
    }

    /**
     * Desserializar objeto
     */
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream      data      = new DataOutputStream(byteArray);

        data.writeInt(this.id);
        data.writeUTF(this.texto);

        return byteArray.toByteArray();
    }

    /**
     * Serializar objeto
     */
    @Override
    public void fromByteArray(byte[] byteObjeto) throws IOException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(byteObjeto);
        DataInputStream      data      = new DataInputStream(byteArray);

        this.id    = data.readInt();
        this.texto = data.readUTF();
    }
    

}
