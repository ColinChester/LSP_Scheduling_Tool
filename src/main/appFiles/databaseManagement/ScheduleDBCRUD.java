package main.appFiles.databaseManagement;

import java.sql.SQLException;

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
}
