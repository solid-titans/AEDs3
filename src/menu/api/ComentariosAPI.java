package menu.api;

import menu.backend.cruds.abstracts.ComentariosInterface;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.frontend.ComentariosFrontEnd;
import produtos.CelulaResposta;
import produtos.Comentario;
//import produtos.Comentario.tipo;

public class ComentariosAPI {
    
    private final byte TAM_MIN_COMENTARIO; // Tamanho minimo para as respostas
    private final byte TAM_MAX_COMENTARIO; // Tamanho maximo para as respostas
    
    private CustomInput  customInput;
    private ComentariosFrontEnd frontEnd;

    public ComentariosAPI(byte TAM_MIN_COMENTARIO, byte TAM_MAX_COMENTARIO, ComentariosFrontEnd frontEnd,
    CustomInput customInput) {
        this.TAM_MIN_COMENTARIO = TAM_MIN_COMENTARIO;
        this.TAM_MAX_COMENTARIO = TAM_MAX_COMENTARIO;
        
        this.frontEnd = frontEnd;
    }

    public ComentariosAPI(ComentariosFrontEnd frontEnd, CustomInput customInput) {
        this.TAM_MIN_COMENTARIO = 1;
        this.TAM_MAX_COMENTARIO = 120;

        this.frontEnd = frontEnd;
        this.customInput = customInput;
    }

    public CelulaResposta comentarPR(ComentariosInterface comentarios, int idPR, int idUsuario, byte tipo) {
        CelulaResposta resultado    = new CelulaResposta();
        CodigoDeProtocolo confirmar = CodigoDeProtocolo.ERRO;
        String comentario           = "";

        comentario = customInput.inserir("Insira a seu comentario", TAM_MIN_COMENTARIO, TAM_MAX_COMENTARIO, true);

        if (comentario.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setComentario(new Comentario(idUsuario, idPR, comentario, tipo));

        confirmar = frontEnd.verificar(resultado.getComentario());
        if (confirmar == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    public CelulaResposta listarComentariosDaPergunta(ComentariosInterface comentarios, UsuarioInterface usuarios,  int idPergunta) {

        CelulaResposta resultado = new CelulaResposta();
        Comentario[] array = comentarios.getComentarioArray(idPergunta,(byte)1);

        if (array == null) {
            System.err.println("\n\n                   ¯\\_(ツ)_/¯");
            System.err.println("Ops.. parece que ninguém submeteu um comentario a essa pergunta...\n");

        } else {
            System.out.println(frontEnd.listar(usuarios, array));
            resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        }

        return resultado;
    }

}

