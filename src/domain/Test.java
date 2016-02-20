package domain;

import nlp.BonitoPosTagger;
import nlp.IPosTagger;
import nlp.IPosTagger.POS;
import nlp.ITokenizer;
import nlp.QuantityClassifier;
import nlp.SimplePosTagger;
import nlp.SimpleTokenizer;

public class Test {
	
	public Test() {
		testSimpleTokenizer();
		testQuantityClassifier();
		testSimplePosTagger();
		testBonitoPosTagger();
		System.out.println("Tests passed!");
	}

	public void testQuantityClassifier() {
		QuantityClassifier c = new QuantityClassifier();
		assert 2 == c.quantityCount("10 ml a 5 mikrogramov");
		assert 1 == c.quantityCount(" alebo preco lebo 8 g len");
		assert 2 == c.quantityCount(" ahoj 3 ml a 5,0 g");
		assert 3 == c.quantityCount("raz 2,3 mg a 3.5 ml lebo 5 mikrogramov");
		assert 2 == c.quantityCount("30 mg lebo 6 ml");
		assert 2 == c.quantityCount("obsahuje: 320 mikrogramov budezonidu/inhaláciu a 9 mikrogramov dihydrátu formoteroliumfumarátu/inhaláciu");
	}
	
	public void testSimplePosTagger() {
		IPosTagger pt = new SimplePosTagger();
		assert pt.tag("cinakalcetu") == POS.A;
		assert pt.tag("cinakalcetu ") == POS.A;
		assert pt.tag("monohydrátu") == POS.A;
		assert pt.tag("laktózy") == POS.G;
	}
	
	public void testIntermediateRepresentation() {
		IntermediateRepresentation ir = new IntermediateRepresentation();
		assert ir.getIntermediateRepresentation("25 mg monohydrátu laktózy") == "QAG";
		assert ir.getIntermediateRepresentation("10ml cinkalátu,") == "QA";
	}
	
	public void testSimpleTokenizer() {
		ITokenizer t = new SimpleTokenizer();
		assert 5 == t.tokenize(" 3 a 9 mikrogramov").length;
	}
	
	public void testBonitoPosTagger() {
		assert System.getenv("SNR_USERNAME").length() > 0;
		assert System.getenv("SNR_PASSWORD").length() > 0;
		IPosTagger bt = new BonitoPosTagger();
		assert bt.tag("mama") == POS.N;
		assert bt.tag("mame") == POS.D;
	}
}
