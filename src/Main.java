import java.io.File;
import menu.Menu;
import menu.Selecao;
import menu.pergunta.PerguntasAPI;
import menu.pergunta.PerguntasFrontEnd;
import menu.resposta.RespostasAPI;
import menu.resposta.RespostasFrontEnd;
import menu.sistema.controle.APIControle;
import menu.sistema.controle.crudmanagers.PerguntasCRUD;
import menu.sistema.controle.crudmanagers.RespostasCRUD;
import menu.sistema.controle.crudmanagers.UsuariosCRUD;
import menu.sistema.graficos.ANSILibrary;
import menu.sistema.graficos.ASCIInterface;
import menu.sistema.input.CustomInput;
import menu.sistema.input.Input;
import menu.usuario.UsuariosAPI;
import menu.usuario.UsuariosFrontEnd;

public class Main {

    public static void main(String[] args) {

        // Verificar se o directorio de dados existe, caso nao exista crie-o
        File directorioDados = new File("Dados");
        if (!directorioDados.exists())
            directorioDados.mkdir();
        // Comum a todos
        Input input                         = new Input();
        ANSILibrary destaqueTamMax          = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
        ANSILibrary destaqueTamMin          = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
        ANSILibrary destaqueObs             = new ANSILibrary(15, 234, ANSILibrary.TEXTO_SUBLINHADO);
        ANSILibrary destaqueData            = new ANSILibrary(15, 232, ANSILibrary.TEXTO_SUBLINHADO);

        // Graficos
        ASCIInterface usuarioGraficos       = new ASCIInterface(92, 226, 93, 232);
        ASCIInterface perguntasGraficos     = new ASCIInterface(21, 111, 232, 15);
        ASCIInterface respostasGraficos     = new ASCIInterface(40, 10, 232, 15);

        // Definicoes
        CustomInput usuariosCustomInput     = new CustomInput(usuarioGraficos, destaqueTamMax, destaqueTamMin, destaqueObs);
        CustomInput perguntasCustomInput    = new CustomInput(perguntasGraficos, destaqueTamMax, destaqueTamMin,
                destaqueObs);

        // Interface
        UsuariosFrontEnd usuariosFrontEnd    = new UsuariosFrontEnd(usuarioGraficos, usuariosCustomInput);
        PerguntasFrontEnd perguntasFrontEnd  = new PerguntasFrontEnd(destaqueData,respostasGraficos, input);
        RespostasFrontEnd respostasFrontEnd  = new RespostasFrontEnd(destaqueData, respostasGraficos, input);

        // APIS
        UsuariosAPI usuariosAPI             = new UsuariosAPI(usuariosFrontEnd, usuariosCustomInput);
        PerguntasAPI perguntasAPI           = new PerguntasAPI(perguntasFrontEnd, perguntasCustomInput);
        RespostasAPI respostasAPI           = new RespostasAPI(respostasFrontEnd, perguntasCustomInput);

        // Cruds
        UsuariosCRUD usuariosCRUD           = new UsuariosCRUD();
        PerguntasCRUD perguntasCRUD         = new PerguntasCRUD("Dados");
        RespostasCRUD respostasCRUD         = new RespostasCRUD("Dados");

        // Menu
        ASCIInterface graficos              = new ASCIInterface(27,255 , 232, 232);
        Selecao selecao                     = new Selecao(graficos, input);
        APIControle minhaAPI                = new APIControle(usuariosAPI, perguntasAPI, respostasAPI, usuariosCRUD, perguntasCRUD,
                respostasCRUD);

        APIControle controle = new APIControle(usuariosAPI, perguntasAPI, respostasAPI, usuariosCRUD, perguntasCRUD,
                respostasCRUD);

        Menu m = new Menu(graficos, minhaAPI, selecao);
        m.Inicio();
    }

}
