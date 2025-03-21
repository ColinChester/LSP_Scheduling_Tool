package main.appFiles.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import main.appFiles.databaseManagement.*;
public class ClearTables {
	public static void clearAllTables() {
		String clearAvailability = "DELETE From availability";
		String clearEmployees = "DELETE FROM employees";
		
		try (var conn = DbConnection.getConnection();
				var stmt = conn.createStatement()){
					stmt.execute("PRAGMA foreign_keys = OFF;");
					stmt.executeUpdate(clearAvailability);
		            stmt.executeUpdate(clearEmployees);
		            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='employees'");
		            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='availability'");
		            stmt.execute("PRAGMA foreign_keys = ON;");
		            System.out.println("All tables cleared.");
		            
		        } catch (SQLException e) {
		            System.err.println("Error clearing tables: " + e.getMessage());
					e.printStackTrace();
				}
	}
}
