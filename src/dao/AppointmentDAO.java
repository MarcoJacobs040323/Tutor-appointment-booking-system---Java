package dao;

import model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    // Database connection configuration
    private final String URL = "jdbc:derby://localhost:1527/StudentWellnessDB";
    private final String USER = "stuWell";
    private final String PASSWORD = "1234";
    
    // Adds a new appointment record to the Appointments table
    public String createAppointment(Appointment appt) {
        // Check for conflict before insert
        if (isAppointmentConflict(appt.getStudentName(), appt.getCounselorId(), appt.getDate(), appt.getTime())) {
            return "Error: Appointment conflict exists for this student and counselor at the given time.";
        }
        
        String sql = "INSERT INTO Appointments (student_name, counselor_id, date, time, status) VALUES (?, ?, ?, ?, ?)";

        try {
            // Load the Derby client JDBC driver explicitly
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, appt.getStudentName());
                stmt.setInt(2, appt.getCounselorId());
                stmt.setDate(3, appt.getDate());
                stmt.setTime(4, appt.getTime());
                stmt.setString(5, appt.getStatus());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    return "Appointment created successfully for: " + appt.getStudentName();
                } else {
                    return "Failed to create appointment.";
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "JDBC Driver class not found.";
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error creating appointment: " + ex.getMessage();
        }
    }

    // Check if counselor is already booked at a date/time
    public boolean isCounselorBooked(int counselorId, Date date, Time time) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE counselor_id = ? AND date = ? AND time = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, counselorId);
            stmt.setDate(2, date);
            stmt.setTime(3, time);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    // Retrieves all appointment records from the database
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        // SQL query to retrieve all records from the Appointments table
        String sql = "SELECT * FROM Appointments";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             // Create a Statement object to execute the query
             Statement stmt = conn.createStatement();
             // Execute the query and store the result in a ResultSet
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop through each record in the ResultSet
            while (rs.next()) {
                // Create a new Appointment object from the current row
                Appointment appt = new Appointment(
                        rs.getInt("id"),
                        rs.getString("student_name"),
                        rs.getInt("counselor_id"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("status")
                );
                list.add(appt);  // Add the Appointment to the list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;  // Return the list of all appointments
    }
    
    // Updates an existing appointment's details in the database
    public String updateAppointment(Appointment appt) {
        // Check for conflict excluding this appointment ID
        if (isAppointmentConflictExcludingId(appt.getStudentName(), appt.getCounselorId(), appt.getDate(), appt.getTime(), appt.getId())) {
            return "Error: Appointment conflict exists for this student and counselor at the given time.";
        }

        // SQL update statement with placeholders
        String sql = "UPDATE Appointments SET student_name = ?, counselor_id = ?, date = ?, time = ?, status = ? WHERE id = ?";

        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the updated values from the Appointment object
            stmt.setString(1, appt.getStudentName());
            stmt.setInt(2, appt.getCounselorId());
            stmt.setDate(3, appt.getDate());
            stmt.setTime(4, appt.getTime());
            stmt.setString(5, appt.getStatus());
            stmt.setInt(6, appt.getId());

            int rowsUpdated = stmt.executeUpdate(); // Execute the update
            if (rowsUpdated > 0) {
                return "Appointment updated successfully (ID: " + appt.getId() + ")";
            } else {
                return "No appointment found with ID: " + appt.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error updating appointment: " + ex.getMessage();
        }
    }
    
    // 
    public boolean isCounselorBookedExcludingId(int counselorId, Date date, Time time, int excludeId) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE counselor_id = ? AND date = ? AND time = ? AND id <> ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, counselorId);
            stmt.setDate(2, date);
            stmt.setTime(3, time);
            stmt.setInt(4, excludeId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    // Deletes an appointment from the database using its ID
    public String deleteAppointment(int id) {
         // SQL delete statement using a placeholder for the appointment ID
        String sql = "DELETE FROM Appointments WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Set the ID of the appointment to be deleted
            int rowsDeleted = stmt.executeUpdate(); // Execute the delete
            if (rowsDeleted > 0) {
                return "Appointment deleted successfully (ID: " + id + ")";
            } else {
                return "No appointment found to delete with ID: " + id;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error deleting appointment: " + ex.getMessage();
        }
    }
    
    // Checks if the student and counselor already have an appointment at the same date/time (for insert)
    public boolean isAppointmentConflict(String studentName, int counselorId, Date date, Time time) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE student_name = ? AND counselor_id = ? AND date = ? AND time = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentName);
            stmt.setInt(2, counselorId);
            stmt.setDate(3, date);
            stmt.setTime(4, time);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Checks if the student and counselor already have an appointment at the same date/time excluding a specific appointment ID (for update)
    public boolean isAppointmentConflictExcludingId(String studentName, int counselorId, Date date, Time time, int excludeId) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE student_name = ? AND counselor_id = ? AND date = ? AND time = ? AND id <> ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentName);
            stmt.setInt(2, counselorId);
            stmt.setDate(3, date);
            stmt.setTime(4, time);
            stmt.setInt(5, excludeId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
