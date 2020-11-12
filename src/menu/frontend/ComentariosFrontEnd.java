package menu.frontend;

import produtos.abstracts.*;
import menu.backend.cruds.abstracts.UsuarioInterface;
import menu.backend.input.CustomInput;
import menu.frontend.genericos.FrontEndPlus;
import menu.frontend.graficos.*;

public class ComentariosFrontEnd extends FrontEndPlus {

    public ComentariosFrontEnd(CustomPrint myPrint, CustomInput myInput, String name) {
        super(myPrint, myInput, name);
    }

    /**
     * Função para retornar todas as respostas em um array em formato String
     * 
     * @param array é o array de respostas que foi enviado
     * @return a String correspondente a listagem das respostas
     */
    public String listar(UsuarioInterface usuarios, RegistroVisualplus[] array) {

        String resp = "";
        String nome = "";

        byte contador = 1;

        resp += myPrint.imprimir("[" + (this.name + "s").toUpperCase() +  "]");

        for (RegistroVisualplus i : array) {
            if (i.getAtiva() == false)
                continue;

            nome = usuarios.achar(i.getIdUsuario()).getNome();

            resp += "\n" + myPrint.imprimir(contador + "." + i.imprimir(nome) + "\n");
            
            contador++;

        }

        return resp;
    }

}