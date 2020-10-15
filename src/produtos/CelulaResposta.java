package produtos;

import menu.sistema.CodigoDeProtocolo;

/**
 * Classe criada para armazenar respostas do CrudAPI
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class CelulaResposta {
    
    private Usuario u;
    private Pergunta p;
    private Resposta r;
    private CodigoDeProtocolo cdp;

    public Usuario getUsuario() {
		return this.u;
	}

    public void setUsuario(Usuario u ) {
		this.u = u;
	}

    public Pergunta getPergunta() {
		return this.p;
	}

    public void setPergunta(Pergunta p) {
		this.p = p;
	}

    public Resposta getResposta() {
		return this.r;
	}

    public void setResposta(Resposta r) {
		this.r = r;
	}

    public CodigoDeProtocolo getCdp() {
        return this.cdp;
    }

    public void setCdp(CodigoDeProtocolo cdp) {
        this.cdp = cdp;
    }

    public CelulaResposta() {
        u   = null;
        p   = null;
        r   = null;
        cdp = CodigoDeProtocolo.ERRO;
    }

    public CelulaResposta(Usuario usuario, Pergunta pergunta, Resposta resposta, CodigoDeProtocolo codigoDeProtocolo) {
        u   = usuario;
        p   = pergunta;
        r   = resposta;
        cdp = codigoDeProtocolo;        
    }

}
