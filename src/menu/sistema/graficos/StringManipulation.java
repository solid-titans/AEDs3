package menu.sistema.graficos;

/**
 * Classe para gerenciar manipulação das String no contexto da criação de caixas de interface
 * @author MysteRys337 (Gustavo Lopes)
 */
class StringManipulation {
    
    /** Calcular quanto tamanho vertical(de linhas) é necessario para 
     ** colocar um array
     * 
     * @param array é o array de Strings com o conteúdo que o usuário quer inserir
     * @param largura é o tamanho máximo de linhas 
     * @return numero de linhas necessaria para colocar um array
     */
    public static int calcularTamanho(String[] array,int largura) {
        
        int tamanhoString = 0;
        int numDeArrays   = 1;
        
        for ( String i : array) {
            if (tamanhoString + i.length() < largura) {
                tamanhoString += i.length() + 1;
            }
            else {
                tamanhoString = i.length() + 1;
                numDeArrays++;
                
            }
        }
        return numDeArrays;
    }

}
