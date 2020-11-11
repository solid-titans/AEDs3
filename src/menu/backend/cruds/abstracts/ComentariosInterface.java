package menu.backend.cruds.abstracts;

import produtos.Comentario;

public interface ComentariosInterface {
    public int inserirPergunta(Comentario comentario, int idPergunta);
    public int inserirResposta(Comentario comentario, int idResposta);
    public Comentario achar(int idComentario);
    public Comentario[] getComentarioArray(int idPergunta);
}
