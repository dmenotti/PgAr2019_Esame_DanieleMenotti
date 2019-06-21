package dmenotti.unipoly;

public class ProbabilitaImprevisto extends Casella {
	String messaggio;
	int offset;

	public ProbabilitaImprevisto(int id, int tipo, String nome, String messaggio, int offset) {
		super(id, tipo, nome);
		this.messaggio = messaggio;
		this.offset = offset;
	}

}
