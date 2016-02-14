package utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileOutput {

	PrintWriter writer = null;
	
	public FileOutput(String filename) {
		try {
			writer = new PrintWriter( filename );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void write(String s) {
		writer.print(s);
	}
	
	public void writeln(String s) {
		writer.println(s);
	}
	
	public void close() {
		writer.close();
	}
}
