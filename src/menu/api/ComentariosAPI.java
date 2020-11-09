package menu.api;

import menu.backend.cruds.abstracts.ComentariosInterface;
import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.genericos.FrontEnd;
import menu.frontend.genericos.FrontEndPlus;
import produtos.CelulaResposta;
import produtos.Comentario;

public class ComentariosAPI {
    
    private final byte TAM_MIN_COMENTARIO; // Tamanho minimo para as respostas
    private final byte TAM_MAX_COMENTARIO; // Tamanho maximo para as respostas
    
    private CustomInput  customInput;
    private FrontEndPlus frontEndPlus;

    public ComentariosAPI(byte TAM_MIN_COMENTARIO, byte TAM_MAX_COMENTARIO, FrontEndPlus frontEndPlus,
    CustomInput customInput) {
        this.TAM_MIN_COMENTARIO = TAM_MIN_COMENTARIO;
        this.TAM_MAX_COMENTARIO = TAM_MAX_COMENTARIO;
        
        this.frontEndPlus = frontEndPlus;
    }

    public ComentariosAPI(FrontEndPlus frontEndPlus, CustomInput customInput) {
        this.TAM_MIN_COMENTARIO = 1;
        this.TAM_MAX_COMENTARIO = 120;

        this.frontEndPlus = frontEndPlus;
        this.customInput = customInput;
    }

    public CelulaResposta comentarPR(ComentariosInterface comentarios, int idPR, int idUsuario) {
        CelulaResposta resultado    = new CelulaResposta();
        CodigoDeProtocolo confirmar = CodigoDeProtocolo.ERRO;
        String comentario           = "";

        comentario = customInput.inserir("Insira a sua resposta", TAM_MIN_COMENTARIO, TAM_MAX_COMENTARIO, true);

        if (comentario.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setComentario(new Comentario(idUsuario, idPR, comentario));

        confirmar = frontEndPlus.verificar(resultado.getComentario());
        if (confirmar == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    public CelulaResposta listarComentariosDaPergunta(ComentariosInterface comentarios, int idPergunta, int idUsuario) {

        CelulaResposta resultado = new CelulaResposta();
        Comentario[] array = comentarios.getComentarioArray(idPergunta);

        if (array == null) {
            System.err.println("\n\n                   ¯\\_(ツ)_/¯");
            System.err.println("Ops.. parece que ninguém submeteu uma resposta a essa pergunta...\n");

        } else {
            System.out.println(frontEndPlus.listar(array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }

        return resultado;
    }

}

