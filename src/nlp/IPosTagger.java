package nlp;

public interface IPosTagger {
	
	public enum POS {
		N ("N"), 
		G ("G"), 
		D ("D"), 
		A ("A"), 
		L ("L"), 
		I ("I"), 
		U ("U"), 
		V ("V");
		
		private final String name;       

	    private POS(String s) {
	        name = s;
	    }
	    
	    public String toString() {
	    	return this.name;
	    }
	}
	
	public POS tag(String token);

}
