import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * PayrollGUI.java
 *
 * A Java Swing GUI application for calculating and displaying payroll data.
 * Captures employee information, computes gross pay, displays a summary
 * dialog, and exports the record to a CSV file.
 */
public class PayrollGUI implements ActionListener {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final String APP_TITLE   = "Kennito Industries";
    private static final String FOOTER_TEXT = "Copyright © 2025 All Rights Reserved";

    private static final Color COLOR_DARK_HEADER = new Color(44, 62, 80);
    private static final Color COLOR_BG          = new Color(240, 240, 240);
    private static final Color COLOR_LABEL_TEXT  = new Color(51, 51, 51);
    private static final Color COLOR_BTN_RESET   = new Color(231, 76, 60);
    private static final Color COLOR_BTN_SUBMIT  = new Color(34, 139, 34);

    private static final Font FONT_HEADER  = new Font("Arial", Font.BOLD, 22);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_FOOTER  = new Font("Arial", Font.PLAIN, 12);
    private static final Font FONT_DISPLAY = new Font("Courier New", Font.PLAIN, 14);

    private static final int MAX_NAME_LENGTH = 60;
    private static final int MAX_DEPT_LENGTH = 40;
    private static final int MAX_HOURS_LENGTH = 6;

    private static final String[] WORK_OPTIONS     = { "Field", "Office" };
    private static final String[] POSITION_OPTIONS = { "Manager", "Supervisor", "Staff" };

    // -------------------------------------------------------------------------
    // Components
    // -------------------------------------------------------------------------

    private JFrame mainFrame;

    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JPanel footerPanel;

    private JTextField nameField;
    private JTextField departmentField;
    private JTextField hoursField;

    private JComboBox<String> workCombo;
    private JComboBox<String> positionCombo;

    private JButton resetButton;
    private JButton submitButton;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Initializes all GUI components.
     */
    public PayrollGUI() {
        initFrame();
        initPanels();
        initInputFields();
        initButtons();
    }

    // -------------------------------------------------------------------------
    // Initialization Helpers
    // -------------------------------------------------------------------------

    private void initFrame() {
        mainFrame = new JFrame(APP_TITLE);
        mainFrame.getContentPane().setBackground(COLOR_BG);
    }

    private void initPanels() {
        headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_DARK_HEADER);

        formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        formPanel.setBackground(COLOR_BG);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(COLOR_BG);

