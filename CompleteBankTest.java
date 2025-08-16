import java.sql.*;

/**
 * اختبار شامل لنظام البنك مع قاعدة البيانات SQLite
 * Comprehensive Bank System Test with SQLite Database
 */
public class CompleteBankTest {
    private static final String DB_URL = "jdbc:sqlite:bank_system.db";
    
    public static void main(String[] args) {
        System.out.println("=== اختبار شامل لنظام البنك / Comprehensive Bank System Test ===");
        
        try {
            // تهيئة قاعدة البيانات
            initializeDatabase();
            
            // اختبار إضافة المستخدمين
            testUserOperations();
            
            // اختبار إضافة الحسابات
            testAccountOperations();
            
            // اختبار المعاملات
            testTransactionOperations();
            
            // اختبار المديرين
            testAdminOperations();
            
            // عرض الإحصائيات
            displayStatistics();
            
            System.out.println("\n🎉 نجح اختبار نظام البنك بالكامل! / Complete Bank System Test Successful!");
            
        } catch (Exception e) {
            System.err.println("❌ خطأ في الاختبار / Test Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void initializeDatabase() throws SQLException {
        System.out.println("\n=== تهيئة قاعدة البيانات / Database Initialization ===");
        
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        
        // إنشاء جدول المستخدمين
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """);
        
        // إنشاء جدول الحسابات
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                account_name TEXT NOT NULL,
                account_type TEXT NOT NULL,
                account_number TEXT UNIQUE NOT NULL,
                currency TEXT DEFAULT 'EGP',
                balance REAL DEFAULT 0.0,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            );
        """);
        
        // إنشاء جدول المعاملات
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account_id INTEGER NOT NULL,
                transaction_type TEXT NOT NULL,
                amount REAL NOT NULL,
                description TEXT,
                balance_after REAL NOT NULL,
                transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
            );
        """);
        
        // إنشاء جدول المديرين
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS admins (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """);
        
