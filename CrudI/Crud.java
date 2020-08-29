import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Crud <T extends Registro> {
    private final String     diretorio = "Dados";
    private RandomAccessFile arquivo;
    private Constructor<T>   constructor;

    // Montar um Crud
    public Crud(String nomeArquivo, Constructor<T> constructor) throws IOException {
        File d           = new File(this.diretorio);
        String enderecoArquivo = this.diretorio + "/" + nomeArquivo + ".db";
        
        // Atribuindo o construtor
        this.constructor = constructor;

        // Verificar se o directorio de dados existe, caso nao exista crie-o
        if(!d.exists()) d.mkdir();
        
        // Abrindo arquivo para leitura
        try {
            arquivo = new RandomAccessFile(enderecoArquivo, "rw"); 
        
        } catch (FileNotFoundException e) {
            System.err.println("Impossível abrir o arquivo " + enderecoArquivo + "\nTipo de erro: " + e);

        }

        // Escrevendo o metadado ID 0, caso o arquivo nao exista
        try {
            if(arquivo.length() == 0) 
                arquivo.writeInt(0);

        } catch(Exception e) { System.err.println(e); }
    }
    
    // Criar um novo CRUD
    public int create(T entidade) {
        int id       = -1;
        long pos     = -1;
        byte[] dadosEntidade;

        try {
            // Montando um novo objeto
            dadosEntidade = entidade.toByteArray();

            // Pegando o tamanho do arquivo e indo para o inicio dele
            pos = arquivo.length();
            arquivo.seek(0);

            // Pegando o metadado ID do arquivo e setando no proximo objeto
            id = arquivo.readInt();
            entidade.setId(id);

            // Indo para o final do arquivo para escrever o novo objeto
            arquivo.seek(pos);
            arquivo.writeChar(' ');                 // Escrevendo a lapide no arquivo
            arquivo.writeInt(dadosEntidade.length); // Escrevendo o tamanho do objeto
            arquivo.write(dadosEntidade);           // Escrevendo o objeto na memoria

            // Sobreescrendo metadado com o novo id
            arquivo.seek(0);
            arquivo.writeInt(id + 1);
        
        } catch (Exception e) { 
            System.err.println(e); 
            id = -1;

        }

        return id;
    }

    // Lendo objeto a partir 
    /*public T read(int id) {
        // Criando uma entidade para receber o byteArray
        T entidade = null;

        try {


        }

        return null;
    }

    // Atualizar um registro
    public boolean update(T entidade) {

        return false;
    }

    // Apagar um elemento do arquivo
    public boolean delete(int id) {

        return false;
    }*/
}