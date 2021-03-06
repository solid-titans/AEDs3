package menu.backend.cruds.abstracts;

import produtos.Pergunta;

public interface PerguntaInterface {
    public int        inserir(Pergunta pergunta, int idUsuario);
    public Pergunta   achar(int id);
    public void       atualizar(Pergunta novo);
    public void       desativar(Pergunta pergunta);
    public void       remover(Pergunta pergunta);
    public Pergunta[] getPerguntaArray(int idUsuario);
    public Pergunta[] getPerguntasPalavrasChave(String[] palavrasChave,int idUsuario);
    
}
