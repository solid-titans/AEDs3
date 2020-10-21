package menu.frontend.abstracts;

import produtos.abstracts.RegistroVisualplus;

public interface FrontEndplus extends FrontEnd{
    public String listar(RegistroVisualplus[] objetos);
    public String listarSimplificado(RegistroVisualplus[] objetos);
}
