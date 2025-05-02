package main.appFiles.databaseManagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.schedulingData.Schedule;

public class ScheduleDBCRUD {
	public static void createScheduleTable(Schedule s) {
        String scheduleAdd = "CREATE TABLE IF NOT EXISTS " + s.getTableName() + " ("
                + "shift_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "employee_id INTEGER NOT NULL,"
                + "day_of_week TEXT NOT NULL,"
                + "shift_start TEXT NOT NULL,"
                + "shift_end TEXT NOT NULL,"
                + "FOREIGN KEY(employee_id) REFERENCES employees(employee_id)"
                + ");";

        try (var conn = DbConnection.getConnection(); var stmt = conn.createStatement()) {
            stmt.execute(scheduleAdd);
            System.out.println("Created schedule table: " + s.getTableName());
        } catch (SQLException e) {
            System.err.println("Error creating schedule table: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	public static void addShifts(Schedule s) {
	    String shiftAdd = "INSERT INTO " + s.getTableName() +
	                 " (employee_id, day_of_week, shift_start, shift_end) VALUES (?, ?, ?, ?)";
	    try (var conn = DbConnection.getConnection()) {
	        for (Shift shift : s.getShifts()) {
	            try (var pstmt = conn.prepareStatement(shiftAdd)) {
	            	pstmt.setInt(1, shift.getEmployeeid());
	                pstmt.setString(2, shift.getDay().toString());
	                pstmt.setString(3, shift.getStart().toString());
	                pstmt.setString(4, shift.getEnd().toString());
	                pstmt.executeUpdate();
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error inserting shifts: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public static List<String> listScheduleTables() {
        List<String> tables = new ArrayList<>();
        String schedule = "SELECT name FROM sqlite_master "
                   + "WHERE type='table' AND name LIKE 'schedule_%';";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(schedule)) {

            while (rs.next()) {
                tables.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("Error listing schedule tables: " + e.getMessage());
            e.printStackTrace();
        }
        return tables;
    }
	
	public static List<Shift> getShiftsFromTable(String tableName) {
        List<Shift> shifts = new ArrayList<>();
        String schedule = "SELECT employee_id, day_of_week, shift_start, shift_end "
                   + "FROM " + tableName + ";";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(schedule)) {
            while (rs.next()) {
                int empId = rs.getInt("employee_id");
                DayOfWeek day = DayOfWeek.valueOf(rs.getString("day_of_week"));
                LocalTime start = LocalTime.parse(rs.getString("shift_start"));
                LocalTime end   = LocalTime.parse(rs.getString("shift_end"));
                shifts.add(new Shift(empId, day, start, end));
            }

        } catch (SQLException e) {
            System.err.println("Error loading shifts from " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return shifts;
    }
}
