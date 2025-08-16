
package bank.account;


public class client extends user{
    private String Name;
    private String Email;
    private String Password;

    
    
    
    public client(){}
    
    
    public client(String username , String fname ,String lname ,String email , String password ,String Name, String Email, String Password) {
        super(username , fname ,lname , email ,password  );
        this.Name = Name;
        this.Email = Email;
        this.Password = Password;
    }
public void showacc(){}
public void withdraw(){}
public void deposit(){}
public void statement(){}
public void transaction(){}

    
      
    
    
  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    @Override
    public String toString() {
        return "client{" + "Name=" + Name + ", Email=" + Email + ", Password=" + Password + '}';
    }

}
