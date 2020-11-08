package menu.api;

import menu.backend.cruds.abstracts.ComentariosInterface;
import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.genericos.FrontEnd;
import produtos.CelulaResposta;
import produtos.Comentario;

public class ComentariosAPI {
    
    private final byte TAM_MIN_COMENTARIO; // Tamanho minimo para as respostas
    private final byte TAM_MAX_COMENTARIO; // Tamanho maximo para as respostas
    
    private CustomInput customInput;
    private FrontEnd    frontEnd;

    public ComentariosAPI(byte TAM_MIN_COMENTARIO, byte TAM_MAX_COMENTARIO, FrontEnd frontEnd,
    CustomInput customInput) {
        this.TAM_MIN_COMENTARIO = TAM_MIN_COMENTARIO;
        this.TAM_MAX_COMENTARIO = TAM_MAX_COMENTARIO;
        
        this.frontEnd = frontEnd;
    }

    public ComentariosAPI(FrontEnd frontEnd, CustomInput customInput) {
        this.TAM_MIN_COMENTARIO = 1;
        this.TAM_MAX_COMENTARIO = 120;

        this.frontEnd = frontEnd;
        this.customInput = customInput;
    }

    public CelulaResposta comentarPR(ComentariosInterface comentarios, int idPR, int idUsuario, boolean ehResp) {
        CelulaResposta resultado    = new CelulaResposta();
        CodigoDeProtocolo confirmar = CodigoDeProtocolo.ERRO;
        char verificaBool           = ehResp ? 'R':'P';
        String comentario           = "";

        comentario = customInput.inserir("Insira a sua resposta", TAM_MIN_COMENTARIO, TAM_MAX_COMENTARIO, true);

        if (comentario.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setComentario(new Comentario(idUsuario, idPR, comentario));

        confirmar = frontEnd.verificar(resultado.getComentario());
        if (confirmar == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }
}

