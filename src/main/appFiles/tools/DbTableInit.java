package main.appFiles.tools;

import main.appFiles.databaseManagement.DbConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DbTableInit { // https://www.sqlitetutorial.net/sqlite-java/create-table/
	public static void employeeTableInit() {
		try(Connection conn = DbConnection.getConnection()){
			if (conn == null) {
				System.out.print("connection error");
				return;
			}
			String tableInit = "CREATE TABLE IF NOT EXISTS employees ("
					+ " employee_id INTEGER PRIMARY KEY,"
					+ " first_name TEXT NOT NULL,"
					+ " last_name TEXT NOT NULL,"
					+ " school_id TEXT NOT NULL UNIQUE,"
					+ " email TEXT NOT NULL,"
					+ " phone_number TEXT NOT NULL,"
					+ " title TEXT"
					+ " );";
			
			try (var stmt = conn.createStatement()){
				stmt.execute(tableInit);
				System.out.print("Created/Verified employee table");
			} catch (SQLException e) {
				System.out.print("Error creating Employee Table: " + e.getMessage());
				e.getStackTrace();
			}
		} catch (SQLException e) {
			System.out.print("Connection Error: " + e.getMessage());
			e.getStackTrace();
		}
	}
}
// Implement for schedule table