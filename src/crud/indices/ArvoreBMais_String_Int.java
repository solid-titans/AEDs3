/*********
 * ARVORE B+ SI
 * String chave, int dado
 * 
 * Os nomes dos métodos foram mantidos em inglês
 * apenas para manter a coerência com o resto da
 * disciplina:
 * - boolean create(String chave, int dado)
 * - int read(String chave)
 * - boolean update(String chave, int dado)
 * - boolean delete(String chave)
 * 
 * Implementado pelo Prof. Marcos Kutova
 * v1.0 - 2018
 */


package aed3;
import java.io.*;
import java.util.ArrayList;

// Árvore B+ para ser usada como índice indireto de algum arquivo de entidades
// CHAVE: String  (usado para algum atributo textual da entidade como Nome, Título, ...)
// VALOR: Int     (usado para o identificador dessa entidade)

public class ArvoreBMais_String_Int {

    private int  ordem;                 // Número máximo de filhos que uma página pode conter
    private int  maxElementos;          // Variável igual a ordem - 1 para facilitar a clareza do código
    private int  maxFilhos;             // Variável igual a ordem para facilitar a clareza do código
    private RandomAccessFile arquivo;   // Arquivo em que a árvore será armazenada
    private String nomeArquivo;
    
    // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
    private String  chaveAux;
    private int     dadoAux;
    private long    paginaAux;
    private boolean cresceu;
    private boolean diminuiu;
    
    // Esta classe representa uma página da árvore (folha ou não folha). 
    private class Pagina {

        protected int      ordem;                 // Número máximo de filhos que uma página pode ter
        protected int      maxElementos;          // Variável igual a ordem - 1 para facilitar a clareza do código
        protected int      maxFilhos;             // Variável igual a ordem  para facilitar a clareza do código
        protected int      n;                     // Número de elementos presentes na página
        protected String[] chaves;                // Chaves
        protected int[]    dados;                 // Dados associados às chaves
        protected long     proxima;               // Próxima folha, quando a página for uma folha
        protected long[]   filhos;                // Vetor de ponteiros para os filhos
        protected int      TAMANHO_CHAVE;         // Tamanho da string máxima usada como chave
        protected int      TAMANHO_REGISTRO;      // Os elementos são de tamanho fixo
        protected int      TAMANHO_PAGINA;        // A página será de tamanho fixo, calculado a partir da ordem

        // Construtor da página
        public Pagina(int o) {

            // Inicialização dos atributos
            n = 0;
            ordem = o;
            maxFilhos = o;
            maxElementos = o-1;
            chaves = new String[maxElementos];
            dados  = new int[maxElementos];
            filhos = new long[maxFilhos];
            proxima = -1;
            
            // Criação de uma página vázia
            for(int i=0; i<maxElementos; i++) {  
                chaves[i] = "";
                dados[i]  = -1;
                filhos[i] = -1;
            }
            filhos[maxFilhos-1] = -1;
            
            // Cálculo do tamanho (fixo) da página
            // n -> 4 bytes
            // cada elemento -> 104 bytes (string + int)
            // cada ponteiro de filho -> 8 bytes (long)
            // último filho -> 8 bytes (long)
            // ponteiro próximo -> 8 bytes
            TAMANHO_CHAVE = 100;
            TAMANHO_REGISTRO = 104;
            TAMANHO_PAGINA = 4 + maxElementos*TAMANHO_REGISTRO + maxFilhos*8 + 16;
        }
        
        // Como uma chave string tem tamanho variável (por causa do Unicode),
        // provavelmente não será possível ter uma string de 100 caracteres.
        // Os caracteres excedentes (já que a página tem que ter tamanho fixo)
        // são preenchidos com espaços em branco
        private byte[] completaBrancos(String str) {
            byte[] aux;
            byte[] buffer = new byte[TAMANHO_CHAVE];
            aux = str.getBytes();
            int i=0; while(i<aux.length) { buffer[i] = aux[i]; i++; }
            while(i<TAMANHO_CHAVE) { buffer[i] = 0x20; i++; }
            return buffer;
        }
        
