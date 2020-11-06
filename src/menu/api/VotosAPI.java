package menu.api;

import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.genericos.FrontEnd;
import produtos.CelulaResposta;
import produtos.Voto;

public class VotosAPI {
    CustomInput   entrada;
    FrontEnd frontEnd;

    public VotosAPI(FrontEnd frontEnd, CustomInput entrada) {
        this.entrada  = entrada;
        this.frontEnd = frontEnd;
    }

    public CelulaResposta votarPR(VotosInterface votos, int idPR, int idUsuario, boolean ehResp) {
        CelulaResposta resultado    = new CelulaResposta();
        CodigoDeProtocolo confirmar = CodigoDeProtocolo.ERRO;
        char verificaBool           = ehResp ? 'R':'P';
        Voto voto                   = null;

        if(votos.achar(idUsuario + "|" + verificaBool + "|" + idPR) != null) {
            System.out.println("ATENÇÃO! Você já avaliou essa " + (ehResp ? "Resposta" : "Pergunta") );
            resultado.setCdp(CodigoDeProtocolo.ERRO);

        } else {
            String indice = entrada.inserir("Insira o seu voto: ", "Obs: Insira positivo(P) ou negativo(N)", 1, 8, false);
            voto = new Voto((byte)1, idUsuario, idPR);

            indice = indice.toLowerCase();

            if(indice.equals("positivo") || indice.equals("p")) {
                voto.setVoto(true);

            } else if(indice.equals("negativo") || indice.equals("n")) {
                voto.setVoto(false);

            } else if(indice.equals("")) {
                resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            
            } else {
                resultado.setCdp(CodigoDeProtocolo.ERRO);
                return resultado;
            }     

            if ( resultado.getCdp() != CodigoDeProtocolo.OPERACAOCANCELADA) {
                confirmar = frontEnd.verificar(voto);
                resultado.setCdp(confirmar);
                resultado.setVoto(voto);
                
            }    
        }

        return resultado;
    }
}
