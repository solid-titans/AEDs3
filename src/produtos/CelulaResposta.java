package produtos;

import menu.backend.misc.CodigoDeProtocolo;

/**
 * Classe criada para armazenar respostas do CrudAPI
 * 
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class CelulaResposta {

    private Usuario usuario;
    private Pergunta pergunta;
    private Resposta resposta;
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

    public CodigoDeProtocolo getCdp() {
        return this.cdp;
    }

    public void setCdp(CodigoDeProtocolo cdp) {
        this.cdp = cdp;
    }

    public CelulaResposta() {
        this.usuario  = null;
        this.pergunta = null;
        this.resposta = null;
        cdp           = CodigoDeProtocolo.ERRO;
    }

    public CelulaResposta(Usuario usuario, Pergunta pergunta, Resposta resposta, CodigoDeProtocolo codigoDeProtocolo) {
        this.usuario  = usuario;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.cdp      = codigoDeProtocolo;
    }

}
