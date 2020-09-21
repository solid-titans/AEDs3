package crud;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

import crud.indices.*;
import crud.lixo.*;

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
    private int                    ordemArvoreB  = 5;           // Ordem da Árvore B+
    private HashExtensivel         arquivoIndiceDireto;         // Arquivo de Hash extensivel para salvar uma ID e um ponteiro para uma entidade
    private ArvoreBMais_String_Int arquivoIndiceIndireto;       // Arquivo de Arvore B+ para procurar elementos por String
    private Lixo                   garbagecolector;             // Arquivo que contem diretorio do lixo
    private Constructor<T>         constructor;                 // Construtor do produto
    private int                    porcentagemReciclagem = 65;  // Porcentagem do tamanho do registro para ser ignora e ir para o final do arquivo

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
            this.garbagecolector       = new Lixo(enderecoArquivo + ".lx", this.porcentagemReciclagem);

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

    /* Método de leitura de uma entidade usando chave Secundaria
    *  @return entidade caso a encontre
    *  Caso a chave não seja encontrada uma exceção será gerada
    **/
    public T read(String chaveSecundaria) throws Exception {
        T entidade = null;
        
        // Procurar uma entidade a partir de sua chave secundaria
        entidade = this.read(this.arquivoIndiceIndireto.read(chaveSecundaria));

        return entidade;
    }

    /* Método de leitura de uma entidade usando Hash direto
    *  @return Entidade
    *  Gera exceção caso não encontre a entidade desejada
    **/

    public T read(int id) throws Exception {
        T entidade = null;
        
        // Procurando a ID da entidade no índice direto, nao achar gera uma exceção
        this.arquivo.seek(this.arquivoIndiceDireto.read(id));

        int tamEntidade = this.arquivo.readInt();  // Ler tamanho da entidade
        byte[] registro = new byte[tamEntidade];   // Criar um byte array do registro
        this.arquivo.read(registro);               // Ler a entidade do disco

        entidade = this.constructor.newInstance(); // Criando um objeto na memória para receber a entidade
        entidade.fromByteArray(registro);          // Recebendo os dados da entidade

        return entidade;
    }

    // Atualizar um registro
    public void update(T entidade, int id) {
        // Modelando o objeto em memória
        Entidade novo = new Entidade(entidade);

        try {
            arquivo.seek(0);
            arquivo.readInt();
            // Apagando o objeto antigo da memória
            this.garbagecolector.create(novo.length, this.arquivoIndiceDireto.read(id));

            // Setando a nova ID da entidade
            novo.objeto.setId(id);
            // Procurando a melhor posição de inserção para o novo objeto
            long melhorPos = this.garbagecolector.read(novo.length());
            this.arquivo.seek(melhorPos);
            // Escrever a entidade no disco
            this.arquivo.write(novo.objeto.toByteArray());
    
            // Atualizando os outros indices
            this.arquivoIndiceDireto.update(id, melhorPos);
            this.arquivoIndiceIndireto.update(novo.objeto.chaveSecundaria(), id);

        } catch(Exception e) { e.printStackTrace(); }
    }

    // Apagar um elemento do banco de dados
    public boolean delete(String chaveSecundaria) {
        boolean encontrar = false;

        try {
            encontrar = this.delete(this.arquivoIndiceIndireto.read(chaveSecundaria));

        } catch (Exception e) {
            //System.err.println("Não foi possível encontrar: " + chaveSecundaria + "\nPor conta disso não foi apagado!");
            
        }

        return encontrar;
    }

    // Apagar um elemento do arquivo
    private boolean delete(int id) {
        // Criando uma entidade para receber o byteArray
        T entidade        = null;
        boolean encontrar = false;
        long pos = -1;

        try {
            // Pegar o ponteiro do arquivo no indice
            pos = this.arquivoIndiceDireto.read(id);

        } catch (Exception e) { e.printStackTrace(); }

        // Removendo apenas itens que não existem na memória
        if (pos != -1) {
            try {
    
                Entidade elemento = new Entidade(pos);                     // Criando uma cópia do objeto na memória
                this.arquivo.seek(pos);                                    // Deslocar para o elemento no arquivo
                this.garbagecolector.create(elemento.length(),pos);        // Remover o elemento do disco
    
                // Removendo dos outros bancos de dados
                this.arquivoIndiceDireto.delete(id);
                this.arquivoIndiceIndireto.delete(elemento.objeto.chaveSecundaria());
    
                // Objeto encontrado e removido!
                encontrar = true;
                
            } catch(Exception e) { e.printStackTrace(); }
        }

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
            arquivo.seek(this.garbagecolector.read(dadosEntidade.length));      // Ir para o final do arquivo
            long pos = arquivo.getFilePointer();                                // Pegar a posicao atual do arquivo para inserir no indice direto e no indireto
            arquivo.writeInt(dadosEntidade.length);                             // Escrevendo o tamanho do objeto
            arquivo.write(dadosEntidade);                                       // Escrevendo o objeto na memoria
            
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

    @SuppressWarnings("unused")
    /* Ler um objeto da memória e retornar um objeto
    *  Essa classe é apenas um conjunto de elementos para facilitar o movimentos de elementos do CRUD
    **/
    private class Entidade {
        int length     = 0;     // Tamanho em bytes do objeto 
        T objeto;               // Ponteiro para um objeto

        // Verficar tamamnho do objeto
        public int length() {
            return this.length;
        }

        // Remontar um objeto a partir de um ponteiro
        public Entidade(long ponteiro) {
            try {
                arquivo.seek(ponteiro);
                int tamEntidade = arquivo.readInt();
                byte[] entidade = new byte[tamEntidade];

                objeto = constructor.newInstance();
                objeto.fromByteArray(entidade);

            } catch(Exception e) { e.printStackTrace(); }
        }

        // Encapsular um objeto
        public Entidade(T objeto) {
            try {
                this.length = objeto.toByteArray().length;
                this.objeto = objeto;

            } catch(Exception e) {}
        }
    }
}