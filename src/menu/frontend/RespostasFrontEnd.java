package menu.frontend;

import produtos.abstracts.*;
import menu.backend.cruds.abstracts.ComentariosInterface;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.cruds.abstracts.VotosInterface;
import menu.backend.input.CustomInput;
import menu.frontend.genericos.FrontEndPlus;
import menu.frontend.graficos.*;

public class RespostasFrontEnd extends FrontEndPlus {

    public RespostasFrontEnd(CustomPrint myPrint, CustomInput myInput, String name) {
        super(myPrint, myInput, name);
    }

    /**
     * Função para retornar todas as respostas em um array em formato String
     * 
     * @param array é o array de respostas que foi enviado
     * @return a String correspondente a listagem das respostas
     */
    public String listarGeral(UsuarioInterface usuarios, VotosInterface votos, ComentariosInterface comentarios, RegistroVisualResposta[] array) {

        String resp = "";
        String nome = "";

        byte contador = 1;

        resp += myPrint.imprimir("[" + (this.name + "s").toUpperCase() +  "]");

        for (RegistroVisualResposta i : array) {
            if (i.getAtiva() == false)
                continue;

            nome = usuarios.achar(i.getIdUsuario()).getNome();

            resp += "\n" + myPrint.imprimir(contador + "." + i.imprimir(nome) + "\n");

            resp += votos.recuperarNota(usuarios.achar(i.getIdUsuario()).getId() + "|R|" + i.getId()) + "\n";
            
            if(comentarios.achar(i.getId()).getTipo() != (byte)0)
            resp += comentarios.getComentarioArray(i.getId());

            contador++;

        }

        return resp;
    }

}