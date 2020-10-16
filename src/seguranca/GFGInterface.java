package seguranca;

public interface GFGInterface {
    public String  hashearSenha(String senha);
    public boolean verificarSenha(String senha, String hashSenha);

}
