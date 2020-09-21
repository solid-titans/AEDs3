//package crud.lixo;

import java.io.RandomAccessFile;

public class Lixo {
    private RandomAccessFile arquivo;                 // Arquivo de lixo no disco
    private final int        PORCENTAGEMSOBREESCRITA; // Porcentagem de um arquivo para sobreescrita

    /** Método de exclusão de itens no Crud de forma eficiente
     * 
     */
    public Lixo(String path, int porcentagemSobreescrita) throws Exception {
        // Setando o valor da porcetagem para sobreescrita de uma entidade
        this.PORCENTAGEMSOBREESCRITA = porcentagemSobreescrita;

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
    public void create(int tamanhoRegistro, long enderecoRegistro) throws Exception {
        // Criando um objeto para fazer a leitura do lixo
        LerLixo ler = new LerLixo(); 

        // Verificar se o registro não é preexistente no crud
        if(ler.registroExistente(enderecoRegistro)) { //&& ler.haExclusao()) {
            // Sera implementado um método de escrita mais eficiente usando pilhas no futuro

        } else {
            // Indo para o final do arquivo escrever um novo registro
            this.arquivo.seek(this.arquivo.length());
            
            // Escrita no final do arquivo de um novo registro deletado
            this.arquivo.writeInt(tamanhoRegistro);
            this.arquivo.writeLong(enderecoRegistro);
        }

        ler = null;

    }
    
    /** Procurar o endereço do melhor candidato a substituir outro arquivo exluido
     * 
     * @return Endereço do melhor candidato para substituir o registro antigo, -1 caso não há (escreva no final do arquivo)
     */
    public long read(int tamRegistro) {
        Pagina pagina                    = new Pagina();
        long enderecoRegistro            = -1;
        double porcentagem               = -1;
        double porcentagemMelhorRegistro = 0;
        
        // Lendo o arquivo sequencialmente procurando a melhor posição para substituir algum lixo
        try {
            this.arquivo.seek(8);  // Pular o metadado do arquivo
            
            while(this.arquivo.getFilePointer() < this.arquivo.length()) {
                // Ler a página seguinte
                pagina.lerProximaPagina();
                
                // Testar o tamanho do registro apenas se ele for um registro livre para uma nova inserção
                if(pagina.tamanhoRegistro != -1) {
                    // Testar os tamanhos dos registros para encontrar aquele que melhor se adequa a situação
                    porcentagem = 100.0 * tamRegistro / pagina.tamanhoRegistro;
                    
                    if(porcentagem >= this.PORCENTAGEMSOBREESCRITA && porcentagem <= 100) {
                        // Melhor caso, os dois registro possuem o mesmo tamanho
                        if(tamRegistro == pagina.tamanhoRegistro) {
                            // Se for encontrado algum registro de mesmo tamanho do que vai ser substituido, pare a pesquisa usaremos ele
                            enderecoRegistro  = pagina.enderecoRegistro;

                            // Ir para o final do arquivo
                            this.arquivo.seek(this.arquivo.length());
                            
                            // Caso o tamanho não seja igual procuremos o que for o mais proximo possivel
                            // Verificando se o novo candidato é melhor que o anterior para trocar
                        } else if(porcentagemMelhorRegistro < porcentagem) {
                            porcentagemMelhorRegistro = porcentagem;
                            enderecoRegistro = pagina.enderecoRegistro;
                            
                        }
                    }
                } 
                //System.out.println((int)porcentagemMelhorRegistro + "%     " + (int)porcentagem + "%");
            }        
        } catch(Exception e ) { e.printStackTrace(); }
        
        pagina = null;
        
        return enderecoRegistro;
    }
    
    /** Apagar registros do arquivo lixo
     * 
     * 
     */
    public void delete(long enderecoRegistro) {
        try {
            this.arquivo.seek(8);
            Pagina pagina = new Pagina();

            while(this.arquivo.getFilePointer() < this.arquivo.length()) {
                pagina.lerProximaPagina();
                // Deslocar sequencialmente no arquivo até achar o registro que está sendo procurado
                if(pagina.enderecoRegistro == enderecoRegistro) {
                    // Voltando o ponteiro do arquivo para sobreescrever os dados antigos
                    this.arquivo.seek(this.arquivo.getFilePointer() - 12);

                    // Sobreescrevendo os dados do crud de lixo
                    this.arquivo.writeInt(-1);
                    this.arquivo.writeLong(-1);
                }

            }
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    /** Classe para ler rapidamente do disco um conjunto de valores necessários
     * 
     */
    private class LerLixo {
        private long enderecoUltimoDeletado;  // Pegar o endereco do ultimo elemento deletado

        private int     tamanhoRegistro;      // Tamanho de um registro deletado
        private long    enderecoRegistro;     // Endereço de um registro deletado
        private boolean haExclusao;           // Verificar se ouve alguma exclusao de algum lixo

        // Ler metadados do lixo
        public LerLixo() throws Exception {
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

        /**
         * Verificar se um registro já existe no banco de dados
         * 
         * @return false: não existe, true: já existe no disco
         */
        public boolean registroExistente(long enderecoRegistro) {
            boolean haRegistro = false;

            try {
                arquivo.seek(8);
                Pagina pagina = new Pagina();

                while(arquivo.getFilePointer() < arquivo.length()) {
                    pagina.lerProximaPagina();
                    
                    // Verificando se esse registro já existe no banco de dados
                    if(pagina.enderecoRegistro == enderecoRegistro) {
                        haRegistro = true;
                        arquivo.seek(arquivo.length());

                    }
                }

            } catch(Exception e) { e.printStackTrace(); }

            return haRegistro;
        }
    }

    /** Ler um conjunto de elementos no disco
     *  Maior facilidade de procurar itens no disco, sem sobreescreve-lo por acidente
     */
    private class Pagina {
        public int  tamanhoRegistro;      // Pegar o tamanho de um registro
        public long enderecoRegistro;     // Pegar o endereço de um registro

        public Pagina() {
            this.tamanhoRegistro  = 0;
            this.enderecoRegistro = 0;

        }

        public void lerProximaPagina() throws Exception {
            this.tamanhoRegistro  = arquivo.readInt();
            this.enderecoRegistro = arquivo.readLong(); 
            
        }

        public String toString() {
            return "Tamanho do registro: "  + this.tamanhoRegistro + "  |  " +
                   "Endereço do registro: " + this.tamanhoRegistro;

        }
    }
}
