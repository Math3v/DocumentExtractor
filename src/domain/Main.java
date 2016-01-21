package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import utils.DatabaseConnection;
import utils.Persistable;
import utils.PostgreSQLPersistable;

public class Main {
		
	public static String parseToHTML(String fileName) throws IOException, SAXException, TikaException {
	    ContentHandler handler = new ToXMLContentHandler();
	    String[] tokens = fileName.split("\\.");
	    int tokenLen = tokens.length;
	    Parser parser;
	    if( tokens[tokenLen - 1].equals(new String("doc")) ) {
	    	parser = new OfficeParser();
	    } else if( tokens[tokenLen - 1].equals(new String("docx")) ) {
	    	parser = new OOXMLParser();
	    } else {
	    	return "";
	    }
	    Metadata metadata = new Metadata();
	    ParseContext ctx = new ParseContext();
	    
	    try (InputStream stream = new FileInputStream(fileName)) {
	        parser.parse(stream, handler, metadata, ctx);
	        return handler.toString();
	    }
	}
	
	public static String autoParseToHTML(String fileName) throws IOException, SAXException, TikaException {
		ContentHandler handler = new ToXMLContentHandler();
		AutoDetectParser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();
		
		try(InputStream stream = new FileInputStream(fileName)) {
			parser.parse(stream, handler, metadata);
			return handler.toString();
		}
	}
	
	public static String parseToXML(String fileName) throws IOException, SAXException, TikaException {
		ContentHandler handler = new ToXMLContentHandler();
		OOXMLParser parser = new OOXMLParser();
		Metadata metadata = new Metadata();
	    ParseContext ctx = new ParseContext();
	    
	    try (InputStream stream = new FileInputStream(fileName)) {
	        parser.parse(stream, handler, metadata, ctx);
	        return handler.toString();
	    }
	}
	
	public static void parseSectionHeadings(String htmlDocument, Integer documentId) {
		Document doc = Jsoup.parse(htmlDocument);
		Elements els = doc.select("p");

		for(int i = 0; i < els.size(); i++) {
			Element e = els.get(i);
			Elements match = e.getElementsMatchingText(Pattern.compile("^\\p{Digit}\\..*"));
			String section = e.text();
			
			if( match.size() > 0 ) {
				System.out.println(section);
				if( Section.usageSectionHeadingSPC(section) ) {
					SectionStateMachine.startUsage();
				} else {
					SectionStateMachine.endUsage();
				}
			} else if( SectionStateMachine.isUsage() && section.length() > 0 ) {
				Persistable p = new PostgreSQLPersistable();
				p.insertSection(new Section(documentId.toString(), "usage", section));
			}
		}
	}
	
	public static void printQuantitiesCounts(String htmlDocument, Integer documentId) {
		Document doc = Jsoup.parse(htmlDocument);
		Elements els = doc.select("p");
		
		for(int i = 0; i < els.size(); i++) {
			Element e = els.get(i);
			Elements match = e.getElementsMatchingText(Pattern.compile("^\\p{Digit}\\..*"));
			String section = e.text();
		}
	}
	
	public static void parseDirectory(String dir) {
		final File folder = new File(dir);
		int documentId = 1;
		for (final File fileEntry : folder.listFiles()) {
			if( fileEntry.isHidden() ) {
				continue;
			}
			if( fileEntry.getName().startsWith(new String("PIL")) ) {
				continue;
			}
	        if (fileEntry.isDirectory()) {
	            System.out.println("Skipping " + fileEntry.getName() + " directory...");
	        } else {
	            try {
	            	System.out.println("Document "+documentId+". "+fileEntry.getName());
	            	String htmlDocument = parseToHTML(fileEntry.getAbsolutePath());
	            	parseSectionHeadings(htmlDocument, new Integer(documentId));
	            } catch (Exception e) {
		            	
	            }
	        }
	        documentId++;
		}
	}
	
	public static void main(String args[]) {
		
		QuantityClassifier c = new QuantityClassifier();
		assert 2 == c.quantityCount("10 ml a 5 mikrogramov");
		assert 1 == c.quantityCount(" alebo preco lebo 8 g len");
		assert 2 == c.quantityCount(" ahoj 3 ml a 5,0 g");
		assert 3 == c.quantityCount("raz 2,3 mg a 3.5 ml lebo 5 mikrogramov");
		assert 2 == c.quantityCount("30 mg lebo 6 ml");
				
		parseDirectory("./data");
		
		/* Close connection */
		try {
			DatabaseConnection.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(1);
		}
	}

}
