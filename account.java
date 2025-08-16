package bank.account;

import bank.database.*;

/**
 * فئة الحساب المصرفي المحدثة مع قاعدة البيانات
 * Updated Bank Account class with database integration
 */
public class account {
    private int id;
    private int userId;
    private String accountName;
    private String accountType;
    private String accountNumber;
    private String currency;
    private double balance;
    private String createdAt;
    
    // المنشئ الافتراضي
    public account() {
        // تهيئة قاعدة البيانات
        DatabaseConnection.initializeDatabase();
    }
    
    // منشئ مع جميع المعاملات
    public account(String accountName, String accountType, String accountNumber, 
                   String currency, double balance, String username, String fname, 
                   String lname, String email, String password, String clientName, 
                   String clientEmail, String clientPassword) {
        
        // تهيئة قاعدة البيانات
        DatabaseConnection.initializeDatabase();
        
        // إضافة المستخدم أولاً
        boolean userAdded = UserDAO.addUser(username, fname, lname, email, password);
        
        if (userAdded) {
            // الحصول على المستخدم المضاف
            User user = UserDAO.getUserByUsername(username);
            if (user != null) {
                this.userId = user.getId();
                
                // إضافة الحساب
                boolean accountAdded = AccountDAO.addAccount(userId, accountName, accountType, 
                                                           accountNumber, currency, balance);
                
                if (accountAdded) {
                    // الحصول على الحساب المضاف
                    BankAccount bankAccount = AccountDAO.getAccountByNumber(accountNumber);
                    if (bankAccount != null) {
                        this.id = bankAccount.getId();
                        this.accountName = bankAccount.getAccountName();
                        this.accountType = bankAccount.getAccountType();
                        this.accountNumber = bankAccount.getAccountNumber();
                        this.currency = bankAccount.getCurrency();
                        this.balance = bankAccount.getBalance();
                        this.createdAt = bankAccount.getCreatedAt();
                        
                        System.out.println("تم إنشاء الحساب بنجاح / Account created successfully");
                    }
                }
            }
        }
    }
    
    // عرض تفاصيل الحساب
    public void showAcc() {
        if (id > 0) {
            BankAccount account = AccountDAO.getAccountById(id);
            if (account != null) {
                System.out.println("=== تفاصيل الحساب / Account Details ===");
                System.out.println("معرف الحساب / Account ID: " + account.getId());
                System.out.println("اسم الحساب / Account Name: " + account.getAccountName());
                System.out.println("نوع الحساب / Account Type: " + account.getAccountType());
                System.out.println("رقم الحساب / Account Number: " + account.getAccountNumber());
                System.out.println("العملة / Currency: " + account.getCurrency());
                System.out.println("الرصيد / Balance: " + account.getBalance());
                System.out.println("تاريخ الإنشاء / Created At: " + account.getCreatedAt());
            } else {
                System.out.println("لم يتم العثور على الحساب / Account not found");
            }
        } else {
            System.out.println("لم يتم تهيئة الحساب بعد / Account not initialized");
        }
    }
    
    // إيداع مبلغ
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("المبلغ يجب أن يكون أكبر من صفر / Amount must be greater than zero");
            return false;
        }
        
        boolean success = AccountDAO.deposit(id, amount);
        if (success) {
            // تحديث الرصيد المحلي
            BankAccount updatedAccount = AccountDAO.getAccountById(id);
            if (updatedAccount != null) {
                this.balance = updatedAccount.getBalance();
            }
            System.out.println("تم الإيداع بنجاح / Deposit successful. مبلغ الإيداع / Amount: " + amount);
            System.out.println("الرصيد الجديد / New Balance: " + this.balance);
        } else {
            System.out.println("فشل الإيداع / Deposit failed");
        }
        return success;
    }
    
    // سحب مبلغ
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("المبلغ يجب أن يكون أكبر من صفر / Amount must be greater than zero");
            return false;
        }
        
        boolean success = AccountDAO.withdraw(id, amount);
        if (success) {
            // تحديث الرصيد المحلي
            BankAccount updatedAccount = AccountDAO.getAccountById(id);
            if (updatedAccount != null) {
                this.balance = updatedAccount.getBalance();
            }
            System.out.println("تم السحب بنجاح / Withdrawal successful. مبلغ السحب / Amount: " + amount);
            System.out.println("الرصيد الجديد / New Balance: " + this.balance);
        } else {
            System.out.println("فشل السحب / Withdrawal failed");
        }
        return success;
    }
    
    // كشف الحساب
    public void statement() {
        System.out.println(TransactionDAO.getAccountStatement(id));
    }
    
    // تنفيذ معاملة
    public boolean transaction(String type, double amount) {
        if (type == null) {
            System.out.println("نوع المعاملة مطلوب / Transaction type required");
            return false;
        }
        
        switch (type.toLowerCase()) {
            case "deposit":
                return deposit(amount);
            case "withdraw":
                return withdraw(amount);
            default:
                System.out.println("نوع معاملة غير صحيح / Invalid transaction type");
                return false;
        }
    }
    
    // تحويل مبلغ إلى حساب آخر
    public boolean transfer(String toAccountNumber, double amount) {
        BankAccount toAccount = AccountDAO.getAccountByNumber(toAccountNumber);
        if (toAccount == null) {
            System.out.println("الحساب المستهدف غير موجود / Target account not found");
            return false;
        }
        
        boolean success = AccountDAO.transfer(id, toAccount.getId(), amount);
        if (success) {
            // تحديث الرصيد المحلي
            BankAccount updatedAccount = AccountDAO.getAccountById(id);
            if (updatedAccount != null) {
                this.balance = updatedAccount.getBalance();
            }
            System.out.println("تم التحويل بنجاح / Transfer successful");
            System.out.println("تم تحويل " + amount + " إلى حساب " + toAccountNumber);
            System.out.println("الرصيد الجديد / New Balance: " + this.balance);
        } else {
            System.out.println("فشل التحويل / Transfer failed");
        }
        return success;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", accountName='" + accountName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}