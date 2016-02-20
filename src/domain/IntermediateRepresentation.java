package domain;

import nlp.BonitoPosTagger;
import nlp.IPosTagger;
import nlp.ITokenizer;
import nlp.QuantityClassifier;
import nlp.SimplePosTagger;
import nlp.SimpleTokenizer;
import nlp.IPosTagger.POS;

public class IntermediateRepresentation {
	
	private boolean isAccusative(String token) {
		IPosTagger t = new SimplePosTagger();
		return POS.A == t.tag(token);
	}
	
	private boolean isGenitiv(String token) {
		IPosTagger t = new SimplePosTagger();
		return POS.G == t.tag(token);
	}
	
	private boolean isQuantity(String t1, String t2) {
		QuantityClassifier c = new QuantityClassifier();
		return 1 == c.quantityCount(new String(t1 + " " + t2));
	}
	
	public String getIntermediateRepresentation(String section) {
		ITokenizer t = new SimpleTokenizer();
		String[] tokens = t.tokenize(section);
		BonitoPosTagger bt = new BonitoPosTagger();
		String result = "";
		
		for(int i = 0; i < tokens.length; i++) {
			if( i != tokens.length - 1 && isQuantity(tokens[i], tokens[i+1]) ) {
				i++;
				result += "QQ";
			} else {
				result += bt.tag(tokens[i]).toString();
			}
		}
		
		return result;
	}

}
