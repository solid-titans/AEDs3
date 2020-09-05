package crud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

import crud.indices.*;

public class Crud <T extends Registro> {
    private final String     diretorio    = "Dados";
    private final byte       tamMetadados = 4;
    private RandomAccessFile arquivo;
    private Constructor<T>   constructor;

    // Montar um Crud
    public Crud(String nomeArquivo, Constructor<T> constructor) throws IOException {
        File d                 = new File(this.diretorio);
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
    
    // Metodo de criar novos objetos do Crud
    public int create(T objeto) {
        int id = -1;
        try {
            // Indo no inicio do arquivo pegar o metadado do ID
            arquivo.seek(0);
            id = arquivo.readInt();

        } catch(IOException ioe) { ioe.printStackTrace(); }

        // Retornando a id usada na criacao do objeto
        if(id != -1) this.create(objeto, id);
        return id;
    }

    // Lendo objeto a partir 
    public T read(int id) {
        // Criando uma entidade para receber o byteArray
        T entidade        = null;
        boolean encontrar = false;

        try {
            // Lendo o primeiro registro do programa, apos os metadados
            arquivo.seek(this.tamMetadados);

            // Procurar o objeto com a id especificada no arquivo
            boolean lapide;
            while(arquivo.getFilePointer() < arquivo.length() && !encontrar) {
                lapide = false;
                if(arquivo.readChar() == '*') lapide = true;
                
                int tamRegistro = arquivo.readInt(); // Pegando o tamanho do registro
                if(lapide) {
                    arquivo.seek(arquivo.getFilePointer() + tamRegistro);

                } else {
                    byte[] registro = new byte[tamRegistro];
                    arquivo.read(registro);

                    // Construindo uma entidade a partir do vetor de bytes dela
                    entidade = this.constructor.newInstance();
                    entidade.fromByteArray(registro);

                    // Verificando se a id do objeto eh a pesquisada
                    if(entidade.getId() == id) {
                        // Objeto encontrado
                        encontrar = true;

                    } // Caso nao encontre continue procurando no while
                }
            }
            
            
        } catch(Exception e) { e.printStackTrace(); }

        if(!encontrar) entidade = null;
        return entidade;
    }

    // Atualizar um registro
    public boolean update(T entidade, int id) {
        boolean atualizou = false;

        // Apagando o registro
        this.delete(id);
        // Atualizando o registro
        if(this.create(entidade, id) != -1) atualizou = true;

        return atualizou;
    }

    // Apagar um elemento do arquivo
    public boolean delete(int id) {
        // Criando uma entidade para receber o byteArray
        T entidade        = null;
        long posLapide    = -1;
        boolean encontrar = false;

        try {
            // Lendo o primeiro registro do programa, apos os metadados
            arquivo.seek(this.tamMetadados);

            // Procurar o objeto com a id especificada no arquivo
            boolean lapide;
            while(arquivo.getFilePointer() < arquivo.length() && !encontrar) {
                lapide    = false;
                posLapide = arquivo.getFilePointer();  // Pegando a posicao atual da lapide
                if(arquivo.readChar() == '*') lapide = true;
                
                int tamRegistro = arquivo.readInt(); // Pegando o tamanho do registro
                if(lapide) {
                    arquivo.seek(arquivo.getFilePointer() + tamRegistro);

                } else {
                    byte[] registro = new byte[tamRegistro];
                    arquivo.read(registro);

                    // Construindo uma entidade a partir do vetor de bytes dela
                    entidade = this.constructor.newInstance();
                    entidade.fromByteArray(registro);

                    // Verificando se a id do objeto eh a pesquisada
                    if(entidade.getId() == id) {
                        // Objeto encontrado
                        encontrar = true;
                        arquivo.seek(posLapide);
                        arquivo.writeChar('*');

                    } // Caso nao encontre continue procurando no while
                }
            }
            
            
        } catch(Exception e) { e.printStackTrace(); }

        return encontrar;
    }

    // Criar um novo CRUD
    private int create(T entidade, int id) {
        byte[] dadosEntidade;

        try {            
            // Setando a id do objeto
            entidade.setId(id);
            // Montando um novo objeto
            dadosEntidade = entidade.toByteArray();

            // Indo para o final do arquivo para escrever o novo objeto
            arquivo.seek(arquivo.length());          // Ir para o final do arquivo
            arquivo.writeChar(' ');                  // Escrevendo a lapide no arquivo
            arquivo.writeInt(dadosEntidade.length);  // Escrevendo o tamanho do objeto
            arquivo.write(dadosEntidade);            // Escrevendo o objeto na memoria

            // Sobreescrendo metadado com o novo id
            arquivo.seek(0);
            arquivo.writeInt(id + 1);
        
        } catch (Exception e) { 
            System.err.println(e); 
            id = -1;

        }

        return id;
    }
}