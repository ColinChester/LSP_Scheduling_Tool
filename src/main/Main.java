package main;

import main.appFiles.databaseManagement.*;
import main.appFiles.tools.*;
import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Availability;

public class Main {
    public static void main(String[] args) {
        DbConnection.connect();
        DbTableInit.TableInit();
        
        // Create employee objects.
        Employee e1 = new Employee("Colon", "Chestnut", "003002001", "colon@gmail.com", "9005476383", "security analyst");
        Employee e2 = new Employee("Test", "Dummy", "000000", "imnotreal@gmail.com", "1234567890", "john doe enthusiast");
        Employee e3 = new Employee("Bob", "Johnson", "001000002", "bob.johnson@example.com", "5552223333", "project manager");
        Employee e4 = new Employee("Carol", "Davis", "001000003", "carol.davis@example.com", "5553334444", "business analyst");
        Employee e5 = new Employee("Dave", "Wilson", "001000004", "dave.wilson@example.com", "5554445555", "quality assurance");
        
        EmployeeDBCRUD.addEmployee(e1);
        EmployeeDBCRUD.addEmployee(e2);
        EmployeeDBCRUD.addEmployee(e3);
        EmployeeDBCRUD.addEmployee(e4);
        EmployeeDBCRUD.addEmployee(e5);
        
        TableReader.readEmployeeTable();
        
        // Add availabilities using the new method.
        e1.addAvailability("MONDAY", "08:00", "12:00");
        e1.addAvailability("MONDAY", "13:00", "17:00");
        e1.addAvailability("TUESDAY", "09:00", "14:00");
        
        e2.addAvailability("THURSDAY", "11:00", "13:00");
        e2.addAvailability("FRIDAY", "07:00", "12:00");
        e2.addAvailability("MONDAY", "08:00", "12:00");
        
        e3.addAvailability("TUESDAY", "09:00", "14:00");
        e3.addAvailability("WEDNESDAY", "10:00", "15:00");
        e3.addAvailability("THURSDAY", "11:00", "13:00");
        
        e4.addAvailability("FRIDAY", "07:00", "12:00");
        e4.addAvailability("MONDAY", "08:00", "12:00");
        e4.addAvailability("TUESDAY", "09:00", "14:00");
        
        e5.addAvailability("WEDNESDAY", "10:00", "15:00");
        e5.addAvailability("THURSDAY", "11:00", "13:00");
        e5.addAvailability("FRIDAY", "07:00", "12:00");
        
        // Insert each employee's availabilities into the database.
        for (Availability avail : e1.getAvailabilities().values()) {
            AvailabilityDBCRUD.addAvailability(e1.getEmployeeId(), avail);
        }
        for (Availability avail : e2.getAvailabilities().values()) {
            AvailabilityDBCRUD.addAvailability(e2.getEmployeeId(), avail);
        }
        for (Availability avail : e3.getAvailabilities().values()) {
            AvailabilityDBCRUD.addAvailability(e3.getEmployeeId(), avail);
        }
        for (Availability avail : e4.getAvailabilities().values()) {
            AvailabilityDBCRUD.addAvailability(e4.getEmployeeId(), avail);
        }
        for (Availability avail : e5.getAvailabilities().values()) {
            AvailabilityDBCRUD.addAvailability(e5.getEmployeeId(), avail);
        }
        
        TableReader.readAvailabilityTable();
        ClearTables.clearAllTables();
    }
}
