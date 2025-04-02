package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.appFiles.databaseManagement.AvailabilityDBCRUD;
import main.appFiles.databaseManagement.DbConnection;
import main.appFiles.tools.DbTableInit;
import main.appFiles.databaseManagement.EmployeeDBCRUD;
import main.appFiles.databaseManagement.ScheduleDBCRUD;
import main.appFiles.scheduleAlgorithm.ScheduleBuilder;
import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Schedule;
import main.appFiles.schedulingData.TimeRange;
import main.appFiles.tools.CSVConverter;
import main.appFiles.tools.ClearTables;

public class ProjectTests {

    @BeforeEach
    void setupDatabase() {
        ClearTables.clearAllTables();
        DbTableInit.TableInit();
    }
    
    @Test
    void testDbConnection() {
        try (Connection conn = DbConnection.getConnection()) {
            assertNotNull(conn, "DbConnection.getConnection() should not return null");
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }
    
    @Test
    void testAddAndGetEmployee() {
        Employee e = new Employee("John", "Doe", "S123", "john@example.com", "555-1234", "Worker");
        EmployeeDBCRUD.addEmployee(e);
        assertTrue(e.getEmployeeId() > 0, "Employee ID should be set after insertion");
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employees WHERE employee_id = ?")) {
            pstmt.setInt(1, e.getEmployeeId());
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Employee should exist in the database");
            assertEquals("John", rs.getString("first_name"));
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
    }
    
    @Test
    void testEditEmployee() {
        Employee e = new Employee("John", "Doe", "S123", "john@example.com", "555-1234", "Worker");
        EmployeeDBCRUD.addEmployee(e);
        e.setFName("Jane");
        EmployeeDBCRUD.editEmployee(e);
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT first_name FROM employees WHERE employee_id = ?")) {
            pstmt.setInt(1, e.getEmployeeId());
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Employee should exist in the database");
            assertEquals("Jane", rs.getString("first_name"));
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
    }
    
    @Test
    void testDeleteEmployee() {
        Employee e = new Employee("John", "Doe", "S123", "john@example.com", "555-1234", "Worker");
        EmployeeDBCRUD.addEmployee(e);
        EmployeeDBCRUD.delEmployee(e);
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employees WHERE school_id = ?")) {
            pstmt.setString(1, e.getSchoolId());
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next(), "Employee should be deleted from the database");
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
    }
    
    @Test
    void testAddAvailability() {
        // Add an employee first.
        Employee e = new Employee("Alice", "Smith", "S124", "alice@example.com", "555-5678", "Manager");
        EmployeeDBCRUD.addEmployee(e);
        
        // Create an availability for Monday.
        Availability availability = new Availability("MONDAY");
        availability.addTimeRange("09:00", "17:00");
        int availId = AvailabilityDBCRUD.addAvailability(e.getEmployeeId(), availability);
        assertTrue(availId > 0, "Availability ID should be greater than 0");
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM availability WHERE availability_id = ?")) {
            pstmt.setInt(1, availId);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Availability should exist in the database");
            assertEquals("MONDAY", rs.getString("day_of_week"));
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
    }
    
    @Test
    void testEditAvailability() {
        Employee e = new Employee("Alice", "Smith", "S124", "alice@example.com", "555-5678", "Manager");
        EmployeeDBCRUD.addEmployee(e);
        
        Availability availability = new Availability("MONDAY");
        availability.addTimeRange("09:00", "17:00");
        int availId = AvailabilityDBCRUD.addAvailability(e.getEmployeeId(), availability);
        
        // Edit availability to new time range.
        Availability newAvailability = new Availability("MONDAY");
        newAvailability.addTimeRange("10:00", "18:00");
        AvailabilityDBCRUD.editAvailability(e.getEmployeeId(), availId, newAvailability);
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT start_time, end_time FROM availability WHERE availability_id = ?")) {
            pstmt.setInt(1, availId);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Availability should exist in the database");
            assertEquals("10:00", rs.getString("start_time"));
            assertEquals("18:00", rs.getString("end_time"));
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
    }

