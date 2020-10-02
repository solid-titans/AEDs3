package menu.pergunta.indices;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;

public class ListaIDs {
    private RandomAccessFile arquivo;              // Banco de dados no disco
    private RandomAccessFile pilhaIds;             // Banco de dados de id das perguntas do usuario
    private final int        TAMDADOSARQUIVO = 12; // Tamanho dos objeto inseriodos no arquivo

    public ListaIDs(String path) {
        try {
            this.arquivo  = new RandomAccessFile(path + ".lst",   "rw"); // Criando o banco de dados no disco
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
            int posInsercao = idUsuario * this.TAMDADOSARQUIVO; // Deslocar para a posicao de insercao do primeiro item da pilha

            if(this.arquivo.length() < posInsercao) {
                // Escrever -1 no disco para todo id que ainda nao escreveram nenhuma pergunta no disco
                while(this.arquivo.length() < posInsercao) {
                    this.arquivo.writeInt(0);    // Escrever o numero de perguntas que o usuario possui no banco de dados
                    this.arquivo.writeLong(-1);  // Escrever um valor vazio para deixar esse espaço vazio para posterior insercao
                    
                }
                
                // Salvando a posicao para a insercao do elemento na pilha
                this.arquivo.writeInt(1);  // Usuario escrendo a primeira pergunta
                this.arquivo.writeLong(this.pilhaIds.length());
                // Escrever um bloco de id
                Bloco blocoPilha = new Bloco(idUsuario);
                this.pilhaIds.seek(this.pilhaIds.length()); // Indo para o final do arquivo inserir um item novo
                this.pilhaIds.write(blocoPilha.toByteArray()); //Transformando em um array de bytes para escrever em disco

                inserido = true;

            }else if(this.arquivo.length() == posInsercao) {
                // Preciso aumentar o arquivo usando o novo objeto
                // Basicamente escrever algo novo no final do arquivo
                this.arquivo.seek(posInsercao);

                // Escrever a primeira pergunta do usuário
                this.arquivo.writeInt(1);
                this.arquivo.writeLong(this.pilhaIds.length()); // Escrever a posição de inserção do novo objeto
            
                // Deslocar na pilha até a nova posição
                this.pilhaIds.seek(posInsercao);
                this.pilhaIds.write(new Bloco(idPergunta).toByteArray()); // Escrever o bloco com a nova pergunta do usuário no disco

            } else {

                // Há duas opções de encontrar o item
                // Ele pode estar com -1, quer dizer que alguma id apos ela ja foi escrita mas essa id ainda não foi
                // Ter um long, sendo o endereco do primerio elemento da pilha

                // Ir ate a posicao de insercao da nova pergunta
                this.arquivo.seek(posInsercao);
                
                // Somar no contador de perguntas a nova pergunta feita pelo usuario
                int numPerguntas  = this.arquivo.readInt();  // Ler o numero de perguntas feitas pelo usuario
                this.arquivo.seek(this.arquivo.getFilePointer() - 4);
                this.arquivo.writeInt(numPerguntas + 1);

                // Descobrir a posicao de inicio da pilha para posterior inserção
                long posicaoAtual = this.arquivo.readLong(); // Ler o long dessa posicao
                
                // Algum usuário com id maior que esse escreveu no banco de dados antes
                if(posicaoAtual == -1) {
                    this.arquivo.seek(this.arquivo.getFilePointer() - 8); // voltar o ponteiro do arquivo para sobreescreve-lo com o valor correto
                    this.arquivo.writeLong(this.pilhaIds.length());       // Escrever o endereço de onde será escrito o novo item da pilha

                    this.pilhaIds.seek(this.pilhaIds.length()); // Ir até o final do arquivo para escrever o novo bloco

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
                    this.pilhaIds.seek(this.pilhaIds.getFilePointer() - blocoLer.TAMBLOCO + 4); // Voltar o tamanho do bloco mas ignorar o id do elemento
                    
                    // Escrevendo o long do endereço onde a entidade vai ser escrita
                    this.pilhaIds.writeLong(this.pilhaIds.length());
                    this.pilhaIds.seek(this.pilhaIds.length());
                }

                // Serializando o objeto e escreve-lo na posição correta
                this.pilhaIds.write(new Bloco(idPergunta).toByteArray()); // Escrevendo o objeto no disco

                inserido = true;
        
            }
        } catch(Exception e) { e.printStackTrace(); }

        return inserido;
    }

    
 /** Retornar todas as ids de perguntas de um usuario
     * 
     * @param idUsuario ID do usuario que se quer todas as perguntas
     * @return Vetor de int contendo todas as ids das perguntas
     *  Possível retorno de erro:
     *  Null -> usuário nunca fez uma pergunta 
     */
    public int[] read(int idUsuario)
    {
        int[] resposta = null;
        try
        {
            if(this.arquivo.length() != 0)
                resposta = this.readP(idUsuario);  
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return resposta;
    }

    private int[] readP(int idUsuario) {
        int[] idsPerguntas = null;
        
        try {
            int posPerguntasUsuario = idUsuario * this.TAMDADOSARQUIVO;  // Descobrir a posição das perguntas do usuário

            if(this.arquivo.length() < posPerguntasUsuario);  // Prevenir erro de deslocamento para uma região do arquivo que não existe
            
            // Se a região do arquivo existe deslocar até ela
            else {
                // Ir até a posição no arquivo
                this.arquivo.seek(posPerguntasUsuario);
                int numPerguntas = this.arquivo.readInt();  // Ler o numero de perguntas feitas pelo usuario
                
                if(numPerguntas > 0) {
                    // O usuario possui alguma pergunta

                    idsPerguntas = new int[numPerguntas]; // Criando um vetor com o tamanho igual ao numero de perguntas que ele tem no banco de dados

                    // Pegar a posição da pilha onde começa as perguntas desse usuario
                    long posPergunta = this.arquivo.readLong();
                    this.pilhaIds.seek(posPergunta); // Deslocar até a pergunta inicial do usuário

                    // Começar a retornar os valores do banco de dados
                    Bloco bloco = new Bloco();
                    bloco.lerBloco();

                    // Ler todos os itens da pilha até desempilhar por completo
                    int perguntasLidas = 0;
                    while(bloco.posItemPilha != -1) {
                        // Lendo as IDs
                        idsPerguntas[perguntasLidas] = bloco.idPergunta; // Adicionar no vetor o novo ID de pergunta lido
                        perguntasLidas++;

                        // Deslocar para a próxima pergunta do usuário
                        this.pilhaIds.seek(bloco.posItemPilha);

                        // Ler o próximo bloco
                        bloco.lerBloco();
                    }

                    // Adicionar a ultima pergunta que não entrou no while
                    idsPerguntas[perguntasLidas] = bloco.idPergunta;
                }
            }
        } catch(Exception e) { e.printStackTrace(); }

        return idsPerguntas;
    }

    /**
     * Classe privada para leitura rápida de um bloco de elementos do disco
     */
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
    }
}