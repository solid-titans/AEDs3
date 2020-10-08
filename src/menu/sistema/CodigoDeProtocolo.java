package menu.sistema;

public enum CodigoDeProtocolo {

    ACESSOAOSISTEMA         ("1"),
    CRIARNOVOUSUARIO        ("2"),
    CRIARSENHATEMPORARIA    ("3"),
    CONSULTARPERGUNTAS      ("21"),
    OLHARNOTIFICACOES       ("31"),
    NOVASENHA               ("41"),
    LISTARPERGUNTAS         ("12"),
    NOVAPERGUNTA            ("22"),
    ALTERARPERGUNTA         ("32"),
    ARQUIVARPERGUNTA        ("42"),

    SUCESSO                 ("111"),
    ERRO                    ("000"),

    MUDARUSUARIO            ("222");

    private String codigo;

    CodigoDeProtocolo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return this.codigo;
    }
}