        // Retorna o vetor de bytes que representa a página para armazenamento em arquivo
        protected byte[] getBytes() throws IOException {
            
            // Um fluxo de bytes é usado para construção do vetor de bytes
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(ba);
            
            // Quantidade de elementos presentes na página
            out.writeInt(n);
            
            // Escreve todos os elementos
            int i=0;
            while(i<n) {
                out.writeLong(filhos[i]);
                out.write(completaBrancos(chaves[i]));
                out.writeInt(dados[i]);
                i++;
            }
            out.writeLong(filhos[i]);
            
            // Completa o restante da página com registros vazios
            byte[] registroVazio = new byte[TAMANHO_REGISTRO];
            while(i<maxElementos){
                out.write(registroVazio);
                out.writeLong(filhos[i+1]);
                i++;
            }

            // Escreve o ponteiro para a próxima página
            out.writeLong(proxima);
            
            // Retorna o vetor de bytes que representa a página
            return ba.toByteArray();
        }

        
        // Reconstrói uma página a partir de um vetor de bytes lido no arquivo
        public void setBytes(byte[] buffer) throws IOException {
            
            // Usa um fluxo de bytes para leitura dos atributos
            ByteArrayInputStream ba = new ByteArrayInputStream(buffer);
            DataInputStream in = new DataInputStream(ba);
            byte[] bs = new byte[TAMANHO_CHAVE];
            
            // Lê a quantidade de elementos da página
            n = in.readInt();
            
            // Lê todos os elementos (reais ou vazios)
            int i=0;
            while(i<maxElementos) {
                filhos[i]  = in.readLong();
                in.read(bs);
                chaves[i] = (new String(bs)).trim();
                dados[i]   = in.readInt(); 
                i++;
            }
            filhos[i] = in.readLong();
            proxima = in.readLong();
        }
    }
    
    // ------------------------------------------------------------------------------
        
    
    public ArvoreBMais_String_Int(int o, String na) throws IOException {
        
        // Inicializa os atributos da árvore
        ordem = o;
        maxElementos = o-1;
        maxFilhos = o;
        nomeArquivo = na;
        
        // Abre (ou cria) o arquivo, escrevendo uma raiz empty, se necessário.
        arquivo = new RandomAccessFile(nomeArquivo,"rw");
        if(arquivo.length()<8) 
            arquivo.writeLong(-1);  // raiz empty
    }
    
    // Testa se a árvore está empty. Uma árvore empty é identificada pela raiz == -1
    public boolean empty() throws IOException {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        return raiz == -1;
    }
    
        
    // Busca recursiva por um elemento a partir da chave. Este metodo invoca 
    // o método recursivo read1, passando a raiz como referência.
    public int read(String c) throws IOException {
        
        // Recupera a raiz da árvore
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        
        // Executa a busca recursiva
        if(raiz!=-1)
            return read1(c,raiz);
        else
            return -1;
    }
    
    // Busca recursiva. Este método recebe a referência de uma página e busca
    // pela chave na mesma. A busca continua pelos filhos, se houverem.
    private int read1(String chave, long pagina) throws IOException {
        
        // Como a busca é recursiva, a descida para um filho inexistente
        // (filho de uma página folha) retorna um valor negativo.
        if(pagina==-1)
            return -1;
        
        // Reconstrói a página passada como referência a partir 
        // do registro lido no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);
 
        // Encontra o ponto em que a chave deve estar na página
        // Primeiro passo - todas as chaves menores que a chave buscada são ignoradas
        int i=0;
        while(i<pa.n && chave.compareTo(pa.chaves[i])>0) {
            i++;
        }
        
        // Chave encontrada (ou pelo menos o ponto onde ela deveria estar).
        // Segundo passo - testa se a chave é a chave buscada e se está em uma folha
        // Obs.: em uma árvore B+, todas as chaves válidas estão nas folhas
        // Obs.: a comparação exata só será possível se considerarmos a menor string
        //       entre a chave e a string na página
        if(i<pa.n && pa.filhos[0]==-1 
                  && chave.compareTo(pa.chaves[i])==0) {
            return pa.dados[i];
        }
        
        // Terceiro passo - ainda não é uma folha, continua a busca recursiva pela árvore
        if(i==pa.n || chave.compareTo(pa.chaves[i])<0)
            return read1(chave, pa.filhos[i]);
        else
            return read1(chave, pa.filhos[i+1]);
    }
        
