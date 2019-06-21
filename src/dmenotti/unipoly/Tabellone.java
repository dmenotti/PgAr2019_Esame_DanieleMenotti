package dmenotti.unipoly;

import java.util.ArrayList;



public class Tabellone {
	private static int CASELLE_MIN = 5; //numero minimo di caselle
	private static int STAZIONI_MIN = 2; //numero minimo di stazioni
	public static ArrayList<Tabellone> archivioMappe = new ArrayList<Tabellone>(); //array contenente tutte le caselle
	
	private ArrayList<Casella> caselle = new ArrayList<Casella>();
	private int edificiPresenti = 0;
	private int stazioniPresenti = 0;		//I 4 contatori vengono usati per far vedere all'utente quante caselle di ogni tipo ci sono
	private int probabilitaPresenti = 0;
	private int imprevistiPresenti = 0;
	private String nome;					//Usato solo nella creazione dell'archivio, ogni mappa ha un suo nome
	private int id;							//E un suo id
	
	
	public Tabellone(int id, int numCaselle, int numStazioni) throws IncorrectSizeException {
		this.id = id;
		if(numCaselle<CASELLE_MIN) throw new IncorrectSizeException("Devono essere presenti almeno " + CASELLE_MIN + " caselle.");		//Verifico il numero di caselle e stazioni
		if(numCaselle<CASELLE_MIN) throw new IncorrectSizeException("Devono essere presenti almeno " + STAZIONI_MIN + " stazioni.");
		double stazPerCaselle = (double)numCaselle/(double)(numStazioni+1);		//Determino ogni quante caselle mettere una stazione in modo da spargerle uniformemente
		double cicloStazCaselle = 0;
		int settoreAttivo = 1;			//Un settore è delimitato da due stazioni
		
		caselle.add(new Casella(0, Casella.TIPO_INIZIALE, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)])); //La prima casella è sempre il via
		int i;
		for(i=1; i<numCaselle; i++) {
			if(cicloStazCaselle>=stazPerCaselle) {				//Se ho messo "stazPerCaselle" caselle è ora di mettere una stazione
				addStazione(i);
				cicloStazCaselle-=stazPerCaselle;				//Riparto con il conto. Facendo così posso gestire anche i casi "una stazione ogni 3.5 caselle"
				settoreAttivo++;								//Avanzo di settore
			} else {
				if(probabilitaPresenti == 0) addPossibilita(i);		//La prima "non stazione" sarà sempre una probabilità
				else if(imprevistiPresenti == 0) addImprevisto(i);	//La seconda sarà sempre un imprevisto
				else creaCasellaRandom(settoreAttivo, i);			//Dalla terza in poi la generazione è casuale, con prevalenza edifici
			}
			cicloStazCaselle++;
		}
		
		String nomeTab = "";		//Essendo l'assegnazione dentro ad un if devo per forza inizializzarlo esternamente
		do {
			int numNome = Utilities.random(0, Utilities.NOMI_TABELLONI.length-1);
			if(Utilities.NOMI_TABELLONI_UTILIZZATI[numNome] == 0) {
				nomeTab = Utilities.NOMI_TABELLONI[numNome];			//Recupero un nome e controllo se è già stato utilizzato, se non lo è lo assegno
				Utilities.NOMI_TABELLONI_UTILIZZATI[numNome] = 1;
				break;
			}
		} while(true);
		
		nome = nomeTab;
	}

	private void creaCasellaRandom(int settoreAttivo, int i) {			//In base al numero random determino che tiipo di casella devo generare
		int tipoRandom = Casella.getTipoRandom();
		switch(tipoRandom) {
		case Casella.TIPO_PROBABILITA:
			addPossibilita(i);
			break;
			
		case Casella.TIPO_IMPREVISTO:
			addImprevisto(i);
			break;
			
		case Casella.TIPO_EDIFICIO:
			addEdificio(i, settoreAttivo);
			break;
			
		}
	}

	private void addStazione(int i) {	//Aggiungo una stazione con nome random e incremento il numero di stazioni presenti
		caselle.add(new Casella(i, Casella.TIPO_STAZIONE, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)]));
		stazioniPresenti++;

	}

	private void addImprevisto(int i) {	//Aggiungo un imprevisto con nome random e incremento il numero di imprevisti presenti
		caselle.add(new ProbabilitaImprevisto(i, Casella.TIPO_IMPREVISTO, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)], Utilities.IMPREVISTI[Utilities.random(0, Utilities.IMPREVISTI.length-1)], -Utilities.random(1, 1000000)));
		imprevistiPresenti++;
	}
	
	private void addEdificio(int i, int settoreAttivo) {	//Aggiungo una casella "edificio" con nome random e incremento il numero di caselle edificio presenti. A differenza degli altri qui passo anche il settore della casella
		caselle.add(new Edificio(i, Casella.TIPO_EDIFICIO, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)], settoreAttivo));
		edificiPresenti++;
	}

	private void addPossibilita(int i) {	//Aggiungo una possibilità con nome random e incremento il numero di possibilità presenti
		caselle.add(new ProbabilitaImprevisto(i, Casella.TIPO_PROBABILITA, Utilities.NOMI_CITTA[Utilities.random(0, Utilities.NOMI_CITTA.length-1)], Utilities.PROBABILITA[Utilities.random(0, Utilities.PROBABILITA.length-1)], Utilities.random(1, 1000000)));
		probabilitaPresenti++;
	}
	
	public Casella getCasella(int i) { //Per recuperare una singola casella data la sua posizione
		return caselle.get(i);
	}
	
	public String viewTabellone() {	//Permette di vedere tutta la struttura di gioco
		String out = "";
		for(Casella casella : caselle) {
			out = out.concat("\n-------------------------------------\n");
			switch(casella.getTipo()) {
			case Casella.TIPO_STAZIONE:
				out = out.concat("Stazione ");
				break;
			case Casella.TIPO_PROBABILITA:
				out = out.concat("Probabilità ");
				break;
			case Casella.TIPO_IMPREVISTO:
				out = out.concat("Imprevisto ");
				break;
			case Casella.TIPO_EDIFICIO:
				out = out.concat("Edificio ");
				break;
				
			case Casella.TIPO_INIZIALE:
				out = out.concat("Inizio ");
				break;
			}
			out = out.concat(casella.getNome());
			if(casella.getTipo() == Casella.TIPO_PROBABILITA || casella.getTipo() == Casella.TIPO_IMPREVISTO) out = out.concat(" " + ((ProbabilitaImprevisto)casella).getMessaggio() + " " + ((ProbabilitaImprevisto)casella).getOffset());
		}
		out = out.concat("\n\nCi sono " + edificiPresenti + " edifici");
		out = out.concat("\nCi sono " + stazioniPresenti + " stazioni");
		out = out.concat("\nCi sono " + probabilitaPresenti + " probabilita");
		out = out.concat("\nCi sono " + imprevistiPresenti + " imprevisti");
		return out;
	}
	
	public String viewStatsTabellone() {	//Visualizza le statistiche ridotte, da visualizzare prima dell'inizio ddella partita
		String out = "";
		out = out.concat("\nCi sono " + edificiPresenti + " edifici");
		out = out.concat("\nCi sono " + stazioniPresenti + " stazioni");
		out = out.concat("\nCi sono " + probabilitaPresenti + " probabilita");
		out = out.concat("\nCi sono " + imprevistiPresenti + " imprevisti");
		return out;
	}

	public ArrayList<Casella> getCaselle() {	//Ritorna l'intero arraylist
		return caselle;
	}
		
	public String getNome() {
		return nome;
	}

	public int getId() {
		return id;
	}

	public static void creaTabelloni(int num) {	//Metodo statico usato per creare l'archivio dei tabelloni
		for(int i=0; i<num; i++) {
			try {
				archivioMappe.add(new Tabellone(i+1, UnipolyMain.DIM_TABELLONE, UnipolyMain.NUM_STAZIONI));	//Aggiungo tutti i tabelloni in un arraylist
			} catch (IncorrectSizeException e) {
			}
		}
	}
}

