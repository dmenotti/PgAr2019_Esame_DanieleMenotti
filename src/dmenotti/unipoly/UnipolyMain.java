package dmenotti.unipoly;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UnipolyMain {
	private static final int DADO_MAX = 12;
	private static final int DADO_MIN = 1;
	private static final int DENARO_MIN = 0;
	private static final int DENARO_MAX = 1000000;
	private static final int DENARO_INIZIALE = 250000;
	public static final int NUM_STAZIONI = 2;
	public static final int DIM_TABELLONE = 6;
	private static Scanner sc = new Scanner(System.in);
	private static Tabellone t = null;
	private static Giocatore g = null;
	
	public static void main(String[] args) {
		System.out.println("Benvenuto in Unipoly");
		g = creaGiocatore();
		System.out.print("Vuoi scegliere il tabellone? (S)i, (N)o, default no: ");
		if(sc.nextLine().equalsIgnoreCase("S")) {
			if(scegliTabellone() == -1) return;
		}
		else creaTabellone();
		posAttuale();
		gioca();
	}

	private static void gioca() {
		do {
			System.out.print("Premi invio per tirare i dadi o scrivi q per uscire ");
			if(sc.nextLine().equalsIgnoreCase("q")) return;
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			int tiroDadi = Utilities.random(DADO_MIN, DADO_MAX);
			System.out.println("I dadi dicono: " + tiroDadi);
			g.avanzaDiN(tiroDadi);
			System.out.println("Sei stato spostato a " + t.getCasella(g.getPosizione()).getNome());
			checkTipo();
		} while(g.getDenaro()>DENARO_MIN && g.getDenaro()<DENARO_MAX);
		System.out.println("La partita è terminata!");
		if(g.getDenaro()<DENARO_MIN) System.out.println("Sei andato in bancarotta con un debito di " + (-g.getDenaro()) + " I€€€");
		if(g.getDenaro()>DENARO_MAX) System.out.println("Hai vinto con " + g.getDenaro() + " I€€€");
	}
	
	private static void checkTipo() {
		switch(t.getCasella(g.getPosizione()).getTipo()) {
		case Casella.TIPO_INIZIALE:
			System.out.println("Casella iniziale: puoi riposarti, non succedera' nulla");
			break;
			
		case Casella.TIPO_EDIFICIO:
			System.out.println("Edificio: che bel cantiere...");
			actionEdificio();
			break;
			
		case Casella.TIPO_PROBABILITA:
			System.out.println("Probabilita': e' il tuo giorno fortunato!");
			actionProbabilita();
			break;
			
		case Casella.TIPO_IMPREVISTO:
			System.out.println("Imprevisto: preparati a dover spendere...");
			actionImprevisto();
			break;
			
		case Casella.TIPO_STAZIONE:
			System.out.println("Stazione: Choo Choo, tutti in carrozza!");
			actionStazione();
			break;
		}
	}
	
	private static void actionEdificio() {
		if(((Edificio)t.getCasella(g.getPosizione())).getEdifPresente() == Edificio.TIPO_EDIF_NESSUNO) {
			actionAcquistaEdificio();
		} else if(((Edificio)t.getCasella(g.getPosizione())).getEdifPresente() == Edificio.TIPO_EDIF_CASA) {
			if(((Edificio)t.getCasella(g.getPosizione())).getProprietario() == g.getId()) {
				System.out.println("Sei proprietario di una casa!");
				System.out.println("Incassi " + ((Edificio)t.getCasella(g.getPosizione())).getGuadCasa() + " I€€€");
				g.aggiornaDenaro(((Edificio)t.getCasella(g.getPosizione())).getGuadCasa());
			}
		} else if(((Edificio)t.getCasella(g.getPosizione())).getEdifPresente() == Edificio.TIPO_EDIF_ALBERGO) {
			if(((Edificio)t.getCasella(g.getPosizione())).getProprietario() == g.getId()) {
				System.out.println("Sei proprietario di un albergo!");
				System.out.println("Incassi " + ((Edificio)t.getCasella(g.getPosizione())).getGuadAlbergo() + " I€€€");
				g.aggiornaDenaro(((Edificio)t.getCasella(g.getPosizione())).getGuadAlbergo());
			}
		}
	}

	private static void actionAcquistaEdificio() {
		int risAzione = -1;
		do {
			System.out.println("La tua disponibilita' e' di " + g.getDenaro() + " I€€€");
			System.out.println("Vuoi acquistare un edificio? (C)asa, (A)lbergo, invio per non acquistare nulla");
			System.out.println("Una casa costa " + ((Edificio)t.getCasella(g.getPosizione())).getCostoCasa() + " I€€€");
			System.out.println("Un albergo costa " + ((Edificio)t.getCasella(g.getPosizione())).getCostoAlbergo() + " I€€€");
			String azione = sc.nextLine();
			if(!azione.isBlank()) {
				azione = Character.toString(sc.nextLine().charAt(0));
				if(azione.equalsIgnoreCase("c")) {
					risAzione = acquistaCasa();
					
				} else if(azione.equalsIgnoreCase("a")) {
					risAzione = acquistaAlbergo();
				} else risAzione = 0;
			} else risAzione = 0;
		} while(risAzione==-1);
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
		}
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
	
	private static void actionProbabilita() {
		System.out.println(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getMessaggio() + ", ricevi " + ((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset() + " I€€€");
		g.aggiornaDenaro(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset());
		if(g.getDenaro()<DENARO_MAX) System.out.println("Ora hai " + g.getDenaro() + " I€€€");
	}
	
	private static void actionImprevisto() {
		System.out.println(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getMessaggio() + ", spendi " + -((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset() + " I€€€");
		g.aggiornaDenaro(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset());
		if(g.getDenaro()>DENARO_MIN) System.out.println("Ora hai " + g.getDenaro() + " I€€€");
	}
	
	private static void actionStazione() {
		do {
		System.out.println("\nEcco le stazioni disponibili:");
		for(Casella casella : t.getCaselle()) {
			if(casella.getTipo() == Casella.TIPO_STAZIONE && casella.getId() != t.getCasella(g.getPosizione()).getId()) {
				System.out.println("ID " + casella.getId() + ", Stazione di " + casella.getNome());
			}
		}
		System.out.println("Scrivi l'ID della stazione di destinazione; se non vuoi spostarti premi semplicemente invio: ");
		String dest = sc.nextLine();
		if(dest.isBlank()) return;
		else {
			int numDest = -1;
			try {
				numDest = Integer.parseInt(dest);
			} catch (NumberFormatException e) {
				
			}
				if(numDest<t.getCaselle().size() && numDest>0) {
					if(t.getCasella(numDest).getTipo() == Casella.TIPO_STAZIONE) {
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
			System.out.println("Quanti tabelloni vuoi creare? (2-20): ");
			try {
				numTabelloni = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
			}
			if(numTabelloni>=2 && numTabelloni<=20) break;
		} while(true);
		Tabellone.creaTabelloni(numTabelloni);
		System.out.println("I tabelloni disponibili sono:");
		for(Tabellone tabEstratto : Tabellone.archivioMappe) {
			System.out.println("Tabellone " + tabEstratto.getId() + ", " + tabEstratto.getNome());
		}
		
		boolean scelto = false;
		do {
			System.out.print("Scrivi il nome del tabellone scelto o q per uscire: ");
			String tabScelto = sc.nextLine();
			if(tabScelto.equalsIgnoreCase("q")) return -1;
			for(Tabellone tabEstratto : Tabellone.archivioMappe) {
				if(tabEstratto.getNome().equalsIgnoreCase(tabScelto)) {
					t = tabEstratto;
					scelto = true;
				}
			}
			if(!scelto) System.out.println("Nome non presente!");
		} while(!scelto);
		
		System.out.println("Il tabellone scelto possiete i seguenti parametri:");
		System.out.println(t.viewStatsTabellone());
		return 0;
	}
	
	private static void creaTabellone() {
		do {
			try {
				t = new Tabellone(0, DIM_TABELLONE, NUM_STAZIONI);
			} catch (IncorrectSizeException e) {
				// TODO Auto-generated catch block
				System.out.println("Oh-oh, errore:" + e.getMessage());
			}
		} while(t==null);
			
		System.out.println("Ho generato un tabellone con i seguenti parametri:");
		System.out.println(t.viewStatsTabellone());
	}
	
	private static Giocatore creaGiocatore() {
		System.out.print("E' ora di creare il tuo personaggio.\nCome ti chiami? ");
		Giocatore g = new Giocatore(0, sc.nextLine(), DENARO_INIZIALE);
		System.out.println(g.getNome() + ", inizi il gioco con " + DENARO_INIZIALE + " I€€€");
		return g;
	}
	
	private static void posAttuale() {
		System.out.println("Attualmente ti trovi a " + t.getCasella(g.getPosizione()).getNome());
	}
}
