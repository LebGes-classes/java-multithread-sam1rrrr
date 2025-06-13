package models;

public class Task {
    private int id;
    private String title;
    private String status;
    private int employeeId;
    private int actualHours;
    private int totalHours;

    public Task() {}

    public Task(int id, String title, String status, int employeeId, int actualHours, int totalHours) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.employeeId = employeeId;
        this.actualHours = actualHours;
        this.totalHours = totalHours;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public int getEmployeeId() { return employeeId; }
    public int getActualHours() { return actualHours; }
    public int getTotalHours() { return totalHours; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public void setActualHours(int actualHours) { this.actualHours = actualHours; }
    public void setTotalHours(int totalHours) { this.totalHours = totalHours; }
}

