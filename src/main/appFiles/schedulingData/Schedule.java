package main.appFiles.schedulingData;

import main.appFiles.databaseManagement.DbConnection;
import main.appFiles.scheduleAlgorithm.ScheduleBuilder;
import main.appFiles.databaseManagement.ScheduleDBCRUD;
import java.time.*;
import java.util.*;

public class Schedule {
    private LocalTime startTime;
    private LocalTime endTime;
	private String tableName;
    private Map<Integer, Employee> employees;

    public Schedule(String start, String end, ArrayList<Employee> employeeList) {
        this.startTime = LocalTime.parse(start);
        this.endTime = LocalTime.parse(end);
        this.employees = new HashMap<>();
        this.tableName = "schedule_" + System.currentTimeMillis();

        for (Employee e : employeeList) {
            employees.put(e.getEmployeeId(), e);
        }
        createTable();
        generateSchedule();
    }
    
    private void createTable() {
    	ScheduleDBCRUD.createScheduleTable(this);
    }
    
    private void generateSchedule() {
        ScheduleBuilder.scheduleBuild(this);
    }

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getTableName() {
		return tableName;
	}


	public Map<Integer, Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Map<Integer, Employee> employees) {
		this.employees = employees;
	}

}
