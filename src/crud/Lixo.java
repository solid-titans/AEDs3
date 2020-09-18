import java.io.RandomAccessFile;

public class Lixo {
    private RandomAccessFile arquivo;  // Arquivo de lixo no disco

    /** Método de exclusão de itens no Crud de forma eficiente
     * 
     */
    public Lixo(String path) {
        
        try {
            this.arquivo = new RandomAccessFile(path, "rw");

        } catch(Exception e) { e.printStackTrace(); }

        // Escrevendo o byte inicial de metadado no arquivo
        if(this.arquivo.length() < 1) 
            this.arquivo.writeLong(-1);
    }

    /** Inserindo um novo elemento no banco de dados
     * 
     */
    public void create(int tamanhoRegistro, long enderecoRegistro) {
        // Criando um objeto para fazer a leitura do lixo
        LerLixo ler = new LerLixo(); 
        ler.escrever(tamanhoRegistro, enderecoRegistro);
        ler = null;

    }

    /** Ler os registros e procurar a melhor opção para substituir o antigo registro
     * 
     * 
     */
    public long read(int tamRegistro) {
        LerLixo ler = new LerLixo();
        long endereco = -1;
        Pagina pagina;

        arquivo.seek(8); // Pular os metadados
        while (arquivo.getFilePointer() < arquivo.length) {
            pagina = new Pagina(arquivo.getFilePointer());

            if(pagina.tamanhoRegistro == tamRegistro) {
                

            }

            // Limpar os itens 
            pagina = null;
        }


        ler = null;
        return endereco;
    }

    /**
     * 
     */
    public void delete() {

    }

    /**
     * 
     */
    private class LerLixo {
        private long enderecoUltimoDeletado;  // Pegar o endereco do ultimo elemento deletado

        private int     tamanhoRegistro;      // Tamanho de um registro deletado
        private long    enderecoRegistro;     // Endereço de um registro deletado
        private boolean haExclusao;           // Verificar se ouve alguma exclusao de algum lixo

        // Ler metadados do lixo
        public LerLixo() {
            arquivo.seek(0);    // Começar a leitura do arquivo

            // Lendo o metadado do último endereço encontrado no lixo
            this.enderecoUltimoDeletado = arquivo.readLong();

            // Verificar o estado 
            if(this.enderecoUltimoDeletado != -1) {
                arquivo.seek(this.enderecoUltimoDeletado);

                // Ler os últimos dados deletados
                this.tamanhoRegistro  = arquivo.readInt();
                this.enderecoRegistro = arquivo.readLong();

                // Marcar como deletado algum registro no meio do arquivo
                this.haExclusao = true;
            
            } else {
                // Desmarcar se houve algum registro deletado no meio do arquivo
                this.haExclusao = false;

            }
        }

        // Verificar se houve alguma exclusao dentro do arquivo
        public boolean haExclusao() {
            return this.haExclusao;
        }

        /** Escrever novos valores no disco
         *  Montar o escrever igual uma pilha sempre guardando o endereço do úlitmo item deletado
         * 
         * @param tamanhoRegistro  Tamanho do registro que foi excluído
         * @param enderecoRegistro Endereço para o registro deletado
         */
        public void escrever(int tamanhoRegistro, long enderecoRegistro) {
            // Ir até a próxima posição livre
            if(this.enderecoUltimoDeletado != -1) {
                // Indo até o registro deletado
                arquivo.seek(this.enderecoUltimoDeletado);

                arquivo.writeInt(tamanhoRegistro);   // Escrevendo o novo registro deletado no disco
                arquivo.writeLong(enderecoRegistro); // Escrevendo o endereço do último registro deletado no disco

                // Reescrevendo 
                arquivo.seek(0);
                arquivo.writeLong(this.enderecoRegistro);
            
            } else {
                // Indo até o final do arquivo para escrever o novo registro excluído
                arquivo.seek(arquivo.length());

                // Escrevendo novos registros no disco
                arquivo.writeInt(tamanhoRegistro);
                arquivo.writeLong(enderecoRegistro);
            }
        }  
    }

    /** Ler um conjunto de elementos no disco
     *  Maior facilidade de procurar itens no disco, sem sobreescreve-lo por acidente
     */
    private class Pagina {
        public int  tamanhoRegistro;      // Pegar o tamanho de um registro
        public long enderecoRegistro;     // Pegar o endereço de um registro

        public Pagina(long enderecoRegistro) {
            arquivo.seek(enderecoRegistro);

            // Ler os registros do disco
            this.tamanhoRegistro  = arquivo.readInt();
            this.enderecoRegistro = arquivo.readLong();
        }
    }
}
