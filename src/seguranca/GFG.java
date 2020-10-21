package seguranca;

import java.math.BigInteger;  
import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest;
import java.util.Random;  


// Programa para cifrar as senhas do banco de dados  
public class GFG implements GFGInterface {  
    private int numMaxInteracoes;  // Salvar o número máximo de interações do hash

    public GFG(int numMaxInteracoes) {
        // Salvar o número máximo de interações do hash
        this.numMaxInteracoes = numMaxInteracoes;

    }

    /**
     * Hashenado a senha com SHA3 de 512bits
     * @param entrada String para ser hasheada
     * @return String hasheada
     */
    private byte[] getSHA(String entrada) {
        byte[] mensagemDigerida = null;

        try {
            // Instânciando um algoritmo de hashing
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            
            // Digerindo a String de entrada
            // Em um array de bytes
            mensagemDigerida = md.digest(entrada.getBytes(StandardCharsets.UTF_8));
            
        } catch(Exception e) { e.printStackTrace(); }

        return mensagemDigerida;
    } 

    /**
     * Transformar um array de bytes e uma String
     * @param hash resultado de um hashing
     * @return String hexadecimal com o hash
     */
    private String toHexString(byte[] hash) {
        // Converter o byte array em uma representação numerica
        BigInteger numero = new BigInteger(1, hash);

        // Tranformar um vetor de bytes em um
        StringBuilder hexString = new StringBuilder(numero.toString(16));

        // Adicionando o pad com zeros caso a String não esteja completa
        while(hexString.length() < 32)
            hexString.insert(0 ,'0');

        return hexString.toString();
    }

    /**
     * Criando um bom salt para a senha
     * @return Salt
     */
    private String salting() {
        String salt = "";             // String contendo um bom salt
        Random random = new Random(); // Instancia de um random para gerar os valores necessários para o salting

        // Gerando um salt para a senha com 10 caracteres de tamanho
        for(int i = 0; i < 10; i++) {
            // Escolhendo algum caractere de letra que esteja na tabela ASCII
            char caractere = (char) (random.nextInt(52) + 48);

            // Concatenando na String o novo caractere
            salt += caractere;

        }

        return salt;
    }

    /**
     * Aleatorizando o número de interações do hash
     * @return Inteiro com o número de interações 
     */
    private int numInteracoes() {
        Random random = new Random();

        // Calcular numero de interacoes
        int minInteracoes = this.numMaxInteracoes / 2;
        int numInteracoes = random.nextInt(this.numMaxInteracoes);

        if(numInteracoes < minInteracoes)
            numInteracoes += minInteracoes;

        // Retornar o número de interações do hash
        return numInteracoes;
    }

    /**
     * Gerar o hash da senha
     * @param salt Receber o salt da senha
     * @param numInteracoes Receber o numero de interações 
     * @return Hashing final da senha
     */
    private String gerarHash(String salt, int numInteracoes, String senha) {
        String hash = senha;

        for(int i = 0; i < numInteracoes; i++) 
            hash = this.toHexString(this.getSHA(salt + hash));  // Hasheando a senha com salt a cada interação do loop       

        return hash;
    }

    /**
     * Criando o hash da senha
     * @param senha Senha para ser hasheada
     * @return String contendo todos os itens da senha hash, interações e o hash da senha
     */
    @Override
    public String hashearSenha(String senha) {
        // Escrever o hash
        String senhaCompleta = ""; 

        // Montando todos os itens para gerar uma senha forte
        String salt       = this.salting();
        int numInteracoes = this.numInteracoes();
        String hash       = this.gerarHash(salt, numInteracoes, senha);

        // Montando a String com o hash totalmente composto
        senhaCompleta = salt + "$" + numInteracoes + "$" + hash;

        return senhaCompleta;
    }

    /**
     * Função para verificar se um hash e uma senha são iguais
     * @param senha Receber a String da senha
     * @param hashSenha Receber um hash gerado pela função GFG.hash(senha)
     * @return Verificar se a senha passada e o hashCompleto batem 
     */
    @Override
    public boolean verificarSenha(String senha, String hashSenha) {
        boolean senhaCorreta = false;

        // Separando elementos da String do hash
        String[] elementosHash = hashSenha.split("[$]");

        // String[0] -> Salt
        String salt    = elementosHash[0];
        // String[1] -> Interações
        int interacoes = Integer.parseInt(elementosHash[1]);
        // String[2] -> Hash
        String hash    = elementosHash[2];

        // Refazendo o hash com os valores iniciais
        String senhaTestando = senha;
        for(int i = 0; i < interacoes; i++) 
            senhaTestando = this.toHexString(this.getSHA(salt + senhaTestando));

        // Verificando se as Strings da senha do usuário e a do banco de dados são iguais
        if(hash.equals(senhaTestando))
            senhaCorreta = true;

        return senhaCorreta;
    }
}