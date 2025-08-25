package controller;

import dao.FeedbackDAO;
import model.Feedback;

import java.util.List;

public class FeedbackController {
    private FeedbackDAO feedbackDAO;

    // Constructor and FeedbackDAO Initialiser
    public FeedbackController() {
        feedbackDAO = new FeedbackDAO();
    }

    // Add feedback
    public String addFeedback(String studentName, int rating, String comments) {
        if (studentName == null || studentName.trim().isEmpty()) {
            return "Student name cannot be empty.";
        }
        if (rating < 1 || rating > 5) {
            return "Rating must be between 1 and 5.";
        }

        Feedback feedback = new Feedback(studentName, rating, comments);
        return feedbackDAO.createFeedback(feedback);
    }

    // Return all feedback
    public List<Feedback> getAllFeedback() {
        return feedbackDAO.getAllFeedback();
    }

    // Update feedback
    public String updateFeedback(int id, String studentName, int rating, String comments) {
        if (studentName == null || studentName.trim().isEmpty()) {
            return "Student name cannot be empty.";
        }
        if (rating < 1 || rating > 5) {
            return "Rating must be between 1 and 5.";
        }

        Feedback feedback = new Feedback(id, studentName, rating, comments);
        return feedbackDAO.updateFeedback(feedback);
    }

    // Delete feedback
    public String deleteFeedback(int id) {
        return feedbackDAO.deleteFeedback(id);
    }
}
