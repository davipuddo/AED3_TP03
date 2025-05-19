package modelo;

import registro.*;
import entidades.Ator;
import lista_invertida.*;

import java.io.File;
import java.util.ArrayList;

public class ArquivoAtores extends Arquivo<Ator> {

  Arquivo<Ator> arqAtores;

  //índice do nome do ator
  ArvoreBMais<ParNomeId> indiceNome;

  //índice de relacionamento ator-série
  ArvoreBMais<ParIdId> indiceRelacaoSerieAtor;

  ListaInvertida listaAtores;
  ListaInvertida listaAux;

  public ArquivoAtores() throws Exception {

    super("episodios", Ator.class.getConstructor());

    File directory = new File("./dados/ator");
    if (!directory.exists()) {
        directory.mkdirs(); // Create the directories if they don't exist
    }

    indiceNome = new ArvoreBMais<>(
    ParNomeId.class.getConstructor(), 5, "./dados/ator" + "/indiceNome.db");

    indiceRelacaoSerieAtor = new ArvoreBMais<>(
    ParIdId.class.getConstructor(), 5, "./dados/ator" + "/indiceRelacaoSerieAtor.db");

	listaAtores = new ListaInvertida(4, "./dados/dicionario.listaAtores.db", "./dados/blocos.listaAtores.db");
	listaAux = new ListaInvertidaAux();
  }

  @Override
  public int create (Ator at) throws Exception {

    int id = super.create(at);

    indiceNome.create(new ParNomeId(at.getNomeAtor(), id));

    //adicionar indice de relacionamento
    indiceRelacaoSerieAtor.create(new ParIdId(at.getIDSerie(), id));

	String[] terms = listaAux.getTerms(at.getNome());

	int n = terms.length;
	float[] fq = listaAux.getFrequency(terms);

	for (int i = 0; i < n; i++)
	{
		listaAtores.create(terms[i], new ElementoLista(id, fq[i]));
	}

    return id;

  }

  public Ator[] readNome (String nome) throws Exception {

    if(nome.length() == 0) return null;

    ArrayList<ParNomeId> pares = indiceNome.read(new ParNomeId(nome, -1));

    if(!pares.isEmpty()){

      Ator[] atores = new Ator[pares.size()];

      int i = 0;

      for(ParNomeId par : pares){

        atores[i++] = read(par.getId());

      }

      return atores;

    } else {

      return null;
    } 

  }

  @Override
  public boolean delete (int id) throws Exception{

    Ator at = read(id);

    if (at != null){

      if(super.delete(id)){

		String[] termos = listaAux.getTerms(at.getNome());

		int n = termos.length;

		for (int i = 0; i < n; i++)
		{
			listaAtores.delete(termos[i], id);
		}
        return indiceNome.delete(new ParNomeId(at.getNomeAtor(), id)) && indiceRelacaoSerieAtor.delete(new ParIdId(at.getIDSerie(), id));
      
      }

    }

    return false;

  }

  @Override
  public boolean update (Ator novoAtor) throws Exception {

    Ator at = read(novoAtor.getID());

    if(at != null){

      if(super.update(novoAtor)){

        if(!at.getNomeAtor().equals(novoAtor.getNomeAtor())){

          indiceNome.delete(new ParNomeId(at.getNomeAtor(), at.getID()));
          indiceNome.create(new ParNomeId(novoAtor.getNomeAtor(), novoAtor.getID()));

        }

		String[] termos = listaAux.getTerms(serie.getNome());
		float[] fq = listaAux.getFrequency(termos);

		int n = termos.length;

		for (int i = 0; i < n; i++)
		{
			boolean status = listaSeries.update(termos[i], new ElementoLista(serie.getID(), fq[i]));
			if (!status)
			{
				System.err.println ("Erro: O termo nao pode ser alterado");
			}
		}

        return true;
      }

    }
    return false;
  }

  //retorna todos os atores linkados a determinada serie
  public Ator[] readPorSerie(int idSerie) throws Exception {

    // Search in the B+ tree for all pairs matching (idSerie, -1)
    ArrayList<ParIdId> pares = indiceRelacaoSerieAtor.read(new ParIdId(idSerie, -1));

    if (pares.isEmpty()) {
      return null;
    }

    //criamos um vetor do tamanho do número de pares encontrados
    Ator[] atores = new Ator[pares.size()];
    int i = 0;

    //para cada par encontrado, buscamos o ator correspondente e guardamos no vetor
    for (ParIdId par : pares) {

      atores[i++] = read(par.getId2());
      
    }

    return atores;

  }

}
