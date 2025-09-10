import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

class User {
    String loginId;
    String password;

    User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}

class Ticket {
    String pnr;
    String name;
    int age;
    String trainNumber;
    String classType;
    String dateOfJourney;
    String startingPoint;
    String destination;

    Ticket(String pnr, String name, int age, String trainNumber, String classType, 
           String dateOfJourney, String startingPoint, String destination) {
        this.pnr = pnr;
        this.name = name;
        this.age = age;
        this.trainNumber = trainNumber;
        this.classType = classType;
        this.dateOfJourney = dateOfJourney;
        this.startingPoint = startingPoint;
        this.destination = destination;
    }
}

public class TicketReservationSystem extends JFrame {
    private HashMap<String, User> users = new HashMap<>();
    private HashMap<String, Ticket> tickets = new HashMap<>();
    private int ticketCounter = 1;
    private JTabbedPane tabbedPane;
    private JPanel loginPanel, reservationPanel, cancellationPanel;

    public TicketReservationSystem() {
        // Initialize sample user
        users.put("admin", new User("admin", "password"));
        
        setTitle("Railway Ticket Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        createLoginPanel();
        createReservationPanel();
        createCancellationPanel();
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Reservation", reservationPanel);
        tabbedPane.addTab("Cancellation", cancellationPanel);
        
        // Initially disable reservation and cancellation tabs until login
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);
        
        add(tabbedPane);
        setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel loginLabel = new JLabel("Login ID:");
        JTextField loginField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        
        loginPanel.add(loginLabel);
        loginPanel.add(loginField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Empty cell
        loginPanel.add(loginButton);
        
        loginButton.addActionListener(e -> {
            String loginId = loginField.getText();
            String password = new String(passwordField.getPassword());
            
            if (authenticateUser(loginId, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                tabbedPane.setEnabledAt(1, true);
                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setSelectedIndex(1);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login ID or Password!");
            }
        });
    }

    private void createReservationPanel() {
        reservationPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        reservationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField trainField = new JTextField();
        JComboBox<String> classCombo = new JComboBox<>(new String[]{"Sleeper", "AC", "First Class", "Second Class"});
        JTextField dateField = new JTextField();
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JButton reserveButton = new JButton("Reserve Ticket");
        
        reservationPanel.add(new JLabel("Name:"));
        reservationPanel.add(nameField);
        reservationPanel.add(new JLabel("Age:"));
        reservationPanel.add(ageField);
        reservationPanel.add(new JLabel("Train Number:"));
        reservationPanel.add(trainField);
        reservationPanel.add(new JLabel("Class Type:"));
        reservationPanel.add(classCombo);
        reservationPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        reservationPanel.add(dateField);
        reservationPanel.add(new JLabel("From:"));
        reservationPanel.add(fromField);
        reservationPanel.add(new JLabel("To:"));
        reservationPanel.add(toField);
        reservationPanel.add(new JLabel());
        reservationPanel.add(reserveButton);
        
        reserveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String trainNumber = trainField.getText();
                String classType = (String) classCombo.getSelectedItem();
                String date = dateField.getText();
                String from = fromField.getText();
                String to = toField.getText();
                
                if (name.isEmpty() || trainNumber.isEmpty() || date.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!");
                    return;
                }
                
                String pnr = "PNR" + (ticketCounter++);
                Ticket ticket = new Ticket(pnr, name, age, trainNumber, classType, date, from, to);
                tickets.put(pnr, ticket);
                
                JOptionPane.showMessageDialog(this, "Ticket Reserved Successfully!\nPNR: " + pnr);
                
                // Clear fields
                nameField.setText("");
                ageField.setText("");
                trainField.setText("");
                classCombo.setSelectedIndex(0);
                dateField.setText("");
                fromField.setText("");
                toField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age!");
            }
        });
    }

    private void createCancellationPanel() {
        cancellationPanel = new JPanel(new BorderLayout(10, 10));
        cancellationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel pnrLabel = new JLabel("Enter PNR Number:");
        JTextField pnrField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        
        inputPanel.add(pnrLabel);
        inputPanel.add(pnrField);
        inputPanel.add(searchButton);
        
        JTextArea detailsArea = new JTextArea(10, 40);
        detailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        
        JButton cancelButton = new JButton("Cancel Ticket");
        cancelButton.setEnabled(false);
        
        cancellationPanel.add(inputPanel, BorderLayout.NORTH);
        cancellationPanel.add(scrollPane, BorderLayout.CENTER);
        cancellationPanel.add(cancelButton, BorderLayout.SOUTH);
        
        searchButton.addActionListener(e -> {
            String pnr = pnrField.getText();
            Ticket ticket = tickets.get(pnr);
            
            if (ticket != null) {
                detailsArea.setText("Ticket Details:\n" +
                    "PNR: " + ticket.pnr + "\n" +
                    "Name: " + ticket.name + "\n" +
                    "Age: " + ticket.age + "\n" +
                    "Train Number: " + ticket.trainNumber + "\n" +
                    "Class Type: " + ticket.classType + "\n" +
                    "Date: " + ticket.dateOfJourney + "\n" +
                    "From: " + ticket.startingPoint + "\n" +
                    "To: " + ticket.destination);
                cancelButton.setEnabled(true);
            } else {
                detailsArea.setText("No ticket found with PNR: " + pnr);
                cancelButton.setEnabled(false);
            }
        });
        
        cancelButton.addActionListener(e -> {
            String pnr = pnrField.getText();
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel this ticket?", "Confirm Cancellation", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                tickets.remove(pnr);
                detailsArea.setText("Ticket cancelled successfully!");
                pnrField.setText("");
                cancelButton.setEnabled(false);
            }
        });
    }

    private boolean authenticateUser(String loginId, String password) {
        User user = users.get(loginId);
        return user != null && user.password.equals(password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicketReservationSystem();
        });
    }
}


