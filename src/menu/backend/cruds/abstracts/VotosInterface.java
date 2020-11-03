package menu.backend.cruds.abstracts;

import produtos.Voto;

public interface VotosInterface {
    public int  inserir(Voto voto);
    public Voto achar(int id);
    public Voto achar(String regex);
    public void atualizar(Voto voto);
    public String recuperarNota(String regex);
}
