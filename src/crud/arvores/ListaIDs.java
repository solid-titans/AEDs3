package crud.arvores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;

public class ListaIDs {
    private RandomAccessFile arquivo;  // Banco de dados no disco
    private RandomAccessFile pilhaIds; // Banco de dados de id das perguntas do usuario

    public ListaIDs(String path) {
        try {
            this.arquivo  = new RandomAccessFile(path + ".lst", "rw");   // Criando o banco de dados no disco
            this.pilhaIds = new RandomAccessFile(path + ".lstID", "rw"); // Criando uma pilha de ids de perguntas

        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Metodo create de id de perguntas
     * 
     * @param idUsuario  Id do usuariu que escreveu a pergunta
     * @param idPergunta Id da pergunta que foi escrita pelo usuario
     * @return Se foi inserido com sucesso
     */
    public boolean create(int idUsuario, int idPergunta) {
        boolean inserido = false;

        try {
            int posInsercao = idUsuario * 8; // Deslocar para a posicao de insercao do primeiro item da pilha

            if(this.arquivo.length() < posInsercao) {
                // Escrever -1 no disco para todo id que ainda nao escreveram nenhuma pergunta no disco
                while(this.arquivo.length() < posInsercao) {
                    this.arquivo.writeLong(-1);  // Escrever um valor vazio para deixar esse espaço vazio para posterior insercao
                    
                }
                
                // Salvando a posicao para a insercao do elemento na pilha
                this.arquivo.writeLong(this.pilhaIds.length());
                // Escrever um bloco de id
                Bloco blocoPilha = new Bloco(idUsuario);
                this.pilhaIds.seek(this.pilhaIds.length()); // Indo para o final do arquivo inserir um item novo
                this.pilhaIds.write(blocoPilha.toByteArray()); //Transformando em um array de bytes para escrever em disco

                inserido = true;
            } else {

                // Há duas opções de encontrar o item
                // Ele pode estar com -1, quer dizer que alguma id apos ela ja foi escrita mas essa id ainda não foi
                // Ter um long, sendo o endereco do primerio elemento da pilha

                this.arquivo.seek(posInsercao);
                long posicaoAtual = this.arquivo.readLong();  // Ler o long dessa posicao
                
                // Verificar se ainda nao existe uma pilha desse usuario
                if(posicaoAtual == -1) {
                    this.arquivo.seek(this.arquivo.getFilePointer() - 8); // Voltar a leitura par aa sobreescrita

                    // Escrever o elemento no final da pilha
                    this.arquivo.writeLong(this.pilhaIds.length());
                    // Indo ate o final do arquivo de pilha
                    this.pilhaIds.seek(this.pilhaIds.length());

                    // Criando o bloco para encaixar na pilha
                    Bloco blocoPilha = new Bloco(idPergunta);

                    // Escrevendo o novo bloco na memoria
                    this.pilhaIds.write(blocoPilha.toByteArray());

                    inserido = true;

                // Deslocar a pilha ate encontrar a posicao de inserir o novo elemento
                } else {
                    // Deslocando a pilha
                    this.pilhaIds.seek(posicaoAtual);  // Deslocando ate a posicao do primeiro bloco da pilha

                    // Deslocar a pilha ate o final
                    Bloco blocoLer = new Bloco();
                    blocoLer.lerBloco();
                    while(blocoLer.posItemPilha != -1){ 
                        // Deslocando rapidamente dentro da pilha
                        this.pilhaIds.seek(blocoLer.posItemPilha);
                        blocoLer.lerBloco();
                    
                    }

                    // Voltar o bloco
                    this.pilhaIds.seek(this.pilhaIds.getFilePointer() - blocoLer.TAMBLOCO + 4); // Voltar o tamanho do bloco mas ignorar o int
                
                    // Escrevendo o long do endereço onde a entidade vai ser escrita
                    this.pilhaIds.writeLong(this.pilhaIds.length());
                    this.pilhaIds.seek(this.pilhaIds.length());

                    // Serializando o objeto
                    Bloco escrever = new Bloco(idPergunta);
                    this.pilhaIds.write(escrever.toByteArray()); // Escrevendo o objeto no disco

                    inserido = true;
                }
            }

        } catch(Exception e) { e.printStackTrace(); }

        return inserido;
    }

    private class Bloco {
        private int  idPergunta;         // Id da pergunta do usuario
        private long posItemPilha;       // Posicao do próximo item da pilha
        private final int TAMBLOCO = 12; // Tamanho do bloco 

        /**
         * Construtor de bloco de pilha vazio
         */
        public Bloco() {
            this.idPergunta   = -1;
            this.posItemPilha = -1;
        }

        /** Blocos da pilha
         * 
         * @param idPergunta   Id da pergunta do usuario
         * @param posItemPilha Proxima pergunta do usuario
         */
        public Bloco(int idPergunta) {
            this.idPergunta   = idPergunta;
            this.posItemPilha = -1;
        
        }

        /**
         * Ler um bloco de dados do disco
         */
        public void lerBloco() {
            try {
                this.idPergunta   = pilhaIds.readInt();
                this.posItemPilha = pilhaIds.readLong();

            } catch(Exception e) { e.printStackTrace(); }
        }

        /**
        * Desserializar objeto
        */
        public byte[] toByteArray() {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            DataOutputStream      data      = new DataOutputStream(byteArray);

            try {
                data.writeInt(this.idPergunta);
                data.writeLong(this.posItemPilha);

            } catch (Exception e) { e.printStackTrace(); }

            return byteArray.toByteArray();
        }

        /**
         * Serializar objeto
         */
        public void fromByteArray(byte[] byteObjeto) {
            ByteArrayInputStream byteArray = new ByteArrayInputStream(byteObjeto);
            DataInputStream      data      = new DataInputStream(byteArray);

            try{
                this.idPergunta   = data.readInt();
                this.posItemPilha = data.readLong();

            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}