package menu.sistema.graficos;

class StringManipulation {
    
    /** Calcular quanto tamanho vertical(de linhas) é necessario para 
     ** colocar um array
     * 
     * @param array
     * @param largura
     * @return numero de linhas necessaria para colocar um array
     */
    public int calcularTamanho(String[] array,int largura) {
        
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
