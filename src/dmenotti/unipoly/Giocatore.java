package dmenotti.unipoly;

public class Giocatore {
	private int id;
	private String nome;
	private int denaro;
	private int posizione = 0;
	
	
	public Giocatore(int id, String nome, int denaro) {
		this.id = id;
		this.nome = nome;
		this.denaro = denaro;
	}


	public int getId() {
		return id;
	}


	public String getNome() {
		return nome;
	}


	public int getDenaro() {
		return denaro;
	}


	public int getPosizione() {
		return posizione;
	}
	
	public void avanzaDiN(int i) {
		posizione+=6;
		if(posizione>UnipolyMain.DIM_TABELLONE) posizione = posizione%UnipolyMain.DIM_TABELLONE;
	}
	
	public void avanzaVerso(int dest) {
		posizione = dest;
	}
	
	public void aggiornaDenaro(int offset) {
		denaro+=offset;
	}
	
}
