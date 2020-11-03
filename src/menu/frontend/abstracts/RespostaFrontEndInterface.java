package menu.frontend.abstracts;

import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.cruds.abstracts.VotosInterface;
import produtos.abstracts.RegistroVisualResposta;

public interface RespostaFrontEndInterface extends FrontEndplus {
    public String listarGeral(UsuarioInterface usuarios,VotosInterface votos,RegistroVisualResposta[] ov);
}
