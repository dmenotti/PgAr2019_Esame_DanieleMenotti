package dmenotti.unipoly;

import java.util.Scanner;

public class UnipolyMain {
	private static final int DENARO_INIZIALE = 100000;
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
			System.out.print("Premi invio per tirare i dadi");
			sc.nextLine();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			int tiroDadi = Utilities.random(1, 6);
			System.out.println("I dadi dicono: " + tiroDadi);
			g.avanzaDiN(tiroDadi);
			System.out.println("Sei stato spostato a " + t.getCasella(g.getPosizione()).getNome());
			checkTipo();
		} while(g.getDenaro()>0 && g.getDenaro()<1000000);
		
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
		if(g.getDenaro()<1000000) System.out.println("Ora hai " + g.getDenaro() + " I€€€");
	}
	
	private static void actionImprevisto() {
		System.out.println(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getMessaggio() + ", spendi " + -((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset() + " I€€€");
		g.aggiornaDenaro(((ProbabilitaImprevisto)t.getCasella(g.getPosizione())).getOffset());
		if(g.getDenaro()>0) System.out.println("Ora hai " + g.getDenaro() + " I€€€");
	}
	
	private static void actionStazione() {
		System.out.println("Ecco le stazioni disponibili:");
		for(Casella casella : t.getCaselle()) {
			if(casella.getTipo() == Casella.TIPO_STAZIONE) {
				System.out.println("ID " + casella.getId() + ", Stazione di " + casella.getNome());
			}
		}
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
		Giocatore g = new Giocatore(0, sc.nextLine(), DENARO_INIZIALE);
		System.out.println(g.getNome() + ", inizi il gioco con " + DENARO_INIZIALE + " I€€€");
		return g;
	}
	
	private static void posAttuale() {
		System.out.println("Attualmente ti trovi a " + t.getCasella(g.getPosizione()).getNome());
	}
}
