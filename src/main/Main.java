package main; // TODO create employee objects for people in db and move to EmployeeList

import main.appFiles.databaseManagement.*;
import main.appFiles.tools.*;
import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Availability;

public class Main {
	public static void main(String[] args) {
		DbConnection.connect();
		DbTableInit.TableInit();
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
        Availability a1 = new Availability("MONDAY", "08:00", "12:00");
		Availability a2 = new Availability("TUESDAY", "09:00", "14:00");
        Availability a3 = new Availability("WEDNESDAY", "10:00", "15:00");
        Availability a4 = new Availability("THURSDAY", "11:00", "13:00");
        Availability a5 = new Availability("FRIDAY", "07:00", "12:00");
		
        AvailabilityDBCRUD.addAvailability(e1.getEmployeeId(), a1);
        AvailabilityDBCRUD.addAvailability(e1.getEmployeeId(), a2);
        AvailabilityDBCRUD.addAvailability(e1.getEmployeeId(), a3);

        AvailabilityDBCRUD.addAvailability(e2.getEmployeeId(), a4);
        AvailabilityDBCRUD.addAvailability(e2.getEmployeeId(), a5);
        AvailabilityDBCRUD.addAvailability(e2.getEmployeeId(), a1);

        AvailabilityDBCRUD.addAvailability(e3.getEmployeeId(), a2);
        AvailabilityDBCRUD.addAvailability(e3.getEmployeeId(), a3);
        AvailabilityDBCRUD.addAvailability(e3.getEmployeeId(), a4);

        AvailabilityDBCRUD.addAvailability(e4.getEmployeeId(), a5);
        AvailabilityDBCRUD.addAvailability(e4.getEmployeeId(), a1);
        AvailabilityDBCRUD.addAvailability(e4.getEmployeeId(), a2);

        AvailabilityDBCRUD.addAvailability(e5.getEmployeeId(), a3);
        AvailabilityDBCRUD.addAvailability(e5.getEmployeeId(), a4);
        AvailabilityDBCRUD.addAvailability(e5.getEmployeeId(), a5);
		
		TableReader.readAvailabilityTable();
		ClearTables.clearAllTables();
		
	}
}
