package dmenotti.unipoly;

public class IncorrectSizeException extends Exception { //Verso la fine mi sono reso conto che avrei poteto non crearla, ma ormai ci sarebbe stato da cambiare tutto il codice...
	private static final long serialVersionUID = -4764903282040312176L;			//Un'eccezione molto base invocata nel caso si inserisse un numero di caselle o stazioni troppo basso


	public IncorrectSizeException(String message) {
		super(message);
	}

}
