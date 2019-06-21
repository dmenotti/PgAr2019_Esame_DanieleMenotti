package dmenotti.unipoly;

public class Edificio extends Casella {
	public static final int TIPO_CASA = 1;
	public static final int TIPO_ALBERGO = 2;
	public static final int BASE_COSTO = 5000;
	public static final int BASE_GUADAGNO = 500;
	
	private int costoCasa;
	private int costoAlbergo;
	private int guadCasa;
	private int guadAlbergo;
	private int edifPresente = 0;
	private String proprietario;

	public Edificio(int id, int tipo, String nome) {
		super(id, tipo, nome);
		costoCasa = BASE_COSTO * (int)(1.0 + (id/10.0));
		costoAlbergo = BASE_COSTO * (int)(1.5 + ((id*2.0)/10.0));
		guadCasa = BASE_GUADAGNO * (int)(1.0 + (id/10.0));
		guadAlbergo = BASE_GUADAGNO * (int)(1.5 + ((id*2.0)/10.0));
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}

	public int getEdifPresente() {
		return edifPresente;
	}

	public int getCostoCasa() {
		return costoCasa;
	}

	public int getCostoAlbergo() {
		return costoAlbergo;
	}

	public int getGuadCasa() {
		return guadCasa;
	}

	public int getGuadAlbergo() {
		return guadAlbergo;
	}

}
