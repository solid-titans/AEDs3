package menu.api;

import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.VotosFrontEnd;
import produtos.CelulaResposta;
import produtos.Voto;

public class VotosAPI {
    CustomInput   entrada;
    VotosFrontEnd frontEnd;

    public VotosAPI(VotosFrontEnd frontEnd, CustomInput entrada) {
        this.entrada  = entrada;
        this.frontEnd = frontEnd;
    }

    public CelulaResposta votarPR(VotosInterface votos, int idPR, int idUsuario, boolean ehResp) {
        CelulaResposta resultado = new CelulaResposta();
        char verificaBool = ehResp ? 'R':'P';

        //System.out.println(votos.achar(idUsuario + "|" + verificaBool + "|" + idPR));
        System.out.println(idUsuario + "|" + verificaBool + "|" + idPR);

        if(votos.achar(idUsuario + "|" + verificaBool + "|" + idPR) != null) {
            resultado.setCdp(CodigoDeProtocolo.ERRO);

        } else {
            String indice = entrada.inserir("Insira o seu indice: ", "Obs: Insira positivo(P) ou negativo(N)", 1, 8, false);
            Voto voto = new Voto((byte)1, idUsuario, idPR);

            if(indice.equals("positivo") || indice.equals("P")) {
                voto.setVoto(true);
                resultado.setVoto(voto);
                resultado.setCdp(CodigoDeProtocolo.SUCESSO);

            } else if(indice.equals("negativo") || indice.equals("N")) {
                voto.setVoto(false);
                resultado.setVoto(voto);
                resultado.setCdp(CodigoDeProtocolo.SUCESSO);

            } else if(indice.equals("")) {
                resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            
            } else {
                resultado.setCdp(CodigoDeProtocolo.ERRO);
            }         
        }

        return resultado;
    }
}
