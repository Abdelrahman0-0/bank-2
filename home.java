
package BankGUi;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
public class home {
     public home(){
      JFrame frame=new JFrame("Home Page");
 frame.setSize(3000, 3000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       JPanel panel = new JPanel();
  JLabel label = new JLabel("Welcome to Home Page!");
   
 // panel.setLayout(new GridLayout(0, 1));
       
 JButton AboutUsButton = new JButton("About Us");  
 JButton CousreButton = new JButton("Course");
   
   JButton AttendanceButton = new JButton("Attendance");
   
   JButton GradesButton = new JButton("Grades");
   
   JButton ExitButton = new JButton("Exit");
       
  CousreButton.addActionListener(e -> {
           new transaction(); 
          frame.setVisible(false);
        frame.dispose(); 
});
  
   AttendanceButton.addActionListener(e -> {
           new account(); 
          frame.setVisible(false);
        frame.dispose(); 
});
   
 
 
    
       
       
       
       
       
       
        frame.add(panel);
        panel.add(label);
        
        panel.add(AboutUsButton);
        
        panel.add(CousreButton);
        
        panel.add(AttendanceButton);
        
        panel.add(GradesButton);
        
        panel.add(ExitButton);
        frame.setVisible(true);
  
    
    
    
    
    
    
    
    
    
}
}
