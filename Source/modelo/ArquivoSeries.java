package modelo;

import registro.*;
import entidades.Serie;
import lista_invertida.*;

import java.io.File;
import java.util.ArrayList;

public class ArquivoSeries extends Arquivo<Serie> {

  Arquivo<Serie> arqSeries;
  ArvoreBMais<ParNomeId> indiceNome;

  ListaInvertida listaSeries;
  ListaInvertidaAux listaAux;

  public ArquivoSeries() throws Exception {

    super("series", Serie.class.getConstructor());

    File directory = new File("./dados/serie");

    if (!directory.exists()) {
        directory.mkdirs();
    }

    indiceNome = new ArvoreBMais<>(
    ParNomeId.class.getConstructor(), 5, "./dados/serie" + "/indiceNome.db");

    listaSeries = new ListaInvertida(4, "./dados/dicionario.listaSeries.db", "./dados/blocos.listaSeries.db");
    listaAux = new ListaInvertidaAux();
  }

  @Override
  public int create (Serie s) throws Exception {

    int id = super.create(s);

    indiceNome.create(new ParNomeId(s.getNome(), id));

    String[] terms = listaAux.getTerms(s.getNome());

    int n = terms.length;
    float[] fq = listaAux.getFrequency(terms);

    for (int i = 0; i < n; i++)
    {
      listaSeries.create(terms[i], new ElementoLista(id, fq[i]));
    }

    return id;

  }

  public Serie[] readNome (String nome) throws Exception {

    if(nome.length() == 0) return null;

    ArrayList<ParNomeId> pares = indiceNome.read(new ParNomeId(nome, -1));

    if(pares.size() > 0){

      Serie[] series = new Serie[pares.size()];

      int i = 0;

      for(ParNomeId par : pares){

        series[i++] = read(par.getId());

      }

      return series;

    } else {

      return null;
    } 

  }

  @Override
  public boolean delete (int id) throws Exception{

    Serie s = read(id);

    if (s != null){

      if(super.delete(id))
	  {
		String[] termos = listaAux.getTerms(s.getNome()); // Encontrar termos

		int n = termos.length;
		
		// Apagar todas as tuplas dessa entidade dos termos
		for (int i = 0; i < n; i++)
		{
			listaSeries.delete(termos[i], id);
		}
        return indiceNome.delete(new ParNomeId(s.getNome(), id));
      }

    }

    return false;

  }

  //UPDATE PUDDO ORIGINAL
  @Override
  public boolean update(Serie novaSerie) throws Exception {
      Serie serie = read(novaSerie.getID()); // Read the existing record

      if (serie != null) {

        if (super.update(novaSerie)) { // Call the superclass update method
          
          if (!serie.getNome().equals(novaSerie.getNome())) {
            System.out.println("Nome da série alterado de '" + serie.getNome() + "' para '" + novaSerie.getNome() + "'.");
        
            // Update the name index if the name has changed
            indiceNome.delete(new ParNomeId(serie.getNome(), serie.getID()));
            indiceNome.create(new ParNomeId(novaSerie.getNome(), novaSerie.getID()));
        
            // Remove old terms from the inverted list if they are not in the new name
            String[] termosAntigos = listaAux.getTerms(serie.getNome());
            String[] termosNovos = listaAux.getTerms(novaSerie.getNome());
        
            if (termosAntigos == null || termosAntigos.length == 0) {
                System.out.println("Nenhum termo antigo encontrado para o nome: " + serie.getNome());
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
                        boolean status = listaSeries.delete(termoAntigo, serie.getID());
                        if (status) {
                            System.out.println("Termo '" + termoAntigo + "' excluído com sucesso.");
                        } else {
                            System.err.println("Erro ao excluir termo '" + termoAntigo + "'.");
                        }
                    }
                }
            }
          }

          String[] termos = listaAux.getTerms(novaSerie.getNome());
          float[] fq = listaAux.getFrequency(termos);

          int n = termos.length;

          for (int i = 0; i < n; i++)
          {

            boolean status = false;

            if (listaSeries.read(termos[i], novaSerie.getID()) == null) // Verificar se o termo nao existe
            {
              status = listaSeries.create(termos[i], new ElementoLista(novaSerie.getID(), fq[i]));
            }
            else // Se sim, altera-lo
            {
              status = listaSeries.update(termos[i], new ElementoLista(novaSerie.getID(), fq[i]));
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

  /*@Override
  public boolean update(Serie novaSerie) throws Exception {

      Serie serie = read(novaSerie.getID()); // Read the existing record

      if (serie != null) {

          if (super.update(novaSerie)) { // Call the superclass update method

              if (!serie.getNome().equals(novaSerie.getNome())) {
                  // Update the name index if the name has changed
                  indiceNome.delete(new ParNomeId(serie.getNome(), serie.getID()));
                  indiceNome.create(new ParNomeId(novaSerie.getNome(), novaSerie.getID()));

                  // Remove old terms from the inverted list
                  String[] termosAntigos = listaAux.getTerms(serie.getNome());
                  for (String termo : termosAntigos) {
                      boolean status = listaSeries.delete(termo, serie.getID());
                      if (!status) {
                          System.err.println("Erro ao remover termo antigo: " + termo);
                      }
                  }
              }

              // Add or update new terms in the inverted list
              String[] termosNovos = listaAux.getTerms(novaSerie.getNome());
              float[] fq = listaAux.getFrequency(termosNovos);

              int n = termosNovos.length;

              for (int i = 0; i < n; i++) {
                  boolean status = false;

                  // Check if the term exists
                  ElementoLista[] elementos = listaSeries.read(termosNovos[i]);
                  if (elementos == null || elementos.length == 0) {
                      // Create the term if it does not exist
                      status = listaSeries.create(termosNovos[i], new ElementoLista(novaSerie.getID(), fq[i]));
                  } else {
                      // Update the term if it exists
                      status = listaSeries.update(termosNovos[i], new ElementoLista(novaSerie.getID(), fq[i]));
                  }

                  if (!status) {
                      System.err.println("Erro ao adicionar/atualizar termo: " + termosNovos[i]);
                  }
              }

              return true;
              
          }
      }

      return false;
      
  }*/

}