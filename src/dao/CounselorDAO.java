package dao;

import model.Counselor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CounselorDAO {
    // Database connection configuration
    private final String URL = "jdbc:derby://localhost:1527/StudentWellnessDB";
    private final String USER = "stuWell";
    private final String PASSWORD = "1234";

    /**
     * Creates a new counselor record if it does not exist.
     * Returns the generated counselor ID on success,
     * -1 if duplicate or error.
     */
    public int createCounselor(Counselor counselor) {
        String checkSql = "SELECT COUNT(*) FROM Counselors WHERE name = ? AND specialization = ?";
        String insertSql = "INSERT INTO Counselors (name, specialization, availability) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Check for duplicates
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, counselor.getName());
                checkStmt.setString(2, counselor.getSpecialization());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Duplicate found
                    return -1;
                }
            }

            // Insert counselor and return generated ID
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, counselor.getName());
                insertStmt.setString(2, counselor.getSpecialization());
                insertStmt.setString(3, counselor.getAvailability());

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows == 0) {
                    return -1; // Insert failed
                }

                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        return -1; // No ID obtained
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    // Method to fetch all counselors from the database
    public List<Counselor> getAllCounselors() {
        List<Counselor> list = new ArrayList<>();
        String sql = "SELECT * FROM Counselors";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop through the result set and create Counselor objects
            while (rs.next()) {
                Counselor counselor = new Counselor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("availability")
                );
                list.add(counselor); // Add to the list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list; // Return the list of all counselors
    }

    // Method to update existing counselor information
    public String updateCounselor(Counselor counselor) {
        String sql = "UPDATE Counselors SET name = ?, specialization = ?, availability = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the new values from the Counselor object
            stmt.setString(1, counselor.getName());
            stmt.setString(2, counselor.getSpecialization());
            stmt.setString(3, counselor.getAvailability());
            stmt.setInt(4, counselor.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return "Counselor updated successfully (ID: " + counselor.getId() + ")";
            } else {
                return "No counselor found with ID: " + counselor.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error updating counselor: " + ex.getMessage();
        }
    }

    // Method to delete a counselor by ID
    public String deleteCounselor(int id) {
        String sql = "DELETE FROM Counselors WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Set the counselor ID to delete
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return "Counselor deleted successfully (ID: " + id + ")";
            } else {
                return "No counselor found to delete with ID: " + id;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error deleting counselor: " + ex.getMessage();
        }
    }
}
