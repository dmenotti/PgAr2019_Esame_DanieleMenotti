package dmenotti.unipoly;

import java.util.ArrayList;

public class Tabellone {
	private static int CASELLE_MIN = 5;
	private static int STAZIONI_MIN = 2;
	public static ArrayList<Tabellone> archivioMappe = new ArrayList<Tabellone>();
	
	private ArrayList<Casella> caselle = new ArrayList<Casella>();
	private int stazioniPresenti = 0;
	private int probabilitaPresenti = 0;
	private int imprevistiPresenti = 0;
	private String nome;
	
	
	public Tabellone(int numCaselle, int numStazioni) throws IncorrectSizeException {
		if(numCaselle<CASELLE_MIN) throw new IncorrectSizeException("Devono essere presenti almeno " + CASELLE_MIN + " caselle.");
		if(numCaselle<CASELLE_MIN) throw new IncorrectSizeException("Devono essere presenti almeno " + STAZIONI_MIN + " stazioni.");
		double stazPerCaselle = (double)numCaselle/(double)(numStazioni+1);
		double cicloStazCaselle = 0;
		
		caselle.add(new Casella(0, Casella.TIPO_INIZIALE, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)]));
		
		for(int i=1; i<numCaselle; i++) {
			if(cicloStazCaselle>stazPerCaselle) {
				cicloStazCaselle = addStazione(stazPerCaselle, cicloStazCaselle, i);
			} else {
				int tipoRandom = Casella.getTipoRandom();
				switch(tipoRandom) {
				case Casella.TIPO_PROBABILITA:
					addPossibilita(i);
					break;
					
				case Casella.TIPO_IMPREVISTO:
					addImprevisto(i);
					break;
					
				}
			}
			cicloStazCaselle++;
		}
		
		nome = Utilities.NOMI_TABELLONI[Utilities.random(0, Utilities.NOMI_TABELLONI.length-1)];
	}

	private double addStazione(double stazPerCaselle, double cicloStazCaselle, int i) {
		caselle.add(new Casella(i, Casella.TIPO_STAZIONE, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)]));
		stazioniPresenti++;
		cicloStazCaselle-=stazPerCaselle;
		return cicloStazCaselle;
	}

	private void addImprevisto(int i) {
		caselle.add(new ProbabilitaImprevisto(i, Casella.TIPO_IMPREVISTO, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)], Utilities.IMPREVISTI[Utilities.random(0, Utilities.IMPREVISTI.length-1)], -Utilities.random(1, 1000000)));
		imprevistiPresenti++;
	}

	private void addPossibilita(int i) {
		caselle.add(new ProbabilitaImprevisto(i, Casella.TIPO_PROBABILITA, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)], Utilities.PROBABILITA[Utilities.random(0, Utilities.PROBABILITA.length-1)], Utilities.random(1, 1000000)));
		probabilitaPresenti++;
	}
	
	public Casella getCasella(int i) {
		return caselle.get(i);
	}
	
	public String viewTabellone() {
		String out = "";
		for(Casella casella : caselle) {
			out = out.concat("\n-------------------------------------\n");
			out = out.concat(casella.getTipo() + " " + casella.getNome());
			if(casella.getTipo() == Casella.TIPO_PROBABILITA || casella.getTipo() == Casella.TIPO_IMPREVISTO) out = out.concat(" " + ((ProbabilitaImprevisto)casella).getMessaggio() + " " + ((ProbabilitaImprevisto)casella).getOffset());
		}
		out = out.concat("\n\nCi sono " + stazioniPresenti + " stazioni");
		out = out.concat("\nCi sono " + probabilitaPresenti + " probabilita");
		out = out.concat("\nCi sono " + imprevistiPresenti + " imprevisti");
		return out;
	}
	
	public String viewStatsTabellone() {
		String out = "";
		out = out.concat("Ci sono " + stazioniPresenti + " stazioni");
		out = out.concat("\nCi sono " + probabilitaPresenti + " probabilita");
		out = out.concat("\nCi sono " + imprevistiPresenti + " imprevisti");
		return out;
	}

	public ArrayList<Casella> getCaselle() {
		return caselle;
	}
		
	public String getNome() {
		return nome;
	}

	public static void creaTabelloni(int num) {
		for(int i=0; i<num; i++) {
			try {
				archivioMappe.add(new Tabellone(UnipolyMain.DIM_TABELLONE, UnipolyMain.NUM_STAZIONI));
			} catch (IncorrectSizeException e) {
			}
		}
	}
}

