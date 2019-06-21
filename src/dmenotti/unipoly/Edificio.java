package dmenotti.unipoly;

public class Edificio extends Casella {
	public static final int TIPO_EDIF_NESSUNO = 0;
	public static final int TIPO_EDIF_CASA = 1;
	public static final int TIPO_EDIF_ALBERGO = 2;
	public static final int BASE_COSTO = 5000;
	public static final int BASE_GUADAGNO = 500;
	public static final double MOLT_BLOCCO = 1.1;
	
	private int costoCasa;
	private int costoAlbergo;
	private int guadCasa;
	private int guadAlbergo;
	private int edifPresente = 0;
	private int proprietario;
	

	public Edificio(int id, int tipo, String nome, int settore) {
		super(id, tipo, nome, settore);
		costoCasa = (int) (BASE_COSTO * (1.0 + ((double)id/50.0)));
		costoAlbergo = (int) (BASE_COSTO * (1.5 + (((double)id*2.0)/50.0)));
		guadCasa = (int) (BASE_GUADAGNO * (1.0 + ((double)id/50.0)));
		guadAlbergo = (int) (BASE_GUADAGNO * (1.5 + (((double)id*2.0)/50.0)));
	}

	public int getProprietario() {
		return proprietario;
	}

	public void setProprietario(int proprietario) {
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

	public void setEdifPresente(int edifPresente) {
		this.edifPresente = edifPresente;
	}

}
