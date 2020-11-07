package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import produtos.abstracts.RegistroVisual;

public class Comentario implements RegistroVisual{
    
    private byte   tipo;
    private int    idUsuario;
    private int    idComentario;
    private int    idPR;
    private String comentario;

    //Construtor de votos vazia
    public Comentario() {
        this.tipo         = -1;
        this.idComentario = -1;
        this.idUsuario    = -1;
        this.idPR         = -1;
        this.comentario   = "";
    }

    //Construtor de votos
    public Comentario(byte tipo, int idUsuario, int idPR) {
        this.tipo         = tipo;
        this.idComentario = -1;
        this.idUsuario    = idUsuario;
        this.idPR         = idPR;
        this.comentario   = "";
    }

    public String chaveSecundaria() {
        return null;
    }

    //Funções 'get'
    public byte getTipo() {
        return this.tipo;
    }
    
    public char getTipoChar() {
        return getTipo() == 0 ? 'R':'P';
    }

    public int getId() {
        return this.idComentario;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public int getIdPR() {
        return this.idPR;
    }

    public String getVoto() {
        return this.comentario;
    }

    //Funções 'set'
    public void setTipo(byte tipo) {
        this.tipo = tipo;
    }

    public void setId(int idComentario) {
        this.idComentario = idComentario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdPR(int idPR) {
        this.idPR = idPR;
    }

    public void setVoto(String comentario) {
        this.comentario = comentario;
    }

    // Serializar objeto
    /*
    *   Faz uso do ByteArrayOutputStream e do DataOutputStream
    *   para armazenar os dados do usuário dentro de um array
    *   bytes e depois retornar esse array.
    */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream      data      = new DataOutputStream(byteArray);

        data.writeInt(this.idComentario);
        data.writeUTF(this.comentario);

        return byteArray.toByteArray();
    }

    // Desserializar objeto
    /*
    *   Recebe um array de bytes, que a partir do uso do 
    *   ByteArrayInputStream e o DataInputStream, consegue
    *   receber os dados do array e armazenar no objeto.
    */
    public void fromByteArray(byte[] arrayObjeto) throws IOException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(arrayObjeto);
        DataInputStream      data      = new DataInputStream(byteArray);

        this.idComentario = data.readInt();
        //this.idUsuario    = data.readInt();
        //this.idPR         = data.readInt();
        this.comentario   = data.readUTF();
    }

    
    @Override
    public String imprimir() {
        return null;
    }
    
}