package bank.database;

import java.util.Scanner;

/**
 * فئة تهيئة قاعدة البيانات واختبار العمليات
 * Database Initializer and Operations Tester
 */
public class DatabaseInitializer {
    
    public static void main(String[] args) {
        System.out.println("=== مرحباً بك في نظام البنك / Welcome to Bank System ===");
        
        // تهيئة قاعدة البيانات
        System.out.println("جاري تهيئة قاعدة البيانات... / Initializing Database...");
        DatabaseConnection.initializeDatabase();
        
        // قائمة التحكم
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.println("\n=== قائمة العمليات / Operations Menu ===");
            System.out.println("1. اختبار عمليات المستخدمين / Test User Operations");
            System.out.println("2. اختبار عمليات الحسابات / Test Account Operations");
            System.out.println("3. اختبار عمليات المعاملات / Test Transaction Operations");
            System.out.println("4. اختبار عمليات المديرين / Test Admin Operations");
            System.out.println("5. عرض جميع البيانات / Display All Data");
            System.out.println("6. إضافة بيانات تجريبية / Add Sample Data");
            System.out.println("0. خروج / Exit");
            System.out.print("اختر العملية / Choose operation: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // تنظيف البافر
            
            switch (choice) {
                case 1:
                    testUserOperations(scanner);
                    break;
                case 2:
                    testAccountOperations(scanner);
                    break;
                case 3:
                    testTransactionOperations(scanner);
                    break;
                case 4:
                    testAdminOperations(scanner);
                    break;
                case 5:
                    displayAllData();
                    break;
                case 6:
                    addSampleData();
                    break;
                case 0:
                    running = false;
                    System.out.println("وداعاً! / Goodbye!");
                    break;
                default:
                    System.out.println("خيار غير صحيح / Invalid choice");
            }
        }
        
        DatabaseConnection.closeConnection();
        scanner.close();
    }
    
