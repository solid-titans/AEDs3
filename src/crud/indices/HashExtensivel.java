package crud.indices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/******************************************************************************
 * A {@code HashExtensivel}
 * <p>
 * {@code boolean create(int chave, long dado)}
 * <p/>
 * <p>
 * {@code long read(int chave)}
 * <p/>
 * <p>
 * {@code boolean update(int chave, long dado)}
 * <p/>
 * <p>
 * {@code boolean delete(int chave)}
 * <p/>
 * 
 * 
 * @category Indice Direto
 ******************************************************************************/

public class HashExtensivel {

    private String nomeArquivoDiretorio;
    private String nomeArquivoCestos;
    private RandomAccessFile arqDiretorio;
    private RandomAccessFile arqCestos;
    private int quantidadeDadosPorCesto;
    private Diretorio diretório;

    public HashExtensivel(int quantidadeDadosPorCesto, String nomeArquivoDiretorio, String nomeArquivoCestos)
            throws Exception {
        // Atruição
        this.quantidadeDadosPorCesto = quantidadeDadosPorCesto;
        this.nomeArquivoDiretorio = nomeArquivoDiretorio;
        this.nomeArquivoCestos = nomeArquivoCestos;

        this.arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
        this.arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

        // Se o diretório ou os cestos estiverem vazios, cria um novo diretório e lista
        // de cestos
        if (arqDiretorio.length() == 0 || arqCestos.length() == 0) {

            // Cria um novo diretório, com profundidade de 0 bits (1 único elemento)
            diretório = new Diretorio();
            byte[] byteDiretorio = diretório.toByteArray();
            arqDiretorio.write(byteDiretorio);

            // Cria um cesto vazio, já apontado pelo único elemento do diretório
            Cesto cesto = new Cesto(quantidadeDadosPorCesto);
            byteDiretorio = cesto.toByteArray();
            arqCestos.seek(0);
            arqCestos.write(byteDiretorio);
        }

    }

    /******************************************************************************
     * {@code Cesto}
     * 
     * @version 0.0.10
     * @category HashExtensive
     ******************************************************************************/
    class Cesto {

        private byte profundidadeLocal; // profundidade local do cesto
        private short quantidade; // quantidade de pares presentes no cesto
        private short quantidadeMaxima; // quantidade máxima de pares que o cesto pode conter
        private int[] chaves; // sequência de chaves armazenadas no cesto
        private long[] dados; // sequência de dados correspondentes às chaves
        private short bytesPorPar; // size fixo de cada par de chave/dado em bytes
        private short bytesPorCesto; // size fixo do cesto em bytes

        public Cesto(int quantidadeMaxima) throws Exception {
            this(quantidadeMaxima, 0);
        }

        public Cesto(int quantidadeMaxima, int profundidadeLocal) throws Exception {

            validarQuantidadeMaxima(quantidadeMaxima);
            validarprofundidadeLocal(profundidadeLocal);

            this.profundidadeLocal = (byte) profundidadeLocal;
            this.quantidade = 0; // O cesto iniciado com nenhum entidade computada
            this.quantidadeMaxima = (short) quantidadeMaxima;
            this.chaves = new int[quantidadeMaxima];
            this.dados = new long[quantidadeMaxima];
            this.bytesPorPar = 12; // int + long (4 +8) respetivamente
            this.bytesPorCesto = (short) (bytesPorPar * quantidadeMaxima + 3);
        }

