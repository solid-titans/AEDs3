package menu.sistema.abstracts.frontend;

import menu.sistema.abstracts.api.crudmanagers.UsuarioInterface;

public interface RespostaFrontEndInterface extends FrontEndplus {
    public String listarGeral(UsuarioInterface usuarios,RegistroVisualResposta[] ov);
}
