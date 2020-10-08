package menu.usuario;

import produtos.*;
import menu.sistema.*;
import menu.sistema.graficos.*;
import seguranca.GFG;

public class UsuarioAPI {

    public static ASCIInterface graficos             = new ASCIInterface(202, 231 , 232, 184);
    private static final byte TAMANHO_MINIMO_EMAIL   = 10;
    private static final byte TAMANHO_MINIMO_NOME    = 3;

    public static int acessarAoSistema() {
        int sucesso           = -1;
        String email          = "";
        String senha          = "";

        Usuario usuarioAcesso = null;

        email = Sistema.inserir(graficos,"INSIRA SEU E-MAIL",TAMANHO_MINIMO_EMAIL);
        if(CrudAPI.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return -1;
        }
        usuarioAcesso = CrudAPI.acharUsuario(email);
        senha = new GFG().senhaHasheada(Sistema.inserir(graficos,"INSIRA SUA SENHA",0));

        if(senha.equals(usuarioAcesso.getSenha()));
            sucesso = usuarioAcesso.getId();

        return sucesso;

    }

    public static Usuario criarNovoUsuario() {

        String email                = "";
        String nome                 = "";
        String senha                = "";
        boolean confirmarOperacao   = false;
        Usuario novoUsuario         = null;

        //Criando novo usuario
        email                 = Sistema.inserir(graficos,"INSIRA O SEU E-MAIL",TAMANHO_MINIMO_EMAIL);

        if(!Sistema.verificarEmail(email) && CrudAPI.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return null;
        }

        nome                  = Sistema.inserir(graficos,"INSIRA UM NOME",TAMANHO_MINIMO_NOME); 
        senha                 = UsuariosFrontEnd.novaSenha("CRIANDO SENHA");

        novoUsuario           = new Usuario(nome,email,senha);
        confirmarOperacao     = UsuariosFrontEnd.verificar(novoUsuario);

        if(confirmarOperacao) {

            novoUsuario.setSenha(new GFG().senhaHasheada(senha));
        }

        return novoUsuario;
    }

    public static Usuario criarSenhaTemporaria() {
        String email           = "";
        String senhaTemporaria = "";

        Usuario usuario = null;

        email = Sistema.inserir(graficos,"INSIRA SEU E-MAIL",TAMANHO_MINIMO_EMAIL);

        if(CrudAPI.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return null;
        }

        senhaTemporaria = Sistema.gerarSenha();
        usuario = CrudAPI.acharUsuario(email);
        Sistema.escreverEmail(senhaTemporaria, usuario.getNome());
        usuario.setSenha(new GFG().senhaHasheada(Sistema.gerarSenha()));

        System.out.println("Um email foi enviado a você com a sua senha temporaria\n(Obs: Olhe a pasta do projeto)");

        return usuario;
    }

    public static Usuario criarNovaSenha(int idUsuario) {
        String senhaAtual      = "";
        String novaSenha       = "";

        Usuario usuarioAtual   = CrudAPI.acharUsuario(idUsuario);
        senhaAtual = new GFG().senhaHasheada(Sistema.inserir(graficos,"INSIRA SUA SENHA ATUAL",0));

        if(!senhaAtual.equals(usuarioAtual.getSenha())) {
            System.err.println("Erro! Senhas não se correspondem!");
            return null;
        }

        novaSenha = UsuariosFrontEnd.novaSenha("CRIANDO SENHA");
        usuarioAtual.setSenha(new GFG().senhaHasheada(novaSenha));

        return usuarioAtual;

    }
}