        /**
         * toByteArray Converte a profundidade local, quantidade atual de
         * elemento(short), as chaves e os endereços que estão relacionado com as mesmas
         * em um vetor de bytes, caso depois dessas informações o tamanho total de
         * elementos não tenham sido preenchido vai ser preenchido com zero até que o
         * valor total de elementos seja preenchido armazenamento em um vetor de lor
         * total de elementos seja preenchido armazenamento em um vetor de
         * 
         * @return
         * @throws IOException
         */
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeLocal);
            dos.writeShort(quantidade);
            int i = 0;
            while (i < quantidade) {
                dos.writeInt(chaves[i]);
                dos.writeLong(dados[i]);
                i++;
            }
            while (i < quantidadeMaxima) {
                dos.writeInt(0);
                dos.writeLong(0);
                i++;
            }
            return baos.toByteArray();
        }

        /**
         * converte o vetor de bytes e seta as seguintes varias profundidadeLocal (byte)
         * quantidade (Short) e o restante até a quantidadeMaxima já definida vai ficar
         * armazendado como chave e endereços de memorias
         * 
         * @param byteArray recebe um vetor de bytes para ser convertido novamente para
         *                  o objeto
         * 
         * @throws IOException
         */
        public void fromByteArray(byte[] byteArray) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeLocal = dis.readByte();
            quantidade = dis.readShort();
            int i = 0;
            while (i < quantidadeMaxima) {
                chaves[i] = dis.readInt();
                dados[i] = dis.readLong();
                i++;
            }
        }

        /**
         * create
         * 
         * @param chave chave que vai ser adicionada
         * @param dado  endereço que vai ser veiculado
         * @return retorna um true caso a operação seja bem sucedida
         * 
         */
        public boolean create(int chave, long dado) {
            boolean resposta = false;
            int i = quantidade - 1;
            if (full() == false) {

                while (i >= 0 && chave < chaves[i]) {
                    chaves[i + 1] = chaves[i];
                    dados[i + 1] = dados[i];
                    i--;
                }
                i++;
                chaves[i] = chave;
                dados[i] = dado;
                quantidade++;
                resposta = true;
            }
            return resposta;
        }

        /**
         * read recupera um dado do arquivo caso o mesmo exista
         * 
         * @param chave valor inteiro que vai ser buscado dentro do arquivo
         * @return caso a chave seja encontrada vai ser retornado um long(endereço),
         *         onde está armazenado o objeto, caso não encontre nada ou o arquivos
         *         esteja vazio retorna -1
         */

        public long read(int chave) {
            long resp = -1;
            int i = 0;
            if (empty() == false) {

                while (i < quantidade && chave > chaves[i]) {
                    i++;
                }
                if (i < quantidade && chave == chaves[i]) {
                    resp = dados[i];
                }
            }
            return resp;
        }

        /**
         * Update
         * 
         * @param chave chave que vai receber um novo endereço
         * @param dado  long - endereço que vai substituir
         * @return
         */
        public boolean update(int chave, long dado) {
            boolean resposta = false;
            int i = 0;

            if (empty() == false) {
                while (i < quantidade && chave > chaves[i])
                    i++;
                if (i < quantidade && chave == chaves[i]) {
                    dados[i] = dado;
                    resposta = true;
                }

            }
            return resposta;
        }

        /**
         * delete Funcao vai receber um chave (int) e vai tentar localizar, caso
         * encontre remover a chave e o endereço que está associado a ela e caso ocorra,
         * a funcao vai retornar tre
         * 
         * @param chave recebe um chave int
         * @return caso ele encontre a chave retorna true caso não false
         */

        public boolean delete(int chave) {
            boolean reposta = false;
            int i = 0;
            if (empty() == false) {

                while (i < quantidade && chave > chaves[i])
                    i++;
                if (chave == chaves[i]) {
                    while (i < quantidade - 1) {
                        chaves[i] = chaves[i + 1];
                        dados[i] = dados[i + 1];
                        i++;
                    }
                    quantidade--;
                    reposta = true;
                }
            }
            return reposta;
        }

        /**
         * Valida se a quantidade Maxima informada está dentro do específico
         * 
         * @param quantidadeMaxima
         * @throws Exception caso a Quantidade informa seja superior a 32767, vai estora
         *                   a exception
         */
        private void validarQuantidadeMaxima(int quantidadeMaxima) throws Exception {
            if (quantidadeMaxima > 32767) {
                throw new Exception("Quantidade máxima de 32.767 elementos");
            }
        }

        /**
         * Valida se a profundidade está dentro do específico
         * 
         * @param profundidadeLocal
         * @throws Exception caso a profundidade local informada seja superior a 127
         *                   bits,vai estora a exception
         */
        private void validarprofundidadeLocal(int profundidadeLocal) throws Exception {
            if (profundidadeLocal > 127)
                throw new Exception("Profundidade local máxima de 127 bits");
        }

        /**
         * empty
         * 
         * @return caso a cesto esteja como vazio ela retorna true
         */
        public boolean empty() {
            return quantidade == 0;
        }

        /**
         * full
         * 
         * @return Caso o cesto esteja completamente cheio ela retorna true
         */
        public boolean full() {
            return quantidade == quantidadeMaxima;
        }

        /**
         * size
         * 
         * @return
         */
        public int size() {
            return bytesPorCesto;
        }

        /**
         * toString converte todos elementos armazendos em uma String formada e retorna
         * a mesma
         */
        public String toString() {
            String s = "\nProfundidade Local: " + profundidadeLocal + "\nQuantidade: " + quantidade + "\n| ";
            int i = 0;
            while (i < quantidade) {
                s += chaves[i] + ";" + dados[i] + " | ";
                i++;
            }
            while (i < quantidadeMaxima) {
                s += "-;- | ";
                i++;
            }
            return s;
        }

    }

    /******************************************************************************
     * A {@code Diretorio}
     * 
     * @version 0.0.0
     * @category HashExtensivel
     ******************************************************************************/
    class Diretorio {

        private byte profundidadeGlobal;
        private long[] enderecos;

        public Diretorio() {
            profundidadeGlobal = 0;
            enderecos = new long[1];
            enderecos[0] = 0;
        }

        /**
         * atualizaEndereco
         * 
         * @param profundidade onde o endereço deve ser armazenado
         * @param endereco     long a ser posicionado
         * @return caso a operação ocorra com sucesso retorna True
         */
        public boolean atualizaEndereco(int profundidade, long endereco) {
            boolean resposta = true;
            if (profundidade > Math.pow(2, profundidadeGlobal)) {

                return false;
            } else {

                enderecos[profundidade] = endereco;
            }
            return resposta;
        }

        /**
         * toByteArray Converte o para metros do objeto e todos os elementos armazendo
         * dele em um array de bytes
         * 
         * @return
         * @throws IOException
         */
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeGlobal);
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            int i = 0;
            while (i < quantidade) {
                dos.writeLong(enderecos[i]);
                i++;
            }
            return baos.toByteArray();
        }

        /**
         * fromByteArray Recebe um vetor de bytes e o transforma no ele um determinado
         * parametros do objetos
         * 
         * @param ba
         * @throws IOException
         */
        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeGlobal = dis.readByte();
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            enderecos = new long[quantidade];
            int i = 0;
            while (i < quantidade) {
                enderecos[i] = dis.readLong();
                i++;
            }
        }

        /**
         * toString Formata o Objeto em uma String customizada
         */
        public String toString() {
            String s = "\nProfundidade global: " + profundidadeGlobal;
            int i = 0;
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            while (i < quantidade) {
                s += "\n" + i + ": " + enderecos[i];
                i++;
            }
            return s;
        }

        protected long endereco(int profubudade) {
            if (profubudade > Math.pow(2, profundidadeGlobal))
                return -1;
            return enderecos[profubudade];
        }

        /**
         * 
         * @return
         */
        protected boolean duplica() {

            if (profundidadeGlobal == 127)
                return false;
            profundidadeGlobal++;
            int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
            int q2 = (int) Math.pow(2, profundidadeGlobal);
            long[] novosEnderecos = new long[q2];
            int i = 0;
            while (i < q1) {
                novosEnderecos[i] = enderecos[i];
                i++;
            }
            while (i < q2) {
                novosEnderecos[i] = enderecos[i - q1];
                i++;
            }
            enderecos = novosEnderecos;
            return true;
        }

        /**
         * hash recebe uma chava e a trasforma e uma hash
         * 
         * @param chave
         * @return
         */
        protected int hash(int chave) {
            return chave % (int) Math.pow(2, profundidadeGlobal);
        }

        /**
         * hash2 cálculo do hash para uma dada profundidade local
         * 
         * @param chave
         * @param pl
         * @return
         */
        protected int hash2(int chave, int profundidadeLocal) {
            return chave % (int) Math.pow(2, profundidadeLocal);
        }

    }

    /**
     * create Função principal da da classe Diretorio
     * 
     * @param chave elemento para ordenar onde o o endereço vai ser armazenado
     * @param dado  long endereço que vai ser armazenado em um local apartir de um
     *              hash
     * @return true caso toda operação seja um sucesso
     * @throws Exception
     */
    public boolean create(int chave, long dado) throws Exception {

        // Carrega o diretório
        byte[] byteDiretorio = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(byteDiretorio);
        diretório = new Diretorio();
        diretório.fromByteArray(byteDiretorio);

        // Identifica a hash do diretório,
        int i = diretório.hash(chave);

        // Recupera o cesto
        long enderecoCesto = diretório.endereco(i);
        Cesto cesto = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[cesto.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        cesto.fromByteArray(ba);

        // Testa se a chave já não existe no cesto
        validaChave(cesto, chave);

        // Testa se o cesto já não está cheio
        // Se não estiver, create o par de chave e dado
        if (!cesto.full()) {
            // Insere a chave no cesto e o atualiza
            cesto.create(chave, dado);
            arqCestos.seek(enderecoCesto);
            arqCestos.write(cesto.toByteArray());
            return true;
        }

        // Duplica o diretório
        byte pl = cesto.profundidadeLocal;
        if (pl >= diretório.profundidadeGlobal)
            diretório.duplica();
        byte pg = diretório.profundidadeGlobal;

        // Cria os novos cestos, com os seus dados no arquivo de cestos
        Cesto c1 = new Cesto(quantidadeDadosPorCesto, pl + 1);
        arqCestos.seek(enderecoCesto);
        arqCestos.write(c1.toByteArray());

        Cesto c2 = new Cesto(quantidadeDadosPorCesto, pl + 1);
        long novoEndereço = arqCestos.length();
        arqCestos.seek(novoEndereço);
        arqCestos.write(c2.toByteArray());

        // Atualiza os dados no diretório
        int inicio = diretório.hash2(chave, cesto.profundidadeLocal);
        int deslocamento = (int) Math.pow(2, pl);
        int max = (int) Math.pow(2, pg);
        boolean troca = false;
        for (int j = inicio; j < max; j += deslocamento) {
            if (troca)
                diretório.atualizaEndereco(j, novoEndereço);
            troca = !troca;
        }

        // Atualiza o arquivo do diretório
        byteDiretorio = diretório.toByteArray();
        arqDiretorio.seek(0);
        arqDiretorio.write(byteDiretorio);

        // Reinsere as chaves
        for (int j = 0; j < cesto.quantidade; j++) {
            create(cesto.chaves[j], cesto.dados[j]);
        }
        create(chave, dado);
        return false;

    }

    /**
     * Verifica se a chave já foi ultilizada no Cesto
     * 
     * @param cesto local onde deve ser validado
     * @param chave que deve ser validada
     * @throws Exception
     */
    private void validaChave(Cesto cesto, int chave) throws Exception {
        // Testa se a chave já não existe no cesto
        if (cesto.read(chave) != -1)
            throw new Exception("Chave já existe");
    }

    /**
     * read
     * 
     * @param chave
     * @return
     * @throws Exception
     */
    public long read(int chave) throws Exception {

        // Carrega o diretório
        byte[] byteDiretorio = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(byteDiretorio);
        diretório = new Diretorio();
        diretório.fromByteArray(byteDiretorio);

        // Identifica a hash do diretório,
        int i = diretório.hash(chave);

        // Recupera o cesto
        long enderecoCesto = diretório.endereco(i);
        Cesto cesto = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[cesto.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        cesto.fromByteArray(ba);

        return cesto.read(chave);
    }

    /**
     * update Recebe uma chave e um novoDado, localiza se nessa chave existe alguma
     * informação
     */
    public boolean update(int chave, long novoDado) throws Exception {

        // Carrega o diretório
        byte[] byteDiretorio = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(byteDiretorio);
        diretório = new Diretorio();
        diretório.fromByteArray(byteDiretorio);

        // Identifica a hash do diretório,
        int i = diretório.hash(chave);

        // Recupera o cesto
        long enderecoCesto = diretório.endereco(i);
        Cesto cesto = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[cesto.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        cesto.fromByteArray(ba);

        // atualiza o dado
        if (!cesto.update(chave, novoDado))
            return false;

        // Atualiza o cesto
        arqCestos.seek(enderecoCesto);
        arqCestos.write(cesto.toByteArray());
        return true;

    }

    /**
     * delete Funcao recebe uma chave (int) localiza se exist algum vinculado a ela
     * caso haja o apaga junto a chave e atualiza o cesto
     * 
     * @param chave
     * @return
     * @throws Exception
     */
    public boolean delete(int chave) throws Exception {

        // Carrega o diretório
        byte[] byteDiretorio = new byte[(int) arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(byteDiretorio);
        diretório = new Diretorio();
        diretório.fromByteArray(byteDiretorio);

        // Identifica a hash do diretório,
        int i = diretório.hash(chave);

        // Recupera o cesto
        long enderecoCesto = diretório.endereco(i);
        Cesto cesto = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[cesto.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        cesto.fromByteArray(ba);

        // delete a chave
        if (!cesto.delete(chave))
            return false;

        // Atualiza o cesto
        arqCestos.seek(enderecoCesto);
        arqCestos.write(cesto.toByteArray());
        return true;
    }

    /**
     * print
     * 
     */
    public void print() {
        try {
            byte[] byteDiretorio = new byte[(int) arqDiretorio.length()];
            arqDiretorio.seek(0);
            arqDiretorio.read(byteDiretorio);
            diretório = new Diretorio();
            diretório.fromByteArray(byteDiretorio);
            System.out.println("\nDIRETÓRIO ------------------");
            System.out.println(diretório);

            System.out.println("\nCESTOS ---------------------");
            arqCestos.seek(0);
            while (arqCestos.getFilePointer() != arqCestos.length()) {
                Cesto c = new Cesto(quantidadeDadosPorCesto);
                byte[] ba = new byte[c.size()];
                arqCestos.read(ba);
                c.fromByteArray(ba);
                System.out.println(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}