    // Atualiza recursivamente um valor a partir da sua chave. Este metodo invoca 
    // o método recursivo update1, passando a raiz como referência.
    public boolean update(String c, int d) throws IOException {
        
        // Recupera a raiz da árvore
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        
        // Executa a busca recursiva
        if(raiz!=-1)
            return update1(c,d,raiz);
        else
            return false;
    }
    
    // Atualização recursiva. Este método recebe a referência de uma página, uma
    // chave de busca e o dado correspondente a ela. 
    private boolean update1(String chave, int dado, long pagina) throws IOException {
        
        // Como a busca é recursiva, a descida para um filho inexistente
        // (filho de uma página folha) retorna um valor negativo.
        if(pagina==-1)
            return false;
        
        // Reconstrói a página passada como referência a partir 
        // do registro lido no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);
 
        // Encontra o ponto em que a chave deve estar na página
        // Primeiro passo - todas as chaves menores que a chave buscada são ignoradas
        int i=0;
        while(i<pa.n && chave.compareTo(pa.chaves[i])>0) {
            i++;
        }
        
        // Chave encontrada (ou pelo menos o ponto onde ela deveria estar).
        // Segundo passo - testa se a chave é a chave buscada e se está em uma folha
        // Obs.: em uma árvore B+, todas as chaves válidas estão nas folhas
        if(i<pa.n && pa.filhos[0]==-1 
                  && chave.compareTo(pa.chaves[i].substring(0,Math.min(chave.length(),pa.chaves[i].length())))==0) {
            pa.dados[i] = dado;
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            return true;
        }
        
        // Terceiro passo - ainda não é uma folha, continua a busca recursiva pela árvore
        if(i==pa.n || chave.compareTo(pa.chaves[i])<0)
            return update1(chave, dado, pa.filhos[i]);
        else
            return update1(chave, dado, pa.filhos[i+1]);
    }
        
    
    // Inclusão de novos elementos na árvore. A inclusão é recursiva. A primeira
    // função chama a segunda recursivamente, passando a raiz como referência.
    // Eventualmente, a árvore pode crescer para cima.
    public boolean create(String c, int d) throws IOException {

        // Chave não pode ser empty
        if(c.compareTo("")==0) {
            System.out.println( "Chave não pode ser vazia" );
            return false;
        }
            
        // Carrega a raiz
        arquivo.seek(0);       
        long pagina;
        pagina = arquivo.readLong();

        // O processo de inclusão permite que os valores passados como referência
        // sejam substituídos por outros valores, para permitir a divisão de páginas
        // e crescimento da árvore. Assim, são usados os valores globais chaveAux 
        // e dadoAux. Quando há uma divisão, a chave e o valor promovidos são armazenados
        // nessas variáveis.
        chaveAux = c;
        dadoAux = d;
        
        // Se houver crescimento, então será criada uma página extra e será mantido um
        // ponteiro para essa página. Os valores também são globais.
        paginaAux = -1;
        cresceu = false;
                
        // Chamada recursiva para a inserção da chave e do valor
        // A chave e o valor não são passados como parâmetros, porque são globais
        boolean inserido = create1(pagina);
        
        // Testa a necessidade de criação de uma nova raiz.
        if(cresceu) {
            
            // Cria a nova página que será a raiz. O ponteiro esquerdo da raiz
            // será a raiz antiga e o seu ponteiro direito será para a nova página.
            Pagina novaPagina = new Pagina(ordem);
            novaPagina.n = 1;
            novaPagina.chaves[0] = chaveAux;
            novaPagina.dados[0]  = dadoAux;
            novaPagina.filhos[0] = pagina;
            novaPagina.filhos[1] = paginaAux;
            
            // Acha o espaço em disco. Nesta versão, todas as novas páginas
            // são escrita no fim do arquivo.
            arquivo.seek(arquivo.length());
            long raiz = arquivo.getFilePointer();
            arquivo.write(novaPagina.getBytes());
            arquivo.seek(0);
            arquivo.writeLong(raiz);
        }
        
        return inserido;
    }
    
    
    // Função recursiva de inclusão. A função passa uma página de referência.
    // As inclusões são sempre feitas em uma folha.
    private boolean create1(long pagina) throws IOException {
        
        // Testa se passou para o filho de uma página folha. Nesse caso, 
        // inicializa as variáveis globais de controle.
        if(pagina==-1) {
            cresceu = true;
            paginaAux = -1;
            return false;
        }
        
        // Lê a página passada como referência
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);
        
