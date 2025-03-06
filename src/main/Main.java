package main; // TODO create employee objects for people in db and move to EmployeeList

import main.appFiles.databaseManagement.*;
import main.appFiles.tools.*;
import main.appFiles.schedulingData.Employee;

public class Main {
	public static void main(String[] args) {
		DbConnection.connect();
		DbTableInit.employeeTableInit();
		Employee e = new Employee(1, "Colon", "Chestnut", null, null, null, null);
		EmployeeDBCRUD.editEmployee(e);
		TableReader.readTable();
	}
}
