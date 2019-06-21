package dmenotti.unipoly;

import java.util.Scanner;

public class UnipolyMain {
	private static final int DENARO_MIN = 0;
	private static final int DENARO_MAX = 1000000;
	private static final int DENARO_INIZIALE = 250000;
	public static final int NUM_STAZIONI = 4;
	public static final int DIM_TABELLONE = 40;
	private static Scanner sc = new Scanner(System.in);
	private static Tabellone t = null;
	private static Giocatore g = null;
	
	public static void main(String[] args) {
		System.out.println("Benvenuto in Unipoly");
		g = creaGiocatore();
		creaTabellone();
		posAttuale();
		gioca();
	}

	private static void gioca() {
		do {
			System.out.print("Premi invio per tirare i dadi o scrivi q per uscire ");
			if(sc.nextLine().equalsIgnoreCase("q")) return;
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			int tiroDadi = Utilities.random(1, 6);
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
	
	private static void creaTabellone() {
		do {
			try {
				t = new Tabellone(g, DIM_TABELLONE, NUM_STAZIONI);
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
		Giocatore g = new Giocatore(DENARO_MIN, sc.nextLine(), DENARO_INIZIALE);
		System.out.println(g.getNome() + ", inizi il gioco con " + DENARO_INIZIALE + " I€€€");
		return g;
	}
	
	private static void posAttuale() {
		System.out.println("Attualmente ti trovi a " + t.getCasella(g.getPosizione()).getNome());
	}
}
