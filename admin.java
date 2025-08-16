package bank.admin;

import bank.database.*;
import java.util.List;

/**
 * فئة إدارة النظام المحدثة مع قاعدة البيانات
 * Updated System Administration class with database integration
 */
public class admin {
    private int id;
    private String username;
    private String email;
    private String password;
    private String createdAt;
    
    // المنشئ الافتراضي
    public admin() {
        DatabaseConnection.initializeDatabase();
    }
    
    // منشئ مع المعاملات
    public admin(String username, String email, String password) {
        DatabaseConnection.initializeDatabase();
        this.username = username;
        this.email = email;
        this.password = password;
        
        // إضافة المدير إلى قاعدة البيانات
        boolean added = AdminDAO.addAdmin(username, email, password);
        if (added) {
            Admin adminData = AdminDAO.authenticateAdmin(username, password);
            if (adminData != null) {
                this.id = adminData.getId();
                this.createdAt = adminData.getCreatedAt();
                System.out.println("تم إنشاء حساب المدير بنجاح / Admin account created successfully");
            }
        } else {
            System.out.println("فشل في إنشاء حساب المدير / Failed to create admin account");
        }
    }
    
    // تسجيل دخول المدير
    public boolean login(String username, String password) {
        Admin adminData = AdminDAO.authenticateAdmin(username, password);
        if (adminData != null) {
            this.id = adminData.getId();
            this.username = adminData.getUsername();
            this.email = adminData.getEmail();
            this.password = adminData.getPassword();
            this.createdAt = adminData.getCreatedAt();
            System.out.println("تم تسجيل دخول المدير بنجاح / Admin login successful");
            return true;
        } else {
            System.out.println("فشل تسجيل دخول المدير / Admin login failed");
            return false;
        }
    }
    
