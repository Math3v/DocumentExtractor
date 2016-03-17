package nlp;

public class QuantityClassifier {
	
	private String num = "\\d{1,5}";
	private String numDot = "\\d{1,5}\\.\\d{1,5}";
	private String numComma = "\\d{1,5},\\d{1,5}";
	private String quantityToken = "(g|mg|ml|mikrogramov|mikrogramom|mikrogramu|mikrogramy|miligramov|mmol|ml|mmol|mg)(\\.|\\,)?";
	
	public int quantityCount(String s) {
		ITokenizer t = new SimpleTokenizer();
		String[] tokens = t.tokenize(s);
		int count = 0;
		
		for( int i = 0; i <= tokens.length - 1; i++ ) {
			if(tokens[i].length() < 1) {
				continue;
			}
			if( tokens[i].charAt(tokens[i].length() - 1) == '.' || 
				tokens[i].charAt(tokens[i].length() - 1) == ',' ) {
				tokens[i] = tokens[i].substring(0, tokens[i].length() - 1);
			}
			if( tokens[i].matches("("+num+"|"+numDot+"|"+numComma+"){1}"+quantityToken) ) {
				count++;
			}else if( tokens[i].matches(num) || tokens[i].matches(numDot) || tokens[i].matches(numComma) ) {
				if( i != tokens.length - 1 && tokens[i+1].matches(quantityToken) ) {
					count++;
				}
			}
		}
		
		return count;
	}

}