        // Busca o próximo ponteiro de descida. Como pode haver repetição
        // da primeira chave, a segunda também é usada como referência.
        // Nesse primeiro passo, todos os pares menores são ultrapassados.
        int i=0;
        while(i<pa.n && chaveAux.compareTo(pa.chaves[i])>0) {
            i++;
        }
        
        // Testa se a chave já existe em uma folha. Se isso acontecer, então 
        // a inclusão é cancelada.
        if(i<pa.n && pa.filhos[0]==-1 && chaveAux.compareTo(pa.chaves[i])==0) {
            cresceu = false;
            return false;
        }
        
        // Continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        boolean inserido;
        if(i==pa.n || chaveAux.compareTo(pa.chaves[i])<0)
            inserido = create1(pa.filhos[i]);
        else
            inserido = create1(pa.filhos[i+1]);
        
        // A partir deste ponto, as chamadas recursivas já foram encerradas. 
        // Assim, o próximo código só é executado ao retornar das chamadas recursivas.

        // A inclusão já foi resolvida por meio de uma das chamadas recursivas. Nesse
        // caso, apenas retorna para encerrar a recursão.
        // A inclusão pode ter sido resolvida porque a chave já existia (inclusão inválida)
        // ou porque o novo elemento coube em uma página existente.
        if(!cresceu)
            return inserido;
        
