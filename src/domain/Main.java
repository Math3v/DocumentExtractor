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

import gui.DictionaryJTable;
import nlp.QuantityClassifier;
import utils.DatabaseConnection;
import utils.FileOutput;
import utils.IPersistable;
import utils.Logger;
import utils.PostgreSQLPersistable;

public class Main {
	
	public static final FileOutput asFile = new FileOutput( "active-substances.txt" );
	public static final Dictionary asDict = new Dictionary();
	public static final Logger l = new Logger(Logger.ERR | Logger.CRI);
		
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
				Main.l.logln(section);
				if( Section.usageSectionHeadingSPC(section) ) {
					SectionStateMachine.startUsage();
				} else {
					SectionStateMachine.endUsage();
				}
			} else if( SectionStateMachine.isUsage() && section.length() > 0 ) {
				IPersistable p = new PostgreSQLPersistable();
				p.insertSection(new Section(documentId.toString(), "usage", section));
			}
		}
	}
	
	public static void printQuantitiesCounts(String htmlDocument, Integer documentId) {
		Document doc = Jsoup.parse(htmlDocument);
		Elements els = doc.select("p");
		Integer  cnt = 0;
		QuantityClassifier c = new QuantityClassifier();
		
		for(int i = 0; i < els.size(); i++) {
			Element e = els.get(i);
			Elements match = e.getElementsMatchingText(Pattern.compile("^\\p{Digit}\\..*"));
			String section = e.text();
			
			if( match.size() > 0 ) {
				Main.l.logln("Count: "+cnt);
				Main.l.logln(section);
				cnt = 0;
			} else {
				cnt += c.quantityCount(section);
			}
		}
	}
	
	public static void parseActiveSubstances(String htmlDocument, Integer documentId) {
		Document doc = Jsoup.parse(htmlDocument);
		Elements els = doc.select("p");
		
		for(int i = 0; i < els.size(); i++) {
			Element e = els.get(i);
			Elements match = e.getElementsMatchingText(Pattern.compile("^\\p{Digit}\\..*"));
			String section = e.text();
			
			if( match.size() > 0 ) {
				if( Section.activeSubstanceSectionHeadingSPC(section) ) {
					SectionStateMachine.startActiveSubstance();
				} else {
					SectionStateMachine.endActiveSubstance();
				}
			} else if( SectionStateMachine.isActiveSubstance() && section.length() > 0 ) {
				IntermediateRepresentation ir = new IntermediateRepresentation();
				Main.l.logln(section, Logger.INF);
				section = Section.removeBrackets(section);
				String[] irValue = ir.getIntermediateRepresentation(section);
				Main.l.logln(section, Logger.INF);
				Section.printActiveSubstances(section, irValue);
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
	            Main.l.logln("Skipping " + fileEntry.getName() + " directory...", Logger.INF);
	        } else {
	            try {
	            	Main.l.logln(fileEntry.getName().substring(0, fileEntry.getName().indexOf('.')), Logger.CRI);
	            	String htmlDocument = parseToHTML(fileEntry.getAbsolutePath());
	            	//parseSectionHeadings(htmlDocument, new Integer(documentId));
	            	//printQuantitiesCounts(htmlDocument, documentId);
	            	parseActiveSubstances(htmlDocument, documentId);
	            } catch (Exception e) {
		            Main.l.logln("Error: parseDirectory " + e.getMessage(), Logger.ERR);
		            e.printStackTrace();
		            System.exit(1);
	            }
	        }
	        documentId++;
		}
	}
	
	public static void main(String args[]) {
		
		new Test();
		parseDirectory("./data");
		
		new DictionaryJTable( Main.asDict );
		
		/* Close connection */
		try {
			if( DatabaseConnection.isConnected() )
				DatabaseConnection.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
			Main.l.logln(e.getClass().getName()+": "+e.getMessage(), Logger.ERR);
			System.exit(1);
		}
		
		/* Close file output */
		Main.asFile.close();
	}

}
