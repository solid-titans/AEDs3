package menu.api;

import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.frontend.VotosFrontEnd;
import produtos.CelulaResposta;

public class VotosAPI {
    CustomInput   entrada;
    VotosFrontEnd frontEnd;

    public VotosAPI(CustomInput entrada, VotosFrontEnd frontEnd) {
        this.entrada  = entrada;
        this.frontEnd = frontEnd;
    }

    public CelulaResposta votarPergunta(VotosInterface votos,int idPergunta, int idUsuario) {
        return null;
    }

    public CelulaResposta votarResposta(VotosInterface votos,int idPergunta, int idUsuario) {
        return null;
    }

}
