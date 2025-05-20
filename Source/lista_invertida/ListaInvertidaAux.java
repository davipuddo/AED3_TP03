package lista_invertida;

import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Collections;

import modelo.*;

public class ListaInvertidaAux 
{
	// Definir stopwords
	private static String[] stopwords;

	public ListaInvertidaAux ()
	{
		stopwords = new String[222];
		init();
	}

	private static void init()
	{
		stopwords[0] = "de";
		stopwords[1] = "a";
		stopwords[2] = "o";
		stopwords[3] = "que";
		stopwords[4] = "e";
		stopwords[5] = "do";
		stopwords[6] = "da";
		stopwords[7] = "em";
		stopwords[8] = "um";
		stopwords[9] = "para";
		stopwords[10] = "é";
		stopwords[11] = "com";
		stopwords[12] = "não";
		stopwords[13] = "uma";
		stopwords[14] = "os";
		stopwords[15] = "no";
		stopwords[16] = "se";
		stopwords[17] = "na";
		stopwords[18] = "por";
		stopwords[19] = "mais";
		stopwords[20] = "as";
		stopwords[21] = "dos";
		stopwords[22] = "como";
		stopwords[23] = "mas";
		stopwords[24] = "foi";
		stopwords[25] = "ao";
		stopwords[26] = "ele";
		stopwords[27] = "das";
		stopwords[28] = "tem";
		stopwords[29] = "à";
		stopwords[30] = "seu";
		stopwords[31] = "sua";
		stopwords[32] = "ou";
		stopwords[33] = "ser";
		stopwords[34] = "quando";
		stopwords[35] = "muito";
		stopwords[36] = "há";
		stopwords[37] = "nos";
		stopwords[38] = "já";
		stopwords[39] = "está";
		stopwords[40] = "eu";
		stopwords[41] = "também";
		stopwords[42] = "só";
		stopwords[43] = "pelo";
		stopwords[44] = "pela";
		stopwords[45] = "até";
		stopwords[46] = "isso";
		stopwords[47] = "ela";
		stopwords[48] = "entre";
		stopwords[49] = "era";
		stopwords[50] = "depois";
		stopwords[51] = "sem";
		stopwords[52] = "mesmo";
		stopwords[53] = "aos";
		stopwords[54] = "ter";
		stopwords[55] = "seus";
		stopwords[56] = "quem";
		stopwords[57] = "nas";
		stopwords[58] = "me";
		stopwords[59] = "esse";
		stopwords[60] = "eles";
		stopwords[61] = "estão";
		stopwords[62] = "você";
		stopwords[63] = "tinha";
		stopwords[64] = "foram";
		stopwords[65] = "essa";
		stopwords[66] = "num";
		stopwords[67] = "nem";
		stopwords[68] = "suas";
		stopwords[69] = "meu";
		stopwords[70] = "às";
		stopwords[71] = "minha";
		stopwords[72] = "têm";
		stopwords[73] = "numa";
		stopwords[74] = "pelos";
		stopwords[75] = "elas";
		stopwords[76] = "havia";
		stopwords[77] = "seja";
		stopwords[78] = "qual";
		stopwords[79] = "será";
		stopwords[80] = "nós";
		stopwords[81] = "tenho";
		stopwords[82] = "lhe";
		stopwords[83] = "deles";
		stopwords[84] = "essas";
		stopwords[85] = "esses";
		stopwords[86] = "pelas";
		stopwords[87] = "este";
		stopwords[88] = "fosse";
		stopwords[89] = "dele";
		stopwords[90] = "tu";
		stopwords[91] = "te";
		stopwords[92] = "vocês";
		stopwords[93] = "vos";
		stopwords[94] = "lhes";
		stopwords[95] = "meus";
		stopwords[96] = "minhas";
		stopwords[97] = "teu";
		stopwords[98] = "tua";
		stopwords[99] = "teus";
		stopwords[100] = "tuas";
		stopwords[101] = "nosso";
		stopwords[102] = "nossa";
		stopwords[103] = "nossos";
		stopwords[104] = "nossas";
		stopwords[105] = "dela";
		stopwords[106] = "delas";
		stopwords[107] = "esta";
		stopwords[108] = "estes";
		stopwords[109] = "estas";
		stopwords[110] = "aquele";
		stopwords[111] = "aquela";
		stopwords[112] = "aqueles";
		stopwords[113] = "aquelas";
		stopwords[114] = "isto";
		stopwords[115] = "aquilo";
		stopwords[116] = "estou";
		stopwords[117] = "está";
		stopwords[118] = "estamos";
		stopwords[119] = "estão";
		stopwords[120] = "estive";
		stopwords[121] = "esteve";
		stopwords[122] = "estivemos";
		stopwords[123] = "estiveram";
		stopwords[124] = "estava";
		stopwords[125] = "estávamos";
		stopwords[126] = "estavam";
		stopwords[127] = "estivera";
		stopwords[128] = "estivéramos";
		stopwords[129] = "esteja";
		stopwords[130] = "estejamos";
		stopwords[131] = "estejam";
		stopwords[132] = "estivesse";
		stopwords[133] = "estivéssemos";
		stopwords[134] = "estivessem";
		stopwords[135] = "estiver";
		stopwords[136] = "estivermos";
		stopwords[137] = "estiverem";
		stopwords[138] = "hei";
		stopwords[139] = "há";
		stopwords[140] = "havemos";
		stopwords[141] = "hão";
		stopwords[142] = "houve";
		stopwords[143] = "houvemos";
		stopwords[144] = "houveram";
		stopwords[145] = "houvera";
		stopwords[146] = "houvéramos";
		stopwords[147] = "haja";
		stopwords[148] = "hajamos";
		stopwords[149] = "hajam";
		stopwords[150] = "houvesse";
		stopwords[151] = "houvéssemos";
		stopwords[152] = "houvessem";
		stopwords[153] = "houver";
		stopwords[154] = "houvermos";
		stopwords[155] = "houverem";
		stopwords[156] = "houverei";
		stopwords[157] = "houverá";
		stopwords[158] = "houveremos";
		stopwords[159] = "houverão";
		stopwords[160] = "houveria";
		stopwords[161] = "houveríamos";
		stopwords[162] = "houveriam";
		stopwords[163] = "sou";
		stopwords[164] = "somos";
		stopwords[165] = "são";
		stopwords[166] = "era";
		stopwords[167] = "éramos";
		stopwords[168] = "eram";
		stopwords[169] = "fui";
		stopwords[170] = "foi";
		stopwords[171] = "fomos";
		stopwords[172] = "foram";
		stopwords[173] = "fora";
		stopwords[174] = "fôramos";
		stopwords[175] = "seja";
		stopwords[176] = "sejamos";
		stopwords[177] = "sejam";
		stopwords[178] = "fosse";
		stopwords[179] = "fôssemos";
		stopwords[180] = "fossem";
		stopwords[181] = "for";
		stopwords[182] = "formos";
		stopwords[183] = "forem";
		stopwords[184] = "serei";
		stopwords[185] = "será";
		stopwords[186] = "seremos";
		stopwords[187] = "serão";
		stopwords[188] = "seria";
		stopwords[189] = "seríamos";
		stopwords[190] = "seriam";
		stopwords[191] = "tenho";
		stopwords[192] = "tem";
		stopwords[193] = "temos";
		stopwords[194] = "tém";
		stopwords[195] = "tinha";
		stopwords[196] = "tínhamos";
		stopwords[197] = "tinham";
		stopwords[198] = "tive";
		stopwords[199] = "teve";
		stopwords[200] = "tivemos";
		stopwords[201] = "tiveram";
		stopwords[202] = "tivera";
		stopwords[203] = "tivéramos";
		stopwords[204] = "tenha";
		stopwords[205] = "tenhamos";
		stopwords[206] = "tenham";
		stopwords[207] = "tivesse";
		stopwords[208] = "tivéssemos";
		stopwords[209] = "tivessem";
		stopwords[210] = "tiver";
		stopwords[211] = "tivermos";
		stopwords[212] = "tiverem";
		stopwords[213] = "terei";
		stopwords[214] = "terá";
		stopwords[215] = "teremos";
		stopwords[216] = "terão";
		stopwords[217] = "teria";
		stopwords[218] = "teríamos";
		stopwords[219] = "teriam";
	}

