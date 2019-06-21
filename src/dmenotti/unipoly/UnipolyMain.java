package dmenotti.unipoly;

public class UnipolyMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Tabellone t = new Tabellone(null, 12, 4);
			System.out.println(t.viewTabellone());
		} catch (IncorrectSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
