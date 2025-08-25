package util;

import dao.AppointmentDAO;
import dao.CounselorDAO;
import dao.FeedbackDAO;
import model.Appointment;
import model.Counselor;
import model.Feedback;

import java.sql.Date;
import java.sql.Time;

public class DatabaseSeeder {
    public static void seed() {
        seedCounselors();
        seedFeedback();
    }

    private static void seedCounselors() {
        CounselorDAO counselorDAO = new CounselorDAO();

        int mayaId = counselorDAO.createCounselor(new Counselor(0, "Dr. Maya Smith", "Mental Health", "Mon–Wed"));
        int alexId = counselorDAO.createCounselor(new Counselor(0, "Mr. Alex Reed", "Academic", "Thu–Fri"));

        if (mayaId == -1 || alexId == -1) {
            System.out.println("Failed to insert counselors, cannot proceed with appointments seeding.");
            return;
        }

        seedAppointments(mayaId, alexId);
    }

    private static void seedAppointments(int mayaId, int alexId) {
        AppointmentDAO apptDAO = new AppointmentDAO();

        apptDAO.createAppointment(new Appointment(0, "Alice Johnson", mayaId, Date.valueOf("2025-07-20"), Time.valueOf("10:00:00"), "Scheduled"));
        apptDAO.createAppointment(new Appointment(0, "Bob Lee", alexId, Date.valueOf("2025-07-21"), Time.valueOf("14:30:00"), "Pending"));
    }

    private static void seedFeedback() {
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        feedbackDAO.createFeedback(new Feedback(0, "Alice Johnson", 5, "Very helpful session"));
        feedbackDAO.createFeedback(new Feedback(0, "Bob Lee", 3, "Needs follow-up"));
    }
}
