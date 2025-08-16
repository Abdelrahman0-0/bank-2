
package BankGUi;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Login {
   
    public Login() {
  
  
      
        JFrame frame = new JFrame("login");
        frame.setSize(3000, 3000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
       JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1)); 
      
        JButton LoginButton = new JButton("Login");
    LoginButton.addActionListener(e -> {
            frame.dispose(); 
            new home();
        });
      JButton signupButton = new JButton("Signup");
       signupButton.addActionListener(e -> {
            frame.dispose();
            new signup(); 
        });
      
    JButton backButton = new JButton("← Back");

        backButton.addActionListener(e -> {
            frame.dispose(); 
            new fpage (); 
        });
    
        JLabel label1 = new JLabel("username:");
        JTextField usernameField = new JTextField(20);
        
        
        JLabel label2 = new JLabel("password:");
    JPasswordField passwordField = new JPasswordField(20);
        
        
       
  
        panel.add(label1);
        panel.add(label2);
        panel.add(usernameField);
        panel.add(passwordField);
       
     
       
        panel.add(LoginButton);
        panel.add(backButton);
        frame.add(panel);
           
            
          
        
        frame.setVisible(true);
 
}
}
