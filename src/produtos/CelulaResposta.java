package produtos;

import menu.backend.misc.CodigoDeProtocolo;

/**
 * Classe criada para armazenar respostas do CrudAPI
 * 
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class CelulaResposta {

    private Usuario           usuario;
    private Pergunta          pergunta;
    private Resposta          resposta;
    private Voto              voto;
    private Comentario        comentario;
    private CodigoDeProtocolo cdp;

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pergunta getPergunta() {
        return this.pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public Resposta getResposta() {
        return this.resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }

    public Voto getVoto() {
        return this.voto;
    }

    public void setVoto(Voto voto) {
        this.voto = voto;
    }

    public Comentario getComentario() {
        return this.comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    public CodigoDeProtocolo getCdp() {
        return this.cdp;
    }

    public void setCdp(CodigoDeProtocolo cdp) {
        this.cdp = cdp;
    }

    public CelulaResposta() {
        this.usuario    = null;
        this.pergunta   = null;
        this.resposta   = null;
        this.voto       = null;
        this.comentario = null;
        cdp             = CodigoDeProtocolo.ERRO;
    }

    public CelulaResposta(Usuario usuario, Pergunta pergunta, Resposta resposta, Voto voto, Comentario comentario, CodigoDeProtocolo codigoDeProtocolo) {
        this.usuario    = usuario;
        this.pergunta   = pergunta;
        this.resposta   = resposta;
        this.voto       = voto;
        this.comentario = comentario;
        this.cdp        = codigoDeProtocolo;
    }

}
