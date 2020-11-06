import java.io.File;
import menu.*;
import menu.api.*;
import menu.backend.cruds.*;
import menu.backend.input.*;
import menu.frontend.*;
import menu.frontend.genericos.FrontEnd;
import menu.frontend.genericos.FrontEndPlus;
import menu.frontend.graficos.*;

public class Main {

    /**
     * Função maestro para orquestrar todos os objetos usados no programa
     * @param args
     */
    public static void main(String[] args) {

        // Verificar se o directorio de dados existe, caso nao exista crie-o
        File directorioDados = new File("Dados");
        if (!directorioDados.exists())
            directorioDados.mkdir();

        // Comum a todos
        Input       input                   = new Input();

        //Destaques
        ANSILibrary destaqueTam             = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
        ANSILibrary destaqueTitulo          = new ANSILibrary(15, 9, ANSILibrary.TEXTO_SUBLINHADO);
        ANSILibrary destaqueObs             = new ANSILibrary(15, 234, ANSILibrary.TEXTO_SUBLINHADO);
        ANSILibrary destaqueData            = new ANSILibrary(15, 232, ANSILibrary.TEXTO_SUBLINHADO);

        //Interfaces ASCII
        ASCIInterface usuariosGraficos      = new ASCIInterface(92, 226, 93, 232);
        ASCIInterface perguntasGraficos     = new ASCIInterface(21, 111, 232, 15);
        ASCIInterface respostasGraficos     = new ASCIInterface(40, 121, 232, 15);
        ASCIInterface votosGraficos         = new ASCIInterface(206, 104, 232, 15);

        //CustomPrints
        CustomPrint usuariosPrint           = new CustomPrint(usuariosGraficos,destaqueTitulo,destaqueObs);
        CustomPrint perguntasPrint          = new CustomPrint(perguntasGraficos,destaqueTitulo,destaqueObs);
        CustomPrint respostasPrint          = new CustomPrint(respostasGraficos,destaqueTitulo,destaqueObs);
        CustomPrint votosPrint              = new CustomPrint(votosGraficos,destaqueTitulo,destaqueObs);

        CustomPrint usuariosInputPrint      = new CustomPrint(usuariosGraficos,destaqueTitulo,destaqueTam);
        CustomPrint perguntasInputPrint     = new CustomPrint(perguntasGraficos,destaqueTitulo,destaqueTam);
        CustomPrint respostasInputPrint     = new CustomPrint(respostasGraficos,destaqueTitulo,destaqueTam);
        CustomPrint votosInputPrint         = new CustomPrint(votosGraficos,destaqueTitulo,destaqueTam);

        // Definicoes
        CustomInput usuariosCustomInput     = new CustomInput(usuariosInputPrint);
        CustomInput perguntasCustomInput    = new CustomInput(perguntasInputPrint);
        CustomInput respostasCustomInput    = new CustomInput(respostasInputPrint);
        CustomInput votosCustomInput        = new CustomInput(votosInputPrint);

        // Interface
        UsuariosFrontEnd  usuariosFrontEnd  = new UsuariosFrontEnd(usuariosPrint, usuariosCustomInput, "usuario");
        FrontEndPlus      perguntasFrontEnd = new FrontEndPlus(perguntasPrint, perguntasCustomInput , "pergunta");
        RespostasFrontEnd respostasFrontEnd = new RespostasFrontEnd(respostasPrint, respostasCustomInput, "resposta");
        FrontEnd          votosFrontEnd     = new FrontEnd(votosPrint, votosCustomInput, "voto");

        // APIS
        UsuariosAPI  usuariosAPI            = new UsuariosAPI(usuariosFrontEnd, usuariosCustomInput);
        PerguntasAPI perguntasAPI           = new PerguntasAPI(perguntasFrontEnd, perguntasCustomInput);
        RespostasAPI respostasAPI           = new RespostasAPI(respostasFrontEnd, respostasCustomInput);
        VotosAPI     votosAPI               = new VotosAPI(votosFrontEnd, votosCustomInput);

        // Cruds
        UsuariosCRUD  usuariosCRUD          = new UsuariosCRUD();
        PerguntasCRUD perguntasCRUD         = new PerguntasCRUD("Dados");
        RespostasCRUD respostasCRUD         = new RespostasCRUD("Dados");
        VotosCRUD     votosCRUD             = new VotosCRUD();

        // Menu
        ASCIInterface graficos              = new ASCIInterface(27,255 , 232, 232);
        CustomPrint   myPrint               = new CustomPrint(graficos,destaqueData,destaqueObs);

        Selecao       selecao               = new Selecao(myPrint, input);
        APIControle   minhaAPI              = new APIControle(usuariosAPI, perguntasAPI, respostasAPI, votosAPI, 
                                                              usuariosCRUD, perguntasCRUD,respostasCRUD, votosCRUD);

        //Iniciar ao menu
        Menu m = new Menu(myPrint, minhaAPI, selecao);
        m.Inicio(); //Começar efetivamente o programa
    }

}
