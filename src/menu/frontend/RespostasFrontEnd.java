package menu.frontend;

import produtos.abstracts.*;
import menu.backend.cruds.abstracts.ComentariosInterface;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.frontend.genericos.FrontEndPlus;
import menu.frontend.graficos.*;

public class RespostasFrontEnd extends FrontEndPlus {

    private ComentariosFrontEnd comentariosFrontEnd;

    public RespostasFrontEnd(CustomPrint myPrint, CustomInput myInput, String name, ComentariosFrontEnd comentariosFrontEnd) {
        super(myPrint, myInput, name);
        this.comentariosFrontEnd = comentariosFrontEnd;
    }

    /**
     * Função para retornar todas as respostas em um array em formato String
     * 
     * @param array é o array de respostas que foi enviado
     * @return a String correspondente a listagem das respostas
     */
    public String listarGeral(int idUsuario, UsuarioInterface usuarios, VotosInterface votos, ComentariosInterface comentarios, RegistroVisualplus[] array) {

        String resp = "";
        String nome = "";

        byte contador = 1;

        resp += myPrint.imprimir("[" + (this.name + "s").toUpperCase() +  "]");

        for (RegistroVisualplus i : array) {
            if (i.getAtiva() == false)
                continue;

            nome = usuarios.achar(i.getIdUsuario()).getNome();

            resp += "\n" + myPrint.imprimir(contador + "." + i.imprimir(nome) + "\n\n");
            resp += votos.recuperarNota(idUsuario + "|R|" + i.getId()) + "\n\n";

            RegistroVisualplus[] comentariosDaResposta = comentarios.getComentarioArray(i.getId(),(byte)0);
            if ( comentariosDaResposta != null)
                resp += comentariosFrontEnd.listar(usuarios,comentariosDaResposta);
            else {
                resp += myPrint.imprimir("{Essa resposta não tem comentarios...}\n");
            }

            contador++;

        }

        return resp;
    }

}