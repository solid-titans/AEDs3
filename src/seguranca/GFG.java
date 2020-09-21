import java.math.BigInteger;  
import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
// Java program to calculate SHA hash value  
  
class GFG {  
    public byte[] getSHA(String input) throws NoSuchAlgorithmException 
    {  
        // Static getInstance method is called with hashing SHA  
        MessageDigest md = MessageDigest.getInstance("SHA3-512");  
  
        // digest() method called  
        // to calculate message digest of an input  
        // and return array of byte 
        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
    } 
    
    public String toHexString(byte[] hash) 
    { 
        // Convert byte array into signum representation  
        BigInteger number = new BigInteger(1, hash);  
  
        // Convert message digest into hex value  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
  
        // Pad with leading zeros 
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
  
        return hexString.toString();  
    } 

    /**
     * Algoritmo de hashing simples para não guardar senha em plain text no banco de dados
     * 
     * @param senha Mandar a String da senha
     * @return Senha hasheada
     */
    public String senhaHasheada(String senha) {
        String hash = "";

        try {
            hash = this.toHexString(getSHA(senha));

        } catch(Exception e) { e.printStackTrace(); }

        return hash;
    }
}  