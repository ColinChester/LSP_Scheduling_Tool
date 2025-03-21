package main.appFiles.schedulingData;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalTime; // Idea for LocalTime implementation came from https://www.w3schools.com/java/java_date.asp

import main.appFiles.databaseManagement.DbConnection;

public class Availability {
	private DayOfWeek day;
	private LocalTime startTime;
	private LocalTime endTime;
	private int availabilityId;
	
	public Availability(String day, String startTime, String endTime) {
		DayOfWeek newDay = DayOfWeek.valueOf(day);
		LocalTime newStart = LocalTime.parse(startTime);
		LocalTime newEnd = LocalTime.parse(endTime);
        
		if (newStart.compareTo(newEnd) >= 0) { // https://www.geeksforgeeks.org/localtime-compareto-method-in-java-with-examples/
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        
        this.day = newDay;
        this.startTime = newStart;
        this.endTime = newEnd;
    }

	public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
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
    
    public int getAvailabilityId() {
    	return availabilityId;
    }
    
    public void setAvailabilityId(int availabilityId) {
    	this.availabilityId = availabilityId;
    }
    
    public boolean isUnavailableAt(LocalTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }
    
    public String toString() {
        return "availabilityId = " + availabilityId + ", day = " + day + ", startTime = " + startTime + ", endTime = " + endTime;
    }
	public void availabilityRefresh() {
		String tableQuery = "SELECT availability_id, day_of_week, start_time, end_time FROM availability WHERE availability_id = ?";
		try (var conn = DbConnection.getConnection();
			var pstmt = conn.prepareStatement(tableQuery)) {
			pstmt.setInt(1, this.getAvailabilityId());
			try (var query = pstmt.executeQuery()) {
				if (query.next()) {
					this.setDay(DayOfWeek.valueOf(query.getString("day_of_week")));
					this.setStartTime(LocalTime.parse(query.getString("start_time")));
					this.setEndTime(LocalTime.parse(query.getString("end_time")));
				}else {
					System.out.println("No data found at availability ID " + this.getAvailabilityId());
				}
			}
		} catch (SQLException e) {
			System.err.println("Error refreshing employee: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