        footerPanel = new JPanel();
        footerPanel.setBackground(COLOR_DARK_HEADER);
    }

    private void initInputFields() {
        nameField       = new JTextField();
        departmentField = new JTextField();
        hoursField      = new JTextField();

        limitLength(nameField, MAX_NAME_LENGTH);
        limitLength(departmentField, MAX_DEPT_LENGTH);
        limitLength(hoursField, MAX_HOURS_LENGTH);

        workCombo = new JComboBox<>(WORK_OPTIONS);
        workCombo.setBackground(Color.WHITE);

        positionCombo = new JComboBox<>(POSITION_OPTIONS);
        positionCombo.setBackground(Color.WHITE);
    }

    private void initButtons() {
        resetButton  = createButton("Reset",  COLOR_BTN_RESET);
        submitButton = createButton("Submit", COLOR_BTN_SUBMIT);
    }

    // -------------------------------------------------------------------------
    // Layout & Launch
    // -------------------------------------------------------------------------

    /**
     * Assembles the layout and makes the frame visible.
     */
    public void launch() {
        mainFrame.setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel(APP_TITLE);
        headerLabel.setFont(FONT_HEADER);
        headerLabel.setForeground(Color.WHITE);
        headerPanel.setLayout(new FlowLayout());
        headerPanel.add(headerLabel);
        mainFrame.add(headerPanel, BorderLayout.NORTH);

        // Form
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.add(createLabel("Full Name:"));            formPanel.add(nameField);
        formPanel.add(createLabel("Department:"));           formPanel.add(departmentField);
        formPanel.add(createLabel("Nature of Work:"));       formPanel.add(workCombo);
        formPanel.add(createLabel("Position:"));             formPanel.add(positionCombo);
        formPanel.add(createLabel("No. of Hours Worked:"));  formPanel.add(hoursField);
        mainFrame.add(formPanel, BorderLayout.CENTER);

        // Buttons
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.add(resetButton);
        buttonPanel.add(submitButton);

        // Footer
        JLabel footerLabel = new JLabel(FOOTER_TEXT);
        footerLabel.setFont(FONT_FOOTER);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(footerLabel);

        // South panel combines buttons + footer
        JPanel southPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        southPanel.add(buttonPanel);
        southPanel.add(footerPanel);
        mainFrame.add(southPanel, BorderLayout.SOUTH);

        // Event listeners
        resetButton.addActionListener(this);
        submitButton.addActionListener(this);

        // Frame settings
        mainFrame.setSize(600, 380);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    // -------------------------------------------------------------------------
    // Entry Point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollGUI app = new PayrollGUI();
            app.launch();
        });
    }

    // -------------------------------------------------------------------------
    // Event Handling
    // -------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            clearForm();
        } else {
            submitForm();
        }
    }

    // -------------------------------------------------------------------------
    // Form Operations
    // -------------------------------------------------------------------------

    /** Resets all input fields and combo boxes to their defaults. */
    private void clearForm() {
        nameField.setText("");
        departmentField.setText("");
        hoursField.setText("");
        workCombo.setSelectedIndex(0);
        positionCombo.setSelectedIndex(0);
    }

    /**
     * Validates input, computes payroll, confirms submission, clears the form,
     * exports the record, and shows the payroll summary.
     */
    private void submitForm() {
        String name       = nameField.getText().trim();
        String department = departmentField.getText().trim();
        String hoursText  = hoursField.getText().trim();

        if (!validateInputs(name, department, hoursText)) return;

        float hours = parseHours(hoursText);
        if (hours < 0) return;   // parseHours already showed the error dialog

        String work     = (String) workCombo.getSelectedItem();
        String position = (String) positionCombo.getSelectedItem();

        // Confirm before clearing (better UX: confirm → clear → export)
        showInfoDialog("Form has been submitted.", "Success");
        clearForm();

        PayrollBean record = buildPayrollRecord(name, department, work, position, hours);
        exportRecord(record);
        showPayrollSummary(record);
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    /** Returns false and shows an error dialog if any field is invalid. */
    private boolean validateInputs(String name, String department, String hoursText) {
        if (name.isEmpty() || department.isEmpty() || hoursText.isEmpty()) {
            showErrorDialog("All inputs must be filled.");
            return false;
        }
        return true;
    }

    /**
     * Parses the hours string to a float.
     *
     * @return The parsed value, or -1 if parsing fails or the value is invalid.
     */
    private float parseHours(String hoursText) {
        float hours;
        try {
            hours = Float.parseFloat(hoursText);
        } catch (NumberFormatException ex) {
            showErrorDialog("Non-numerical values are not allowed.");
            return -1;
        }

        if (hours <= 0) {
            showErrorDialog("Invalid number for hours.");
            return -1;
        }

        return hours;
    }

    // -------------------------------------------------------------------------
    // Payroll Record
    // -------------------------------------------------------------------------

    /** Builds and returns a fully populated PayrollBean. */
    private PayrollBean buildPayrollRecord(
            String name, String department,
            String work, String position, float hours) {

        PayrollBean record = new PayrollBean();
        record.setName(name);
        record.setDepartment(department);
        record.setNatureWork(work);
        record.setPositionWork(position);
        record.setHoursWorked(hours);
        return record;
    }

    /** Writes the payroll record to the CSV export file. */
    private void exportRecord(PayrollBean record) {
        PayrollExport exporter = new PayrollExport();
        exporter.write(
            record.getName(),
            record.getDepartment(),
            record.getNatureWork(),
            record.getPositionWork(),
            String.format("%.1f", record.getHoursWorked()),
            String.valueOf(record.getRate()),
            String.valueOf(record.getAllowance()),
            String.valueOf(record.getGrossPay())
        );
    }

    // -------------------------------------------------------------------------
    // Summary Dialog
    // -------------------------------------------------------------------------

    /** Builds and shows the payroll summary dialog for the given record. */
    private void showPayrollSummary(PayrollBean record) {
        String html = buildSummaryHtml(record);
        JLabel summaryLabel = new JLabel(html);
        summaryLabel.setFont(FONT_DISPLAY);

        JOptionPane.showMessageDialog(
            mainFrame,
            summaryLabel,
            "Payroll Information for:",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    /** Formats payroll details as an HTML string for display in a JLabel. */
    private String buildSummaryHtml(PayrollBean record) {
        return String.format(
            "<html>"
            + "<div style='text-align:center; font-family:Courier New; font-size:14pt;'>"
            + "<b><i>%s</i></b></div>"
            + "<pre>"
            + "%-22s %28s%n"
            + "%-22s %28s%n"
            + "%-22s %28.1f%n"
            + "%-22s %28s%n"
            + "%-22s %28s%n"
            + "%-22s %28s"
            + "</pre></html>",
            record.getName(),
            "Position:",            record.getPositionWork(),
            "Department:",          record.getDepartment(),
            "No. of hours worked:", record.getHoursWorked(),
            "Rate:",                "₱" + record.getRate(),
            "Field pay:",           "₱" + record.getAllowance(),
            "Total amount:",        "₱" + record.getGrossPay()
        );
    }

    // -------------------------------------------------------------------------
    // Dialog Helpers
    // -------------------------------------------------------------------------

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoDialog(String message, String title) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // -------------------------------------------------------------------------
    // Component Factory Helpers
    // -------------------------------------------------------------------------

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_LABEL_TEXT);
        return label;
    }

    private JButton createButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    /**
     * Attaches a document filter to a text field that caps input
     * at the given maximum character length.
     */
    private void limitLength(JTextField field, int maxLength) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(
            new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset,
                        String text, AttributeSet attrs) throws BadLocationException {
                    if (text != null &&
                            (fb.getDocument().getLength() + text.length()) <= maxLength) {
                        super.insertString(fb, offset, text, attrs);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length,
                        String text, AttributeSet attrs) throws BadLocationException {
                    if (text != null &&
                            (fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        );
    }
}