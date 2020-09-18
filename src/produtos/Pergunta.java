

package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import crud.Registro;

public class Pergunta implements Registro {

    private int idPergunta;
    private int idUsuario;
    private long criacao;
    private short nota;
    private String pergunta;
    private boolean ativa;

    //Construtor de uma pergunta vazia
    public Pergunta(){
        this(-1,-1,-1,-1,"",false);
    }

    //Construtor de uma pergunta
    public Pergunta(int idPergunta, int idUsuario, long criacao, short nota, String pergunta, boolean ativa) {
        this.idPergunta = idPergunta;
        this.idUsuario  = idUsuario;
        this.criacao    = criacao;
        this.nota       = nota;
        this.pergunta   = pergunta;
        this.ativa      = ativa;
    }

    //Funções 'set'
    public void setId(int idPergunta) {
        this.idPergunta = idPergunta;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setCriacao(long criacao) {
        this.criacao = criacao;
    }

    public void setNota(short nota) {
        this.nota = nota;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    //Funções 'get'
    public String chaveSecundaria() {
        return null;
    }

    public int getId() {
        return this.idPergunta;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public long getCriacao() {
        return this.criacao;
    }

    public short getNota() {
        return this.nota;
    }

    public String getPergunta() {
        return this.pergunta;
    }

    public boolean getAtiva() {
        return this.ativa;
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
        
        data.writeInt(this.idPergunta);
        data.writeInt(this.idUsuario);
        data.writeLong(this.criacao);
        data.writeShort((int)this.nota); //O unico jeito de escrever short é usando int
        data.writeUTF(this.pergunta);
        data.writeBoolean(this.ativa);
        
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

        this.idPergunta = data.readInt();
        this.idUsuario  = data.readInt();
        this.criacao    = data.readLong();
        this.nota       = data.readShort();
        this.pergunta   = data.readUTF();
        this.ativa      = data.readBoolean();
    }
    
}