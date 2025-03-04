package main;

import main.appFiles.databaseManagement.*;
import main.appFiles.tools.*;
import main.appFiles.schedulingData.Employee;

public class Main {
	public static void main(String[] args) {
		DbConnection.connect();
		DbTableInit.employeeTableInit();
		Employee e = new Employee("Colin", "Chester", "003103203", "colin.chester@bison.howard.edu", "9084151592", "Senior Resident Assistant");
		EmployeeDBCRUD.addEmployee(e);
		TableReader.readTable();
	}
}
