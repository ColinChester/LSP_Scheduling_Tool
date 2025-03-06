package main.appFiles.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import main.appFiles.schedulingData.Employee;
import main.appFiles.databaseManagement.*;

public class CSVConverter {
	public static void readFile(File csv) { // I repurposed the ReadFile class from https://www.w3schools.com/java/java_files_read.asp
		try { // TODO Add input validation to ensure import goes well
			Scanner sc = new Scanner(csv);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] lineList = line.split(",");
				if (lineList.length == 6) {
					Employee e = new Employee(lineList[0], lineList[1], lineList[2], lineList[3], lineList[4], lineList[5]);
					EmployeeDBCRUD.addEmployee(e);
				}else {
					System.out.print("Invalid line: " + line);
					continue;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			e.printStackTrace();
		}
	}
}
