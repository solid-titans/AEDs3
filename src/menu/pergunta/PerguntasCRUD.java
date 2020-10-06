// Classe para gerenciar interação com o CRUD de perguntas
/*
*	Autor: Gustavo Lopes( MysteRys337)  
*	Versão: 0.0.1	
*	
*/
package menu.pergunta;

import java.util.ArrayList;

import crud.Crud;
import menu.pergunta.indices.*;
import produtos.*;

class PerguntasCRUD {
	// Path dos Cruds
	private final String path = "Dados";

	//Cruds
    public ListaIDs       ids;
	public Crud<Pergunta> perguntas;
	public ListaInvertida lista;

	//Construtor
    public PerguntasCRUD() 
    {
        try {
            this.ids       = new ListaIDs(this.path + "/" + "IDs");
			this.perguntas = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
			this.lista 	   = new ListaInvertida(10,"Dados/indice.dict","Dados/indice.dictBlock");
			
        } catch (Exception e) {}

	}
	
	//Encontrar uma pergunta no CRUD a partir do ID 
	public Pergunta acharPergunta(int id) {

		Pergunta p = null;
		try {
			p = perguntas.read(id);
		} catch(Exception e ) { e.printStackTrace(); }
		return p;
	}
	
	/** Adicionar uma nova pergunta ao usuário
	 * 
	 * @param p         Objeto que contem a pergunta feita pelo usuario
	 * @param IdUsuario Id do usuario que fez a pergunta
	 * @return          Retornar a id do objeto no banco de dados
	 */
	public int novaPergunta(Pergunta p, int idUsuario) {
		int resp = -1;

		resp = perguntas.create(p);
		ids.create(idUsuario,resp);
		try {
			lista.create(p.getPalavrasChave(),resp);

		} catch(Exception e) {e.printStackTrace();}
		return resp;
	}

	//Atualizar alguma pergunta
	public void atualizarPergunta(Pergunta novo,String palavrasChaveAntiga) {

		String[] split = palavrasChaveAntiga.split(" ");

		try {
			for (byte i = 0;i < split.length; i++) {
				lista.delete(split[i],novo.getId());
			}

		} catch(Exception e) {e.printStackTrace();}

		split = novo.getPalavrasChave().split(" ");

		try {
			for (byte i = 0;i < split.length; i++) {
				lista.create(split[i],novo.getId());
			}

		} catch(Exception e) {e.printStackTrace();}
		perguntas.update(novo,novo.getId());
	}

	public void desativarPergunta(Pergunta p) {

		String[] split = p.getPalavrasChave().split(" ");

		try {
			for (byte i = 0;i < split.length; i++) {
				lista.delete(split[i],p.getId());
			}

		} catch(Exception e) {e.printStackTrace();}
		perguntas.update(p,p.getId());
	}

	//Armazenar as perguntas do usuário em um array e voltar
	/*
	*	A partir do ID Usuario, o programa irá acessar o CRUD
	*	de IDs e pegar a String de IDs de perguntas que o usuário
	*	tem. se o resultado der positivo, então ele irá pegar o 
	*	resultado do 'Split', a partir do '-' para pesquisar
	*	dentro do CRUD de perguntas as perguntas do usuario
	*/
	public Pergunta[] getPerguntaArray(int idUsuario) {
		    
		Pergunta[] resp  = null;
		int[] ids = this.ids.read(idUsuario);

        if(ids == null)
			return  null;
		else {
			resp = new Pergunta[ids.length];
		}

		int contador = 0;
        for (int i : ids) {
            try {
                Pergunta temp = this.perguntas.read(i);
                if(temp == null)
                    continue;

                resp[contador] = temp; 
                contador++;
            }catch(Exception e) { e.printStackTrace(); }
        }


		return resp;
	}

	public void inserirPalavrasChave(String palavrasChave, int idPergunta) {

		String[] split = palavrasChave.split(" ");

		try {
			for(String i : split) {
				lista.create(i,idPergunta);
			}

		}
		catch(Exception e) {e.printStackTrace();}

	}

	public Pergunta[] getPerguntasPalavrasChave(String[] palavrasChave,int idUsuario) {

		ArrayList<Pergunta> array = new ArrayList<>();
		Pergunta[] resp 		  = null;

		int[] idArray = null;

		try { 
			for (byte i = 0; i < palavrasChave.length; i++) {
				idArray = lista.read(palavrasChave[i]);

				for (byte j = 0; j < idArray.length; j++) {
	
					Pergunta tmp = perguntas.read(idArray[j]);

					if(tmp.getIdUsuario() != idUsuario)
						array.add(perguntas.read(idArray[j]));
				}
			}
		} catch(Exception e) {e.printStackTrace();}

		resp = new Pergunta[array.size()];

		for (byte i = 0; i < array.size(); i++) {
			resp[i] = array.get(i);
		}

		return resp;
	}

}
   
