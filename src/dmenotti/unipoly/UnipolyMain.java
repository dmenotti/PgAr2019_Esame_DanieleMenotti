package dmenotti.unipoly;

import java.util.Scanner;

public class UnipolyMain {
	private static Scanner sc = new Scanner(System.in);
	
	
	public static void main(String[] args) {
		System.out.println("Benvenuto in Unipoly");
		Giocatore g = creaGiocatore();
		Tabellone t = null;
		do {
			try {
				t = new Tabellone(g, 40, 4);
			} catch (IncorrectSizeException e) {
				// TODO Auto-generated catch block
				System.out.println("Oh-oh, hai commesso un errore:" + e.getMessage());
			}
		} while(t==null);
			
		System.out.println("Ho generato un tabellone con i seguenti parametri:\n");
		System.out.println(t.viewStatsTabellone());
		
	}
	
	public static Giocatore creaGiocatore() {
		System.out.print("E' ora di creare il tuo personaggio.\nCome ti chiami? ");
		Giocatore g = new Giocatore(0, sc.nextLine(), 10000);
		System.out.println(g.getNome() + ", inizi il gioco con 10000 I€€€");
		return g;
	}
}
