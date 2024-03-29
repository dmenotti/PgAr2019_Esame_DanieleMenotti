package dmenotti.unipoly;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UnipolyMain {
	private static final int DADO_MAX = 12;			//Numero massimo e minimo che può uscire dal dado
	private static final int DADO_MIN = 1;
	private static final int DENARO_MIN = 0;
	private static final int DENARO_MAX = 1000000;	//Soglie per vincita e bancarotta
	private static final int DENARO_INIZIALE = 250000;	//Denaro iniziale posseduto dal giocatore
	public static final int NUM_STAZIONI = 4;
	public static final int DIM_TABELLONE = 40;		//Numero di caselle e stazioni totali. DIM_TABELLONE comprende anche il numero di stazioni
	private static Scanner sc = new Scanner(System.in);
	private static Tabellone t = null;		//Oggetti tabellone e giocatore
	private static Giocatore g = null;
	
	public static void main(String[] args) {
		System.out.println("Benvenuto in Unipoly");
		g = creaGiocatore();
		System.out.print("Vuoi scegliere il tabellone? (S)i, (N)o, default no: ");
		if(sc.nextLine().equalsIgnoreCase("S")) {
			if(scegliTabellone() == -1) return;		//Se inserisco q durante la scelta del tabellone posso uscire dal programma
		}
		else creaTabellone();		//Se non ne scelgo uno ne viene creato uno random
		posAttuale();				//Mi informa della mia posizione attuale (solo all'inizio), non ha un'utilità ai fini del gioco ma lo rende più bello
		gioca();					//Tutte le funzioni di gioco sono parte di questo metodo
	}

	private static void gioca() {
		do {
			System.out.print("Premi invio per tirare i dadi o scrivi q per uscire ");
			if(sc.nextLine().equalsIgnoreCase("q")) return;								//Uscita rapida dal gioco
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");	//Un po' di spazio tra una giocata e l'altra
			int tiroDadi = Utilities.random(DADO_MIN, DADO_MAX);
			System.out.println("I dadi dicono: " + tiroDadi);
			g.avanzaDiN(tiroDadi);														//Avanzo di "tiroDadi" poszioni
			System.out.println("Sei stato spostato a " + t.getCasella(g.getPosizione()).getNome());		//Il gioco mi mostra il nome della casella
			checkTipo();	//In base al tipo di cella faccio una determinata azione
		} while(g.getDenaro()>DENARO_MIN && g.getDenaro()<DENARO_MAX);		//Se esco dal ciclo significa che ho vinto o sono andato in bancarotta
		System.out.println("La partita è terminata!");
		if(g.getDenaro()<DENARO_MIN) System.out.println("Sei andato in bancarotta con un debito di " + (-g.getDenaro()) + " I€€€");		//Mostra il "saldo finale"
		if(g.getDenaro()>DENARO_MAX) System.out.println("Hai vinto con " + g.getDenaro() + " I€€€");
		
		System.out.print("Vuoi vedere la struttura del tabellone? (S)i, (N)o, default no ");
		String input = sc.nextLine();
		if(!input.isBlank()) {																	//Se l'utente vuole può vedere la struttura del tabellone su cui ha giocato
			if(Character.toString(input.charAt(0)).equalsIgnoreCase("s")) System.out.println(t.viewTabellone());
		}
	}
	
	private static void checkTipo() {
		switch(t.getCasella(g.getPosizione()).getTipo()) {
		case Casella.TIPO_INIZIALE:
			System.out.println("Casella iniziale: puoi riposarti, non succedera' nulla");
			break;
			
		case Casella.TIPO_EDIFICIO:
			System.out.println("Edificio: che bel cantiere...");
			actionEdificio();	//Azioni specifiche per il tipo edificio
			break;
			
		case Casella.TIPO_PROBABILITA:
			System.out.println("Probabilita': e' il tuo giorno fortunato!");
			actionProbabilita();	//Azioni specifiche per il tipo probabilità
			break;
			
		case Casella.TIPO_IMPREVISTO:
			System.out.println("Imprevisto: preparati a dover spendere...");
			actionImprevisto();		//Azioni specifiche per il tipo imprevisto
			break;
			
		case Casella.TIPO_STAZIONE:
			System.out.println("Stazione: Choo Choo, tutti in carrozza!");
			actionStazione();		//Azioni specifiche per il tipo stazione
			break;
		}
	}
	
	private static void actionEdificio() {
		System.out.println("La proprietà appartiene alla zona " + ((Edificio)t.getCasella(g.getPosizione())).getGruppoAppartenenza());	//Mostro la zona di appartenenza
		boolean proprietarioBlocco = proprietarioDelBlocco(((Edificio)t.getCasella(g.getPosizione())).getGruppoAppartenenza());			//La struttura (Edificio)t.getCasella(g.getPosizione())).----- la si vede spesso: per poter usare le funzioni di edificio devo castare il tipo di casella, in quanto le salvo all'interno dell'array solo come "Casella generica"
		if(((Edificio)t.getCasella(g.getPosizione())).getEdifPresente() == Edificio.TIPO_EDIF_NESSUNO) {								
			actionAcquistaEdificio();																									//Se l'edificio non è di nessuno lo posso acquistare
		} else if(((Edificio)t.getCasella(g.getPosizione())).getEdifPresente() == Edificio.TIPO_EDIF_CASA) {
			if(((Edificio)t.getCasella(g.getPosizione())).getProprietario() == g.getId()) {												//Se è casa o albergo controllo chi è il proprietario, se è il giocatore "attivo" (ovvero l'unico) gli mostro un messaggio
				System.out.println("Sei proprietario di una casa!");
				int incasso = 0;
				if(proprietarioBlocco) incasso = (int) (((Edificio)t.getCasella(g.getPosizione())).getGuadCasa() * Edificio.MOLT_BLOCCO);	//Se il proprietario possiede l'intero blocco allora moltiplico il guadagno per l'apposito moltiplicatore
				else incasso = (int) (((Edificio)t.getCasella(g.getPosizione())).getGuadCasa());
				System.out.println("Incassi " + incasso + " I€€€");
				g.aggiornaDenaro(incasso);																								//Aggiorno il denaro in possesso del giocatore
			}
		} else if(((Edificio)t.getCasella(g.getPosizione())).getEdifPresente() == Edificio.TIPO_EDIF_ALBERGO) {							//La stessa cosa si ripete per l'albergo
			if(((Edificio)t.getCasella(g.getPosizione())).getProprietario() == g.getId()) {
				System.out.println("Sei proprietario di un albergo!");
				int incasso = 0;
				if(proprietarioBlocco) incasso = (int) (((Edificio)t.getCasella(g.getPosizione())).getGuadAlbergo() * Edificio.MOLT_BLOCCO);
				else incasso = (int) (((Edificio)t.getCasella(g.getPosizione())).getGuadAlbergo());
				System.out.println("Incassi " + incasso + " I€€€");
				g.aggiornaDenaro(incasso);
			}
		}
	}
	
	private static boolean proprietarioDelBlocco(int zona) {
		for(Casella casella : t.getCaselle()) {																			//Controllo se seono proprietario del blocco (controllando se esistono caselle del blocco non appartenenti a me)
			if((casella.getTipo() == Casella.TIPO_EDIFICIO && ((Edificio)casella).getGruppoAppartenenza() == zona)) {
				if(((Edificio)casella).getProprietario() != g.getId()) return false;
			}
		}
		System.out.println("Sei proprietario dell'intero blocco! Avrai un incasso bonus dalle tue proprietà!");
		return true;
	}
	private static void actionAcquistaEdificio() {
		int risAzione = -1;
		do {
			System.out.println("La tua disponibilita' e' di " + g.getDenaro() + " I€€€");										//Recupero le informazioni sull'edificio (prezzo casa e albergo) e le visualizzo
			System.out.println("Vuoi acquistare un edificio? (C)asa, (A)lbergo, invio per non acquistare nulla");
			System.out.println("Una casa costa " + ((Edificio)t.getCasella(g.getPosizione())).getCostoCasa() + " I€€€");
			System.out.println("Un albergo costa " + ((Edificio)t.getCasella(g.getPosizione())).getCostoAlbergo() + " I€€€");
			String azione = sc.nextLine();
			if(!azione.isBlank()) {
				azione = Character.toString(azione.charAt(0));
				if(azione.equalsIgnoreCase("c")) {
					risAzione = acquistaCasa();
																	//In base all'azione effettuata (inserito "c", "a" o nulla) acquisto una casa, un albergo o passo oltre
				} else if(azione.equalsIgnoreCase("a")) {
					risAzione = acquistaAlbergo();
				} else risAzione = 0;
			} else risAzione = 0;
		} while(risAzione==-1);			//risAzione -1 è per indicare che non ho acquistato nulla
	}

	private static int acquistaAlbergo() {
		if(g.getDenaro()-((Edificio)t.getCasella(g.getPosizione())).getCostoAlbergo()>0) {
			((Edificio)t.getCasella(g.getPosizione())).setEdifPresente(Edificio.TIPO_EDIF_ALBERGO);
			((Edificio)t.getCasella(g.getPosizione())).setProprietario(g.getId());
			g.aggiornaDenaro(-((Edificio)t.getCasella(g.getPosizione())).getCostoAlbergo());
			System.out.println("Hai acquistato un albergo!");
			return 0;
		} else {
			System.out.println("Non hai abbastanza soldi :(");
			return -1;
		}																												//Se il giocatore ha abbastanza soldi acquista una casa o albergo impostandone il tipo e il proprietario, in caso contrario visualizza un messaggio di errore
	}

	private static int acquistaCasa() {
		if(g.getDenaro()-((Edificio)t.getCasella(g.getPosizione())).getCostoCasa()>0) {
			((Edificio)t.getCasella(g.getPosizione())).setEdifPresente(Edificio.TIPO_EDIF_CASA);
			((Edificio)t.getCasella(g.getPosizione())).setProprietario(g.getId());
			g.aggiornaDenaro(-((Edificio)t.getCasella(g.getPosizione())).getCostoCasa());
			System.out.println("Hai acquistato una casa!");
			return 0;
		} else {
			System.out.println("Non hai abbastanza soldi :(");
			return -1;
		}
	}
	
	private static void actionProbabilita() {	//Se capito su una probabilità recupero testo e guadagno dagli attributi della casella, visualizzo il testo e aggiungo il guadagno al denaro posseduto
		System.out.println(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getMessaggio() + ", ricevi " + ((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset() + " I€€€");
		g.aggiornaDenaro(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset());
		if(g.getDenaro()<DENARO_MAX) System.out.println("Ora hai " + g.getDenaro() + " I€€€");
	}
	
	private static void actionImprevisto() {	//Se capito su un imprevisto recupero testo e perdita dagli attributi della casella, visualizzo il testo e tolgo la perdita dal denaro posseduto
		System.out.println(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getMessaggio() + ", spendi " + -((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset() + " I€€€");
		g.aggiornaDenaro(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset());
		if(g.getDenaro()>DENARO_MIN) System.out.println("Ora hai " + g.getDenaro() + " I€€€");
	}
	
	private static void actionStazione() {
		do {													//Nel caso capitassi su una stazione recupero tutte le altre stazioni presenti nel tabellone e le mostro all'utente
		System.out.println("\nEcco le stazioni disponibili:");
		for(Casella casella : t.getCaselle()) {
			if(casella.getTipo() == Casella.TIPO_STAZIONE && casella.getId() != t.getCasella(g.getPosizione()).getId()) {
				System.out.println("ID " + casella.getId() + ", Stazione di " + casella.getNome());
			}
		}
		System.out.print("Scrivi l'ID della stazione di destinazione; se non vuoi spostarti premi semplicemente invio: ");
		String dest = sc.nextLine();
		if(dest.isBlank()) return;
		else {								//Ogni casella ha un ID, qui viene usato per determinare la stazione di destinazione
			int numDest = -1;
			try {
				numDest = Integer.parseInt(dest);
			} catch (NumberFormatException e) {
				
			}
				if(numDest<t.getCaselle().size() && numDest>0) {
					if(t.getCasella(numDest).getTipo() == Casella.TIPO_STAZIONE) {			//Appurato che l'id inserito è valido ed è di una stazione avviene il viaggio vero e proprio
						System.out.println("In viaggio verso " + t.getCasella(numDest).getNome() + "...");
						g.avanzaVerso(numDest);
						return;
					} else System.out.println("Hai inserito un id non valido!");
				} else System.out.println("Hai inserito un id non valido!");
			}
		} while(true);
	}
	
	private static int scegliTabellone() {
		int numTabelloni = -1;
		do {
			System.out.print("Quanti tabelloni vuoi creare? (2-20): ");		//L'utente può decidere quanti tabelloni creare. Il numero massimo è dato dal numero di nomi dedicati ai tabelloni
			try {
				numTabelloni = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
			}
			if(numTabelloni>=2 && numTabelloni<=20) break;
			System.out.println("Numero inserito non valido!");
		} while(true);
		Tabellone.creaTabelloni(numTabelloni);
		System.out.println("I tabelloni disponibili sono:");				//Visualizzo l'elenco di tutti i tabelloni creati, con ID e nome
		for(Tabellone tabEstratto : Tabellone.archivioMappe) {
			System.out.println("Tabellone " + tabEstratto.getId() + ", " + tabEstratto.getNome());
		}
		
		boolean scelto = false;
		do {
			System.out.print("Scrivi il nome del tabellone scelto o q per uscire: ");
			String tabScelto = sc.nextLine();
			if(tabScelto.equalsIgnoreCase("q")) return -1;
			for(Tabellone tabEstratto : Tabellone.archivioMappe) {			//Una volta chiesto il nome all'utente cerco se questo esiste all'interno dell'array. Se esiste associo il tabellone scelto a quello attivo e si è pronti per giocare
				if(tabEstratto.getNome().equalsIgnoreCase(tabScelto)) {
					t = tabEstratto;
					scelto = true;
				}
			}
			if(!scelto) System.out.println("Nome non presente!");
		} while(!scelto);
		
		System.out.println("Il tabellone scelto possiete i seguenti parametri:");
		System.out.println(t.viewStatsTabellone());							//Visualizzo i parametri del tabellone
		return 0;
	}
	
	private static void creaTabellone() {
		do {
			try {
				t = new Tabellone(0, DIM_TABELLONE, NUM_STAZIONI);			//Creo un unico tabellone, che sarà quello utilizzato nella partita
			} catch (IncorrectSizeException e) {
				System.out.println("Oh-oh, errore:" + e.getMessage());
			}
		} while(t==null);
			
		System.out.println("Ho generato un tabellone con i seguenti parametri:");
		System.out.println(t.viewStatsTabellone());							//Visualizzo le statistiche del tabellone appena creato
	}
	
	private static Giocatore creaGiocatore() {
		System.out.print("E' ora di creare il tuo personaggio.\nCome ti chiami? ");
		String nome;														//Chiedo il nome del giocatore e gli assegno il denaro iniziale
		do {
			nome = sc.nextLine();
		} while(nome.isBlank());
		Giocatore g = new Giocatore(0, nome, DENARO_INIZIALE);
		System.out.println(g.getNome() + ", inizi il gioco con " + DENARO_INIZIALE + " I€€€");
		return g;
	}
	
	private static void posAttuale() {
		System.out.println("Attualmente ti trovi a " + t.getCasella(g.getPosizione()).getNome());		//Visualizzo la posizione attuale. Usato solo all'inizio del programma
	}
}
