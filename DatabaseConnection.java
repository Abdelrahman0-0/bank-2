package bank.database;

import java.sql.*;
import java.io.File;

/**
 * فئة الاتصال بقاعدة البيانات SQLite
 * Database Connection Class for SQLite
 */
public class DatabaseConnection {
    private static final String DB_NAME = "bank_system.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;
    private static Connection connection = null;
    
    // إنشاء اتصال بقاعدة البيانات
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("تم الاتصال بقاعدة البيانات بنجاح / Database connected successfully");
            } catch (ClassNotFoundException e) {
                System.err.println("SQLite JDBC driver not found: " + e.getMessage());
                throw new SQLException("SQLite driver not found", e);
            }
        }
        return connection;
    }
    
    // إغلاق الاتصال
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("تم إغلاق الاتصال بقاعدة البيانات / Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    // إنشاء جميع الجداول
    public static void createTables() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // إنشاء جدول المستخدمين
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                );
            """;
            
            // إنشاء جدول المديرين
            String createAdminsTable = """
                CREATE TABLE IF NOT EXISTS admins (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                );
            """;
            
            // إنشاء جدول الحسابات
            String createAccountsTable = """
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
            """;
            
            // إنشاء جدول المعاملات
            String createTransactionsTable = """
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
            """;
            
            stmt.execute(createUsersTable);
            stmt.execute(createAdminsTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
            
            System.out.println("تم إنشاء جميع الجداول بنجاح / All tables created successfully");
            
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }
    
    // تهيئة قاعدة البيانات مع بيانات أولية
    public static void initializeDatabase() {
        createTables();
        
        // إضافة مدير افتراضي
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT OR IGNORE INTO admins (username, email, password) VALUES (?, ?, ?)")) {
            
            pstmt.setString(1, "admin");
            pstmt.setString(2, "admin@bank.com");
            pstmt.setString(3, "admin123");
            pstmt.executeUpdate();
            
            System.out.println("تم إضافة المدير الافتراضي / Default admin added");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}