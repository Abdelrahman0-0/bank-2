
package bank.account;

import java.util.Scanner;


public class Banktest {

    public static void main(String[] args) {
    Scanner b= new Scanner(System.in);
        
client c = new client();
account a= new account(
            "Body",                 
            "Savings",              
            "123456789",             
            "EGP",                   
            1000.0,                  
            "body",              
            "Body",                  
            "elghonemy",                 
            "body@gmail.com",      
            "12122004",         
            "Body Client",           
            "Body client@gmail.com",    
            "clientPass456"          
        );

    
      
        a.showAcc();

      
        System.out.println(" Depositing 500:");
        a.deposit(500);

       
        System.out.println(" Withdrawing 300:");
        a.withdraw(300);

      
        System.out.println("Statement:");
        a.statement();

       
        System.out.println(" Transaction (withdraw 200):");
        a.transaction("withdraw", 200);

       
        System.out.println(" Full Account Info:");
        System.out.println(a);
 


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    }
    
}
