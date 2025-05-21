package modelo;

import registro.*;
import entidades.Episodio;
import lista_invertida.*;

import java.io.File;
import java.util.ArrayList;

public class ArquivoEpisodios extends Arquivo<Episodio> {

  Arquivo<Episodio> arqEpisodios;
  ArvoreBMais<ParNomeId> indiceNome;
  ArvoreBMais<ParIdId> indiceRelacaoSerieEp;
  
  ListaInvertida listaEpisodios;
  ListaInvertidaAux listaAux;

  public ArquivoEpisodios() throws Exception {

    super("episodios", Episodio.class.getConstructor());

    File directory = new File("./dados/episodio");
    if (!directory.exists()) {
        directory.mkdirs(); // Create the directories if they don't exist
    }

    indiceNome = new ArvoreBMais<>(
    ParNomeId.class.getConstructor(), 5, "./dados/episodio" + "/indiceNome.db");

    indiceRelacaoSerieEp = new ArvoreBMais<>(
    ParIdId.class.getConstructor(), 5, "./dados/episodio" + "/indiceRelacaoSerieEp.db");

    listaEpisodios = new ListaInvertida(4, "./dados/dicionario.listaEpisodios.db", "./dados/blocos.listaEpisodios.db");
    listaAux = new ListaInvertidaAux();
  }

  @Override
  public int create (Episodio ep) throws Exception {

    int id = super.create(ep);

    indiceNome.create(new ParNomeId(ep.getNome(), id));

    //adicionar indice de relacionamento
    indiceRelacaoSerieEp.create(new ParIdId(ep.getIDSerie(), id));

    String[] terms = listaAux.getTerms(ep.getNome()); // Encontrar termos
    float[] fq = listaAux.getFrequency(terms);	// Encontrar frequencia

    int n = terms.length;

    // Adicionar termos
    for (int i = 0; i < n; i++)
    {
      listaEpisodios.create(terms[i], new ElementoLista(id, fq[i]));
    }
    return id;
  }

  public Episodio[] readNome (String nome) throws Exception {

    if(nome.length() == 0) return null;

    ArrayList<ParNomeId> pares = indiceNome.read(new ParNomeId(nome, -1));

    if(pares.size() > 0){

      Episodio[] episodios = new Episodio[pares.size()];

      int i = 0;

      for(ParNomeId par : pares){

        episodios[i++] = read(par.getId());

      }

      return episodios;

    } else {

      return null;
    } 

  }

  @Override
  public boolean delete (int id) throws Exception{

    Episodio ep = read(id);

    if (ep != null){

      if(super.delete(id)){

		String[] termos = listaAux.getTerms(ep.getNome()); // Le os termos da entidade

		int n = termos.length;

		for (int i = 0; i < n; i++)
		{
			listaEpisodios.delete(termos[i], id);	// Apaga as tuplas dessa entidade dos termos
		}
        return indiceNome.delete(new ParNomeId(ep.getNome(), id)) && indiceRelacaoSerieEp.delete(new ParIdId(ep.getIDSerie(), id));
      
      }

    }

    return false;

  }

  @Override
  public boolean update (Episodio novoEpisodio) throws Exception {

    Episodio ep = read(novoEpisodio.getID());

    if(ep != null){

      if(super.update(novoEpisodio)){

        if(!ep.getNome().equals(novoEpisodio.getNome())){
          System.out.println("Nome do episódio alterado de '" + ep.getNome() + "' para '" + novoEpisodio.getNome() + "'.");
          indiceNome.delete(new ParNomeId(ep.getNome(), ep.getID()));
          indiceNome.create(new ParNomeId(novoEpisodio.getNome(), novoEpisodio.getID()));

          // Remove old terms from the inverted list if they are not in the new name
          String[] termosAntigos = listaAux.getTerms(ep.getNome());
          String[] termosNovos = listaAux.getTerms(novoEpisodio.getNome());
      
          if (termosAntigos == null || termosAntigos.length == 0) {
              System.out.println("Nenhum termo antigo encontrado para o nome: " + ep.getNome());
          } else {
              System.out.println("Termos antigos encontrados: " + String.join(", ", termosAntigos));
              System.out.println("Termos novos encontrados: " + String.join(", ", termosNovos));
      
              for (String termoAntigo : termosAntigos) {
                  boolean encontrado = false;
      
                  for (String termoNovo : termosNovos) {
                      if (termoAntigo.equals(termoNovo)) {
                          encontrado = true;
                          break;
                      }
                  }
      
                  if (!encontrado) {
                      System.out.println("Tentando excluir termo antigo: " + termoAntigo);
                      boolean status = listaEpisodios.delete(termoAntigo, ep.getID());
                      if (status) {
                          System.out.println("Termo '" + termoAntigo + "' excluído com sucesso.");
                      } else {
                          System.err.println("Erro ao excluir termo '" + termoAntigo + "'.");
                      }
                  }
              }
          }

        }

        String[] termos = listaAux.getTerms(novoEpisodio.getNome());
        float[] fq = listaAux.getFrequency(termos);

        int n = termos.length;

        for (int i = 0; i < n; i++)
        {
          boolean status = false;
          if (listaEpisodios.read(termos[i], novoEpisodio.getID()) == null) // Verificar se o termo nao existe
          {
            status = listaEpisodios.create(termos[i], new ElementoLista(novoEpisodio.getID(), fq[i]));
          }
          else // Se sim, altera-lo
          {
            status = listaEpisodios.update(termos[i], new ElementoLista(novoEpisodio.getID(), fq[i]));
          }

          if (!status) {
            System.err.println("Erro: O termo '" + termos[i] + "' não pode ser alterado.");
          }
        }
            return true;
          }

        }
        return false;
  }

  //todos os episodios linkados a determinada serie
  public Episodio[] readPorSerie(int idSerie) throws Exception {

    // Search in the B+ tree for all pairs matching (idSerie, -1)
    ArrayList<ParIdId> pares = indiceRelacaoSerieEp.read(new ParIdId(idSerie, -1));

    if (pares.isEmpty()) {
      return null;
    }

    Episodio[] episodios = new Episodio[pares.size()];
    int i = 0;

    // For each pair, get the episode ID and read the full record
    for (ParIdId par : pares) {

      episodios[i++] = read(par.getId2());
      
    }

    return episodios;

  }

}
