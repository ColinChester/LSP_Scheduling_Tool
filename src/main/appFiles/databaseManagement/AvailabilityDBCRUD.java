package main.appFiles.databaseManagement;

import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class AvailabilityDBCRUD {
	public static void addAvailability(int employeeId, Availability a) {
		String availabilityAdd = "INSERT INTO availability (employee_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)"; // https://www.sqlite.org/pragma.html#pragma_foreign_keys
		try (var conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(availabilityAdd, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, employeeId);
			pstmt.setString(2, a.getDay().toString());
			pstmt.setString(3, a.getStartTime().toString());
			pstmt.setString(4, a.getEndTime().toString());
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				try (var newKeys = pstmt.getGeneratedKeys()){
					if (newKeys.next()) {
						int newId = newKeys.getInt(1);
						a.setAvailabilityId(newId);
					}
				}
			}
			a.availabilityRefresh();
		} catch (SQLException e) {
			System.err.println("Connection error 7: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void delAvailability(int availabilityId) {
		String availabilityDel = "DELETE FROM availability WHERE availability_id = ?";
		try (var conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(availabilityDel);
			pstmt.setInt(1, availabilityId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Connection error 7: " + e.getMessage());
			e.printStackTrace();
		}
	}
	//TODO mass delete method
	
	public static void editAvailability(int employeeId, int availabilityId, Availability a) {
		String availabilityEdit = "UPDATE availability SET"
				+ " employee_id = COALESCE(?, employee_id),"
				+ " day_of_week = COALESCE(?, day_of_week),"
				+ " start_time = COALESCE(?, start_time),"
				+ " end_time = COALESCE(?, end_time)"
				+ " WHERE availability_id = ?";
		try (var conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(availabilityEdit);
			pstmt.setInt(1, employeeId);
			pstmt.setString(2,  a.getDay().toString());
			pstmt.setString(3, a.getStartTime().toString());
			pstmt.setString(4, a.getEndTime().toString());
			pstmt.setInt(5, availabilityId);
			
			int changedRows = pstmt.executeUpdate();
			if (changedRows == 0) {
				System.out.println("Availability not found.");
			}
		} catch (SQLException e) {
			System.err.println("Connection Error 8: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void getAvailability(int availabilityId) {
		String tableQuery = "SELECT availability_id, employee_id, day_of_week, start_time, end_time FROM availability WHERE availability_id = ?";
		try (var conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(tableQuery);
			pstmt.setInt(1, availabilityId);
			var query = pstmt.executeQuery();
			System.out.printf("%-5s%-5s%-10s%-7s%-7s%n",
					query.getInt("availability_id"),
					query.getInt("employee_id"),
					query.getString("day_of_week"),
					query.getString("start_time"),
					query.getString("end_time"));
			} catch (SQLException e) {
				System.err.println("Connection error 9: " + e.getMessage());
				e.printStackTrace();
			}
	}
	
	public static void getEmployeeAvailability(int employeeId) {
		String tableQuery = "SELECT availability_id, day_of_week, start_time, end_time FROM availability WHERE employee_id = ?";
		try (var conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(tableQuery);
			pstmt.setInt(1, employeeId);
			try (var query = pstmt.executeQuery()){
				while (query.next()) {
					System.out.printf("%-5s%-10s%-7s%-7s%n",
							query.getInt("availability_id"),
							query.getString("day_of_week"),
							query.getString("start_time"),
							query.getString("end_time"));
				}
			}
		} catch (SQLException e) {
			System.err.println("Connection error 9: " + e.getMessage());
			e.printStackTrace();// query.next impoet ResultSet
			}
	}
}
