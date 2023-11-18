package com.mycompany.guiform2;

/**
 * Registration Form
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GUIFORM2 extends JFrame {
    private JTextField idField, nameField, addressField, contactField;
    private JComboBox<String> genderComboBox;
    private JButton registerButton, resetButton;
    private JTextArea displayArea;

    public GUIFORM2() {
        setTitle("Registration Form");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        idField = new JTextField(20);
        nameField = new JTextField(20);
        addressField = new JTextField(20);
        contactField = new JTextField(20);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        registerButton = new JButton("Register");
        resetButton = new JButton("Reset");
        displayArea = new JTextArea(50, 70);

        // Set layout
        setLayout(new BorderLayout());

        // Left panel for input fields
        JPanel leftPanel = new JPanel(new GridLayout(6, 2));
        leftPanel.add(new JLabel("ID:"));
        leftPanel.add(idField);
        leftPanel.add(new JLabel("Name:"));
        leftPanel.add(nameField);
        leftPanel.add(new JLabel("Gender:"));
        leftPanel.add(genderComboBox);
        leftPanel.add(new JLabel("Address:"));
        leftPanel.add(addressField);
        leftPanel.add(new JLabel("Contact:"));
        leftPanel.add(contactField);
        leftPanel.add(registerButton);
        leftPanel.add(resetButton);

        // Right panel for displaying data
        JScrollPane scrollPane = new JScrollPane(displayArea);
        JPanel rightPanel = new JPanel();
        rightPanel.add(scrollPane);

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // Add action listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
    }

    private void registerUser() {
        String id = idField.getText();
        String name = nameField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String address = addressField.getText();
        String contact = contactField.getText();

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/javaform2";
        String username = "root";
        String password = "";

        try {
            // Establish a connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL statement
            String sql = "INSERT INTO user (id, name, gender, address, contact) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, contact);

            // Execute the statement
            preparedStatement.executeUpdate();

            // Display a success message
            JOptionPane.showMessageDialog(this, "User registered successfully!");

            // Close the connection
            connection.close();

            // Display the data in the right-side control
            displayUserData(id, name, gender, address, contact);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering user. Please try again.");
        }
    }

    private void displayUserData(String id, String name, String gender, String address, String contact) {
        String userData = "ID: " + id + "\nName: " + name + "\nGender: " + gender + "\nAddress: " + address + "\nContact: " + contact;
        displayArea.setText(userData + "\n\n");
    }

    private void resetForm() {
        idField.setText("");
        nameField.setText("");
        genderComboBox.setSelectedIndex(0);
        addressField.setText("");
        contactField.setText("");
        displayArea.setText("");
    }

    public static void main(String[] args) {
        // Ensure that the MySQL JDBC driver is in the classpath
        // You can download it from: https://dev.mysql.com/downloads/connector/j/
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Create and show the registration form
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIFORM2().setVisible(true);
            }
        });
    }
}
