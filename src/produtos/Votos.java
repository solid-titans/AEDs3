package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Votos {
    
    private byte tipo;
    private int idUsuario;
    private int idVoto;
    private int idPR;
    private boolean voto;

    //Construtor de votos vazia
    public Votos() {
        this.tipo      = -1;
        this.idVoto    = -1;
        this.idUsuario = -1;
        this.idPR      = -1;
        this.voto      = false;
    }

    //Construtor de votos
    public Votos(byte tipo, int idUsuario, int idVoto, int idPR, boolean voto) {
        this.tipo      = tipo;
        this.idVoto    = idVoto;
        this.idUsuario = idUsuario;
        this.idPR      = idPR;
        this.voto      = voto;
    }

    public String chaveSecundaria() {
        return idUsuario + "|" + (tipo?'P':'R') + "|" + idPR;
    }

    //Funções 'get'
    public byte getTipo() {
        return this.tipo;
    }

    public int getIdVoto() {
        return this.idVoto;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public int getIdPR() {
        return this.idPR;
    }

    public boolean getVoto() {
        return this.voto;
    }

    //Funções 'set'
    public void setTipo(byte tipo) {
        this.tipo = tipo;
    }

    public void setIdVoto(int idVoto) {
        this.idVoto = idVoto;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdPR(int idPR) {
        this.idPR = idPR;
    }

    public void setVoto(boolean voto) {
        this.voto = voto;
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

        data.writeByte(this.tipo);
        data.writeInt(this.idVoto);
        data.writeInt(this.idUsuario);
        data.writeInt(this.idPR);
        data.writeBoolean(this.voto);

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

        this.tipo          = data.readByte();
        this.idVoto        = data.readInt();
        this.idUsuario     = data.readInt();
        this.idPR          = data.readInt();
        this.voto          = data.readBoolean();
    }
}