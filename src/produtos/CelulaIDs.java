package produtos;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import crud.Registro;

public class CelulaIDs implements Registro{
    
    private int    idUsuario;
    private String idsPergunta;

    public CelulaIDs() {
        this(-1,"");
    }
    public CelulaIDs(int idUsuario,String idsPergunta) {
        this.idUsuario   = idUsuario;
        this.idsPergunta = idsPergunta;
    }

	public void setId(int id) {
		this.idUsuario  = id;
	}

	public int getId() {
		return this.idUsuario;
    }
    
	public void setChaveSecundaria(String idsPergunta) {
		this.idsPergunta = idsPergunta;
    }

	public String chaveSecundaria() {
		return this.idsPergunta;
    }
    


	public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream      data      = new DataOutputStream(byteArray);

        data.writeUTF(this.idsPergunta);
        data.writeInt(this.idUsuario);

        return byteArray.toByteArray();
	}

	public void fromByteArray(byte[] arrayObjeto) throws IOException {

        ByteArrayInputStream byteArray = new ByteArrayInputStream(arrayObjeto);
        DataInputStream      data      = new DataInputStream(byteArray);

        this.idsPergunta = data.readUTF();
        this.idUsuario   = data.readInt();
		
	}
}
