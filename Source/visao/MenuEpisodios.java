package visao;
import entidades.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import lista_invertida.*;
import modelo.*;

public class MenuEpisodios {
    
    ArquivoEpisodios arqEpisodios;
    ArquivoSeries arqSeries;
    private static Scanner console = new Scanner(System.in);

	ListaInvertida listaEpisodios;
	ListaInvertidaAux listaAux;

    public MenuEpisodios() throws Exception {
        arqEpisodios = new ArquivoEpisodios();
        arqSeries = new ArquivoSeries();

		listaEpisodios = new ListaInvertida(4, "./dados/dicionario.listaEpisodios.db", "./dados/blocos.listaEpisodios.db");
		listaAux = new ListaInvertidaAux();
    }

    public void menu() {

        int opcao;
        do {

            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Inicio > Episodios");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir um episodio");
            System.out.println("5 - Excluir todos os episodios de uma serie");
            System.out.println("0 - Voltar");

            System.out.print("\nOpcao: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarEpisodio();
                    break;
                case 2:
                    incluirEpisodio();
                    break;
                case 3:
                    alterarEpisodio();
                    break;
                case 4:
                    excluirEpisodio();
                    break;
                case 5:
                    excluirEpisodiosPorSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void buscarEpisodio() {

        System.out.println("\nBusca de episodio");
        System.out.print("Digite o nome do episodio: "); 
        String nome = console.nextLine(); 
    
        try 
		{
			String[] termos = listaAux.getTerms(nome); // Encontrar termos

			int n = termos.length;

			ElementoLista[][] EL = new ElementoLista[n][];	// Vetores de tuplas para todos os termos

			for (int i = 0; i < n; i++)
			{
				EL[i] = listaEpisodios.read(termos[i]);	// Recebe vetores de tuplas dos termos
			}

			int IDCount = arqEpisodios.readAll().size(); 	// Ler quantidade total de Episodios

			int[] IDs = listaAux.getQueryOrder(EL, IDCount); // IDs encontrados ordenados por IDF

			Episodio[] episodios = new Episodio[IDs.length];
			
			// Encontrar entidades
			for (int i = 0; i < IDs.length; i++)
			{
				episodios[i] = arqEpisodios.read(IDs[i]);
			}

            //Episodio[] episodios = arqEpisodios.readNome(nome); (busca antiga)
    
            if (episodios != null && episodios.length > 0) {
                boolean encontrouEpisodio = false;
    
                for (Episodio episodio : episodios) {
                    if (episodio != null) { // Ensure the episode is not null (not deleted)
                        mostraEpisodio(episodio);
                        encontrouEpisodio = true;
                    }
                }
    
                if (!encontrouEpisodio) {
                    System.out.println("Nenhum episodio encontrado com esse nome.");
                }
    
            } else {
                System.out.println("Episodio nao encontrado.");
            }
    
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel buscar o episodio!");
            e.printStackTrace();
        }
    }

    public void incluirEpisodio(){

        System.out.println("\nInclusao de episodio");
        String nome = "";
        int IDSerie = 0;
        int temporada = 0;
        LocalDate lancamento = null;
        int duracao = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Serie[] sa = null;

        do{
            System.out.println("\nQual o nome da serie a qual esse episodio pertence? ");
            String nomeSerie = console.nextLine();

            try {

                sa = arqSeries.readNome(nomeSerie);

            } catch (Exception e) {

                System.out.println("Erro do sistema. Nao foi possivel buscar a serie!");
                e.printStackTrace();

            }

            if(sa == null){

                System.out.println("ERRO: Serie nao cadastrada. Tente novamente: ");

            }else{ 

                IDSerie = sa[0].getID();
            }
            
        } while(sa==null);

        do {

            System.out.print("\nNome do episodio (min. de 1 letras ou vazio para cancelar): ");

            nome = console.nextLine();

            if(nome.length()==0){
                return;
            }
            
            if(nome.length()<1){
                System.err.println("O nome do episodio deve ter no minimo 1 caracteres.");
            }

            if (nome.length()>50){
                System.err.println("O nome do episodio deve ter no maximo 50 caracteres.");
            }

        } while(nome.length()<1 || nome.length()>50);

        do{
            System.out.print("Temporada: (Numero inteiro positivo): ");
            temporada = console.nextInt();
            console.nextLine(); 
            if(temporada<0)
                System.out.println("O numero da temporada deve ser inteiro e positivo: ");
        }while (temporada<0);

        // Ler data de lancamento
        boolean dadosCorretos = false;
        do 
        {
            System.out.print("Data de lancamento (DD/MM/AAAA): ");
            String dataStr = console.nextLine();

            try 
            {
                lancamento = LocalDate.parse(dataStr, formatter);
                dadosCorretos = true;
            } 
            catch (Exception e) 
            {
                System.err.println ("Data invalida! Use o formato DD/MM/AAAA.");
            }

        } while (!dadosCorretos);
        
        do{
            System.out.print("Duracao em minutos: (Numero inteiro positivo): ");
            duracao = console.nextInt();
            console.nextLine();
            if(duracao<=0)
                System.out.println("A duracao deve ser um numero inteiro e positivo: ");

        }while (duracao<=0);

        System.out.print("\nConfirma a inclusao da episodio? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') {
            try {
                Episodio c = new Episodio(IDSerie, nome, temporada, lancamento, duracao);
                arqEpisodios.create(c);
                System.out.println("Episodio incluido com sucesso.");
            } catch(Exception e) {
                System.out.println("Erro do sistema. Nao foi possivel incluir o episodio!");
            }
        }
    }

    public void alterarEpisodio() {

        System.out.println("\nAlteracao de episodio");
        System.out.print("Digite o nome do episodio: ");
        String nome = console.nextLine();
        Episodio[] ea = null;
    
        try {

            // Buscar todos os episodios com o nome
            ea = arqEpisodios.readNome(nome);
            if (ea != null && ea.length > 0) {
                System.out.println("\nEpisodios encontrados com o nome '" + nome + "':");
                for (int i = 0; i < ea.length; i++) {
                    System.out.println("\n[" + (i + 1) + "] ");
                    mostraEpisodio(ea[i]);
                }
    
                System.out.printf("\nSelecione (1-%d) qual episodio deseja alterar: ", ea.length);
                int opcao = console.nextInt();
                console.nextLine(); // consumir quebra de linha
    
                if (opcao < 1 || opcao > ea.length) {
                    System.out.println("Opcao invalida.");
                    return;
                }
    
                Episodio episodio = ea[opcao - 1];
                System.out.println("Episodio escolhido:");
                mostraEpisodio(episodio);
    
                // Alterar nome
                System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
                String novoNome = console.nextLine();
                if (!novoNome.isEmpty()) {
                    episodio.setNome(novoNome);
                }
    
                // Alterar temporada
                System.out.print("Nova temporada (deixe em branco para manter a anterior): ");
                String novaTemporada = console.nextLine();
                if (!novaTemporada.isEmpty()) {
                    try {
                        episodio.setTemporada(Integer.parseInt(novaTemporada));
                    } catch (NumberFormatException e) {
                        System.err.println("Temporada invalida. Valor mantido.");
                    }
                }
    
                // Alterar duracao
                System.out.print("Nova duracao (deixe em branco para manter a anterior): ");
                String novaDuracaoStr = console.nextLine();
                if (!novaDuracaoStr.isEmpty()) {
                    try {
                        episodio.setDuracao(Integer.parseInt(novaDuracaoStr));
                    } catch (NumberFormatException e) {
                        System.err.println("Duracao invalida. Valor mantido.");
                    }
                }
    
                // Alterar data de lancamento
                System.out.print("Nova data de lancamento (DD/MM/AAAA) (deixe em branco para manter a anterior): ");
                String novaDataStr = console.nextLine();
                if (!novaDataStr.isEmpty()) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        episodio.setLancamento(LocalDate.parse(novaDataStr, formatter));
                    } catch (Exception e) {
                        System.err.println("Data invalida. Valor mantido.");
                    }
                }
    
                // Confirmar alteracoes
                System.out.print("\nConfirma as alteracoes? (S/N) ");
                char resp = console.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    boolean alterado = arqEpisodios.update(episodio);
                    if (alterado) {
                        System.out.println("Episodio alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o episodio.");
                    }
                } else {
                    System.out.println("Alteracoes canceladas.");
                }
            } else {
                System.out.println("Nenhum episodio encontrado com esse nome.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel alterar o episodio!");
            e.printStackTrace();
        }
    }

