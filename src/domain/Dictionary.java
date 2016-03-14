package domain;

import java.util.ArrayList;

import utils.Logger;

public class Dictionary {
	
	private ArrayList<String> dictionary = null;
	
	public Dictionary() {
		dictionary = new ArrayList<String>();
	}
	
	public boolean addWord(String word) {
		return dictionary.add(prepareWord(word));
	}
	
	protected void delWord(int i) {
		dictionary.remove(i);
	}
	
	public void showDictionary() {
		Main.l.logln("=====DICTIONARY=====", Logger.INF);
		for(String s : dictionary) {
			Main.l.logln(s, Logger.INF);
		}
		Main.l.logln("=====DICTIONARY=====", Logger.INF);
	}
	
	public Object[] toArray() {
		Object[] returnArray = new Object[dictionary.size()];
		int idx = 0;
		
		for( String s : dictionary ) {
			returnArray[idx] = s;
			idx++;
		}
		
		return returnArray;
	}
	
	public int size() {
		return dictionary.size();
	}
	
	private String prepareWord(String word) {
		String tmp = word.trim();
		if( tmp.charAt(tmp.length() - 1) == '.' ||
			tmp.charAt(tmp.length() - 1) == ',' ) {
			return tmp.substring(0, tmp.length() - 1);
		} else {
			return tmp;
		}
	}

}
