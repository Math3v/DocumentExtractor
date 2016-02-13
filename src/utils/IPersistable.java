package utils;

import domain.Section;

public interface IPersistable {
	
	public boolean insertSection(Section s);
	public boolean selectSection();

}
