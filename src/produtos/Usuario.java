/*
* Registro de usuario para ser usado no CRUD
*/

package produtos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import menu.sistema.abstracts.frontend.RegistroVisual;

public class Usuario implements RegistroVisual {
    private String nome;
    private String email;
    private String senha;
    private int    idUsuario = -1; 

    // Construtor de um livro vazio
    public Usuario() {
        this("", "", "");
    }

    // Construtor de um Livro
    public Usuario(String nome, String email, String senha) {
        this.nome     = nome;
        this.email    = email;
        this.senha    = senha;
    }

    // Retorno da chave secundaria de pesquisa
    public String chaveSecundaria() {
        return this.email;
    }

    // Set ID
    public void setId(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Set nome do nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Set nome do email
    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // Get ID
    public int getId() {
        return this.idUsuario;
    }

    // Get nome do nome
    public String getNome() {
        return this.nome;
    }

    // Get nome do email
    public String getEmail() {
        return this.email;
    }

    // Get senha
    public String getSenha() {
        return this.senha;
    }

    public String imprimir() {
        return null;
    }

    /*  Serializar objeto
    *   Faz uso do ByteArrayOutputStream e do DataOutputStream
    *   para armazenar os dados do usuário dentro de um array
    *   bytes e depois retornar esse array.
    */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream      data      = new DataOutputStream(byteArray);
        
        data.writeInt(this.idUsuario);
        data.writeUTF(this.nome);
        data.writeUTF(this.senha);
        data.writeUTF(this.email);
        
        return byteArray.toByteArray();
    }

    /*  Desserializar objeto
    *   Recebe um array de bytes, que a partir do uso do 
    *   ByteArrayInputStream e o DataInputStream, consegue
    *   receber os dados do array e armazenar no objeto.
    */
    public void fromByteArray(byte[] arrayObjeto) throws IOException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(arrayObjeto);
        DataInputStream      data      = new DataInputStream(byteArray);

        this.idUsuario = data.readInt();
        this.nome      = data.readUTF();
        this.senha     = data.readUTF();
        this.email     = data.readUTF();
    }

    public boolean getAtiva() {
        return false;
    }

}