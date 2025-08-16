package bank.transaction;

import bank.database.*;

/**
 * فئة المعاملات المالية المحدثة مع قاعدة البيانات
 * Updated Financial Transaction class with database integration
 */
public class transaction {
    private int id;
    private int accountId;
    private String transactionType;
    private double amount;
    private String description;
    private double balanceAfter;
    private String transactionDate;
    
    // المنشئ الافتراضي
    public transaction() {
        DatabaseConnection.initializeDatabase();
    }
    
    // منشئ مع المعاملات
    public transaction(int accountId, String transactionType, double amount, String description) {
        DatabaseConnection.initializeDatabase();
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }
    
    // تنفيذ المعاملة
    public boolean execute() {
        BankAccount account = AccountDAO.getAccountById(accountId);
        if (account == null) {
            System.out.println("الحساب غير موجود / Account not found");
            return false;
        }
        
        boolean success = false;
        
        switch (transactionType.toUpperCase()) {
            case "DEPOSIT":
                success = AccountDAO.deposit(accountId, amount);
                break;
            case "WITHDRAW":
                success = AccountDAO.withdraw(accountId, amount);
                break;
            default:
                System.out.println("نوع معاملة غير مدعوم / Unsupported transaction type");
                return false;
        }
        
        if (success) {
            // الحصول على المعاملة المضافة
            Transaction lastTransaction = TransactionDAO.getLastNTransactions(accountId, 1).get(0);
            if (lastTransaction != null) {
                this.id = lastTransaction.getId();
                this.balanceAfter = lastTransaction.getBalanceAfter();
                this.transactionDate = lastTransaction.getTransactionDate();
            }
            System.out.println("تمت المعاملة بنجاح / Transaction completed successfully");
        }
        
        return success;
    }
    
    // الحصول على تاريخ المعاملات
    public static void displayTransactionHistory(int accountId) {
        System.out.println("=== تاريخ المعاملات / Transaction History ===");
        TransactionDAO.getTransactionsByAccountId(accountId).forEach(System.out::println);
    }
    
    // الحصول على المعاملات حسب النوع
    public static void displayTransactionsByType(int accountId, String type) {
        System.out.println("=== معاملات " + type + " / " + type + " Transactions ===");
        TransactionDAO.getTransactionsByType(accountId, type).forEach(System.out::println);
    }
    
    // الحصول على إجمالي المبلغ حسب النوع
    public static double getTotalAmountByType(int accountId, String type) {
        return TransactionDAO.getTotalByTransactionType(accountId, type);
    }
    
    // عرض تفاصيل المعاملة
    public void displayDetails() {
        if (id > 0) {
            Transaction trans = TransactionDAO.getTransactionById(id);
            if (trans != null) {
                System.out.println("=== تفاصيل المعاملة / Transaction Details ===");
                System.out.println("معرف المعاملة / Transaction ID: " + trans.getId());
                System.out.println("معرف الحساب / Account ID: " + trans.getAccountId());
                System.out.println("نوع المعاملة / Transaction Type: " + trans.getTransactionType());
                System.out.println("المبلغ / Amount: " + trans.getAmount());
                System.out.println("الوصف / Description: " + trans.getDescription());
                System.out.println("الرصيد بعد المعاملة / Balance After: " + trans.getBalanceAfter());
                System.out.println("تاريخ المعاملة / Transaction Date: " + trans.getTransactionDate());
            }
        } else {
            System.out.println("لم يتم تنفيذ المعاملة بعد / Transaction not executed yet");
        }
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    
    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", balanceAfter=" + balanceAfter +
                ", transactionDate='" + transactionDate + '\'' +
                '}';
    }
}
