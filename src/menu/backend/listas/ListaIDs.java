package menu.backend.listas;

import java.io.RandomAccessFile;
import menu.backend.listas.abstracts.*;

public class ListaIDs implements ListaIDsInterface {
    private RandomAccessFile indice;      // Banco de dados no disco
    private RandomAccessFile pilhaIds;    // Banco de dados de id das perguntas do usuario
    private LixoListaIDs     lixo;          

    public ListaIDs(String path) {
        try {
            this.indice    = new RandomAccessFile(path + ".lst",   "rw"); 
            this.pilhaIds  = new RandomAccessFile(path + ".lstID", "rw");
            this.lixo      = new LixoListaIDs();
        
        } catch (Exception e) { e.printStackTrace(); }

    }

    /** Metodo create de id de perguntas
     * 
     * @param idUsuario  Id do usuariu que escreveu a pergunta
     * @param idPergunta Id da pergunta que foi escrita pelo usuario
     * @return Se foi inserido com sucesso
     */
    @Override
    public boolean create(int idUsuario, int idPergunta) {
        boolean inserido = false;

        if(idUsuario < 0 || idPergunta < 0) 
            throw new IllegalArgumentException("Erro valor fora do esperado! \n" +
                                               "Idusuario: "  + idUsuario + "\n" +
                                               "IdPergunta: " + idPergunta);
                
        long raizPos = this.inserirIndice(idUsuario);
        
        if(raizPos != -1)
            if(this.inserirPilha(raizPos, idPergunta))
                inserido = true;
                                               
        return inserido;
    }

    private long inserirIndice(int idUsuario) {
        long posInsercaoArv = -1;
        int posUsuario = idUsuario * 12;
        
        try {

            if(posUsuario > this.indice.length()) {
                this.indice.seek(this.indice.length());

                while(posUsuario > this.indice.length()) {
                    this.indice.writeInt(0);
                    this.indice.writeLong(-1);

                } 

                this.indice.writeInt(1);
                posInsercaoArv = this.pilhaIds.length();
                this.indice.writeLong(posInsercaoArv);
            
            } else if (posUsuario == this.indice.length()) {
                this.indice.seek(posUsuario);
                this.indice.writeInt(1);
                posInsercaoArv = this.pilhaIds.length();
                this.indice.writeLong(posInsercaoArv);

            } else {
                this.indice.seek(posUsuario);
                int numElementos = this.indice.readInt();
                long enderecoRaiz = this.indice.readLong();
                this.indice.seek(posUsuario);

                if(numElementos == 0) {
                    this.indice.writeInt(1);
                    posInsercaoArv = this.pilhaIds.length();
                    this.indice.writeLong(posInsercaoArv);
            
                } else {
                    this.indice.writeInt(numElementos + 1);
                    posInsercaoArv = enderecoRaiz;

                }
            }
        } catch(Exception e) { e.printStackTrace(); }

        return posInsercaoArv;
    }

    private boolean inserirPilha(long raizPilha, int idPergunta) {
        boolean sucesso = false;

        try {
            if(raizPilha == this.pilhaIds.length()) {
                this.pilhaIds.seek(raizPilha);

                this.pilhaIds.writeInt(-1);
                this.pilhaIds.writeLong(this.pilhaIds.length() + 8);

                this.pilhaIds.writeInt(idPergunta);
                this.pilhaIds.writeLong(-1);

                sucesso = true;

            } else {
                this.pilhaIds.seek(raizPilha);

                this.pilhaIds.readInt();
                long proxPos = this.pilhaIds.readLong();

                if(proxPos != -1)
                    this.inserirPilha(proxPos, idPergunta);
                
                else {
                    this.pilhaIds.seek(raizPilha + 4);
                    
                    long posLivre = this.lixo.delete();
                    if(posLivre == -1) 
                        posLivre = this.pilhaIds.length();
                    
                    this.pilhaIds.writeLong(posLivre);
                    this.pilhaIds.seek(posLivre);

                    this.pilhaIds.writeInt(idPergunta);
                    this.pilhaIds.writeLong(-1);

                    sucesso = true;
                }
            }

        }catch(Exception e) { e.printStackTrace(); }

        return sucesso;
    }


    /** Retornar todas as ids de perguntas de um usuario
     * 
     * @param idUsuario ID do usuario que se quer todas as perguntas
     * @return Vetor de int contendo todas as ids das perguntas
     *  Possível retorno de erro:
     *  Null -> usuário nunca fez uma pergunta 
     */
    @Override
    public int[] read(int idUsuario) {
        int[] idsPerguntas = null;

        try {
            // Verificando se existe a posicao no arquivo
            if(idUsuario*12 < this.indice.length()) {
                this.indice.seek(idUsuario*12);
                
                int numElementos = this.indice.readInt();
                long posCabeca   = this.indice.readLong();
                
                // Verificar se a posição mesmo que válida, possui algum elemento válido
                if(posCabeca != -1) {
                    this.pilhaIds.seek(posCabeca);
                    this.pilhaIds.readInt();
                    long proxPosicao = this.pilhaIds.readLong();
                    if (proxPosicao != -1) {
                        this.pilhaIds.seek(proxPosicao);
            
                        idsPerguntas = new int[numElementos];
            
                        long proxPos = -1;
                        for(int i = 0; i < numElementos; i++) {
                            idsPerguntas[i] = this.pilhaIds.readInt();
                            
                            proxPos = this.pilhaIds.readLong();
                            if(proxPos != -1)
                                this.pilhaIds.seek(proxPos);
            
                        }
                    }
                }
            }
    
        } catch(Exception e) { e.printStackTrace(); }

        return idsPerguntas;
    }

    /**
     * Remover uma ID da pilha de IDs
     * @param idUsuario id do usuário que possui a pergunra para ser deletada
     * @param idPergunta Id da pergunta para ser deletada
     * @return sucesso no processo de delete
     */
    @Override
    public boolean delete(int idUsuario, int idPergunta) {
        boolean sucesso = false;

        try {
            this.indice.seek(idUsuario*12);

            int numElementos = this.indice.readInt() - 1;
            this.indice.seek(this.indice.getFilePointer() - 4);
            this.indice.writeInt(numElementos);

            this.pilhaIds.seek(this.indice.readLong());
            
            long anterior = this.pilhaIds.getFilePointer();
            this.pilhaIds.readInt();
            this.pilhaIds.seek(this.pilhaIds.readLong());
            long atual = this.pilhaIds.getFilePointer();

            int id = this.pilhaIds.readInt();
            this.pilhaIds.seek(this.pilhaIds.getFilePointer() - 4);

            while(id != idPergunta) {
                id = this.pilhaIds.readInt();
                
                if (id != idPergunta) {
                    anterior = atual;
                    atual = this.pilhaIds.readLong();
                    this.pilhaIds.seek(atual);

                }
            }

            this.pilhaIds.seek(atual + 4);
            long novaPos = this.pilhaIds.readLong();
            this.pilhaIds.seek(anterior + 4);
            this.pilhaIds.writeLong(novaPos);

            this.lixo.create(atual);
            sucesso = true;

        } catch(Exception e) { e.printStackTrace(); }

        return sucesso;
    }
}