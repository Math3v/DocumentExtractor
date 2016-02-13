package domain;

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

}
