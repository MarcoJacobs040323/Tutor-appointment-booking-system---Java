package model;

public class Feedback {
    private int id;
    private String studentName;
    private int rating;       
    private String comments;

    // Default Constructors
    public Feedback() {}

    // Constructor without ID
    public Feedback(String studentName, int rating, String comments) {
        this.studentName = studentName;
        this.rating = rating;
        this.comments = comments;
    }
    
    // Constructor with ID
    public Feedback(int id, String studentName, int rating, String comments) {
        this(studentName, rating, comments);
        this.id = id;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
