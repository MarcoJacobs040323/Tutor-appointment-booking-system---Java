package view;

import controller.FeedbackController;
import model.Feedback;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class FeedbackPanel extends JPanel {
    private final FeedbackController controller = new FeedbackController();
    private int selectedFeedbackID = -1;

    // Form components
    private final JTextField txtStudentName = new JTextField(20);
    private final JComboBox<Integer> cmbRating = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
    private final JTextArea txtComments = new JTextArea(3, 20);

    // Buttons
    private final JButton btnAdd = new JButton("Add");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnClear = new JButton("Clear");

    // Table
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"ID", "Student", "Rating", "Comments"}, 0);
    private final JTable tblFeedback = new JTable(tableModel);

    public FeedbackPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Form Panel ===
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Feedback Details", TitledBorder.LEFT, TitledBorder.TOP));

        formPanel.add(createLabeledField("Student Name:", txtStudentName));
        formPanel.add(createLabeledField("Rating (1–5):", cmbRating));

        JScrollPane commentScroll = new JScrollPane(txtComments);
        commentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        commentScroll.setPreferredSize(new Dimension(200, 60));
        formPanel.add(createLabeledField("Comments:", commentScroll));

        // === Buttons Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);

        // === Table Panel ===
        JScrollPane tableScrollPane = new JScrollPane(tblFeedback);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Feedback List", TitledBorder.LEFT, TitledBorder.TOP));
        add(tableScrollPane, BorderLayout.CENTER);

        // === Action Listeners ===
        btnAdd.addActionListener(e -> {
            if (!validateInput()) return;

                String student = txtStudentName.getText().trim();
                int rating = (int) cmbRating.getSelectedItem();
                String comments = txtComments.getText().trim();

                try {
                    String result = controller.addFeedback(student, rating, comments);
                    JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadFeedback();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error adding feedback:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedFeedbackID == -1 || !validateInput()) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                "Update feedback with ID: " + selectedFeedbackID + "?",
                "Confirm Update", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String student = txtStudentName.getText().trim();
                int rating = (int) cmbRating.getSelectedItem();
                String comments = txtComments.getText().trim();

                try {
                    String result = controller.updateFeedback(selectedFeedbackID, student, rating, comments);
                    JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadFeedback();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error updating feedback:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedFeedbackID == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete feedback with ID: " + selectedFeedbackID + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String result = controller.deleteFeedback(selectedFeedbackID);
                    JOptionPane.showMessageDialog(this, result, "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    loadFeedback();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting feedback:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tblFeedback.getSelectionModel().addListSelectionListener(e -> {
            int row = tblFeedback.getSelectedRow();
            if (row != -1) {
                selectedFeedbackID = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtStudentName.setText(tableModel.getValueAt(row, 1).toString());
                cmbRating.setSelectedItem(Integer.parseInt(tableModel.getValueAt(row, 2).toString()));
                txtComments.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        btnClear.addActionListener(e -> clearFields());

        loadFeedback();
    }

    private JPanel createLabeledField(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(140, 25));
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return panel;
    }

    private void loadFeedback() {
        tableModel.setRowCount(0);
            try {
                List<Feedback> feedbackList = controller.getAllFeedback();
                for (Feedback fb : feedbackList) {
                    tableModel.addRow(new Object[]{
                            fb.getId(), fb.getStudentName(), fb.getRating(), fb.getComments()
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading feedback:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtStudentName.setText("");
        cmbRating.setSelectedIndex(0);
        txtComments.setText("");
        selectedFeedbackID = -1;
        tblFeedback.clearSelection();
    }

    private boolean validateInput() {
        String student = txtStudentName.getText().trim();
        String comments = txtComments.getText().trim();

        if (student.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (comments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter comments.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int rating = (int) cmbRating.getSelectedItem();
        if (rating < 1 || rating > 5) {
            JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
