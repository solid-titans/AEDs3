package menu.usuario;

import produtos.*;
import menu.sistema.*;
import menu.sistema.graficos.*;

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
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public static CelulaResposta acessarAoSistema() {
        String email                    = "";

        CelulaResposta resultado        = new CelulaResposta();

        CodigoDeProtocolo acertouAsenha = CodigoDeProtocolo.ERRO;

        Usuario           usuarioAcesso = null;

        email = Sistema.inserir(graficos,"Insira o seu email",TAMANHO_MINIMO_EMAIL,TAMANHO_MAXIMO_EMAIL,false);
        if(email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }
        else if(CrudAPI.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return resultado;
        }

        usuarioAcesso = CrudAPI.acharUsuario(email);

        acertouAsenha = UsuariosFrontEnd.inserirSenha(usuarioAcesso.getSenha(),3);
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
    public static CelulaResposta criarNovoUsuario() {

        String email                        = "";
        String nome                         = "";
        String senha                        = "";

        CodigoDeProtocolo confirmarOperacao = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado            = new CelulaResposta();

        email = Sistema.inserir(graficos,"Insira o seu email",TAMANHO_MINIMO_EMAIL,TAMANHO_MAXIMO_EMAIL,true);
        if (email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;

        } else if(CrudAPI.acharUsuario(email) != null ) {
            System.err.println("Erro! Esse email já possui uma conta registrada");
            return resultado;

        } else if(!Sistema.verificarEmail(email)) {
            System.err.println("Erro! Esse email é inválido");
            return resultado;

        }

        nome = Sistema.inserir(graficos,"Insira o nome do usuário",TAMANHO_MINIMO_NOME,TAMANHO_MAXIMO_NOME,true);
        if(nome.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        } 

        senha = UsuariosFrontEnd.novaSenha();
        if(senha.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        } 

        resultado.setUsuario(new Usuario(nome,email,senha));
        confirmarOperacao     = UsuariosFrontEnd.verificar(resultado.getUsuario());

        if(confirmarOperacao == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return resultado;
        }

        resultado.getUsuario().setSenha(CrudAPI.hasheador.hash(senha));
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;
    }

    /**
     * Função para registrar uma senha temporária ao usuário com um email que o mesmo inserir
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public static CelulaResposta criarSenhaTemporaria() {
        String email             = "";
        String senhaTemporaria   = "";

        CelulaResposta resultado = new CelulaResposta();

        email = Sistema.inserir(graficos,"Insira o seu email",TAMANHO_MINIMO_EMAIL,TAMANHO_MAXIMO_EMAIL,false);
        if (email.equals("")) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return null;
        }
        if(CrudAPI.acharUsuario(email) == null) {
            System.err.println("Erro! E-mail inválido");
            return null;
        }

        senhaTemporaria = Sistema.gerarSenha();
        resultado.setUsuario(CrudAPI.acharUsuario(email));
        Sistema.escreverEmail(senhaTemporaria, resultado.getUsuario().getNome());
        resultado.getUsuario().setSenha(CrudAPI.hasheador.hash(senhaTemporaria));

        resultado.setCdp(CodigoDeProtocolo.SUCESSO);
        System.out.println("Um email foi enviado a você com a sua senha temporaria\n(Obs: Olhe a pasta do projeto)");

        return resultado;
    }

    /**
     * Função para o usuário registrar uma nova senha para ele mesmo
     * @return uma CelulaResposta com os resultados da operação armazenada
     */
    public static CelulaResposta criarNovaSenha(int idUsuario) {
        String            novaSenha     = "";
        CodigoDeProtocolo acertouAsenha = CodigoDeProtocolo.ERRO;

        CelulaResposta resultado        = new CelulaResposta();

        resultado.setUsuario(CrudAPI.acharUsuario(idUsuario));
        acertouAsenha = UsuariosFrontEnd.inserirSenha(resultado.getUsuario().getSenha(),3);

        if(acertouAsenha == CodigoDeProtocolo.ERRO) {
            System.err.println("Erro! Senhas não se correspondem!");
            return null;
        }
        else if (acertouAsenha == CodigoDeProtocolo.OPERACAOCANCELADA) {
            resultado.setCdp(CodigoDeProtocolo.OPERACAOCANCELADA);
            return null;
        }

        novaSenha = UsuariosFrontEnd.novaSenha();
        resultado.getUsuario().setSenha(CrudAPI.hasheador.hash(novaSenha));
        resultado.setCdp(CodigoDeProtocolo.SUCESSO);

        return resultado;

    }
    
}
