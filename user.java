
package bank.account;

abstract class user {
private String username;
private String fname;
private String lname;
private String email;
private String password;

public user(){}
public user(String username , String fname ,String lname ,String email , String password  ){
this.username=username;
this.fname=fname;
this.lname=lname;
this.email=email;
this.password=password;
}

    public void setusername(String username) {
        this.username = username;
    }

    public void setfname(String fname) {
        this.fname = fname;
    }

    public void setlname(String lname) {
        this.lname = lname;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public void sepPassword(String password) {
        this.password = password;
    }

    public String getusername() {
        return username;
    }

    public String getfname() {
        return fname;
    }

    public String getlname() {
        return lname;
    }

    public String getemail() {
        return email;
    }

    public String getpassword() {
        return password;
    }

    @Override
    public String toString() {
        return "user{" + "username=" + username + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", password=" + password + '}';
    }

}
