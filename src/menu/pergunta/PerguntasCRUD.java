// Classe para gerenciar interação com o CRUD de perguntas
/*
*	Autor: Gustavo Lopes( MysteRys337)  
*	Versão: 0.0.1	
*	
*/
package menu.pergunta;

import crud.Crud;
import produtos.*;

class PerguntasCRUD {

	//Cruds
    public Crud<CelulaIDs> ids;
    public Crud<Pergunta> perguntas;

	//Construtor
    public PerguntasCRUD() 
    {
        try {
            this.ids = new Crud<>("Ids",  CelulaIDs.class.getConstructor());
            this.perguntas = new Crud<>("Perguntas",  Pergunta.class.getConstructor());
        } catch (Exception e) {}

	}
	
	//Encontrar uma pergunta no CRUD a partir do ID 
	public Pergunta acharPergunta(int id) {

		Pergunta p = null;
		try {
			p = perguntas.read(id);
		} catch(Exception e ) {}
		return p;
	}
	
	//Adicionar uma nova pergunta aos CRUDS
	public int novaPergunta(Pergunta p,int IdUsuario) {
		int resp = -1;
		resp = perguntas.create(p);
		novoParId(IdUsuario,resp);
		return resp;

	}

	//Atualizar alguma pergunta
	public void atualizarPergunta(Pergunta p) {
		perguntas.update(p,p.getId());
	}

	//Salvar um par de IDs 
	/*
	*	Além do CRUD de perguntas, o sistema precisa salvar
	*	o par: ID do usuário, e as IDs das perguntas que esse
	*	usuário tem. O método na qual o CRUD salva é a partir
	*	de um inteiro(Id Usuario) é uma String(que são os Ids
	*	das perguntas separadas por '-')
	*/
	private void novoParId(int IdUsuario, int IdPergunta ) {

		CelulaIDs tmp   = null;
		String    idTmp = "";

		System.out.println(IdUsuario);

		//Procurar pelo ID Usuario na Crud
		try {
		    tmp = ids.read(IdUsuario);
		}
		catch(Exception e) {}

		//Se já tiver registrado a ID do usuário
		/*
		*	Ele irá então atualizar a chave secundaria(a String de IDs)
		*	e então perdir para atualizar o CRUD com essa nova informação
		*
		*	Caso contrário, o programa então irá criar uma nova entrada
		*	no CRUD.
		*/
		if ( tmp != null) {

		    idTmp = tmp.chaveSecundaria() + "-" + String.valueOf(IdPergunta);
		    tmp.setChaveSecundaria(idTmp);
		    ids.update(tmp,tmp.getId());
		}
		else {
		    ids.create(new CelulaIDs(IdUsuario,String.valueOf(IdPergunta)));
		}
	}

	//Armazenar as perguntas do usuário em um array e voltar
	/*
	*	A partir do ID Usuario, o programa irá acessar o CRUD
	*	de IDs e pegar a String de IDs de perguntas que o usuário
	*	tem. se o resultado der positivo, então ele irá pegar o 
	*	resultado do 'Split', a partir do '-' para pesquisar
	*	dentro do CRUD de perguntas as perguntas do usuario
	*/
	public Pergunta[] getPerguntaArray(int IdUsuario) {
		    
		Pergunta[] resp = null;
		CelulaIDs  tmp  = null;
		String[] split  = null;

		try {     
			tmp = ids.read(IdUsuario);	
		}
		catch(Exception e) {}

		if ( tmp != null) {

		    split = tmp.chaveSecundaria().split("-"); 
		    resp = new Pergunta[split.length];

		    for (byte i = 0 ; i < split.length; i++) {

		        resp[i] = acharPergunta(Integer.parseInt(split[i]));
		    }
		            
		}
		return resp;
	}

}
   
