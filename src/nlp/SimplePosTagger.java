package nlp;

public class SimplePosTagger implements IPosTagger {

	@Override
	public POS tag(String token) {
		if( token.matches(".*u\\p{Space}*") ) {
			return POS.A;
		} else if( token.matches(".*y\\p{Space}*") ) {
			return POS.G;
		} else {
			return POS.U;
		}
	}

}
