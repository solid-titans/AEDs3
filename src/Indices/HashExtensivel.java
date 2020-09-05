/*********
 * TABELA HASH EXTENSÍVEL
 * int chave, long dado
 * 
 * Os nomes dos métodos foram mantidos em inglês
 * apenas para manter a coerência com o resto da
 * disciplina:
 * - boolean create(int chave, long dado)
 * - long read(int chave)
 * - boolean update(int chave, long dado)
 * - boolean delete(int chave)
 * 
 * Implementado pelo Prof. Marcos Kutova
 * v1.0 - 2019
 */
package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class HashExtensivel {
   
    String           nomeArquivoDiretorio;
    String           nomeArquivoCestos;
    RandomAccessFile arqDiretório;
    RandomAccessFile arqCestos;
    int              quantidadeDadosPorCesto;
    Diretório        diretório;
    
    class Cesto {

        byte   profundidadeLocal;   // profundidade local do cesto
        short  quantidade;          // quantidade de pares presentes no cesto
        short  quantidadeMaxima;    // quantidade máxima de pares que o cesto pode conter
        int[]  chaves;              // sequência de chaves armazenadas no cesto
        long[] dados;               // sequência de dados correspondentes às chaves
        short  bytesPorPar;         // size fixo de cada par de chave/dado em bytes
        short  bytesPorCesto;       // size fixo do cesto em bytes

        public Cesto(int qtdmax) throws Exception {
            this(qtdmax, 0);
        }

        public Cesto(int qtdmax, int pl) throws Exception {
            if(qtdmax>32767)
                throw new Exception("Quantidade máxima de 32.767 elementos");
            if(pl>127)
                throw new Exception("Profundidade local máxima de 127 bits");
            profundidadeLocal = (byte)pl;
            quantidade = 0;
            quantidadeMaxima = (short)qtdmax;
            chaves = new int[quantidadeMaxima];
            dados = new long[quantidadeMaxima];
            bytesPorPar = 12;  // int + long
            bytesPorCesto = (short)(bytesPorPar * quantidadeMaxima + 3);
        }

        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeLocal);
            dos.writeShort(quantidade);
            int i=0;
            while(i<quantidade) {
                dos.writeInt(chaves[i]);
                dos.writeLong(dados[i]);
                i++;
            }
            while(i<quantidadeMaxima) {
                dos.writeInt(0);
                dos.writeLong(0);
                i++;
            }
            return baos.toByteArray();            
        }

        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeLocal = dis.readByte();
            quantidade = dis.readShort();
            int i=0;
            while(i<quantidadeMaxima) {
                chaves[i] = dis.readInt();
                dados[i] = dis.readLong();
                i++;
            }
        }

        public boolean create(int c, long d) {
            if(full())
                return false;
            int i=quantidade-1;
            while(i>=0 && c<chaves[i]) {
                chaves[i+1] = chaves[i];
                dados[i+1] = dados[i];
                i--;
            }
            i++;
            chaves[i] = c;
            dados[i] = d;
            quantidade++;
            return true;
        }

        public long read(int c) {
            if(empty())
                return -1;
            int i=0;
            while(i<quantidade && c>chaves[i])
                i++;
            if(i<quantidade && c==chaves[i])
                return dados[i];
            else
                return -1;        
        }

        public boolean update(int c, long d) {
            if(empty())
                return false;
            int i=0;
            while(i<quantidade && c>chaves[i])
                i++;
            if(i<quantidade && c==chaves[i]) {
                dados[i] = d;
                return true;
            }
            else
                return false;        
        }

        public boolean delete(int c) {
            if(empty())
                return false;
            int i=0;
            while(i<quantidade && c>chaves[i])
                i++;
            if(c==chaves[i]) {
                while(i<quantidade-1) {
                    chaves[i] = chaves[i+1];
                    dados[i] = dados[i+1];
                    i++;
                }
                quantidade--;
                return true;
            }
            else
                return false;        
        }

        public boolean empty() {
            return quantidade == 0;
        }

        public boolean full() {
            return quantidade == quantidadeMaxima;
        }

        public String toString() {
            String s = "\nProfundidade Local: "+profundidadeLocal+
                       "\nQuantidade: "+quantidade+
                       "\n| ";
            int i=0;
            while(i<quantidade) {
                s += chaves[i] + ";" + dados[i] + " | ";
                i++;
            }
            while(i<quantidadeMaxima) {
                s += "-;- | ";
                i++;
            }
            return s;
        }

        public int size() {
            return bytesPorCesto;
        }

    }

    class Diretório {

        byte   profundidadeGlobal;
        long[] endereços;

        public Diretório() {
            profundidadeGlobal = 0;
            endereços = new long[1];
            endereços[0] = 0;
        }

        public boolean atualizaEndereco(int p, long e) {
            if(p>Math.pow(2,profundidadeGlobal))
                return false;
            endereços[p] = e;
            return true;
        }

        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeGlobal);
            int quantidade = (int)Math.pow(2,profundidadeGlobal);
            int i=0;
            while(i<quantidade) {
                dos.writeLong(endereços[i]);
                i++;
            }
            return baos.toByteArray();            
        }

        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeGlobal = dis.readByte();
            int quantidade = (int)Math.pow(2,profundidadeGlobal);
            endereços = new long[quantidade];
            int i=0;
            while(i<quantidade) {
                endereços[i] = dis.readLong();
                i++;
            }
        }

        public String toString() {
            String s = "\nProfundidade global: "+profundidadeGlobal;
            int i=0;
            int quantidade = (int)Math.pow(2,profundidadeGlobal);
            while(i<quantidade) {
                s += "\n" + i + ": " + endereços[i];
                i++;
            }
            return s;
        }

        protected long endereço(int p) {
            if(p>Math.pow(2,profundidadeGlobal))
                return -1;
            return endereços[p];
        }

        protected boolean duplica() {
            if(profundidadeGlobal==127)
                return false;
            profundidadeGlobal++;
            int q1 = (int)Math.pow(2,profundidadeGlobal-1);
            int q2 = (int)Math.pow(2,profundidadeGlobal);
            long[] novosEnderecos = new long[q2];
            int i=0;
            while(i<q1) {
                novosEnderecos[i]=endereços[i];
                i++;
            }
            while(i<q2) {
                novosEnderecos[i]=endereços[i-q1];
                i++;
            }
            endereços = novosEnderecos;
            return true;
        }

        protected int hash(int chave) {
            return chave % (int)Math.pow(2, profundidadeGlobal);
        }

        protected int hash2(int chave, int pl) { // cálculo do hash para uma dada profundidade local
            return chave % (int)Math.pow(2, pl);            
        }

    }
    
    
    
    public HashExtensivel(int n, String nd, String nc ) throws Exception {
        quantidadeDadosPorCesto = n;
        nomeArquivoDiretorio = nd;
        nomeArquivoCestos = nc;
        
        
        arqDiretório = new RandomAccessFile(nomeArquivoDiretorio,"rw");
        arqCestos = new RandomAccessFile(nomeArquivoCestos,"rw");

        // Se o diretório ou os cestos estiverem vazios, cria um novo diretório e lista de cestos
        if(arqDiretório.length()==0 || arqCestos.length()==0) {

            // Cria um novo diretório, com profundidade de 0 bits (1 único elemento)
            diretório = new Diretório();
            byte[] bd = diretório.toByteArray();
            arqDiretório.write(bd);
            
            // Cria um cesto vazio, já apontado pelo único elemento do diretório
            Cesto c = new Cesto(quantidadeDadosPorCesto);
            bd = c.toByteArray();
            arqCestos.seek(0);
            arqCestos.write(bd);
        }
    }
    
    public boolean create(int chave, long dado) throws Exception {
        
        //Carrega o diretório
        byte[] bd = new byte[(int)arqDiretório.length()];
        arqDiretório.seek(0);
        arqDiretório.read(bd);
        diretório = new Diretório();
        diretório.fromByteArray(bd);        
        
        // Identifica a hash do diretório,
        int i = diretório.hash(chave);
        
        // Recupera o cesto
        long endereçoCesto = diretório.endereço(i);
        Cesto c = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(endereçoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        // Testa se a chave já não existe no cesto
        if(c.read(chave)!=-1)
            throw new Exception("Chave já existe");     

        // Testa se o cesto já não está cheio
        // Se não estiver, create o par de chave e dado
        if(!c.full()) {
            // Insere a chave no cesto e o atualiza 
            c.create(chave, dado);
            arqCestos.seek(endereçoCesto);
            arqCestos.write(c.toByteArray());
            return true;        
        }
        
        // Duplica o diretório
        byte pl = c.profundidadeLocal;
        if(pl>=diretório.profundidadeGlobal)
            diretório.duplica();
        byte pg = diretório.profundidadeGlobal;

        // Cria os novos cestos, com os seus dados no arquivo de cestos
        Cesto c1 = new Cesto(quantidadeDadosPorCesto, pl+1);
        arqCestos.seek(endereçoCesto);
        arqCestos.write(c1.toByteArray());

        Cesto c2 = new Cesto(quantidadeDadosPorCesto, pl+1);
        long novoEndereço = arqCestos.length();
        arqCestos.seek(novoEndereço);
        arqCestos.write(c2.toByteArray());
        
        // Atualiza os dados no diretório
        int inicio = diretório.hash2(chave, c.profundidadeLocal);
        int deslocamento = (int)Math.pow(2,pl);
        int max = (int)Math.pow(2,pg);
        boolean troca = false;
        for(int j=inicio; j<max; j+=deslocamento) {
            if(troca)
                diretório.atualizaEndereco(j,novoEndereço);
            troca=!troca;
        }
        
        // Atualiza o arquivo do diretório
        bd = diretório.toByteArray();
        arqDiretório.seek(0);
        arqDiretório.write(bd);
        
        // Reinsere as chaves
        for(int j=0; j<c.quantidade; j++) {
            create(c.chaves[j], c.dados[j]);
        }
        create(chave,dado);
        return false;   

    }
    
    public long read(int chave) throws Exception {
        
        //Carrega o diretório
        byte[] bd = new byte[(int)arqDiretório.length()];
        arqDiretório.seek(0);
        arqDiretório.read(bd);
        diretório = new Diretório();
        diretório.fromByteArray(bd);        
        
        // Identifica a hash do diretório,
        int i = diretório.hash(chave);
        
        // Recupera o cesto
        long endereçoCesto = diretório.endereço(i);
        Cesto c = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(endereçoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        return c.read(chave);
    }
    
    public boolean update(int chave, long novoDado) throws Exception {
        
        //Carrega o diretório
        byte[] bd = new byte[(int)arqDiretório.length()];
        arqDiretório.seek(0);
        arqDiretório.read(bd);
        diretório = new Diretório();
        diretório.fromByteArray(bd);        
        
        // Identifica a hash do diretório,
        int i = diretório.hash(chave);
        
        // Recupera o cesto
        long endereçoCesto = diretório.endereço(i);
        Cesto c = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(endereçoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        // atualiza o dado
        if(!c.update(chave, novoDado))
            return false;
        
        // Atualiza o cesto
        arqCestos.seek(endereçoCesto);
        arqCestos.write(c.toByteArray());
        return true;
        
    }
    
    public boolean delete(int chave) throws Exception {
        
        //Carrega o diretório
        byte[] bd = new byte[(int)arqDiretório.length()];
        arqDiretório.seek(0);
        arqDiretório.read(bd);
        diretório = new Diretório();
        diretório.fromByteArray(bd);        
        
        // Identifica a hash do diretório,
        int i = diretório.hash(chave);
        
        // Recupera o cesto
        long endereçoCesto = diretório.endereço(i);
        Cesto c = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(endereçoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        // delete a chave
        if(!c.delete(chave))
            return false;
        
        // Atualiza o cesto
        arqCestos.seek(endereçoCesto);
        arqCestos.write(c.toByteArray());
        return true;
    }
    
    public void print() {
        try {
            byte[] bd = new byte[(int)arqDiretório.length()];
            arqDiretório.seek(0);
            arqDiretório.read(bd);
            diretório = new Diretório();
            diretório.fromByteArray(bd);   
            System.out.println("\nDIRETÓRIO ------------------");
            System.out.println(diretório);

            System.out.println("\nCESTOS ---------------------");
            arqCestos.seek(0);
            while(arqCestos.getFilePointer() != arqCestos.length()) {
                Cesto c = new Cesto(quantidadeDadosPorCesto);
                byte[] ba = new byte[c.size()];
                arqCestos.read(ba);
                c.fromByteArray(ba);
                System.out.println(c);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
//
//    // Método principal apenas para testes
//    public static void main(String[] args) {
//        
//        HashExtensivel he;
//        Scanner console = new Scanner(System.in);
//    
//        try {
//            he = new HashExtensivel(4, "diretorio.hash.db", "cestos.hash.db");
//
//
//            int opcao;
//            do {
//                System.out.println("\n\n-------------------------------");
//                System.out.println("              MENU");
//                System.out.println("-------------------------------");
//                System.out.println("1 - Inserir");
//                System.out.println("2 - Buscar");
//                System.out.println("3 - Excluir");
//                System.out.println("4 - Imprimir");
//                System.out.println("0 - Sair");
//                try {
//                    opcao = Integer.valueOf(console.nextLine());
//                } catch(NumberFormatException e) {
//                    opcao = -1;
//                }
//                
//                switch(opcao) {
//                    case 1: {
//                        System.out.println("\nINCLUSÃO");
//                        System.out.print("Chave: ");
//                        int chave = Integer.valueOf(console.nextLine());
//                        System.out.print("Dado: ");
//                        long dado = Long.valueOf(console.nextLine());
//                        he.create(chave, dado);
//                        he.print();
//                    }break;
//                    case 2: {
//                        System.out.println("\nBUSCA");
//                        System.out.print("Chave: ");
//                        int chave = Integer.valueOf(console.nextLine());
//                        System.out.print("Dado: "+he.read(chave));
//                    }break;
//                    case 3: {
//                        System.out.println("\nEXCLUSÃO");
//                        System.out.print("Chave: ");
//                        int chave = Integer.valueOf(console.nextLine());
//                        he.delete(chave);
//                        he.print();
//                    } break;
//                    case 4: {
//                        he.print();
//                    } break;
//                    case 0: break;
//                    default: System.out.println("Opção inválida");
//                }
//            } while(opcao != 0);
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    
}
