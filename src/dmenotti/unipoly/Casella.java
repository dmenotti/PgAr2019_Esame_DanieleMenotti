package dmenotti.unipoly;

public class Casella {
	public static final int TIPO_INIZIALE = 0;
	public static final int TIPO_STAZIONE = 1;
	public static final int TIPO_PROBABILITA = 2;
	public static final int TIPO_IMPREVISTO = 3;
	public static final int TIPO_EDIFICIO = 4;
	
	
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
	


	public static int getTipoRandom() {
		int num = (int)Math.round(Math.random()*3+2);
		if(num>4) num = 4;
		return num;
	}
}
