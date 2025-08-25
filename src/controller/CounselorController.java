package controller;

import dao.CounselorDAO;
import model.Counselor;

import java.util.List;

public class CounselorController {
    private CounselorDAO counselorDAO;

    // Constructor and CounselorDAO Initialiser
    public CounselorController() {
        counselorDAO = new CounselorDAO();
    }

    // Add counselor
    public int addCounselor(String name, String specialization, String availability) {
        if (name == null || name.trim().isEmpty() ||
            specialization == null || specialization.trim().isEmpty() ||
            availability == null || availability.trim().isEmpty()) {
            System.out.println("All fields must be filled out.");
            return -1;
        }
        
        int newCounselorId = counselorDAO.createCounselor(new Counselor(name, specialization, availability));
        if (newCounselorId == -1) {
            System.out.println("Counselor already exists or insertion failed");
        } else {
            System.out.println("Counselor added with ID: " + newCounselorId);
        }
        return newCounselorId;
    }

    // Return all counselors
    public List<Counselor> getAllCounselors() {
        return counselorDAO.getAllCounselors();
    }

    // Update counselor
    public String updateCounselor(int id, String name, String specialization, String availability) {
        if (id <= 0 || name == null || name.trim().isEmpty() ||
            specialization == null || specialization.trim().isEmpty() ||
            availability == null || availability.trim().isEmpty()) {
            return "Invalid counselor data. Please check all fields.";
        }

        Counselor counselor = new Counselor(id, name, specialization, availability);
        return counselorDAO.updateCounselor(counselor);
    }

    // Delete counselor
    public String deleteCounselor(int id) {
        if (id <= 0) {
            return "Invalid counselor ID.";
        }

        return counselorDAO.deleteCounselor(id);
    }
}
