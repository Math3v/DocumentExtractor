package utils;

import domain.Section;

public interface Persistable {
	
	public boolean insertSection(Section s);
	public boolean selectSection();

}