	// Remover acentos
    private static String normalize (String str) 
	{
		String res = null;
        if (str != null) 
		{
			String nm = Normalizer.normalize(str, Normalizer.Form.NFD);
			Pattern pt = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			res = pt.matcher(nm).replaceAll("");
        }
		return (res);
    }
	
	// Receber frequencia dos termos
	public static float[] getFrequency (String[] terms)
	{
		float[] res = null;

		if (terms != null)
		{
			int n = terms.length;

			int[] counter = new int[n];	   // Contador de palavras
			String[] used = new String[n]; // Palavras usadas

			int ui = 0;

			for (int i = 0; i < n; i++) // Contar termos
			{
				String aux = terms[i];
				
				int found = -1;
				for (int j = 0; j < n; j++) // Procurar na lista de palavras usadas
				{
					if (aux.equals(used[j]))
					{
						found = j;
					}
				}

				if (found > -1)	// Se a palavra for encontrada
				{
					counter[found]++;
				}
				else			// Se nao
				{
					used[ui] = aux;
					counter[ui++]++;
				}
			}

			res = new float[ui];

			for (int i = 0; i < ui; i++)
			{
				res[i] = ((float)counter[i]/(float)n); // Calcular frequencia
			}
		}
		return (res);
	}

	// Separar termos e remover stopwords
	public static String[] getTerms (String str)
	{
		String[] res = null;
		String[] palavras = str.split(" ");

		int n = palavras.length;
		int c = 0;

		for (int i = 0; i < n; i++)
		{
			palavras[i] = palavras[i].toLowerCase();

			int stopCount = 0;
			for (int j = 0; j < 220; j++) // Remover stopwords
			{
				if (palavras[i].equals(stopwords[j]))
				{
					palavras[i] = "";
					j = 220;

					stopCount++;
				}
			}
			if (stopCount < 221) // Contador de termos validos
			{
				c++;
			}
		}

		// Formatar resultado
		if (c > 0)
		{
			res = new String[c];

			int j = 0;

			for (int i = 0; i < n; i++)
			{
				if (!palavras[i].equals(""))
				{
					res[j++] = normalize(palavras[i]); // Remover acentos
				}
			}
		}
		return (res);
	}

