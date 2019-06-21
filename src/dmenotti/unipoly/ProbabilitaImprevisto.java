package dmenotti.unipoly;

public class ProbabilitaImprevisto extends Casella {
	private String messaggio;
	private int offset;

	public ProbabilitaImprevisto(int id, int tipo, String nome, String messaggio, int offset, int settore) {
		super(id, tipo, nome, settore);
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
