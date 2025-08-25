package controller;

import dao.AppointmentDAO;
import model.Appointment;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AppointmentController {
    private AppointmentDAO appointmentDAO;
    
    //Constructor and AppointmentDAO initialiser
    public AppointmentController() {
        appointmentDAO = new AppointmentDAO();
    }

    public String addAppointment(String studentName, int counselorId, Date date, Time time, String status) {
        // Validate input
        if (studentName == null || studentName.trim().isEmpty() ||
            date == null || time == null ||
            status == null || status.trim().isEmpty()) {
            return "All fields must be filled out.";
        }

        // Check for double booking
        if (appointmentDAO.isCounselorBooked(counselorId, date, time)) {
            return "This counselor is already booked at the selected date and time.";
        }

        Appointment appt = new Appointment(studentName, counselorId, date, time, status);
        return appointmentDAO.createAppointment(appt);
    }
    
    // Return all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    // Update Appointment
    public String updateAppointment(int id, String studentName, int counselorId, Date date, Time time, String status) {
        if (studentName == null || studentName.trim().isEmpty() ||
            date == null || time == null ||
            status == null || status.trim().isEmpty()) {
            return "All fields must be filled out.";
        }

        if (appointmentDAO.isCounselorBookedExcludingId(counselorId, date, time, id)) {
            return "This counselor is already booked at the selected date and time.";
        }

        Appointment appt = new Appointment(id, studentName, counselorId, date, time, status);
        return appointmentDAO.updateAppointment(appt);
    }

    // Delete Appointment
    public String deleteAppointment(int id) {
        return appointmentDAO.deleteAppointment(id);
    }
}

