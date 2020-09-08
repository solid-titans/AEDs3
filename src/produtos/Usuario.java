package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import crud.Registro;

public class Usuario implements Registro {
    private String username;
    private String email;
    private int    id = -1; 

    // Construtor de um livro vazio
    public Usuario() {
        this("", "");
    }

    // Construtor de um Livro
    public Usuario(String username, String email) {
        this.username = username;
        this.email    = email;
    }

    // Retorno da chave secundaria de pesquisa
    public String chaveSecundaria() {
        return this.username;
    }

    // Set ID
    public void setId(int id) {
        this.id = id;
    }

    // Set nome do username
    public void setUsername(String username) {
        this.username = username;
    }

    // Set nome do email
    public void setEmail(String email) {
        this.email = email;
    }

    // Get ID
    public int getId() {
        return this.id;
    }

    // Get nome do username
    public String getUsername() {
        return this.username;
    }

    // Get nome do email
    public String getEmail() {
        return this.email;
    }

    // Serializar objeto
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream      data      = new DataOutputStream(byteArray);
        
        data.writeInt(this.id);
        data.writeUTF(this.username);
        data.writeUTF(this.email);
        
        return byteArray.toByteArray();
    }

    // Desserializar objeto
    public void fromByteArray(byte[] arrayObjeto) throws IOException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(arrayObjeto);
        DataInputStream      data      = new DataInputStream(byteArray);

        this.id        = data.readInt();
        this.username  = data.readUTF();
        this.email     = data.readUTF();
    }

    public String toString() {
        return "ID: "                + this.id        + "\n" +
                "Nome de usuário: "  + this.username  + "\n" +
                "Email: "            + this.email     + "\n";
    }
}