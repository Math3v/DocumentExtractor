package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import domain.Main;
import domain.Section;

public class PostgreSQLPersistable implements IPersistable {

	@Override
	public boolean insertSection(Section s) {
		Connection c = DatabaseConnection.getConnection();
		Statement stmt = null;
		
		try {
			stmt = c.createStatement();
			String sql = "INSERT INTO sections (id, doc_id, stype, content) VALUES ("+
						""+s.id+", "+
						""+s.doc_id+", "+
						"'"+s.stype+"', "+
						"'"+s.content+"');";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(1);
		}
		return true;
	}
	
	@Override
	public boolean selectSection() {
		Connection c = DatabaseConnection.getConnection();
		Statement stmt = null;
		
		try {
			stmt = c.createStatement();
			String sql = "SELECT * FROM sections;";
			ResultSet rs = stmt.executeQuery(sql);
			while( rs.next() ) {
				Main.l.log("ID: "+rs.getInt("id")+" DOC_ID: "+rs.getInt("doc_id")+" ", Logger.INF);
				Main.l.log("STYPE: "+rs.getString("stype")+"\n", Logger.INF);
			}
		} catch(Exception e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(1);
		}
		return true;
	}

}
