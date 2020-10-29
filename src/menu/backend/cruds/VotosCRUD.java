package menu.backend.cruds;

import produtos.Voto;
import crud.Crud;
import menu.backend.cruds.abstracts.VotosInterface;

public class VotosCRUD implements VotosInterface {
    
    private static Crud<Voto> votos;

    public VotosCRUD() {

        try {
            votos = new Crud<>("Votos", Voto.class.getConstructor());

        } catch(Exception e) { e.printStackTrace(); }

    }

    @Override
    public int inserir(Voto voto) {
        return votos.create(voto);
    }

    @Override
    public Voto achar(int id) {
        Voto voto = null;

        try {
            voto = votos.read(id);
            
        } catch(Exception e) { e.printStackTrace(); }

        return voto;
    }

    @Override
    public Voto achar(String regex) {
        Voto voto = null;

        try {
            voto = votos.read(regex);
            
        } catch(Exception e) { e.printStackTrace(); }
        
        return voto;
    }

    @Override
    public void atualizar(Voto voto) {
		votos.update(voto, voto.getId());
    }
}