    // عرض جميع المستخدمين
    public void viewAllUsers() {
        System.out.println("\n=== جميع المستخدمين / All Users ===");
        List<User> users = UserDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("لا يوجد مستخدمون / No users found");
        } else {
            users.forEach(user -> {
                System.out.println("المعرف / ID: " + user.getId() + 
                                 ", اسم المستخدم / Username: " + user.getUsername() + 
                                 ", الاسم / Name: " + user.getFirstName() + " " + user.getLastName() + 
                                 ", البريد الإلكتروني / Email: " + user.getEmail());
            });
        }
    }
    
    // عرض جميع الحسابات
    public void viewAllAccounts() {
        System.out.println("\n=== جميع الحسابات / All Accounts ===");
        List<BankAccount> accounts = AccountDAO.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("لا توجد حسابات / No accounts found");
        } else {
            accounts.forEach(account -> {
                System.out.println("معرف الحساب / Account ID: " + account.getId() + 
                                 ", رقم الحساب / Account Number: " + account.getAccountNumber() + 
                                 ", النوع / Type: " + account.getAccountType() + 
                                 ", الرصيد / Balance: " + account.getBalance() + " " + account.getCurrency());
            });
        }
    }
    
    // عرض جميع المعاملات
    public void viewAllTransactions() {
        System.out.println("\n=== جميع المعاملات / All Transactions ===");
        List<Transaction> transactions = TransactionDAO.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("لا توجد معاملات / No transactions found");
        } else {
            transactions.forEach(transaction -> {
                System.out.println("معرف المعاملة / Transaction ID: " + transaction.getId() + 
                                 ", معرف الحساب / Account ID: " + transaction.getAccountId() + 
                                 ", النوع / Type: " + transaction.getTransactionType() + 
                                 ", المبلغ / Amount: " + transaction.getAmount() + 
                                 ", التاريخ / Date: " + transaction.getTransactionDate());
            });
        }
    }
    
    // البحث عن مستخدم
    public void searchUser(String username) {
        User user = UserDAO.getUserByUsername(username);
        if (user != null) {
            System.out.println("\n=== نتائج البحث / Search Results ===");
            System.out.println("المعرف / ID: " + user.getId());
            System.out.println("اسم المستخدم / Username: " + user.getUsername());
            System.out.println("الاسم / Name: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("البريد الإلكتروني / Email: " + user.getEmail());
            System.out.println("تاريخ الإنشاء / Created At: " + user.getCreatedAt());
            
            // عرض حسابات المستخدم
            List<BankAccount> userAccounts = AccountDAO.getAccountsByUserId(user.getId());
            if (!userAccounts.isEmpty()) {
                System.out.println("\n--- حسابات المستخدم / User Accounts ---");
                userAccounts.forEach(account -> {
                    System.out.println("رقم الحساب / Account Number: " + account.getAccountNumber() + 
                                     ", النوع / Type: " + account.getAccountType() + 
                                     ", الرصيد / Balance: " + account.getBalance());
                });
            }
        } else {
            System.out.println("لم يتم العثور على المستخدم / User not found");
        }
    }
    
    // البحث عن حساب
    public void searchAccount(String accountNumber) {
        BankAccount account = AccountDAO.getAccountByNumber(accountNumber);
        if (account != null) {
            System.out.println("\n=== تفاصيل الحساب / Account Details ===");
            System.out.println("معرف الحساب / Account ID: " + account.getId());
            System.out.println("رقم الحساب / Account Number: " + account.getAccountNumber());
            System.out.println("اسم الحساب / Account Name: " + account.getAccountName());
            System.out.println("نوع الحساب / Account Type: " + account.getAccountType());
            System.out.println("العملة / Currency: " + account.getCurrency());
            System.out.println("الرصيد / Balance: " + account.getBalance());
            System.out.println("تاريخ الإنشاء / Created At: " + account.getCreatedAt());
            
            // عرض آخر المعاملات
            List<Transaction> recentTransactions = TransactionDAO.getLastNTransactions(account.getId(), 5);
            if (!recentTransactions.isEmpty()) {
                System.out.println("\n--- آخر المعاملات / Recent Transactions ---");
                recentTransactions.forEach(System.out::println);
            }
        } else {
            System.out.println("لم يتم العثور على الحساب / Account not found");
        }
    }
    
    // حذف مستخدم (إدارة المديرين فقط)
    public boolean deleteUser(int userId) {
        if (this.id <= 0) {
            System.out.println("يجب تسجيل الدخول أولاً / Must login first");
            return false;
        }
        
        boolean deleted = UserDAO.deleteUser(userId);
        if (deleted) {
            System.out.println("تم حذف المستخدم بنجاح / User deleted successfully");
        } else {
            System.out.println("فشل حذف المستخدم / Failed to delete user");
        }
        return deleted;
    }
    
    // تغيير كلمة مرور المدير
    public boolean changePassword(String newPassword) {
        if (this.id <= 0) {
            System.out.println("يجب تسجيل الدخول أولاً / Must login first");
            return false;
        }
        
        boolean changed = AdminDAO.changeAdminPassword(this.id, newPassword);
        if (changed) {
            this.password = newPassword;
            System.out.println("تم تغيير كلمة المرور بنجاح / Password changed successfully");
        } else {
            System.out.println("فشل تغيير كلمة المرور / Failed to change password");
        }
        return changed;
    }
    
    // عرض إحصائيات النظام
    public void viewSystemStatistics() {
        System.out.println("\n=== إحصائيات النظام / System Statistics ===");
        
        // عدد المستخدمين
        List<User> users = UserDAO.getAllUsers();
        System.out.println("عدد المستخدمين / Total Users: " + users.size());
        
        // عدد الحسابات
        List<BankAccount> accounts = AccountDAO.getAllAccounts();
        System.out.println("عدد الحسابات / Total Accounts: " + accounts.size());
        
        // عدد المعاملات
        List<Transaction> transactions = TransactionDAO.getAllTransactions();
        System.out.println("عدد المعاملات / Total Transactions: " + transactions.size());
        
        // إجمالي الأموال في النظام
        double totalBalance = accounts.stream().mapToDouble(BankAccount::getBalance).sum();
        System.out.println("إجمالي الأموال / Total Balance: " + totalBalance + " EGP");
    }
    
    // عرض تفاصيل المدير
    public void displayAdminDetails() {
        if (this.id > 0) {
            System.out.println("\n=== تفاصيل المدير / Admin Details ===");
            System.out.println("معرف المدير / Admin ID: " + this.id);
            System.out.println("اسم المستخدم / Username: " + this.username);
            System.out.println("البريد الإلكتروني / Email: " + this.email);
            System.out.println("تاريخ الإنشاء / Created At: " + this.createdAt);
        } else {
            System.out.println("لم يتم تسجيل الدخول / Not logged in");
        }
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}