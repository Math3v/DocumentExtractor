package nlp;

public class QuantityClassifier {
	
	public int quantityCount(String s) {
		ITokenizer t = new SimpleTokenizer();
		String[] tokens = t.tokenize(s);
		int count = 0;
		
		for( int i = 0; i < tokens.length - 1; i++ ) {
			if( tokens[i].matches("\\d{1,5}") ||
				tokens[i].matches("\\d{1,5}\\.\\d{1,5}") ||
				tokens[i].matches("\\d{1,5},\\d{1,5}") ) {
				if( tokens[i+1].matches("g|mg|ml|mikrogramov|mikrogramom|mikrogramu|miligramov|mmol|ml\\.|mmol\\.|mg\\.") ) {
					count++;
				}
			}
		}
		
		return count;
	}

}
