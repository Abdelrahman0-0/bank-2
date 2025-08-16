package bank.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة إدارة عمليات قاعدة البيانات للمعاملات المالية
 * Data Access Object for Financial Transactions
 */
public class TransactionDAO {
    
    // إضافة معاملة جديدة
    public static boolean addTransaction(int accountId, String transactionType, double amount, String description, double balanceAfter) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, description, balance_after) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setString(2, transactionType);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);
            pstmt.setDouble(5, balanceAfter);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            return false;
        }
    }
    
    // الحصول على معاملة باستخدام المعرف
    public static Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getDouble("balance_after"),
                    rs.getString("transaction_date")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transaction by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // الحصول على جميع معاملات الحساب
    public static List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getDouble("balance_after"),
                    rs.getString("transaction_date")
                );
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting account transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    // الحصول على معاملات الحساب في فترة زمنية محددة
    public static List<Transaction> getTransactionsByAccountIdAndDateRange(int accountId, String startDate, String endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND DATE(transaction_date) BETWEEN ? AND ? ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setString(2, startDate);
            pstmt.setString(3, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getDouble("balance_after"),
                    rs.getString("transaction_date")
                );
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions by date range: " + e.getMessage());
        }
        
        return transactions;
    }
    
    // الحصول على معاملات حسب النوع
    public static List<Transaction> getTransactionsByType(int accountId, String transactionType) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND transaction_type = ? ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setString(2, transactionType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getDouble("balance_after"),
                    rs.getString("transaction_date")
                );
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions by type: " + e.getMessage());
        }
        
        return transactions;
    }
    
    // الحصول على آخر N معاملات
    public static List<Transaction> getLastNTransactions(int accountId, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getDouble("balance_after"),
                    rs.getString("transaction_date")
                );
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting last N transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    // حساب مجموع المعاملات حسب النوع
    public static double getTotalByTransactionType(int accountId, String transactionType) {
        String sql = "SELECT SUM(amount) as total FROM transactions WHERE account_id = ? AND transaction_type = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setString(2, transactionType);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total by transaction type: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // عد المعاملات حسب النوع
    public static int getTransactionCountByType(int accountId, String transactionType) {
        String sql = "SELECT COUNT(*) as count FROM transactions WHERE account_id = ? AND transaction_type = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            pstmt.setString(2, transactionType);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transaction count: " + e.getMessage());
        }
        
        return 0;
    }
    
    // حذف معاملات الحساب
    public static boolean deleteTransactionsByAccountId(int accountId) {
        String sql = "DELETE FROM transactions WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected >= 0; // يمكن أن يكون صفراً إذا لم تكن هناك معاملات
            
        } catch (SQLException e) {
            System.err.println("Error deleting account transactions: " + e.getMessage());
            return false;
        }
    }
    
    // الحصول على كشف الحساب الشامل
    public static String getAccountStatement(int accountId) {
        StringBuilder statement = new StringBuilder();
        List<Transaction> transactions = getTransactionsByAccountId(accountId);
        
        statement.append("=== كشف الحساب / Account Statement ===\n");
        statement.append("رقم الحساب / Account ID: ").append(accountId).append("\n");
        statement.append("عدد المعاملات / Total Transactions: ").append(transactions.size()).append("\n\n");
        
        if (transactions.isEmpty()) {
            statement.append("لا توجد معاملات / No transactions found\n");
        } else {
            statement.append("التاريخ\t\tالنوع\t\tالمبلغ\t\tالوصف\t\tالرصيد بعد المعاملة\n");
            statement.append("Date\t\tType\t\tAmount\t\tDescription\t\tBalance After\n");
            statement.append("=".repeat(100)).append("\n");
            
            for (Transaction transaction : transactions) {
                statement.append(String.format("%s\t%s\t%.2f\t%s\t%.2f\n",
                    transaction.getTransactionDate(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getBalanceAfter()
                ));
            }
        }
        
        return statement.toString();
    }
    
    // الحصول على جميع المعاملات
    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getDouble("balance_after"),
                    rs.getString("transaction_date")
                );
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
        }
        
        return transactions;
    }
}

// فئة Transaction للاستخدام مع قاعدة البيانات
class Transaction {
    private int id;
    private int accountId;
    private String transactionType;
    private double amount;
    private String description;
    private double balanceAfter;
    private String transactionDate;
    
    public Transaction(int id, int accountId, String transactionType, double amount, String description, double balanceAfter, String transactionDate) {
        this.id = id;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.transactionDate = transactionDate;
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