    // اختبار عمليات المستخدمين
    private static void testUserOperations(Scanner scanner) {
        System.out.println("\n=== اختبار عمليات المستخدمين / Testing User Operations ===");
        
        System.out.print("اسم المستخدم / Username: ");
        String username = scanner.nextLine();
        System.out.print("الاسم الأول / First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("الاسم الأخير / Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("البريد الإلكتروني / Email: ");
        String email = scanner.nextLine();
        System.out.print("كلمة المرور / Password: ");
        String password = scanner.nextLine();
        
        // إضافة المستخدم
        boolean added = UserDAO.addUser(username, firstName, lastName, email, password);
        System.out.println("إضافة المستخدم / Add User: " + (added ? "نجح / Success" : "فشل / Failed"));
        
        // التحقق من تسجيل الدخول
        if (added) {
            User user = UserDAO.authenticateUser(username, password);
            if (user != null) {
                System.out.println("تسجيل الدخول نجح / Login Success: " + user);
            } else {
                System.out.println("فشل تسجيل الدخول / Login Failed");
            }
        }
    }
    
    // اختبار عمليات الحسابات
    private static void testAccountOperations(Scanner scanner) {
        System.out.println("\n=== اختبار عمليات الحسابات / Testing Account Operations ===");
        
        System.out.print("معرف المستخدم / User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("اسم الحساب / Account Name: ");
        String accountName = scanner.nextLine();
        System.out.print("نوع الحساب / Account Type: ");
        String accountType = scanner.nextLine();
        System.out.print("رقم الحساب / Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("العملة / Currency: ");
        String currency = scanner.nextLine();
        System.out.print("الرصيد الأولي / Initial Balance: ");
        double initialBalance = scanner.nextDouble();
        
        // إضافة الحساب
        boolean added = AccountDAO.addAccount(userId, accountName, accountType, accountNumber, currency, initialBalance);
        System.out.println("إضافة الحساب / Add Account: " + (added ? "نجح / Success" : "فشل / Failed"));
        
        // عرض الحساب
        if (added) {
            BankAccount account = AccountDAO.getAccountByNumber(accountNumber);
            if (account != null) {
                System.out.println("تفاصيل الحساب / Account Details: " + account);
            }
        }
    }
    
    // اختبار عمليات المعاملات
    private static void testTransactionOperations(Scanner scanner) {
        System.out.println("\n=== اختبار عمليات المعاملات / Testing Transaction Operations ===");
        
        System.out.print("معرف الحساب / Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("نوع المعاملة (DEPOSIT/WITHDRAW) / Transaction Type: ");
        String transactionType = scanner.nextLine();
        System.out.print("المبلغ / Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("الوصف / Description: ");
        String description = scanner.nextLine();
        
        // تنفيذ المعاملة
        boolean success = false;
        if (transactionType.equalsIgnoreCase("DEPOSIT")) {
            success = AccountDAO.deposit(accountId, amount);
        } else if (transactionType.equalsIgnoreCase("WITHDRAW")) {
            success = AccountDAO.withdraw(accountId, amount);
        }
        
        System.out.println("المعاملة / Transaction: " + (success ? "نجحت / Success" : "فشلت / Failed"));
        
        // عرض المعاملات
        if (success) {
            System.out.println("\n=== آخر 5 معاملات / Last 5 Transactions ===");
            TransactionDAO.getLastNTransactions(accountId, 5).forEach(System.out::println);
        }
    }
    
    // اختبار عمليات المديرين
    private static void testAdminOperations(Scanner scanner) {
        System.out.println("\n=== اختبار عمليات المديرين / Testing Admin Operations ===");
        
        System.out.print("اسم المستخدم / Username: ");
        String username = scanner.nextLine();
        System.out.print("البريد الإلكتروني / Email: ");
        String email = scanner.nextLine();
        System.out.print("كلمة المرور / Password: ");
        String password = scanner.nextLine();
        
        // إضافة المدير
        boolean added = AdminDAO.addAdmin(username, email, password);
        System.out.println("إضافة المدير / Add Admin: " + (added ? "نجح / Success" : "فشل / Failed"));
        
        // التحقق من تسجيل الدخول
        if (added) {
            Admin admin = AdminDAO.authenticateAdmin(username, password);
            if (admin != null) {
                System.out.println("تسجيل دخول المدير نجح / Admin Login Success: " + admin);
            } else {
                System.out.println("فشل تسجيل دخول المدير / Admin Login Failed");
            }
        }
    }
    
    // عرض جميع البيانات
    private static void displayAllData() {
        System.out.println("\n=== عرض جميع البيانات / Display All Data ===");
        
        System.out.println("\n--- المستخدمون / Users ---");
        UserDAO.getAllUsers().forEach(System.out::println);
        
        System.out.println("\n--- المديرون / Admins ---");
        AdminDAO.getAllAdmins().forEach(System.out::println);
        
        System.out.println("\n--- الحسابات / Accounts ---");
        AccountDAO.getAllAccounts().forEach(System.out::println);
        
        System.out.println("\n--- المعاملات / Transactions ---");
        TransactionDAO.getAllTransactions().forEach(System.out::println);
    }
    
    // إضافة بيانات تجريبية
    private static void addSampleData() {
        System.out.println("\n=== إضافة بيانات تجريبية / Adding Sample Data ===");
        
        // إضافة مستخدمين تجريبيين
        UserDAO.addUser("ahmed123", "أحمد", "محمد", "ahmed@example.com", "password123");
        UserDAO.addUser("fatima456", "فاطمة", "علي", "fatima@example.com", "password456");
        
        // إضافة حسابات تجريبية
        AccountDAO.addAccount(1, "حساب التوفير", "Savings", "ACC001", "EGP", 5000.0);
        AccountDAO.addAccount(1, "حساب جاري", "Current", "ACC002", "EGP", 2000.0);
        AccountDAO.addAccount(2, "حساب التوفير", "Savings", "ACC003", "EGP", 3000.0);
        
        // إضافة معاملات تجريبية
        AccountDAO.deposit(1, 1000.0);
        AccountDAO.withdraw(1, 500.0);
        AccountDAO.deposit(2, 800.0);
        AccountDAO.transfer(1, 2, 300.0);
        
        System.out.println("تم إضافة البيانات التجريبية بنجاح / Sample data added successfully!");
    }
}