package domain;

import java.util.ArrayList;

public class Dictionary {
	
	private ArrayList<String> dictionary = null;
	
	public Dictionary() {
		dictionary = new ArrayList<String>();
	}
	
	public boolean addWord(String word) {
		return dictionary.add(word);
	}
	
	protected void delWord(int i) {
		dictionary.remove(i);
	}
	
	public void showDictionary() {
		System.out.println("=====DICTIONARY=====");
		for(String s : dictionary) {
			System.out.println(s);
		}
		System.out.println("=====DICTIONARY=====");
	}

}
