package nlp;

public class SimpleTokenizer implements ITokenizer {

	@Override
	public String[] tokenize(String s) {
		return s.split(new String(" "));
	}
	
}
