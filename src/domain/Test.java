package domain;

import nlp.IPosTagger;
import nlp.IPosTagger.POS;
import nlp.QuantityClassifier;
import nlp.SimplePosTagger;

public class Test {
	
	public Test() {
		testQuantityClassifier();
		testSimplePosTagger();
		System.out.println("Tests passed!");
	}

	public void testQuantityClassifier() {
		QuantityClassifier c = new QuantityClassifier();
		assert 2 == c.quantityCount("10 ml a 5 mikrogramov");
		assert 1 == c.quantityCount(" alebo preco lebo 8 g len");
		assert 2 == c.quantityCount(" ahoj 3 ml a 5,0 g");
		assert 3 == c.quantityCount("raz 2,3 mg a 3.5 ml lebo 5 mikrogramov");
		assert 2 == c.quantityCount("30 mg lebo 6 ml");
	}
	
	public void testSimplePosTagger() {
		IPosTagger pt = new SimplePosTagger();
		assert pt.tag("cinakalcetu") == POS.A;
		assert pt.tag("cinakalcetu ") == POS.A;
		assert pt.tag("monohydrátu") == POS.A;
		assert pt.tag("laktózy") == POS.G;
	}
}
