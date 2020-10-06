package crud.lixo;

import java.io.RandomAccessFile;

/** Sistema de lixo eficiente
 * @author Lucas Santiago
 * @funcao create -> Recebe um int com o tamanho do registro deletado e a posição de exclusão dele
 * @funcao Read   -> Recebe um int com o tamanho de uma nova entidade para sobreescrever um registro
 * @funcao Delete -> Deletar um lixo que tiver sido usado
 */
public class Lixo {
    private RandomAccessFile arquivo;                 // Arquivo de lixo no disco
    private final int        PORCENTAGEMSOBREESCRITA; // Porcentagem de um arquivo para sobreescrita
    private final long       tamMetadados = 8;        // Tamanho dos metadados

    /** Método de exclusão de itens no Crud de forma eficiente
     * 
     */
    public Lixo(String path, int porcentagemSobreescrita)  {
        // Setando o valor da porcetagem para sobreescrita de uma entidade
        this.PORCENTAGEMSOBREESCRITA = porcentagemSobreescrita;
        
        try {
            // Criando o banco de dados no disco
            this.arquivo = new RandomAccessFile(path, "rw");
    
            // Escrevendo o byte inicial de metadado no arquivo
            if(this.arquivo.length() < 1) 
                this.arquivo.writeLong(-1);

        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Inserindo um novo elemento no banco de dados
     * @param tamanhoRegistro Tamanho do registro que foi excluido.
     * @param enderecoRegistro long contendo o endereço do registro deletado.
     * 
     * @return boolean Se houve escrita no disco
     */
    public boolean create(int tamanhoRegistro, long enderecoRegistro) {
        boolean inseriu = false; 

        try {
            // Criando um objeto para fazer a leitura do lixo
            LerLixo ler     = new LerLixo(); 
    
            // Se o registro já existir no banco ignorar inserção
            if(!ler.registroExistente(enderecoRegistro)) {
    
                // Ir no inicio do arquivo pegar a posicao de insercao do registro
                this.arquivo.seek(0);
                long melhorPos = this.arquivo.readLong();
    
                if(melhorPos == -1) {
                    // Indo ate o final do arquivo escrevero registro
                    this.arquivo.seek(this.arquivo.length());
                    this.arquivo.writeInt(tamanhoRegistro);
                    this.arquivo.writeLong(enderecoRegistro);
    
                } else {
                    // Indo na posicao de insercao do novo item
                    this.arquivo.seek(melhorPos + 4);  // Pular o int -1, que representa que o item do lixo foi excluido
                    long itemAnteriorPilha = this.arquivo.readLong();
    
                    // Voltando a leitura para sobreescrever os dados
                    this.arquivo.seek(this.arquivo.getFilePointer() - 12); // Voltar a leitura do elemento int + long
    
                    // Escrever o registro novo
                    this.arquivo.writeInt(tamanhoRegistro);
                    this.arquivo.writeLong(enderecoRegistro);
    
                    // Ir no inicio do arquivo e atualizar a pilha de elementos excluidos
                    this.arquivo.seek(0);
                    this.arquivo.writeLong(itemAnteriorPilha);
                }
    
                inseriu = true;
            }
    
            ler = null;

        } catch(Exception e) { e.printStackTrace(); }

        return inseriu;
    }
    
    /** Procurar o endereço do melhor candidato a substituir outro arquivo exluido
     * 
     * @param tamRegistro Ler no banco de dados o melhor candidato para sobreescrever esse lixo
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
            this.arquivo.seek(this.tamMetadados);  // Pular o metadado do arquivo
            
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
     * @param endereçoRegistro Excluir um lixo do banco de dados, usar apos dar um read no lixo e escrever outro objeto nessa posição
     */
    public boolean delete(long enderecoRegistro) {
        boolean achou = false;
        
        try {
            this.arquivo.seek(0);
            long valorMetadado = this.arquivo.readLong();  // Recebendo o ultimo valor do metadado que foi deletado
            Pagina pagina = new Pagina();

            while(this.arquivo.getFilePointer() < this.arquivo.length() && !achou) {
                pagina.lerProximaPagina();
                // Deslocar sequencialmente no arquivo até achar o registro que está sendo procurado
                if(pagina.enderecoRegistro == enderecoRegistro) {
                    // Voltando o ponteiro do arquivo para sobreescrever os dados antigos
                    this.arquivo.seek(this.arquivo.getFilePointer() - 12);  // Voltando o tamanho de um int + um long
                    System.err.println(this.arquivo.getFilePointer());

                    // Marcando como excluido o elemento
                    this.arquivo.writeInt(-1);

                    // Receber a posição atual para ser inserida no inicio do arquivo
                    long metadadoAtualizado = this.arquivo.getFilePointer() - 4;  // Ignorar o int que acabou de ser escrito
                    // Escrevendo o valor do metadado no long da posição do item deletado anteriormente
                    this.arquivo.writeLong(valorMetadado);                                               

                    // Voltando ao inicio do arquivo para escrever o novo valor do metadado
                    this.arquivo.seek(0);
                    this.arquivo.writeLong(metadadoAtualizado);

                    // Marcando a flag como encontrado
                    achou = true;
                }

            }
        } catch(Exception e) { e.printStackTrace(); }

        return achou;
    }
    
    /** Classe para ler rapidamente do disco um conjunto de valores necessários
     * 
     */
    private class LerLixo {
        /**
         * Verificar se um registro já existe no banco de dados
         * 
         * @return false: não existe, true: já existe no disco
         */
        public boolean registroExistente(long enderecoRegistro) {
            boolean haRegistro = false;

            try {
                arquivo.seek(tamMetadados);
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
            if (arquivo.getFilePointer() < arquivo.length()) {
                this.tamanhoRegistro  = arquivo.readInt();
                this.enderecoRegistro = arquivo.readLong(); 

            }
        }

        public String toString() {
            return "Tamanho do registro: "  + this.tamanhoRegistro + "  |  " +
                   "Endereço do registro: " + this.tamanhoRegistro;

        }
    }
}
