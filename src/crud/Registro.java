package crud;

import java.io.IOException;

public interface Registro {
    // Adicionar uma ID em um objeto
    public void setId(int id);

    // Pegar a ID de um objeto
    public int getId();

    // Transformar um objeto   em um vetor de bytes
    public byte[] toByteArray() throws IOException;
    
    // Transformar o vetor de bytes de volta no objeto
    public void fromByteArray(byte[] byteArray) throws IOException;
}