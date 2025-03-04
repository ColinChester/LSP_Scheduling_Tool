package main.appFiles.databaseManagement;

import main.appFiles.schedulingData.*;
import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeDBCRUD {
	public static void addEmployee(Employee employee) {// https://www.sqlitetutorial.net/sqlite-java/insert/
		String employeeAdd = "INSERT INTO employees (first_name, last_name, school_id, email, phone_number, title) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(employeeAdd);
			pstmt.setString(1, employee.getFName());
			pstmt.setString(2, employee.getLName());
			pstmt.setString(3, employee.getSchoolId());
			pstmt.setString(4, employee.getEmail());
			pstmt.setString(5, employee.getPhoneNum());
			pstmt.setString(6, employee.getTitle());
			pstmt.executeUpdate();
			employee.employeeRefresh();
		} catch (SQLException e) {
			System.out.print("Connection error: " + e.getMessage());
			e.getStackTrace();
		}
	}
	
	public static void delEmployee(Employee employee) { //https://www.sqlitetutorial.net/sqlite-java/delete/
		String employeeDel = "DELETE FROM employees WHERE school_id = ?";
		try (Connection conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(employeeDel);
			pstmt.setString(1, employee.getSchoolId());
			pstmt.executeUpdate();
			System.out.println("User Successfully deleted");
		} catch (SQLException e) {
			System.out.print("Connection error: " + e.getMessage());
			e.getStackTrace();
		}
	}
}
