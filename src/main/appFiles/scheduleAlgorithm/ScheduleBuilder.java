package main.appFiles.scheduleAlgorithm;

import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Schedule;

public class ScheduleBuilder {
	public static Schedule scheduleBuild(Schedule s) {
		for (Employee e : s.getEmployees().values()) {
		    System.out.println(e);
		}
	    return s;
	}
}
