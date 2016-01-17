package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import domain.Section;

public class PostgreSQLPersistable implements Persistable {

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
				System.out.print("ID: "+rs.getInt("id")+" DOC_ID: "+rs.getInt("doc_id")+" ");
				System.out.print("STYPE: "+rs.getString("stype")+"\n");
			}
		} catch(Exception e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(1);
		}
		return true;
	}

}