package nlp;

public interface IPosTagger {
	
	public enum POS {
		N, G, D, A, L, I, U
	}
	
	public POS tag(String token);

}
