package dmenotti.unipoly;

public class UnipolyMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Giocatore g = new Giocatore(0, "Ernestino", 10000);
			Tabellone t = new Tabellone(g, 120, 50);
			System.out.println(t.viewTabellone());
		} catch (IncorrectSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
