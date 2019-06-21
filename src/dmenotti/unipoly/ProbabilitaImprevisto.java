package dmenotti.unipoly;

public class ProbabilitaImprevisto extends Casella {	//Estensione della classe Casella contenente gli attributi specifici delle caselle probabilità e imprevisto
	private String messaggio;
	private int offset;

	public ProbabilitaImprevisto(int id, int tipo, String nome, String messaggio, int offset) {
		super(id, tipo, nome);
		this.messaggio = messaggio;
		this.offset = offset;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public int getOffset() {
		return offset;
	}
	
	

}
