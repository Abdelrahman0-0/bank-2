
package BankGUi;


import bank.account.account;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class fpage extends JFrame {

   public fpage() {
      
       JFrame frame = new JFrame("Home");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

       
        JButton LoginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");
        
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(LoginButton);
        roleGroup.add(signupButton);

      

        LoginButton.addActionListener(e -> {
            frame.dispose();
            new Login(); 
        });

        signupButton.addActionListener(e -> {
            frame.dispose();
            new signup(); 
        });

       
        panel.add(LoginButton);
        panel.add(signupButton);
        frame.add(panel);
        frame.setVisible(true);
    

   }
}
