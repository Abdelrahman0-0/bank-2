package bank.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة إدارة عمليات قاعدة البيانات للحسابات المصرفية
 * Data Access Object for Bank Accounts
 */
public class AccountDAO {
    
    // إضافة حساب جديد
    public static boolean addAccount(int userId, String accountName, String accountType, String accountNumber, String currency, double initialBalance) {
        String sql = "INSERT INTO accounts (user_id, account_name, account_type, account_number, currency, balance) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, accountName);
            pstmt.setString(3, accountType);
            pstmt.setString(4, accountNumber);
            pstmt.setString(5, currency);
            pstmt.setDouble(6, initialBalance);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding account: " + e.getMessage());
            return false;
        }
    }
    
    // الحصول على حساب باستخدام المعرف
    public static BankAccount getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new BankAccount(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("account_name"),
                    rs.getString("account_type"),
                    rs.getString("account_number"),
                    rs.getString("currency"),
                    rs.getDouble("balance"),
                    rs.getString("created_at")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting account by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // الحصول على حساب برقم الحساب
    public static BankAccount getAccountByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new BankAccount(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("account_name"),
                    rs.getString("account_type"),
                    rs.getString("account_number"),
                    rs.getString("currency"),
                    rs.getDouble("balance"),
                    rs.getString("created_at")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting account by number: " + e.getMessage());
        }
        
        return null;
    }
    
    // الحصول على جميع حسابات المستخدم
    public static List<BankAccount> getAccountsByUserId(int userId) {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BankAccount account = new BankAccount(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("account_name"),
                    rs.getString("account_type"),
                    rs.getString("account_number"),
                    rs.getString("currency"),
                    rs.getDouble("balance"),
                    rs.getString("created_at")
                );
                accounts.add(account);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user accounts: " + e.getMessage());
        }
        
        return accounts;
    }
    
    // تحديث رصيد الحساب
    public static boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }
    
    // إيداع مبلغ في الحساب
    public static boolean deposit(int accountId, double amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // بدء المعاملة
            
            // الحصول على الرصيد الحالي
            String getBalanceSql = "SELECT balance FROM accounts WHERE id = ?";
            PreparedStatement getBalanceStmt = conn.prepareStatement(getBalanceSql);
            getBalanceStmt.setInt(1, accountId);
            ResultSet rs = getBalanceStmt.executeQuery();
            
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            
            double currentBalance = rs.getDouble("balance");
            double newBalance = currentBalance + amount;
            
            // تحديث الرصيد
            String updateBalanceSql = "UPDATE accounts SET balance = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSql);
            updateStmt.setDouble(1, newBalance);
            updateStmt.setInt(2, accountId);
            
            int rowsAffected = updateStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // إضافة سجل المعاملة
                TransactionDAO.addTransaction(accountId, "DEPOSIT", amount, "إيداع / Deposit", newBalance);
                conn.commit(); // تأكيد المعاملة
                return true;
            } else {
                conn.rollback();
                return false;
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback error: " + rollbackEx.getMessage());
            }
            System.err.println("Error depositing: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // سحب مبلغ من الحساب
    public static boolean withdraw(int accountId, double amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // بدء المعاملة
            
            // الحصول على الرصيد الحالي
            String getBalanceSql = "SELECT balance FROM accounts WHERE id = ?";
            PreparedStatement getBalanceStmt = conn.prepareStatement(getBalanceSql);
            getBalanceStmt.setInt(1, accountId);
            ResultSet rs = getBalanceStmt.executeQuery();
            
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            
            double currentBalance = rs.getDouble("balance");
            
            if (currentBalance < amount) {
                System.out.println("رصيد غير كافي / Insufficient balance");
                conn.rollback();
                return false;
            }
            
            double newBalance = currentBalance - amount;
            
            // تحديث الرصيد
            String updateBalanceSql = "UPDATE accounts SET balance = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSql);
            updateStmt.setDouble(1, newBalance);
            updateStmt.setInt(2, accountId);
            
            int rowsAffected = updateStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // إضافة سجل المعاملة
                TransactionDAO.addTransaction(accountId, "WITHDRAW", amount, "سحب / Withdrawal", newBalance);
                conn.commit(); // تأكيد المعاملة
                return true;
            } else {
                conn.rollback();
                return false;
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback error: " + rollbackEx.getMessage());
            }
            System.err.println("Error withdrawing: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // تحويل مبلغ بين الحسابات
    public static boolean transfer(int fromAccountId, int toAccountId, double amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // بدء المعاملة
            
            // التحقق من الرصيد في الحساب المرسل
            String getBalanceSql = "SELECT balance FROM accounts WHERE id = ?";
            PreparedStatement getBalanceStmt = conn.prepareStatement(getBalanceSql);
            getBalanceStmt.setInt(1, fromAccountId);
            ResultSet rs = getBalanceStmt.executeQuery();
            
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            
            double fromBalance = rs.getDouble("balance");
            
            if (fromBalance < amount) {
                System.out.println("رصيد غير كافي للتحويل / Insufficient balance for transfer");
                conn.rollback();
                return false;
            }
            
            // الحصول على رصيد الحساب المستقبل
            getBalanceStmt.setInt(1, toAccountId);
            rs = getBalanceStmt.executeQuery();
            
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            
            double toBalance = rs.getDouble("balance");
            
            // تحديث رصيد الحساب المرسل
            String updateSql = "UPDATE accounts SET balance = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setDouble(1, fromBalance - amount);
            updateStmt.setInt(2, fromAccountId);
            updateStmt.executeUpdate();
            
            // تحديث رصيد الحساب المستقبل
            updateStmt.setDouble(1, toBalance + amount);
            updateStmt.setInt(2, toAccountId);
            updateStmt.executeUpdate();
            
            // إضافة سجلات المعاملات
            TransactionDAO.addTransaction(fromAccountId, "TRANSFER_OUT", amount, "تحويل صادر / Transfer Out", fromBalance - amount);
            TransactionDAO.addTransaction(toAccountId, "TRANSFER_IN", amount, "تحويل وارد / Transfer In", toBalance + amount);
            
            conn.commit(); // تأكيد المعاملة
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback error: " + rollbackEx.getMessage());
            }
            System.err.println("Error transferring: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // حذف حساب
    public static boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }
    
    // التحقق من وجود رقم الحساب
    public static boolean accountNumberExists(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking account number existence: " + e.getMessage());
        }
        
        return false;
    }
    
    // الحصول على جميع الحسابات
    public static List<BankAccount> getAllAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                BankAccount account = new BankAccount(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("account_name"),
                    rs.getString("account_type"),
                    rs.getString("account_number"),
                    rs.getString("currency"),
                    rs.getDouble("balance"),
                    rs.getString("created_at")
                );
                accounts.add(account);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all accounts: " + e.getMessage());
        }
        
        return accounts;
    }
}

// فئة BankAccount للاستخدام مع قاعدة البيانات
class BankAccount {
    private int id;
    private int userId;
    private String accountName;
    private String accountType;
    private String accountNumber;
    private String currency;
    private double balance;
    private String createdAt;
    
    public BankAccount(int id, int userId, String accountName, String accountType, String accountNumber, String currency, double balance, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
        this.createdAt = createdAt;
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
        return "BankAccount{" +
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