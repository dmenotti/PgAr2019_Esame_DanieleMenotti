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
		
		
		for(int i=0; i<numCaselle; i++) {
			if(i%(numCaselle/numStazioni) == 0) {
				caselle.add(new Casella(i, Casella.TIPO_STAZIONE, Utilities.NOMI_CITTA[i]));
				stazioniPresenti++;
			} else {
				int tipoRandom = Casella.getTipoRandom();
				switch(tipoRandom) {
				case Casella.TIPO_PROBABILITA:
					caselle.add(new Casella(i, Casella.TIPO_PROBABILITA, Utilities.NOMI_CITTA[i]));
					probabilitaPresenti++;
					break;
					
				case Casella.TIPO_IMPREVISTO:
					caselle.add(new Casella(i, Casella.TIPO_IMPREVISTO, Utilities.NOMI_CITTA[i]));
					imprevistiPresenti++;
					break;
					
				};
			}
		}
	}
	
	
	public String viewTabellone() {
		String out = "";
		for(Casella casella : caselle) {
			out = out.concat("\n-------------------------------------\n");
			out = out.concat(casella.getTipo() + " " + casella.getNome());
		}
		return out;
	}

	
	
}

