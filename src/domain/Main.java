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
	
	public static void printSectionHeadings(String htmlDocument, Integer documentId) {
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
	            	//String xmlDocument = parseToXML(fileEntry.getAbsolutePath());
	            	//String autoParsedHtmlDocument = autoParseToHTML(fileEntry.getAbsolutePath());
	            	printSectionHeadings(htmlDocument, new Integer(documentId));
	            	//System.out.println(xmlDocument);
	            	//System.out.println(htmlDocument);
	            } catch (Exception e) {
		            	
	            }
	        }
	        documentId++;
		}
	}
	
	public static void main(String args[]) {
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
