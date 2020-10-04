// Classe para gerenciar interação com o CRUD de perguntas
/*
*	Autor: Gustavo Lopes( MysteRys337)  
*	Versão: 0.0.1	
*	
*/
package menu.pergunta;

import crud.Crud;
import menu.pergunta.indices.ListaIDs;
import produtos.*;

class PerguntasCRUD {
	// Path dos Cruds
	private final String path = "Dados";

	//Cruds
    public ListaIDs       ids;
    public Crud<Pergunta> perguntas;

	//Construtor
    public PerguntasCRUD() 
    {
        try {
            this.ids       = new ListaIDs(this.path + "/" + "IDs");
			this.perguntas = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
			
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
	public int novaPergunta(Pergunta p, int IdUsuario) {
		int resp = -1;
		Pergunta inserir = new Pergunta();

		try {
			inserir.fromByteArray(p.toByteArray());
			inserir.setIdUsuario(IdUsuario);

		}catch (Exception e) { e.printStackTrace(); }

		resp = perguntas.create(inserir);
		return resp;

	}

	//Atualizar alguma pergunta
	public void atualizarPergunta(Pergunta p) {
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

}
   
