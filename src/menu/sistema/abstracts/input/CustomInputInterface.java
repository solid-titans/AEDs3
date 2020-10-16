package menu.sistema.abstracts.input;

public interface CustomInputInterface {

    public String inserir(String titulo,String observacao, int limMin, int limMax,boolean printRestr);
    public String inserir(String titulo,int limMin, int limMax,boolean printRestr);
    
}
