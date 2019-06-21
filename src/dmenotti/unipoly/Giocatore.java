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
	
	
	
}
