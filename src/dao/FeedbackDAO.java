package dao;

import model.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    // Database connection details
    private final String URL = "jdbc:derby://localhost:1527/StudentWellnessDB";
    private final String USER = "stuWell";
    private final String PASSWORD = "1234";

    // Method to create and insert new feedback into the database
    public String createFeedback(Feedback feedback) {
        // SQL INSERT statement with placeholders
        String sql = "INSERT INTO Feedback (student_name, rating, comments) VALUES (?, ?, ?)";
        
        // Establish connection to database
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // Prepare statement to prevent SQL injection
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters from the Feedback object
            stmt.setString(1, feedback.getStudentName());
            stmt.setInt(2, feedback.getRating());
            stmt.setString(3, feedback.getComments());;

            // Execute the insert command
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return "Feedback submitted successfully for " + feedback.getStudentName() + ".";
            } else {
                return "Failed to submit feedback.";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error submitting feedback: " + ex.getMessage();
        }
    }

    // Method to retrieve all feedback entries from the database
    public List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM Feedback";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Iterate over the result set and create Feedback objects
            while (rs.next()) {
                Feedback feedback = new Feedback(
                        rs.getInt("id"),
                        rs.getString("student_name"),
                        rs.getInt("rating"),
                        rs.getString("comments")
                );
                list.add(feedback); // Add each feedback to the list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list; // Return list of all feedback
    }

    // Method to update existing feedback by ID
    public String updateFeedback(Feedback feedback) {
        String sql = "UPDATE Feedback SET student_name = ?, rating = ?, comments = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters based on the new values in the Feedback object
            stmt.setString(1, feedback.getStudentName());
            stmt.setInt(2, feedback.getRating());
            stmt.setString(3, feedback.getComments());
            stmt.setInt(4, feedback.getId());

            // Execute update command
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return "Feedback updated successfully (ID: " + feedback.getId() + ")";
            } else {
                return "No feedback found to update for ID: " + feedback.getId();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error updating feedback: " + ex.getMessage();
        }
    }


    // Method to delete feedback from the database by ID
    public String deleteFeedback(int id) {
        String sql = "DELETE FROM Feedback WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);// Set ID to delete
            int rowsDeleted = stmt.executeUpdate(); // Execute delete command
            
            if (rowsDeleted > 0) {
                return "Feedback deleted successfully (ID: " + id + ")";
            } else {
                return "No feedback found to delete for ID: " + id;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error deleting feedback: " + ex.getMessage();
        }
    }
    
   // Method to retrieve a single feedback entry by its ID
    public Feedback getFeedbackById(int id) {
        String sql = "SELECT * FROM Feedback WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id); // Set the ID parameter
            ResultSet rs = stmt.executeQuery(); // Execute the query

            // If a matching record is found, return the Feedback object
            if (rs.next()) {
                System.out.println("Feedback found for ID: " + id);
                return new Feedback(
                    rs.getInt("id"),
                    rs.getString("student_name"),
                    rs.getInt("rating"),
                    rs.getString("comments")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // If no matching feedback is found
    }
}
