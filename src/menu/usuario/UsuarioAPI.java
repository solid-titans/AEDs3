package menu.usuario;

import produtos.*;
import menu.sistema.*;
import menu.sistema.graficos.*;
import seguranca.GFG;

/**
 * Classe para gerenciar todas as funções de controle de Usuário
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class UsuarioAPI {

    public static ASCIInterface graficos               = new ASCIInterface(202, 231 , 232, 184);

    private static final byte   TAMANHO_MINIMO_EMAIL   = 10;    //Tamanho mínimo do email
    private static final byte   TAMANHO_MAXIMO_EMAIL   = 50;    //Tamanho máximo do email

    private static final byte   TAMANHO_MINIMO_NOME    = 3;     //Tamanho mínimo do nome
    private static final byte   TAMANHO_MAXIMO_NOME    = 25;    //Tamanho máximo do nome

    /**
     * Função para tentar ao acessar o sistema
     * @return o ID do usuário que tentar acessar(Se o usuário conseguir acertar)
     */
    public static int acessarAoSistema() {
        int sucesso           = -1;
        String email          = "";

        boolean acertouAsenha = false;
        Usuario usuarioAcesso = null;

        email = Sistema.inserir(graficos,"Insira o seu email",TAMANHO_MINIMO_EMAIL,TAMANHO_MAXIMO_EMAIL,false);
        if(CrudAPI.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return -1;
        }
        usuarioAcesso = CrudAPI.acharUsuario(email);

        acertouAsenha = UsuariosFrontEnd.inserirSenha(usuarioAcesso.getSenha(),3);
        if(!acertouAsenha) {
            System.err.println("Erro! Você passou todas as suas tentativas");
            return -1;
        }
        
        sucesso = usuarioAcesso.getId();
        return sucesso;

    }

    /**
     * Função para a criação de um novo usuário ao CRUD
     * @return O novo usuário criado(Se o próprio usuário não cancelar o processo)
     */
    public static Usuario criarNovoUsuario() {

        String email                = "";
        String nome                 = "";
        String senha                = "";
        boolean confirmarOperacao   = false;
        Usuario novoUsuario         = null;

        //Criando novo usuario
        email                 = Sistema.inserir(graficos,"Insira o seu email",TAMANHO_MINIMO_EMAIL,TAMANHO_MAXIMO_EMAIL,true);

        if(CrudAPI.acharUsuario(email) != null ) {
            System.err.println("Erro! Esse email já possui uma conta registrada");
            return null;
        }
        else if(!Sistema.verificarEmail(email)) {
            System.err.println("Erro! Esse email é inválido");
            return null;
        }

        nome                  = Sistema.inserir(graficos,"Insira o nome do usuário",TAMANHO_MINIMO_NOME,TAMANHO_MAXIMO_NOME,true); 
        senha                 = UsuariosFrontEnd.novaSenha();

        novoUsuario           = new Usuario(nome,email,senha);
        confirmarOperacao     = UsuariosFrontEnd.verificar(novoUsuario);

        if(!confirmarOperacao) {
            return null;
        }
        novoUsuario.setSenha(new GFG().senhaHasheada(senha));

        return novoUsuario;
    }

    /**
     * Função para registrar uma senha temporária ao usuário com um email que o mesmo inserir
     * @return o usuário que precisa ter a nova senha temporará registrada
     */
    public static Usuario criarSenhaTemporaria() {
        String email           = "";
        String senhaTemporaria = "";

        Usuario usuario = null;

        email = Sistema.inserir(graficos,"Insira o seu email",TAMANHO_MINIMO_EMAIL,TAMANHO_MAXIMO_EMAIL,false);

        if(CrudAPI.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return null;
        }

        senhaTemporaria = Sistema.gerarSenha();
        usuario = CrudAPI.acharUsuario(email);
        Sistema.escreverEmail(senhaTemporaria, usuario.getNome());
        usuario.setSenha(new GFG().senhaHasheada(senhaTemporaria));

        System.out.println("Um email foi enviado a você com a sua senha temporaria\n(Obs: Olhe a pasta do projeto)");

        return usuario;
    }

    /**
     * Função para o usuário registrar uma nova senha para ele mesmo
     * @return O usuário com a nova senha integrada
     */
    public static Usuario criarNovaSenha(int idUsuario) {
        String novaSenha       = "";
        boolean acertouAsenha = false;

        Usuario usuarioAtual   = CrudAPI.acharUsuario(idUsuario);
        acertouAsenha = UsuariosFrontEnd.inserirSenha(usuarioAtual.getSenha(),3);

        if(!acertouAsenha) {
            System.err.println("Erro! Senhas não se correspondem!");
            return null;
        }

        novaSenha = UsuariosFrontEnd.novaSenha();
        usuarioAtual.setSenha(new GFG().senhaHasheada(novaSenha));

        return usuarioAtual;

    }
}
