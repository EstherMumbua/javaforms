
/**
 *
 * @author ESTHER
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GUIFORM extends JFrame {

    private JTextField nameField, mobileField, addressField, dobField;
    private JComboBox<String> genderComboBox;
    private JCheckBox termsCheckBox;

    public GUIFORM() {
        setTitle("Registration Form");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Mobile Number:"));
        mobileField = new JTextField();
        panel.add(mobileField);

        panel.add(new JLabel("Gender:"));
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        panel.add(genderComboBox);

        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        dobField = new JTextField();
        panel.add(dobField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        termsCheckBox = new JCheckBox("I accept the terms and conditions");
        panel.add(termsCheckBox);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (termsCheckBox.isSelected()) {
                    registerUser();
                } else {
                    JOptionPane.showMessageDialog(GUIFORM.this, "Please accept the terms and conditions.");
                }
            }
        });
        panel.add(submitButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
        panel.add(resetButton);

        add(panel, BorderLayout.WEST);

       
        JPanel displayPanel = new JPanel(new BorderLayout());
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(displayPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String dob = dobField.getText();
        String address = addressField.getText();

        
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaform", "root", "");
            String query = "INSERT INTO users (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, mobile);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, dob);
            preparedStatement.setString(5, address);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving to database");
        }

        
        displayUserData(name, mobile, gender, dob, address);
    }

    private void displayUserData(String name, String mobile, String gender, String dob, String address) {
        String userData = "Name: " + name + "\nMobile: " + mobile + "\nGender: " + gender + "\nDOB: " + dob + "\nAddress: " + address;
        JTextArea displayArea = (JTextArea) ((JScrollPane) getContentPane().getComponent(1)).getViewport().getView();
        displayArea.setText(userData);
    }

    private void resetForm() {
        nameField.setText("");
        mobileField.setText("");
        genderComboBox.setSelectedIndex(0);
        dobField.setText("");
        addressField.setText("");
        termsCheckBox.setSelected(false);
        JTextArea displayArea = (JTextArea) ((JScrollPane) getContentPane().getComponent(1)).getViewport().getView();
        displayArea.setText("");
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new GUIFORM());
    }
}
