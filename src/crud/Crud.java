package crud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

import crud.indices.*;

/*
* Contrutor de um banco de dados no disco
* @create  -> Criar um novo produto no disco
* @read    -> Ler os dados do disco
* @update  -> Atualizar os dados do disco
* @delete  -> Apagar dados do disco
**/
public class Crud <T extends Registro> {
    private final String           diretorio     = "Dados";     // Diretorio dos arquivos de dados
    private final byte             tamMetadados  = 4;           // Tamanho dos metadados dos arquivos
    private RandomAccessFile       arquivo;                     // Ponteiro para o arquivo dos produtos
    private int                    numDadosCesto = 100;         // Tamanho máximo do número de elementos do cesto
    private int                    ordemArvoreB  = 5;           // Ordem da Árvore b+
    private HashExtensivel         arquivoIndiceDireto;         // Arquivo de Hash extensivel para salvar uma ID e um ponteiro para uma entidade
    private ArvoreBMais_String_Int arquivoIndiceIndireto;       // Arquivo de Arvore B+ para procurar elementos por String
    private Constructor<T>         constructor;                 // Construtor do produto

    // Montar um Crud
    public Crud(String nomeArquivo, Constructor<T> constructor) throws IOException {
        File d                 = new File(this.diretorio);
        String enderecoArquivo = this.diretorio + "/" + nomeArquivo;
        
        // Atribuindo o construtor
        this.constructor = constructor;

        // Verificar se o directorio de dados existe, caso nao exista crie-o
        if(!d.exists()) d.mkdir();
        
        // Abrindo arquivo para leitura
        try {
            this.arquivo               = new RandomAccessFile(enderecoArquivo + ".db", "rw"); 
            this.arquivoIndiceDireto   = new HashExtensivel(this.numDadosCesto, enderecoArquivo + ".hx", enderecoArquivo + ".chx");
            this.arquivoIndiceIndireto = new ArvoreBMais_String_Int(this.ordemArvoreB, enderecoArquivo + ".bplus");

        } catch (Exception e) {
            System.err.println("Impossível abrir o conjunto de arquivos " + enderecoArquivo + "\nTipo de erro: " + e);

        }

        // Escrevendo os metadados nos arquivos
        try {
            // Escrevendo o metadado ID 0, caso o arquivo nao exista
            if(this.arquivo.length() == 0) 
                this.arquivo.writeInt(0);

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

    /* Lendo objeto dentro do banco de dados de forma sequencial
    *
    *
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
    */

    // Método de leitura de uma entidade usando Hash direto
    public T read(int id) {
        T entidade = null;

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
            arquivo.seek(arquivo.length());            // Ir para o final do arquivo
            long pos = arquivo.getFilePointer();       // Pegar a posicao atual do arquivo para inserir no indice direto e no indireto
            arquivo.writeChar(' ');                    // Escrevendo a lapide no arquivo
            arquivo.writeInt(dadosEntidade.length);    // Escrevendo o tamanho do objeto
            arquivo.write(dadosEntidade);              // Escrevendo o objeto na memoria
            
            // Inserindo nos outros arquivos de banco de dados
            this.arquivoIndiceDireto.create(id, pos);  // Inserindo a id e o pos no hash extensivel
            this.arquivoIndiceIndireto.create(entidade.chaveSecundaria(), id); // Inserindo a chave de pesquisa na arvore B+

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