    @Test
    void testDeleteAvailability() {
        Employee e = new Employee("Alice", "Smith", "S124", "alice@example.com", "555-5678", "Manager");
        EmployeeDBCRUD.addEmployee(e);
        
        Availability availability = new Availability("MONDAY");
        availability.addTimeRange("09:00", "17:00");
        int availId = AvailabilityDBCRUD.addAvailability(e.getEmployeeId(), availability);
        
        AvailabilityDBCRUD.delAvailability(availId);
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM availability WHERE availability_id = ?")) {
            pstmt.setInt(1, availId);
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next(), "Availability should be deleted from the database");
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
    }

    @Test
    void testValidTimeRange() {
        TimeRange tr = new TimeRange("08:00", "12:00");
        assertEquals("08:00-12:00", tr.toString());
    }
    
    @Test
    void testInvalidTimeRange() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TimeRange("12:00", "08:00");
        });
        String expectedMessage = "Start time must be before end time.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testScheduleGeneration() {
        // Create two employees (all available, since no availabilities are set)
        ArrayList<Employee> employees = new ArrayList<>();
        Employee e1 = new Employee("John", "Doe", "S100", "john@example.com", "555-0000", "Staff");
        Employee e2 = new Employee("Jane", "Doe", "S101", "jane@example.com", "555-1111", "Staff");
        EmployeeDBCRUD.addEmployee(e1);
        EmployeeDBCRUD.addEmployee(e2);
        employees.add(e1);
        employees.add(e2);
        
        // Create a schedule from 08:00 to 12:00 (4 hour slots)
        Schedule schedule = new Schedule("08:00", "12:00", employees);
        String tableName = schedule.getTableName();
        
        // Query the schedule table to ensure that 4 shifts were created.
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM " + tableName)) {
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Result set should not be empty");
            int count = rs.getInt("count");
            assertEquals(4, count, "There should be 4 shifts for a 4-hour schedule");
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
    }
    
    @Test
    void testCSVConverter() throws IOException {
        String csvContent = "Alice,Smith,S102,alice@company.com,555-2222,Manager\n" +
                            "Bob,Johnson,S103,bob@company.com,555-3333,Staff";
        File tempFile = File.createTempFile("test", ".csv");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(csvContent);
        }
        
        // Clear and reinitialize the employees table.
        ClearTables.clearAllTables();
        DbTableInit.TableInit();
        CSVConverter.readFile(tempFile);
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM employees")) {
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Result set should not be empty");
            int count = rs.getInt("count");
            assertEquals(2, count, "CSVConverter should add 2 employees");
        } catch (SQLException ex) {
            fail("SQLException occurred: " + ex.getMessage());
        }
        tempFile.delete();
    }

    @Test
    void testShift() {
        Shift shift = new Shift(1, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertEquals(1, shift.getEmployeeid());
        assertEquals(DayOfWeek.MONDAY, shift.getDay());
        assertEquals(LocalTime.of(8, 0), shift.getStart());
        assertEquals(LocalTime.of(9, 0), shift.getEnd());
        assertTrue(shift.toString().contains("Shift:"), "Shift toString should include 'Shift:'");
    }

    @Test
    void testAvailabilityToStringAndEmployeeAddAvailability() {
        Availability avail = new Availability("TUESDAY");
        avail.addTimeRange("10:00", "14:00");
        String str = avail.toString();
        assertTrue(str.contains("TUESDAY"));
        assertTrue(str.contains("10:00-14:00"));
        
        Employee e = new Employee("Test", "User", "S104", "test@user.com", "555-4444", "Staff");
        e.addAvailability("WEDNESDAY", "08:00", "12:00");
        Map<String, Availability> availMap = e.getAvailabilities();
        assertTrue(availMap.containsKey("WEDNESDAY"));
        Availability eAvail = availMap.get("WEDNESDAY");
        assertFalse(eAvail.getTimeRanges().isEmpty());
        assertEquals("08:00-12:00", eAvail.getTimeRanges().get(0).toString());
    }
}
