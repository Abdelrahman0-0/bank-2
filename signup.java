
package BankGUi;
import javax.swing.*;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class signup {
    public signup() {
        JFrame frame = new JFrame("Sign Up");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        JLabel label1 = new JLabel("First Name:");
        JTextField firstNameField = new JTextField(20);

        JLabel label2 = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField(20);

        JLabel label4 = new JLabel("Email:");
        JTextField emailField = new JTextField(20);

        JLabel label5 = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton signupButton = new JButton("Signup");
           signupButton.addActionListener(e -> {
            frame.dispose();
            new Login(); 
        });
        JButton backButton = new JButton("← Back");

      

        panel.add(label1);
        panel.add(firstNameField);
        panel.add(label2);
        panel.add(lastNameField);
        panel.add(label4);
        panel.add(emailField);
        panel.add(label5);
        panel.add(passwordField);
        panel.add(signupButton);
        panel.add(backButton);
        frame.add(panel);
        frame.setVisible(true);

      
   
    
    
    }

}
