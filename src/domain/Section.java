package domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nlp.ITokenizer;
import nlp.QuantityClassifier;
import nlp.SimpleTokenizer;

public class Section {
	
	public String id;
	public String doc_id;
	public String stype;
	public String content;
	
	private static Integer uid = 0;
	
	public Section(String doc_id, String stype, String content) {
		this.id = uid.toString();
		this.doc_id = doc_id;
		this.stype = stype;
		this.content = content;
		uid++;
	}
	
	public static boolean usageSectionHeadingPIL(String heading) {	
		return (
			heading.charAt(0) == '1' &&
			heading.toLowerCase().contains(new String("na čo")) &&
			heading.toLowerCase().contains(new String("používa")));
	}
	
	public static boolean usageSectionHeadingSPC(String heading) {
		boolean sectionNumberOpt = heading.matches(new String("4\\.\\s{0,5}1.*"));
		boolean sectionWordOccur = heading.toLowerCase().contains(new String("indikácie"));
		return sectionNumberOpt && sectionWordOccur;
	}
	
	public static boolean activeSubstanceSectionHeadingSPC(String heading) {
		boolean sectionNumberOpt = heading.matches(new String("2\\..*"));
		boolean sectionWordOccur = heading.toLowerCase().contains(new String("zloženie"));
		return sectionNumberOpt && sectionWordOccur;
	}
	
	public static void printActiveSubstance__(String section, String ir) {
		if( ir.contains("QA") || ir.contains("QG") ) {
			ITokenizer t = new SimpleTokenizer();
			String tokens[] = t.tokenize(section);
			int irIdx = 0, tokIdx = 0;
			
			System.out.println("---------------------------");
			for(; tokIdx < tokens.length - 1; tokIdx++, irIdx++) {
				if(ir.charAt(irIdx) == 'Q' && ir.charAt(irIdx+1) == 'G'
				|| ir.charAt(irIdx) == 'Q' && ir.charAt(irIdx+1) == 'A') {
					System.out.println(tokens[tokIdx] + " " + tokens[tokIdx+1] + " " + tokens[tokIdx+2]);
					tokIdx += 2;
					irIdx++;
				} else if(ir.charAt(irIdx) == 'Q') {
					tokIdx++;
				}
			}
			System.out.println("---------------------------");
		} 
	}
	
	public static void printActiveSubstances(String section, String[] ir) {
		ITokenizer t = new SimpleTokenizer();
		String tokens[] = t.tokenize(section);
		
		for(int i = 0; i < tokens.length; ) {
			int step = checkOccurences(tokens, ir, i);
			System.out.println(tokens[i]+"-"+ir[i]);
			i += step;
		}
	}
	
	private static int checkOccurences(String[] tokens, String[] ir, int pos) {
		if(
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "AAis2x", "Unknown"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "SSis2", "Unknown"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "SSis2", "SSfs2"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "Unknown", "%"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "AAis2x", "SSis2"}, pos) ||

			checkOccurence(tokens, ir, new String[] {"Unknown", "SSis1", "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"SSis1", "Unknown", "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"%", "Unknown",     "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Unknown", "%",     "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"%", "%",           "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"SSis2", "SSfs2",   "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Unknown", "SSfs4", "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"AAfs1x", "SSfs4",  "Quantity", "Quantity"}, pos) ) {
			return 4;
		}
		if(
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "SSis2"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "AAis2x"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "SSip2"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "Unknown"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "SSfs2"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "SSfs4"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "Dx"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"Quantity", "Quantity", "%"}, pos) ||
			
			checkOccurence(tokens, ir, new String[] {"Unknown",  "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"SSis2",    "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"SSis1",    "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"SSip1",    "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"VLdscm+",  "Quantity", "Quantity"}, pos) ||
			checkOccurence(tokens, ir, new String[] {"%",        "Quantity", "Quantity"}, pos) ) {
			return 3;
		}
		
		return 1;
	}
	
	private static boolean checkOccurence(String[] tokens, String[] ir, String[] tags, int pos) {
		for(int i = 0; i < tags.length; i++) {
			try {
				@SuppressWarnings("unused")
				String s = ir[pos+i];
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
			if( ir[pos+i].equals(tags[i]) ) {
				continue;
			} else {
				return false;
			}
		}
		System.out.print("AS: ");
		for(int i = 0; i < tags.length; i++) {
			if( tags[i].equals("Quantity") == false ) {
				Main.asDict.addWord(tokens[pos+i]);
			}
			System.out.print(tokens[pos+i]+" ");
		}
		System.out.println();
		return true;
	}
	
	@SuppressWarnings("unused")
	private static boolean print3AS__(String[] tokens, int i, String ir) {
		try {
			if( ir.substring(i, i+4).equals("QQAA") ||
				ir.substring(i, i+4).equals("QQGG") ||
				ir.substring(i, i+4).equals("QQGA") ||
				ir.substring(i, i+4).equals("QQAG") ||
				ir.substring(i, i+4).equals("QQUU") ||
				ir.substring(i, i+4).equals("UUQQ")) {
				System.out.println("AS: "+tokens[i]+" "+tokens[i+1]+" "+tokens[i+2]+" "+tokens[i+3]);
				Main.asFile.writeln("AS: "+tokens[i]+" "+tokens[i+1]+" "+tokens[i+2]+" "+tokens[i+3]);
				return true;
			}
		} catch (StringIndexOutOfBoundsException e) {
			/* This is OK */
			return false;
		} catch (Exception e) {
			/* This is not */
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private static boolean print2AS__(String[] tokens, int i, String ir) {
		try {
			if( ir.substring(i, i+3).equals("QQA") ||
				ir.substring(i, i+3).equals("QQG") ||
				ir.substring(i, i+3).equals("QQU") ||
				ir.substring(i, i+3).equals("UQQ")) {
				System.out.println("AS: "+tokens[i]+" "+tokens[i+1]+" "+tokens[i+2]);
				Main.asFile.writeln("AS: "+tokens[i]+" "+tokens[i+1]+" "+tokens[i+2]);
				return true;
			}
		} catch (StringIndexOutOfBoundsException e) {
			/* This is OK */
			return false;
		} catch (Exception e) {
			/* This is not */
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}
	
	public static String removeBrackets(String section) {
		Pattern pattern = Pattern.compile("\\([^\\(\\)]*\\)");
		Matcher matcher = pattern.matcher(section);
		QuantityClassifier c = new QuantityClassifier();
		
		while( matcher.find() ) {
			String s = matcher.group();
			int count = c.quantityCount(s.substring(1, s.length() - 1));
			if( count == 0 ) {
				section = section.replace(s, "");
			} else {
				section = section.replace(s, s.substring(1, s.length() - 1));
			}
		}
		return section;
	}

}
