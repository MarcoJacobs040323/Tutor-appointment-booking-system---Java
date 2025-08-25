package view;

import controller.CounselorController;
import model.Counselor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class CounselorPanel extends JPanel {

    private final CounselorController controller = new CounselorController();
    private int selectedCounselorID = -1;

    // Form fields
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtSpecialization = new JTextField(20);
    private final JTextField txtAvailability = new JTextField(20);

    // Buttons
    private final JButton btnAdd = new JButton("Add");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnClear = new JButton("Clear");

    // Table
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"ID", "Name", "Specialization", "Availability"}, 0);
    private final JTable tblCounselors = new JTable(tableModel);

    public CounselorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Counselor Details", TitledBorder.LEFT, TitledBorder.TOP));

        formPanel.add(createLabeledField("Name:", txtName));
        formPanel.add(createLabeledField("Specialization:", txtSpecialization));
        formPanel.add(createLabeledField("Availability:", txtAvailability));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(buttonPanel);
        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        JScrollPane scrollPane = new JScrollPane(tblCounselors);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Counselor List", TitledBorder.LEFT, TitledBorder.TOP));
        add(scrollPane, BorderLayout.CENTER);

        // Load table data
        loadCounselors();

        // Button Actions
        btnAdd.addActionListener(e -> {
            if (!validateInput()) return;

           try {
                int id = controller.addCounselor(
                    txtName.getText().trim(),
                    txtSpecialization.getText().trim(),
                    txtAvailability.getText().trim());

                if (id != -1) {
                    JOptionPane.showMessageDialog(this, "Counselor added successfully (ID: " + id + ")");
                    loadCounselors();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add counselor. It may already exist.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding counselor: " + ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedCounselorID == -1 || !validateInput()) return;

            try {
                String message = controller.updateCounselor(
                    selectedCounselorID,
                    txtName.getText().trim(),
                    txtSpecialization.getText().trim(),
                    txtAvailability.getText().trim());

                JOptionPane.showMessageDialog(this, message);
                loadCounselors();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating counselor: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedCounselorID == -1) {
                JOptionPane.showMessageDialog(this, "Please select a counselor to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete counselor with ID: " + selectedCounselorID + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    JOptionPane.showMessageDialog(this, controller.deleteCounselor(selectedCounselorID));
                    loadCounselors();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting counselor: " + ex.getMessage());
                }
            }
        });

        tblCounselors.getSelectionModel().addListSelectionListener(e -> {
            int row = tblCounselors.getSelectedRow();
            if (row != -1) {
                selectedCounselorID = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtSpecialization.setText(tableModel.getValueAt(row, 2).toString());
                txtAvailability.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        btnClear.addActionListener(e -> clearFields());
    }

    private JPanel createLabeledField(String label, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(100, 25));
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return panel;
    }

    private void loadCounselors() {
        try {
            tableModel.setRowCount(0);
            List<Counselor> counselors = controller.getAllCounselors();
            for (Counselor c : counselors) {
                tableModel.addRow(new Object[]{
                    c.getId(), c.getName(), c.getSpecialization(), c.getAvailability()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading counselors: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtSpecialization.setText("");
        txtAvailability.setText("");
        selectedCounselorID = -1;
        tblCounselors.clearSelection();
    }

    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty() ||
            txtSpecialization.getText().trim().isEmpty() ||
            txtAvailability.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
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
