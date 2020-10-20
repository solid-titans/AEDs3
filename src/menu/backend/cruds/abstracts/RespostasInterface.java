package menu.backend.cruds.abstracts;

import produtos.Resposta;

public interface RespostasInterface {
    public int        inserir(Resposta resposta, int idPergunta);
    public Resposta   achar(int id);
    public void       atualizar(Resposta resposta);
    public Resposta[] getRespostaArrayUser(int idUsuario);
    public Resposta[] getRespostaArrayGeral(int idPergunta);
}
