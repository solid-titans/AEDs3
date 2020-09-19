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

        this.arquivo.readChar();                   // Pulando a lápide (Se a entidade tiver sido removida criara uma exceção)
        int tamEntidade = this.arquivo.readInt();  // Ler tamanho da entidade
        byte[] registro = new byte[tamEntidade];   // Criar um byte array do registro
        this.arquivo.read(registro);               // Ler a entidade do disco

        entidade = this.constructor.newInstance(); // Criando um objeto na memória para receber a entidade
        entidade.fromByteArray(registro);          // Recebendo os dados da entidade

        return entidade;
    }

    // Atualizar um registro
    public boolean update(T entidade, int id) {
        boolean atualizou = false;  // Verificar se foi possivel atualizar um elemento
        Entidade antigo   = null;   // Montar um objeto que estava no disco na memoria
        Entidade novo     = null;   // Montar um novo objeto para atualizar o antigo

        try {
            antigo = new Entidade(this.arquivoIndiceDireto.read(id));  // Pegando do disco um elemento
            novo   = new Entidade(entidade);                           // Montar um ponteiro para o novo objeto

        } catch(Exception e) { e.printStackTrace(); }

        // Verificar tamamnho do registro para substituir antigo pelo novo na mesma posição
        boolean substituir = antigo.length != 0 && novo.length / antigo.length * 100 >= this.porcentagemReciclagem && novo.length / antigo.length * 100 <= 100;
        if(substituir) {  
            try {
                arquivo.seek(this.arquivoIndiceDireto.read(id));  // Ir ate a posição do elemento antigo
                arquivo.writeChar(' ');                           // Sobreescrever a lápide vazia 
                arquivo.writeInt(antigo.length);                  // Escrevendo tamanho do registro antigo
                arquivo.write(novo.objeto.toByteArray());         // Escrevendo o registro novo

                // Atualizar a chave secundária no índice Indireto
                // Não há necessidade de modificar no índice direto, pois não foi modificado a id do objeto
                this.arquivoIndiceIndireto.update(novo.objeto.chaveSecundaria(), id);

                atualizou = true;  // Registro substituido com sucesso

            } catch (Exception e) { e.printStackTrace(); }
        
        } else {
            // Apagando o registro
            this.delete(id);
            
            try {
                // Modificando o endereço no índice direto
                this.arquivoIndiceDireto.update(id, this.arquivo.length());

            } catch (Exception e) { e.printStackTrace(); }

            // Atualizando o registro
            if(this.create(entidade, id) != -1) atualizou = true; // Escrevendo o registro novo no final do arquivo

        }


        return atualizou;
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
    
                Entidade elemento = new Entidade(pos); // Criando uma cópia do objeto na memória
                this.arquivo.seek(pos);                // Deslocar para o elemento no arquivo
                this.arquivo.writeChar('*');           // Remover o elemento do disco
    
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

    @SuppressWarnings("unused")
    /* Ler um objeto da memória e retornar um objeto
    *  Essa classe é apenas um conjunto de elementos para facilitar o movimentos de elementos do CRUD
    **/
    private class Entidade {
        boolean lapide = false; // Verificar se o objeto tem lápide
        int length     = 0;     // Tamanho em bytes do objeto 
        T objeto;               // Ponteiro para um objeto

        // Verificar se ha lapide
        public boolean getLapide() {
            return this.lapide;
        }

        // Verficar tamamnho do objeto
        public int length() {
            return this.length;
        }

        // Remontar um objeto a partir de um ponteiro
        public Entidade(long ponteiro) {
            try {
                arquivo.seek(ponteiro);
                if(arquivo.readChar() == '*') this.lapide = true;
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