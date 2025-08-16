import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("=== اختبار قاعدة بيانات SQLite / SQLite Database Test ===");
        
        try {
            // تحميل مكتبة SQLite
            Class.forName("org.sqlite.JDBC");
            
            // إنشاء الاتصال
            Connection connection = DriverManager.getConnection("jdbc:sqlite:bank_system.db");
            System.out.println("✅ تم الاتصال بقاعدة البيانات بنجاح / Database connected successfully");
            
            // إنشاء جدول تجريبي
            Statement stmt = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS test_table (id INTEGER PRIMARY KEY, name TEXT)";
            stmt.execute(createTable);
            System.out.println("✅ تم إنشاء الجدول بنجاح / Table created successfully");
            
            // إدراج بيانات تجريبية
            String insertData = "INSERT INTO test_table (name) VALUES ('Test User')";
            stmt.execute(insertData);
            System.out.println("✅ تم إدراج البيانات بنجاح / Data inserted successfully");
            
            // استعلام البيانات
            ResultSet rs = stmt.executeQuery("SELECT * FROM test_table");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }
            System.out.println("✅ تم استعلام البيانات بنجاح / Data queried successfully");
            
            // إغلاق الاتصال
            connection.close();
            System.out.println("✅ تم إغلاق الاتصال بنجاح / Connection closed successfully");
            
            System.out.println("\n🎉 جميع اختبارات قاعدة البيانات نجحت / All database tests passed!");
            
        } catch (Exception e) {
            System.err.println("❌ خطأ في قاعدة البيانات / Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}