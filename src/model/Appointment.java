package model;

import java.sql.Date;
import java.sql.Time;

public class Appointment {
    private int id;
    private String studentName;
    private int counselorId;
    private Date date;
    private Time time;
    private String status;
    
    // Default Constructors
    public Appointment() {}

    // Constructor without ID
    public Appointment(String studentName, int counselorId, Date date, Time time, String status) {
        this.studentName = studentName;
        this.counselorId = counselorId;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    
    // Constructor with ID
    public Appointment(int id, String studentName, int counselorId, Date date, Time time, String status) {
        this(studentName, counselorId, date, time, status);
        this.id = id;
    }
    
    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getCounselorId() { return counselorId; }
    public void setCounselorId(int counselorId) { this.counselorId = counselorId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
