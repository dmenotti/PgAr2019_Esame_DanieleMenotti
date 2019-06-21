package dmenotti.unipoly;

public class Casella {
	private int id;
	private int tipo;
	private String nome;
	
	
	public Casella(int id, int tipo, String nome) {
		this.id = id;
		this.tipo = tipo;
		this.nome = nome;
	}


	public int getId() {
		return id;
	}


	public int getTipo() {
		return tipo;
	}


	public String getNome() {
		return nome;
	}
	
	
}
