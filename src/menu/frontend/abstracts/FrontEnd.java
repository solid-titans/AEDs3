package menu.frontend.abstracts;

import menu.backend.misc.CodigoDeProtocolo;
import produtos.abstracts.RegistroVisual;

public interface FrontEnd {
    public CodigoDeProtocolo verificar(RegistroVisual objeto);
}
