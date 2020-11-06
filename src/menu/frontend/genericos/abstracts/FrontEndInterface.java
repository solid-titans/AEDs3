package menu.frontend.genericos.abstracts;

import menu.backend.misc.CodigoDeProtocolo;
import produtos.abstracts.RegistroVisual;

public interface FrontEndInterface {
    public CodigoDeProtocolo verificar(RegistroVisual objeto);
}
