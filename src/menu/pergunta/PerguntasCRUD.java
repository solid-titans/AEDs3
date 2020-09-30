package menu.pergunta;

import crud.Crud;
import produtos.*;

class PerguntasCRUD {

    public Crud<CelulaIDs> ids;
    public Crud<Pergunta> perguntas;


    public PerguntasCRUD() 
    {
        try {
            this.ids = new Crud<>("Ids",  CelulaIDs.class.getConstructor());
            this.perguntas = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
        } catch (Exception e) {}

	}
	
	public Pergunta acharPergunta(int id) {

		Pergunta p = null;
		try {
			p = perguntas.read(id);
		} catch(Exception e ) {}
		return p;
	}
		
	public int novaPergunta(Pergunta p,int IdUsuario) {
		int resp = -1;
		resp = perguntas.create(p);
		novoParId(IdUsuario,resp);
		return resp;

	}

	public void atualizarPergunta(Pergunta p) {
		perguntas.update(p,p.getId());
	}

	private void novoParId(int IdUsuario, int IdPergunta ) {

		CelulaIDs tmp   = null;
		String    idTmp = "";

		try {
		    tmp = ids.read(IdUsuario);
		}
		catch(Exception e) {}

		if ( tmp != null) {

		    idTmp = tmp.chaveSecundaria() + "-" + String.valueOf(IdPergunta);
		    tmp.setChaveSecundaria(idTmp);
		    ids.update(tmp,tmp.getId());

		}
		else {
		    ids.create(new CelulaIDs(IdUsuario,String.valueOf(IdPergunta)));
		}
	}

	public Pergunta[] getPerguntaArray(int IdUsuario) {
		    
		Pergunta[] resp = null;
		CelulaIDs  tmp  = null;
		String[] split  = null;

		try {
		        
		    tmp = ids.read(IdUsuario);
		    if ( tmp != null) {

		        split = tmp.chaveSecundaria().split("-"); 
		        resp = new Pergunta[split.length];

		        for (byte i = 0 ; i < split.length; i++) {

		        	resp[i] = perguntas.read(Integer.parseInt(split[i]));
		        }
		            
		    }

		}
		catch(Exception e) {}
		return resp;
	}

}
   
