package nlp;

import domain.BonitoService;

public class BonitoPosTagger implements IPosTagger {

	@Override
	public POS tag(String token) {
		BonitoService bService = new BonitoService();
		String tag = bService.getPosTag(token);
		switch(tag) {
		case "N":
			return POS.N;
		case "G":
			return POS.G;
		case "D":
			return POS.D;
		case "A":
			return POS.A;
		case "L":
			return POS.L;
		case "I":
			return POS.I;
		case "V":
			return POS.V;
		default:
			return POS.U;
		}
	}

}
