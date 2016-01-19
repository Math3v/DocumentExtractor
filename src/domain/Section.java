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
	
	public static boolean usageSectionHeading(String heading) {	
		return (
			heading.charAt(0) == '1' &&
			heading.toLowerCase().contains(new String("na čo")) &&
			heading.toLowerCase().contains(new String("používa")));
	}

}
