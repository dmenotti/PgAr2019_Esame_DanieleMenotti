package dmenotti.unipoly;

import java.util.ArrayList;

public class Tabellone {
	private static int CASELLE_MIN = 5;
	private static int STAZIONI_MIN = 2;
	
	private ArrayList<Casella> caselle = new ArrayList<Casella>();
	private Giocatore giocatore;
	private int stazioniPresenti = 0;
	private int probabilitaPresenti = 0;
	private int imprevistiPresenti = 0;
	
	public Tabellone(Giocatore giocatore, int numCaselle, int numStazioni) throws IncorrectSizeException {
		this.giocatore = giocatore;
		if(numCaselle<CASELLE_MIN) throw new IncorrectSizeException("Devono essere presenti almeno " + CASELLE_MIN + " caselle.");
		if(numCaselle<CASELLE_MIN) throw new IncorrectSizeException("Devono essere presenti almeno " + STAZIONI_MIN + " stazioni.");
		double stazPerCaselle = (double)numCaselle/(double)(numStazioni+1);
		double cicloStazCaselle = 0;
		
		for(int i=0; i<numCaselle; i++) {
			if(cicloStazCaselle>stazPerCaselle) {
				caselle.add(new Casella(i, Casella.TIPO_STAZIONE, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)]));
				stazioniPresenti++;
				cicloStazCaselle-=stazPerCaselle;
			} else {
				int tipoRandom = Casella.getTipoRandom();
				switch(tipoRandom) {
				case Casella.TIPO_PROBABILITA:
					caselle.add(new ProbabilitaImprevisto(i, Casella.TIPO_PROBABILITA, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)], Utilities.PROBABILITA[Utilities.random(0, Utilities.PROBABILITA.length-1)], Utilities.random(1, 1000000)));
					probabilitaPresenti++;
					break;
					
				case Casella.TIPO_IMPREVISTO:
					caselle.add(new ProbabilitaImprevisto(i, Casella.TIPO_IMPREVISTO, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)], Utilities.IMPREVISTI[Utilities.random(0, Utilities.IMPREVISTI.length-1)], -Utilities.random(1, 1000000)));
					imprevistiPresenti++;
					break;
					
				}
			}
			cicloStazCaselle++;
		}
		
		
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

	
	
}

