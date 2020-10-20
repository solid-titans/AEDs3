package menu.api;

import produtos.CelulaResposta;
import produtos.Usuario;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.input.CustomInput;
import menu.backend.misc.CodigoDeProtocolo;
import menu.backend.misc.Email;
import menu.backend.misc.Regex;
import menu.frontend.UsuariosFrontEnd;

/**
 * Classe para gerenciar todas as funções de controle de Usuário
 * 
 * @author MysteRys337 ( Gustavo Lopes )
 * @version 0.0.2
 */
public class UsuariosAPI {

    private final byte TAM_MIN_EMAIL; // Tamanho mínimo do email
    private final byte TAM_MAX_EMAIL; // Tamanho máximo do email

    private final byte TAM_MIN_NOME; // Tamanho mínimo do nome
    private final byte TAM_MAX_NOME; // Tamanho máximo do nome

    private UsuariosFrontEnd usuariosFrontEnd;

    private CustomInput customInput;

    public UsuariosAPI(byte TAM_MIN_EMAIL, byte TAM_MAX_EMAIL, byte TAM_MIN_NOME, byte TAM_MAX_NOME,
            UsuariosFrontEnd usuariosFrontEnd, CustomInput customInput) {

        this.TAM_MIN_EMAIL = TAM_MIN_EMAIL;
        this.TAM_MAX_EMAIL = TAM_MAX_EMAIL;

        this.TAM_MIN_NOME = TAM_MIN_NOME;
        this.TAM_MAX_NOME = TAM_MAX_NOME;

        this.usuariosFrontEnd = usuariosFrontEnd;

        this.customInput = customInput;
    }

    public UsuariosAPI(UsuariosFrontEnd usuariosFrontEnd, CustomInput customInput) {

        this.TAM_MIN_EMAIL    = 10;
        this.TAM_MAX_EMAIL    = 127;

        this.TAM_MIN_NOME     = 4;
        this.TAM_MAX_NOME     = 70;

        this.usuariosFrontEnd = usuariosFrontEnd;
        this.customInput      = customInput;
    }

    /**
     * Função para tentar ao acessar o sistema
     * 
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta acessarAoSistema(UsuarioInterface usuarios) {
        String email = "";

        CelulaResposta resultado = new CelulaResposta();

        CodigoDeProtocolo acertouAsenha = CodigoDeProtocolo.ERRO;

        Usuario usuarioAcesso = null;

        email = customInput.inserir("Insira o seu email", TAM_MIN_EMAIL, TAM_MAX_EMAIL, false);// lopinho35@gmail.com
        if (email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        usuarioAcesso = usuarios.achar(email);// lopinho35@gmail.com
        if (usuarioAcesso == null) {
            System.err.println("Erro! Esse email não possui conta");
            return resultado;
        }

        acertouAsenha = usuariosFrontEnd.inserirSenha(usuarios, usuarioAcesso.getSenha(), 3);
        if (acertouAsenha == CodigoDeProtocolo.ERRO) {
            System.err.println("Erro! Você passou todas as suas tentativas");
            return resultado;
        } else if (acertouAsenha == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setUsuario(usuarioAcesso);
        resultado.setCdp(CodigoDeProtocolo.MUDARUSUARIO);

        return resultado;
    }

    /**
     * Função para a criação de um novo usuário ao CRUD
     * 
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta criarNovoUsuario(UsuarioInterface usuarios) {

        String email = "";
        String nome = "";
        String senha = "";

        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado = new CelulaResposta();

        email = customInput.inserir("Insira o seu email", TAM_MIN_EMAIL, TAM_MAX_EMAIL, true);
        if (email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;

        } else if (usuarios.achar(email) != null) {
            System.err.println("Erro! Esse email já possui uma conta registrada");
            return resultado;

        } else if (!Regex.verificarEmail(email)) {
            System.err.println("Erro! Esse email é inválido");
            return resultado;

        }

        nome = customInput.inserir("Insira o nome do usuário", TAM_MIN_NOME, TAM_MAX_NOME, true);
        if (nome.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        senha = usuariosFrontEnd.novaSenha();
        if (senha.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setUsuario(new Usuario(nome, email, senha));
        confirmarOperacao = usuariosFrontEnd.verificar(resultado.getUsuario());

        if (confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para registrar uma senha temporária ao usuário com um email que o
     * mesmo inserir
     * 
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta criarSenhaTemporaria(UsuarioInterface usuarios) {
        String email = "";
        String senhaTemporaria = "";
        Usuario usuario = null;

        CelulaResposta resultado = new CelulaResposta();

        email = customInput.inserir("Insira o seu email", TAM_MIN_EMAIL, TAM_MAX_EMAIL, false);
        if (email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            customInput.esperarUsuario();
            return resultado;
        }

        usuario = usuarios.achar(email);

        if (usuario == null) {
            System.err.println("Erro! E-mail inválido");
            return resultado;
        }

        senhaTemporaria = Regex.gerarSenha();
        resultado.setUsuario(usuario);
        Email.escreverEmail(senhaTemporaria, resultado.getUsuario().getNome());
        resultado.getUsuario().setSenha(senhaTemporaria);

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        System.out.println("Um email foi enviado a você com a sua senha temporaria\n(Obs: Olhe a pasta do projeto)");

        return resultado;
    }

    /**
     * Função para o usuário registrar uma nova senha para ele mesmo
     * 
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta criarNovaSenha(UsuarioInterface usuarios, int idUsuario) {
        String novaSenha = "";
        CodigoDeProtocolo acertouAsenha = CodigoDeProtocolo.ERRO;
        CelulaResposta resultado = new CelulaResposta();

        Usuario usuario = usuarios.achar(idUsuario);

        acertouAsenha = usuariosFrontEnd.inserirSenha(usuarios, usuario.getSenha(), 3);

        if (acertouAsenha == CodigoDeProtocolo.ERRO) {
            System.err.println("Erro! Senhas não se correspondem!");
            return resultado;

        } else if (acertouAsenha == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.setUsuario(usuario);

        novaSenha = usuariosFrontEnd.novaSenha();
        resultado.getUsuario().setSenha(novaSenha);
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;

    }

}