        // Se tiver espaço na página, faz a inclusão nela mesmo
        if(pa.n<maxElementos) {

            // Puxa todos elementos para a direita, começando do último
            // para gerar o espaço para o novo elemento
            for(int j=pa.n; j>i; j--) {
                pa.chaves[j] = pa.chaves[j-1];
                pa.dados[j] = pa.dados[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves[i] = chaveAux;
            pa.dados[i] = dadoAux;
            pa.filhos[i+1] = paginaAux;
            pa.n++;
            
            // Escreve a página atualizada no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            
            // Encerra o processo de crescimento e retorna
            cresceu=false;
            return true;
        }
        
        // O elemento não cabe na página. A página deve ser dividida e o elemento
        // do meio deve ser promovido (sem retirar a referência da folha).
        
        // Cria uma nova página
        Pagina np = new Pagina(ordem);
        
        // Copia a metade superior dos elementos para a nova página,
        // considerando que maxElementos pode ser ímpar
        int meio = maxElementos/2;
        for(int j=0; j<(maxElementos-meio); j++) {    
            
            // copia o elemento
            np.chaves[j] = pa.chaves[j+meio];
            np.dados[j] = pa.dados[j+meio];   
            np.filhos[j+1] = pa.filhos[j+meio+1];  
            
            // limpa o espaço liberado
            pa.chaves[j+meio] = "";
            pa.dados[j+meio] = 0;
            pa.filhos[j+meio+1] = -1;
        }
        np.filhos[0] = pa.filhos[meio];
        np.n = maxElementos-meio;
        pa.n = meio;
        
        // Testa o lado de inserção
        // Caso 1 - Novo registro deve ficar na página da esquerda
        if(i<=meio) {   
            
            // Puxa todos os elementos para a direita
            for(int j=meio; j>0 && j>i; j--) {
                pa.chaves[j] = pa.chaves[j-1];
                pa.dados[j] = pa.dados[j-1];
                pa.filhos[j+1] = pa.filhos[j];
            }
            
            // Insere o novo elemento
            pa.chaves[i] = chaveAux;
            pa.dados[i] = dadoAux;
            pa.filhos[i+1] = paginaAux;
            pa.n++;
            
            // Se a página for folha, seleciona o primeiro elemento da página 
            // da direita para ser promovido, mantendo-o na folha
            if(pa.filhos[0]==-1) {
                chaveAux = np.chaves[0];
                dadoAux = np.dados[0];
            }
            
            // caso contrário, promove o maior elemento da página esquerda
            // removendo-o da página
            else {
                chaveAux = pa.chaves[pa.n-1];
                dadoAux = pa.dados[pa.n-1];
                pa.chaves[pa.n-1] = "";
                pa.dados[pa.n-1] = 0;
                pa.filhos[pa.n] = -1;
                pa.n--;
            }
        } 
        
        // Caso 2 - Novo registro deve ficar na página da direita
        else {
            int j;
            for(j=maxElementos-meio; j>0 && chaveAux.compareTo(np.chaves[j-1])<0; j--) {
                np.chaves[j] = np.chaves[j-1];
                np.dados[j] = np.dados[j-1];
                np.filhos[j+1] = np.filhos[j];
            }
            np.chaves[j] = chaveAux;
            np.dados[j] = dadoAux;
            np.filhos[j+1] = paginaAux;
            np.n++;

            // Seleciona o primeiro elemento da página da direita para ser promovido
            chaveAux = np.chaves[0];
            dadoAux = np.dados[0];
            
            // Se não for folha, remove o elemento promovido da página
            if(pa.filhos[0]!=-1) {
                for(j=0; j<np.n-1; j++) {
                    np.chaves[j] = np.chaves[j+1];
                    np.dados[j] = np.dados[j+1];
                    np.filhos[j] = np.filhos[j+1];
                }
                np.filhos[j] = np.filhos[j+1];
                
                // apaga o último elemento
                np.chaves[j] = "";
                np.dados[j] = 0;
                np.filhos[j+1] = -1;
                np.n--;
            }

        }
        
        // Se a página era uma folha e apontava para outra folha, 
        // então atualiza os ponteiros dessa página e da página nova
        if(pa.filhos[0]==-1) {
            np.proxima=pa.proxima;
            pa.proxima = arquivo.length();
        }

        // Grava as páginas no arquivos arquivo
        paginaAux = arquivo.length();
        arquivo.seek(paginaAux);
        arquivo.write(np.getBytes());

        arquivo.seek(pagina);
        arquivo.write(pa.getBytes());
        
        return true;
    }

    
    // Remoção elementos na árvore. A remoção é recursiva. A primeira
    // função chama a segunda recursivamente, passando a raiz como referência.
    // Eventualmente, a árvore pode reduzir seu tamanho, por meio da exclusão da raiz.
    public boolean delete(String chave) throws IOException {
                
        // Encontra a raiz da árvore
        arquivo.seek(0);       
        long pagina;                
        pagina = arquivo.readLong();

        // variável global de controle da redução do tamanho da árvore
        diminuiu = false;  
                
        // Chama recursivamente a exclusão de registro (na chave1Aux e no 
        // chave2Aux) passando uma página como referência
        boolean excluido = delete1(chave, pagina);
        
        // Se a exclusão tiver sido possível e a página tiver reduzido seu tamanho,
        // por meio da fusão das duas páginas filhas da raiz, elimina essa raiz
        if(excluido && diminuiu) {
            
            // Lê a raiz
            arquivo.seek(pagina);
            Pagina pa = new Pagina(ordem);
            byte[] buffer = new byte[pa.TAMANHO_PAGINA];
            arquivo.read(buffer);
            pa.setBytes(buffer);
            
            // Se a página tiver 0 elementos, apenas atualiza o ponteiro para a raiz,
            // no cabeçalho do arquivo, para o seu primeiro filho.
            if(pa.n == 0) {
                arquivo.seek(0);
                arquivo.writeLong(pa.filhos[0]);  
            }
        }
         
        return excluido;
    }
    

    // Função recursiva de exclusão. A função passa uma página de referência.
    // As exclusões são sempre feitas em folhas e a fusão é propagada para cima.
    private boolean delete1(String chave, long pagina) throws IOException {
        
        // Declaração de variáveis
        boolean excluido=false;
        int diminuido;
        
        // Testa se o registro não foi encontrado na árvore, ao alcançar uma folha
        // inexistente (filho de uma folha real)
        if(pagina==-1) {
            diminuiu=false;
            return false;
        }
        
        // Lê o registro da página no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);

        // Encontra a página em que a chave está presente
        // Nesse primeiro passo, salta todas as chaves menores
        int i=0;
        while(i<pa.n && chave.compareTo(pa.chaves[i])>0) {
            i++;
        }

        // Chaves encontradas em uma folha
        if(i<pa.n && pa.filhos[0]==-1 && chave.compareTo(pa.chaves[i])==0) {

            // Puxa todas os elementos seguintes para uma posição anterior, sobrescrevendo
            // o elemento a ser excluído
            int j;
            for(j=i; j<pa.n-1; j++) {
                pa.chaves[j] = pa.chaves[j+1];
                pa.dados[j] = pa.dados[j+1];
            }
            pa.n--;
            
            // limpa o último elemento
            pa.chaves[pa.n] = "";
            pa.dados[pa.n] = 0;
            
            // Atualiza o registro da página no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            
            // Se a página contiver menos elementos do que o mínimo necessário,
            // indica a necessidade de fusão de páginas
            diminuiu = pa.n<maxElementos/2;
            return true;
        }

        // Se a chave não tiver sido encontrada (observar o return true logo acima),
        // continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        // A variável diminuído mantem um registro de qual página eventualmente 
        // pode ter ficado com menos elementos do que o mínimo necessário.
        // Essa página será filha da página atual
        if(i==pa.n || chave.compareTo(pa.chaves[i])<0) {
            excluido = delete1(chave, pa.filhos[i]);
            diminuido = i;
        } else {
            excluido = delete1(chave, pa.filhos[i+1]);
            diminuido = i+1;
        }
        
        
        // A partir deste ponto, o código é executado após o retorno das chamadas
        // recursivas do método
        
        // Testa se há necessidade de fusão de páginas
        if(diminuiu) {

            // Carrega a página filho que ficou com menos elementos do 
            // do que o mínimo necessário
            long paginaFilho = pa.filhos[diminuido];
            Pagina pFilho = new Pagina(ordem);
            arquivo.seek(paginaFilho);
            arquivo.read(buffer);
            pFilho.setBytes(buffer);
            
            // Cria uma página para o irmão (da direita ou esquerda)
            long paginaIrmao;
            Pagina pIrmao;
            
            // Tenta a fusão com irmão esquerdo
            if(diminuido>0) {
                
                // Carrega o irmão esquerdo
                paginaIrmao = pa.filhos[diminuido-1];
                pIrmao = new Pagina(ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(buffer);
                pIrmao.setBytes(buffer);
                
                // Testa se o irmão pode ceder algum registro
                if(pIrmao.n>maxElementos/2) {
                    
                    // Move todos os elementos do filho aumentando uma posição
                    // à esquerda, gerando espaço para o elemento cedido
                    for(int j=pFilho.n; j>0; j--) {
                        pFilho.chaves[j] = pFilho.chaves[j-1];
                        pFilho.dados[j] = pFilho.dados[j-1];
                        pFilho.filhos[j+1] = pFilho.filhos[j];
                    }
                    pFilho.filhos[1] = pFilho.filhos[0];
                    pFilho.n++;
                    
                    // Se for folha, copia o elemento do irmão, já que o do pai
                    // será extinto ou repetido
                    if(pFilho.filhos[0]==-1) {
                        pFilho.chaves[0] = pIrmao.chaves[pIrmao.n-1];
                        pFilho.dados[0] = pIrmao.dados[pIrmao.n-1];
                    }
                    
                    // Se não for folha, rotaciona os elementos, descendo o elemento do pai
                    else {
                        pFilho.chaves[0] = pa.chaves[diminuido-1];
                        pFilho.dados[0] = pa.dados[diminuido-1];
                    }

                    // Copia o elemento do irmão para o pai (página atual)
                    pa.chaves[diminuido-1] = pIrmao.chaves[pIrmao.n-1];
                    pa.dados[diminuido-1] = pIrmao.dados[pIrmao.n-1];
                        
                    
                    // Reduz o elemento no irmão
                    pFilho.filhos[0] = pIrmao.filhos[pIrmao.n];
                    pIrmao.n--;
                    diminuiu = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.filhos[0] != -1) {
                        pIrmao.chaves[pIrmao.n] = pa.chaves[diminuido-1];
                        pIrmao.dados[pIrmao.n] = pa.dados[diminuido-1];
                        pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[0];
                        pIrmao.n++;
                    }
                    
                    
                    // Copia todos os registros para o irmão da esquerda
                    for(int j=0; j<pFilho.n; j++) {
                        pIrmao.chaves[pIrmao.n] = pFilho.chaves[j];
                        pIrmao.dados[pIrmao.n] = pFilho.dados[j];
                        pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[j+1];
                        pIrmao.n++;
                    }
                    pFilho.n = 0;   // aqui o endereço do filho poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se as páginas forem folhas, copia o ponteiro para a folha seguinte
                    if(pIrmao.filhos[0]==-1)
                        pIrmao.proxima = pFilho.proxima;
                    
                    // puxa os registros no pai
                    int j;
                    for(j=diminuido-1; j<pa.n-1; j++) {
                        pa.chaves[j] = pa.chaves[j+1];
                        pa.dados[j] = pa.dados[j+1];
                        pa.filhos[j+1] = pa.filhos[j+2];
                    }
                    pa.chaves[j] = "";
                    pa.dados[j] = -1;
                    pa.filhos[j+1] = -1;
                    pa.n--;
                    diminuiu = pa.n<maxElementos/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Faz a fusão com o irmão direito
            else {
                
                // Carrega o irmão
                paginaIrmao = pa.filhos[diminuido+1];
                pIrmao = new Pagina(ordem);
                arquivo.seek(paginaIrmao);
                arquivo.read(buffer);
                pIrmao.setBytes(buffer);
                
                // Testa se o irmão pode ceder algum elemento
                if(pIrmao.n>maxElementos/2) {
                    
                    // Se for folha
                    if( pFilho.filhos[0]==-1 ) {
                    
                        //copia o elemento do irmão
                        pFilho.chaves[pFilho.n] = pIrmao.chaves[0];
                        pFilho.dados[pFilho.n] = pIrmao.dados[0];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;

                        // sobe o próximo elemento do irmão
                        pa.chaves[diminuido] = pIrmao.chaves[1];
                        pa.dados[diminuido] = pIrmao.dados[1];
                        
                    } 
                    
                    // Se não for folha, rotaciona os elementos
                    else {
                        
                        // Copia o elemento do pai, com o ponteiro esquerdo do irmão
                        pFilho.chaves[pFilho.n] = pa.chaves[diminuido];
                        pFilho.dados[pFilho.n] = pa.dados[diminuido];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;
                        
                        // Sobe o elemento esquerdo do irmão para o pai
                        pa.chaves[diminuido] = pIrmao.chaves[0];
                        pa.dados[diminuido] = pIrmao.dados[0];
                    }
                    
                    // move todos os registros no irmão para a esquerda
                    int j;
                    for(j=0; j<pIrmao.n-1; j++) {
                        pIrmao.chaves[j] = pIrmao.chaves[j+1];
                        pIrmao.dados[j] = pIrmao.dados[j+1];
                        pIrmao.filhos[j] = pIrmao.filhos[j+1];
                    }
                    pIrmao.filhos[j] = pIrmao.filhos[j+1];
                    pIrmao.n--;
                    diminuiu = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.filhos[0] != -1) {
                        pFilho.chaves[pFilho.n] = pa.chaves[diminuido];
                        pFilho.dados[pFilho.n] = pa.dados[diminuido];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
                        pFilho.n++;
                    }
                    
                    // Copia todos os registros do irmão da direita
                    for(int j=0; j<pIrmao.n; j++) {
                        pFilho.chaves[pFilho.n] = pIrmao.chaves[j];
                        pFilho.dados[pFilho.n] = pIrmao.dados[j];
                        pFilho.filhos[pFilho.n+1] = pIrmao.filhos[j+1];
                        pFilho.n++;
                    }
                    pIrmao.n = 0;   // aqui o endereço do irmão poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se a página for folha, copia o ponteiro para a próxima página
                    pFilho.proxima = pIrmao.proxima;
                    
                    // puxa os registros no pai
                    for(int j=diminuido; j<pa.n-1; j++) {
                        pa.chaves[j] = pa.chaves[j+1];
                        pa.dados[j] = pa.dados[j+1];
                        pa.filhos[j+1] = pa.filhos[j+2];
                    }
                    pa.n--;
                    diminuiu = pa.n<maxElementos/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Atualiza todos os registros
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            arquivo.seek(paginaFilho);
            arquivo.write(pFilho.getBytes());
            arquivo.seek(paginaIrmao);
            arquivo.write(pIrmao.getBytes());
        }
        return excluido;
    }
    
    
    // Imprime a árvore, usando uma chamada recursiva.
    // A função recursiva é chamada com uma página de referência (raiz)
    public void print() throws IOException {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        if(raiz!=-1)
            print1(raiz);
        System.out.println();
    }
    
    // Impressão recursiva
    private void print1(long pagina) throws IOException {
        
        // Retorna das chamadas recursivas
        if(pagina==-1)
            return;
        int i;

        // Lê o registro da página passada como referência no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.setBytes(buffer);
        
        // Imprime a página
        String endereco = String.format("%04d", pagina);
        System.out.print(endereco+"  " + pa.n +":"); // endereço e número de elementos
        for(i=0; i<maxElementos; i++) {
            System.out.print("("+String.format("%04d",pa.filhos[i])+") "+pa.chaves[i]+","+String.format("%2d",pa.dados[i])+" ");
        }
        System.out.print("("+String.format("%04d",pa.filhos[i])+")");
        if(pa.proxima==-1)
            System.out.println();
        else
            System.out.println(" --> ("+String.format("%04d", pa.proxima)+")");
        
        // Chama recursivamente cada filho, se a página não for folha
        if(pa.filhos[0] != -1) {
            for(i=0; i<pa.n; i++)
                print1(pa.filhos[i]);
            print1(pa.filhos[i]);
        }
    }
       
    
    // Apaga o arquivo do índice, para que possa ser reconstruído
    public void apagar() throws IOException {

        File f = new File(nomeArquivo);
        f.delete();

        arquivo = new RandomAccessFile(nomeArquivo,"rw");
        arquivo.writeLong(-1);  // raiz empty
    }
    
}