    public void excluirEpisodio() {

        System.out.println("\nExclusao de episodio");
        System.out.print("Digite o nome do episodio: ");
        String nome = console.nextLine();
        Episodio[] ea = null;
    
        try {

            // Buscar todos os episodios com o nome
            ea = arqEpisodios.readNome(nome);

            if (ea != null && ea.length > 0) {

                System.out.println("\nEpisodios encontrados com o nome '" + nome + "':");
                for (int i = 0; i < ea.length; i++) {
                    System.out.println("\n[" + (i + 1) + "] ");
                    mostraEpisodio(ea[i]);  // Exibe detalhes de cada episodio encontrado
                }
    
                System.out.printf("\nSelecione (1-%d) qual episodio deseja excluir: ", ea.length);
                int opcao = console.nextInt();
                console.nextLine(); // Consumir a quebra de linha
    
                if (opcao < 1 || opcao > ea.length) {
                    System.out.println("Opcao invalida.");
                    return;
                }
    
                Episodio episodio = ea[opcao - 1];
                System.out.print("\nConfirma a exclusao do episodio? (S/N) ");
                char resp = console.nextLine().charAt(0);
    
                if (resp == 'S' || resp == 's') {

                    boolean excluido = arqEpisodios.delete(episodio.getID());
                    if (excluido) {
                        System.out.println("Episodio excluido com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o episodio.");
                    }

                } else {
                    System.out.println("Exclusao cancelada.");
                }

            } else {
                System.out.println("Nenhum episodio encontrado com esse nome.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel excluir o episodio!");
            e.printStackTrace();
        }
    }


    public void mostraEpisodio(Episodio episodio) {
        if (episodio != null) {
            System.out.println("\nDetalhes do Episodio:");
            System.out.println("----------------------");
            try {
                //ler a serie de arqSeries usando o ID do episodio
                Serie s = arqSeries.read(episodio.getIDSerie());
                System.out.printf("Serie........: %s\n", (s != null ? s.getNome() : "Serie nao encontrada"));
            } catch (Exception e) {
                System.out.println("Erro: nao foi possivel buscar a serie do episodio.");
            }
            System.out.printf("Nome.........: %s\n", episodio.getNome());
            System.out.printf("Temporada....: %d\n", episodio.getTemporada());
            System.out.printf("lancamento...: %s\n", episodio.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.printf("Duracao......: %d\n", episodio.getDuracao());
            System.out.println("----------------------");
        }
    }

    public void excluirEpisodiosPorSerie() {
        System.out.print("Digite o nome da serie para excluir todos os episodios: ");
        String nomeSerie = console.nextLine();
    
        try {
            // Retrieve the series by name
            Serie[] series = arqSeries.readNome(nomeSerie);
    
            if (series == null || series.length == 0) {
                System.out.println("Serie nao encontrada.");
                return;
            }
    
            Serie serie = series[0]; // Assuming the first match is the desired series
            System.out.println("Serie encontrada:");
            System.out.printf("Nome: %s\n", serie.getNome());
    
            // Confirm deletion
            System.out.print("\nConfirma a exclusao de todos os episodios da serie? (S/N) ");
            char resp = console.nextLine().charAt(0);
    
            if (resp == 'S' || resp == 's') {
                boolean encontrouErro = false;
    
                // Keep deleting episodes until none are left
                while (true) {
                    Episodio[] episodios = arqEpisodios.readPorSerie(serie.getID());
    
                    if (episodios == null || episodios.length == 0) {
                        break; // No more episodes to delete
                    }
    
                    for (Episodio episodio : episodios) {
                        try {
                            System.out.printf("Tentando excluir o episodio '%s'...\n", episodio.getNome());
                            boolean excluido = arqEpisodios.delete(episodio.getID());
                            if (excluido) {
                                System.out.printf("Episodio '%s' excluido com sucesso.\n", episodio.getNome());
                            } else {
                                System.out.printf("Erro ao excluir o episodio '%s'.\n", episodio.getNome());
                                encontrouErro = true;
                            }
                        } catch (Exception e) {
                            System.out.printf("Erro ao excluir o episodio '%s': %s\n", episodio.getNome(), e.getMessage());
                            e.printStackTrace();
                            encontrouErro = true;
                        }
                    }
                }
    
                if (!encontrouErro) {
                    System.out.println("Todos os episodios da serie foram excluidos com sucesso.");
                } else {
                    System.out.println("Alguns episodios nao puderam ser excluidos.");
                }
            } else {
                System.out.println("Exclusao cancelada.");
            }
    
        } catch (Exception e) {
            System.out.println("Erro ao excluir episodios da serie!");
            e.printStackTrace();
        }
    }

}
