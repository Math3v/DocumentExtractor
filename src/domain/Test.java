package domain;

import nlp.BonitoPosTagger;
import nlp.IPosTagger;
import nlp.IPosTagger.POS;
import utils.Logger;
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
		testRemoveBrackets();
		Main.l.logln("Tests passed!", Logger.CRI);
	}

	public void testQuantityClassifier() {
		QuantityClassifier c = new QuantityClassifier();
		assert 2 == c.quantityCount("10 ml a 5 mikrogramov");
		assert 1 == c.quantityCount(" alebo preco lebo 8 g len");
		assert 2 == c.quantityCount(" ahoj 3 ml a 5,0 g");
		assert 3 == c.quantityCount("raz 2,3 mg a 3.5 ml lebo 5 mikrogramov");
		assert 2 == c.quantityCount("30 mg lebo 6 ml");
		assert 2 == c.quantityCount("obsahuje: 320 mikrogramov budezonidu/inhaláciu a 9 mikrogramov dihydrátu formoteroliumfumarátu/inhaláciu");
		assert 5 == c.quantityCount("Saponín 5 mg, hemihydrát kodeíniumfosfátu 3,06 mg, prilbicová tinktúra 40 mg, pomarančová tinktúra 150 mg, tekutý tymianový extrakt 30 mg v");
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
		assert ir.getIntermediateRepresentation("25")[0] == "Quantity";
		assert ir.getIntermediateRepresentation("mg")[0] == "Quantity";
		assert ir.getIntermediateRepresentation("monohydrátu")[0] == "SSis2";
		assert ir.getIntermediateRepresentation("lakózy")[0] == "SSfs2";
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
	
	public void testRemoveBrackets() {
		assert Section.removeBrackets("slovo (E 240) slovo slovo").equals("slovo  slovo slovo");
		assert Section.removeBrackets("slovo (20 mg monohydratu) slovo").equals("slovo 20 mg monohydratu slovo");
		assert Section.removeBrackets("nieco (E240) nieco nic").equals("nieco  nieco nic");
		assert Section.removeBrackets("mikrogramu tiotropia (ako monohydrát bromidu) a 2,5 mikrogramu olodaterolu (ako hydrochlorid) na jeden vstrek")
				.equals("mikrogramu tiotropia  a 2,5 mikrogramu olodaterolu  na jeden vstrek");
		assert Section.removeBrackets("slovo (E240).").equals("slovo .");
		assert Section.removeBrackets("(10 mikrogramov) cholekalciferolu").equals("10 mikrogramov cholekalciferolu");
	}
}
