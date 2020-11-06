package menu.frontend.genericos.abstracts;

import produtos.abstracts.RegistroVisualplus;

public interface FrontEndplusInterface extends FrontEndInterface{
    public String listar(RegistroVisualplus[] objetos);
    public String listarSimplificado(RegistroVisualplus[] objetos);
    public int    escolher(RegistroVisualplus[] array);
}
