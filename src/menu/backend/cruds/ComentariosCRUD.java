package menu.backend.cruds;

import java.util.ArrayList;

import crud.Crud;
import menu.backend.cruds.abstracts.ComentariosInterface;
import menu.backend.listas.ListaIDs;
import produtos.Comentario;


public class ComentariosCRUD implements ComentariosInterface {
    
    private final String path;

    private static Crud<Comentario> comentarios;
    private static ListaIDs comentPerguntasIDs;
    private static ListaIDs comentRespostasIDs;

    public ComentariosCRUD(String path) {
        this.path = path;

        try {
            comentarios = new Crud<>("Comentarios",Comentario.class.getConstructor());
            comentPerguntasIDs = new ListaIDs(this.path + "/" + "comentPerguntasIDs");
			comentRespostasIDs = new ListaIDs(this.path + "/" + "comentRespostasIDs");

        } catch(Exception e) { e.printStackTrace(); }
    }

    @Override
    public int inserirPergunta(Comentario comentario, int idPergunta) {
        int idComentario  = -1;

		idComentario = comentarios.create(comentario);
		comentario.setId(idComentario);
		comentPerguntasIDs.create(idPergunta, comentario.getId());

        return idComentario;
    }

    @Override
    public int inserirResposta(Comentario comentario, int idResposta) {
        int idComentario  = -1;

		idComentario = comentarios.create(comentario);
		comentario.setId(idComentario);
		comentRespostasIDs.create(idResposta, comentario.getId());

        return idComentario;
    }

    @Override
    public Comentario achar(int idComentario) {
        Comentario comentario = null;
        
        try {
            comentario = comentarios.read(idComentario);
            
		} catch (Exception e) { e.printStackTrace(); }
	    
        return comentario;
    }

    @Override
    public Comentario[] getComentarioArray(int idPergunta, byte tipo) {
        
        Comentario[]          resp           = null;
        int[]                 idsComentarios = null;
        ArrayList<Comentario> array          = new ArrayList<Comentario>();

        idsComentarios = comentPerguntasIDs.read(idPergunta);
        
		if (idsComentarios == null)
			return null;

		for (int i : idsComentarios) {
			try {
				Comentario temp = comentarios.read(i);
				if (temp == null)
                    continue;
                    
                if (temp.getTipo() != tipo)
                    continue;

                array.add(temp);
                    
            } catch (Exception e) { e.printStackTrace(); }
            
		}
        resp = new Comentario[array.size()];

        for ( int i = 0 ; i < array.size() ; i++ ) 
            resp[i] = array.get(i);

		return resp;
    }
}
