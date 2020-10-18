package menu.usuario;

import produtos.*;
import menu.sistema.*;
import menu.sistema.abstracts.api.crudManagers.UsuarioInterface;
import menu.sistema.controle.CodigoDeProtocolo;
import menu.sistema.graficos.*;
import menu.sistema.input.CustomInput;
import menu.sistema.misc.Email;
import menu.sistema.misc.Regex;

/**
 * Classe para gerenciar todas as funções de controle de Usuário
 * @author MysteRys337 ( Gustavo Lopes )
 */
public class UsuariosAPI {

    private final byte       TAM_MIN_EMAIL; //Tamanho mínimo do email
    private final byte       TAM_MAX_EMAIL; //Tamanho máximo do email

    private final byte       TAM_MIN_NOME;  //Tamanho mínimo do nome
    private final byte       TAM_MAX_NOME;  //Tamanho máximo do nome

    private UsuariosFrontEnd usuariosFrontEnd;

    private CustomInput      customInput;

    public UsuariosAPI(byte TAM_MIN_EMAIL,byte TAM_MAX_EMAIL, byte TAM_MIN_NOME,byte TAM_MAX_NOME, UsuariosFrontEnd usuariosFrontEnd,CustomInput customInput) {

        this.TAM_MIN_EMAIL    = TAM_MIN_EMAIL;
        this.TAM_MAX_EMAIL    = TAM_MAX_EMAIL;

        this.TAM_MIN_NOME     = TAM_MIN_NOME;
        this.TAM_MAX_NOME     = TAM_MAX_NOME;

        this.usuariosFrontEnd = usuariosFrontEnd;

        this.customInput      = customInput;
    }

    /**
     * Função para tentar ao acessar o sistema
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta acessarAoSistema(UsuarioInterface usuarios) {
        String            email         = "";

        CelulaResposta    resultado     = new CelulaResposta();

        CodigoDeProtocolo acertouAsenha = CodigoDeProtocolo.ERRO;

        Usuario           usuarioAcesso = null;

        email = customInput.inserir("Insira o seu email",TAM_MIN_EMAIL,TAM_MAX_EMAIL,false);//lopinho35@gmail.com
        if(email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        usuarioAcesso = usuarios.achar(email);//lopinho35@gmail.com
        if(usuarioAcesso == null) {
            System.err.println("Erro! Esse email não possui conta");
            return resultado;
        }

        acertouAsenha = usuariosFrontEnd.inserirSenha(usuarioAcesso.getSenha(),3);
        if(acertouAsenha == CodigoDeProtocolo.ERRO) {
            System.err.println("Erro! Você passou todas as suas tentativas");
            return resultado;
        }
        else if ( acertouAsenha == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }
        
        resultado.setUsuario(usuarioAcesso);
        resultado.setCdp(CodigoDeProtocolo.MUDARUSUARIO);

        return resultado;
    }

    /**
     * Função para a criação de um novo usuário ao CRUD
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta criarNovoUsuario() {

        String email                        = "";
        String nome                         = "";
        String senha                        = "";

        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado            = new CelulaResposta();

        email = customInput.inserir("Insira o seu email",TAM_MIN_EMAIL,TAM_MAX_EMAIL,true);
        if (email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;

        } else if(APIControle.acharUsuario(email) != null ) {
            System.err.println("Erro! Esse email já possui uma conta registrada");
            return resultado;

        } else if(!Regex.verificarEmail(email)) {
            System.err.println("Erro! Esse email é inválido");
            return resultado;

        }

        nome = customInput.inserir("Insira o nome do usuário",TAM_MIN_NOME,TAM_MAX_NOME,true);
        if(nome.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        } 

        senha = usuariosFrontEnd.novaSenha();
        if(senha.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        } 

        resultado.setUsuario(new Usuario(nome,email,senha));
        confirmarOperacao = usuariosFrontEnd.verificar(resultado.getUsuario());

        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.getUsuario().setSenha(APIControle.hasheador.hash(senha));
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para registrar uma senha temporária ao usuário com um email que o mesmo inserir
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta criarSenhaTemporaria() {
        String email             = "";
        String senhaTemporaria   = "";

        CelulaResposta resultado = new CelulaResposta();

        email = customInput.inserir("Insira o seu email",TAM_MIN_EMAIL,TAM_MAX_EMAIL,false);
        if (email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return null;
        }
        if(APIControle.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return null;
        }

        senhaTemporaria = Regex.gerarSenha();
        resultado.setUsuario(APIControle.acharUsuario(email));
        Email.escreverEmail(senhaTemporaria, resultado.getUsuario().getNome());
        resultado.getUsuario().setSenha(APIControle.hasheador.hash(senhaTemporaria));

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        System.out.println("Um email foi enviado a você com a sua senha temporaria\n(Obs: Olhe a pasta do projeto)");

        return resultado;
    }

    /**
     * Função para o usuário registrar uma nova senha para ele mesmo
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public CelulaResposta criarNovaSenha(int idUsuario) {
        String            novaSenha     = "";
        CodigoDeProtocolo acertouAsenha = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado        = new CelulaResposta();

        resultado.setUsuario(APIControle.acharUsuario(idUsuario));
        acertouAsenha = usuariosFrontEnd.inserirSenha(resultado.getUsuario().getSenha(),3);

        if(acertouAsenha == CodigoDeProtocolo.ERRO) {
            System.err.println("Erro! Senhas não se correspondem!");
            return null;
        }
        else if (acertouAsenha == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return null;
        }

        novaSenha = usuariosFrontEnd.novaSenha();
        resultado.getUsuario().setSenha(APIControle.hasheador.hash(novaSenha));
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;

    }
    
}
