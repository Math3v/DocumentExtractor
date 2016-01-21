package domain;

public class SimpleTokenizer implements Tokenizer {

	@Override
	public String[] tokenize(String s) {
		return s.split(new String(" "));
	}
	
}
