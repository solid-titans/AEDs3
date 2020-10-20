package menu.frontend.abstracts;

import menu.backend.cruds.abstracts.UsuarioInterface;
import produtos.abstracts.RegistroVisualResposta;

public interface RespostaFrontEndInterface extends FrontEndplus {
    public String listarGeral(UsuarioInterface usuarios,RegistroVisualResposta[] ov);
}
