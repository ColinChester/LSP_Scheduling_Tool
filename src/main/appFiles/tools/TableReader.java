package main.appFiles.tools;

import main.appFiles.databaseManagement.DbConnection;
import java.sql.SQLException;

public class TableReader {// Test resource, https://www.sqlitetutorial.net/sqlite-java/select/
	public static void readTable() {
		String tableQuery = "SELECT employee_id, first_name, last_name, school_id, email, phone_number, title FROM employees";
		try (var conn = DbConnection.getConnection()){
			var stmt = conn.createStatement();
			var query = stmt.executeQuery(tableQuery);
			
			while (query.next()) {
				System.out.printf("%-5s%-10s%-10s%-10s%-25s%-15s%-10s%n",
						query.getInt("employee_id"),
						query.getString("first_name"),
						query.getString("last_name"),
						query.getString("school_id"),
						query.getString("email"),
						query.getString("phone_number"),
						query.getString("title")
					);
			}
		} catch (SQLException e) {
			System.out.print("Connection error: " + e.getMessage());
			e.getStackTrace();
		}
	}
}
