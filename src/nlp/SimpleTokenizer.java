package nlp;

public class SimpleTokenizer implements ITokenizer {

	@Override
	public String[] tokenize(String s) {
		return s.replace("\u00A0", " ").split("(\\s|/)+");
	}
	
}