        conn.close();
        System.out.println("✅ تم إنشاء جميع الجداول بنجاح / All tables created successfully");
    }
    
    private static void testUserOperations() throws SQLException {
        System.out.println("\n=== اختبار عمليات المستخدمين / Testing User Operations ===");
        
        Connection conn = DriverManager.getConnection(DB_URL);
        
        // إضافة مستخدمين تجريبيين
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT OR IGNORE INTO users (username, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)");
        
        // مستخدم أول
        pstmt.setString(1, "ahmed123");
        pstmt.setString(2, "أحمد");
        pstmt.setString(3, "محمد");
        pstmt.setString(4, "ahmed@test.com");
        pstmt.setString(5, "password123");
        pstmt.executeUpdate();
        
        // مستخدم ثاني
        pstmt.setString(1, "fatima456");
        pstmt.setString(2, "فاطمة");
        pstmt.setString(3, "علي");
        pstmt.setString(4, "fatima@test.com");
        pstmt.setString(5, "password456");
        pstmt.executeUpdate();
        
        System.out.println("✅ تم إضافة المستخدمين بنجاح / Users added successfully");
        
        // التحقق من المستخدمين
        PreparedStatement authStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        authStmt.setString(1, "ahmed123");
        authStmt.setString(2, "password123");
        ResultSet rs = authStmt.executeQuery();
        
        if (rs.next()) {
            System.out.println("✅ تم التحقق من تسجيل الدخول بنجاح / Login verification successful");
            System.out.println("المستخدم: " + rs.getString("first_name") + " " + rs.getString("last_name"));
        }
        
        conn.close();
    }
    
    private static void testAccountOperations() throws SQLException {
        System.out.println("\n=== اختبار عمليات الحسابات / Testing Account Operations ===");
        
        Connection conn = DriverManager.getConnection(DB_URL);
        
        // إضافة حسابات تجريبية
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT OR IGNORE INTO accounts (user_id, account_name, account_type, account_number, currency, balance) VALUES (?, ?, ?, ?, ?, ?)");
        
        // حساب أول
        pstmt.setInt(1, 1); // ahmed123
        pstmt.setString(2, "حساب التوفير");
        pstmt.setString(3, "Savings");
        pstmt.setString(4, "ACC001");
        pstmt.setString(5, "EGP");
        pstmt.setDouble(6, 5000.0);
        pstmt.executeUpdate();
        
        // حساب ثاني
        pstmt.setInt(1, 2); // fatima456
        pstmt.setString(2, "حساب جاري");
        pstmt.setString(3, "Current");
        pstmt.setString(4, "ACC002");
        pstmt.setString(5, "EGP");
        pstmt.setDouble(6, 3000.0);
        pstmt.executeUpdate();
        
        System.out.println("✅ تم إضافة الحسابات بنجاح / Accounts added successfully");
        
        // عرض الحسابات
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
        
        while (rs.next()) {
            System.out.println("حساب رقم: " + rs.getString("account_number") + 
                             ", النوع: " + rs.getString("account_type") + 
                             ", الرصيد: " + rs.getDouble("balance") + " " + rs.getString("currency"));
        }
        
        conn.close();
    }
    
    private static void testTransactionOperations() throws SQLException {
        System.out.println("\n=== اختبار عمليات المعاملات / Testing Transaction Operations ===");
        
        Connection conn = DriverManager.getConnection(DB_URL);
        conn.setAutoCommit(false); // بدء المعاملات
        
        try {
            // إيداع مبلغ في الحساب الأول
            double depositAmount = 1000.0;
            
            // الحصول على الرصيد الحالي
            PreparedStatement getBalance = conn.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
            getBalance.setInt(1, 1);
            ResultSet rs = getBalance.executeQuery();
            
            double currentBalance = rs.getDouble("balance");
            double newBalance = currentBalance + depositAmount;
            
            // تحديث الرصيد
            PreparedStatement updateBalance = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?");
            updateBalance.setDouble(1, newBalance);
            updateBalance.setInt(2, 1);
            updateBalance.executeUpdate();
            
            // إضافة سجل المعاملة
            PreparedStatement addTransaction = conn.prepareStatement(
                "INSERT INTO transactions (account_id, transaction_type, amount, description, balance_after) VALUES (?, ?, ?, ?, ?)");
            addTransaction.setInt(1, 1);
            addTransaction.setString(2, "DEPOSIT");
            addTransaction.setDouble(3, depositAmount);
            addTransaction.setString(4, "إيداع تجريبي");
            addTransaction.setDouble(5, newBalance);
            addTransaction.executeUpdate();
            
            conn.commit(); // تأكيد المعاملة
            System.out.println("✅ تم الإيداع بنجاح: " + depositAmount + " EGP");
            System.out.println("الرصيد الجديد: " + newBalance + " EGP");
            
            // سحب مبلغ
            double withdrawAmount = 500.0;
            
            getBalance.setInt(1, 1);
            rs = getBalance.executeQuery();
            currentBalance = rs.getDouble("balance");
            
            if (currentBalance >= withdrawAmount) {
                newBalance = currentBalance - withdrawAmount;
                
                updateBalance.setDouble(1, newBalance);
                updateBalance.setInt(2, 1);
                updateBalance.executeUpdate();
                
                addTransaction.setInt(1, 1);
                addTransaction.setString(2, "WITHDRAW");
                addTransaction.setDouble(3, withdrawAmount);
                addTransaction.setString(4, "سحب تجريبي");
                addTransaction.setDouble(5, newBalance);
                addTransaction.executeUpdate();
                
                conn.commit();
                System.out.println("✅ تم السحب بنجاح: " + withdrawAmount + " EGP");
                System.out.println("الرصيد الجديد: " + newBalance + " EGP");
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
        
        conn.close();
    }
    
    private static void testAdminOperations() throws SQLException {
        System.out.println("\n=== اختبار عمليات المديرين / Testing Admin Operations ===");
        
        Connection conn = DriverManager.getConnection(DB_URL);
        
        // إضافة مدير افتراضي
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT OR IGNORE INTO admins (username, email, password) VALUES (?, ?, ?)");
        pstmt.setString(1, "admin");
        pstmt.setString(2, "admin@bank.com");
        pstmt.setString(3, "admin123");
        pstmt.executeUpdate();
        
        System.out.println("✅ تم إضافة المدير الافتراضي / Default admin added");
        
        // التحقق من تسجيل دخول المدير
        PreparedStatement authAdmin = conn.prepareStatement("SELECT * FROM admins WHERE username = ? AND password = ?");
        authAdmin.setString(1, "admin");
        authAdmin.setString(2, "admin123");
        ResultSet rs = authAdmin.executeQuery();
        
        if (rs.next()) {
            System.out.println("✅ تم التحقق من تسجيل دخول المدير / Admin login verified");
            System.out.println("مدير النظام: " + rs.getString("username"));
        }
        
        conn.close();
    }
    
    private static void displayStatistics() throws SQLException {
        System.out.println("\n=== إحصائيات النظام / System Statistics ===");
        
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        
        // عدد المستخدمين
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
        if (rs.next()) {
            System.out.println("عدد المستخدمين / Total Users: " + rs.getInt("count"));
        }
        
        // عدد الحسابات
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM accounts");
        if (rs.next()) {
            System.out.println("عدد الحسابات / Total Accounts: " + rs.getInt("count"));
        }
        
        // عدد المعاملات
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM transactions");
        if (rs.next()) {
            System.out.println("عدد المعاملات / Total Transactions: " + rs.getInt("count"));
        }
        
        // إجمالي الأموال
        rs = stmt.executeQuery("SELECT SUM(balance) as total FROM accounts");
        if (rs.next()) {
            System.out.println("إجمالي الأموال / Total Balance: " + rs.getDouble("total") + " EGP");
        }
        
        // آخر المعاملات
        System.out.println("\n--- آخر المعاملات / Recent Transactions ---");
        rs = stmt.executeQuery("SELECT * FROM transactions ORDER BY transaction_date DESC LIMIT 5");
        while (rs.next()) {
            System.out.println("حساب " + rs.getInt("account_id") + ": " + 
                             rs.getString("transaction_type") + " " + 
                             rs.getDouble("amount") + " EGP - " + 
                             rs.getString("description"));
        }
        
        conn.close();
    }
}