import java.io.*;

class Crud {
    private RandomAccessFile arquivo;
    private int ultimoElementoCrud;

    public Crud() {
        try {
            RandomAccessFile arq = new RandomAccessFile("data.db", "rw");
            this.arquivo = arq;
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    // Guardando o objeto numa váriavel
    public int create(Livro obj) {
        int id = this.ultimoElementoCrud + 1;
        
        //Elemento<Livro> elemento = new Elemento<Livro>(Livro.class.getConstructor());
        //this.arquivo.writeInt(elemento.getId());
        //this.arquivo.write(elemento.toByteArray());

        return id;
    }

    
    
}

// Elementos para armazenar no Crud
class Elemento<T> implements Registro {
    private byte lapide;
    private int id;
    private T objeto;

    // Set da ID do objeto
    public void setId(int id) {
        this.id = id;
    }

    // Set do objeto
    public void setObjeto(T obj) {
        this.objeto = obj;
    }

    // Set da Lapide
    public void setLapide(byte lap) {
        this.lapide = lap;
    }

    // Get da ID do objeto
    public int getId() {
        return this.id;
    }

    // Get da ID do objeto
    public T getObjeto() {
        return this.objeto;
    }

    // Get da ID do objeto
    public byte getLapide() {
        return this.lapide;
    }

    // Transformar um objeto em um array de bytes
    public byte[] toByteArray() {
        return this.objeto.toByteArray();
    }

    // Retornar um array de bytes para um objeto
    public void fromByteArray(byte[] byteArray) {
        this.objeto = byteArray;
    }
}

// Elementos que o Crud precisa necessariamente ter
interface Registro {
    public int getId();
    public byte getLapide();
    public void setId(int id);
    public void setLapide(byte lapide);
    public byte[] toByteArray();
    public void fromByteArray(byte[] byteArray);
}