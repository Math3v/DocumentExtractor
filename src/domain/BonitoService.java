package domain;

import java.io.IOException;
import java.util.ListIterator;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BonitoService {

	private final static String username = System.getenv("SNR_USERNAME");
	private final static String password = System.getenv("SNR_PASSWORD");
	private final static String login = username+":"+password;
	private final static String base64login = new String(Base64.encodeBase64(login.getBytes()));
	
	private String getUrl(String word) {
		return "http://bonito.korpus.sk/run.cgi/freqs?q=aword%2C%5Bword%3D%22%28%3Fi%29"+word+"%22%7Clemma%3D%22%28%3Fi%29"+word+"%22%5D&q=f;corpname=prim-7.0-public-all&attrs=word%2Ctag&ctxattrs=word&refs=%3Ddoc.bogo&iquery="+word+";fcrit=tag/e+0~0%3E0;ml=0";
	}
	

	public void test(String token) {
		try {
			Document document = Jsoup.connect(getUrl(token))
						.header("Authorization", "Basic " + base64login)
						.timeout(7000)
						.get();
			Elements elements = document.select("td.word");
			//System.out.println( document.html() );
			ListIterator<Element> it = elements.listIterator();
			while( it.hasNext() ) {
				Element e = it.next();
				System.out.println(e.text());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFullTag(String word) {
		if( word.length() < 4 ) {
			return "TooShort";
		}
		
		Document document = null;
		try {
			document = Jsoup.connect(getUrl(word)).header("Authorization", "Basic " + base64login).timeout(0).get();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		if( document == null ) {
			System.err.println("Error: document empty");
			System.exit(1);
		} else {
			Elements elements = document.select("td.word");
			if( elements.size() > 0 ) {
				Element element = elements.get(0);
				return element.text();
			} else {
				return "Unknown";
			}
		}
		
		return null;
		
	}
	
	public String getPosTag(String word) {
		if( word.length() < 4 ) {
			return "U";
		}
		
		Document document = null;
		try {
			document = Jsoup.connect(getUrl(word)).header("Authorization", "Basic " + base64login).timeout(0).get();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		if( document == null ) {
			System.err.println("Error: document empty");
			System.exit(1);
		} else {
			Elements elements = document.select("td.word");
			if( elements.size() > 0 ) {
				Element element = elements.get(0);
				char sPosTag = '0';
				try {
					sPosTag = element.text().charAt(4);
				} catch( StringIndexOutOfBoundsException e) {
					System.err.println("Out of bounds "+element.text());
					return "U";
				}
				
				switch(sPosTag) {
				case '1':
					return "N";
				case '2':
					return "G";
				case '3':
					return "D";
				case '4':
					return "A";
				case '5':
					return "V";
				case '6':
					return "L";
				case '7':
					return "I";
				default:
					return "U";
					
				}
			} else {
				return "U";
			}
		}
		return "U";
	}
}