	private static int getMax (ElementoLista[][] EL)
	{
		int max = 0;
		int n = EL.length;
		for (int i = 0; i < n; i++)
		{
			int N = EL[i].length;
			for (int j = 0; j < N; j++)
			{
				int id = EL[i][j].getId();

				if (id > max)
				{
					max = id;
				}
			}
		}
		return (max);
	}

	public static int[] getQueryOrder (ElementoLista[][] EL, int IDCount) throws Exception
	{
		int[] res = null;
		if (EL != null)
		{
			int n = EL.length;

			ElementoLista[][] OEL = EL.clone(); // EL Original

			int aTPN = getMax(EL)+1; // Maior ID + 1

			float[] aTP = new float[aTPN]; // Tuplas alteradas
			int[] idTP = new int [aTPN];

			for (int i = 0; i < aTPN; i++)
			{
				aTP[i] = -1;
			}

			int c = 0; // Contador de IDs com match

			for (int i = 0; i < n; i++)	// Termos
			{
				int TupN = EL[i].length;	// Quantidade de tuplas do termo

				double buf = ((double)IDCount/(double)TupN);

				float IDF = (float)(Math.log10(buf)); // Calcular IDF

				IDF += 1;

				for (int j = 0; j < TupN; j++) // Tuplas
				{
					ElementoLista tuple = EL[i][j];

					float fq = OEL[i][j].getFrequencia(); // Frequencia base
					int id = tuple.getId();

					if (aTP[id] != -1)	// Outra tupla com esse id foi alterada
					{
						float addFq = (fq*IDF);

						aTP[id] += addFq;
					}
					else
					{
						aTP[id] = (fq*IDF);
						idTP[c++] = id;
					}
				}
			}

			System.out.println ("ids validos: "+c);

			int ri = 0; // index do resultado;

			for (int i = 0; i < c; i++)
			{
				for (int j = 0; j < c-1; j++)
				{
					if (aTP[idTP[c]] > aTP[idTP[c+1]])
					{
						int tmp = idTP[c];
						idTP[c] = idTP[c+1];
						idTP[c+1] = tmp;
					}
				}
			}

			res = new int[c];

			for (int i = 0; i < c; i++)
			{
				res[i] = idTP[i];
			}
		}
		return (res);
	}
}
