#!/bin/bash

# سكريبت إعداد قاعدة البيانات وتثبيت المكتبات المطلوبة
# Database Setup Script and Required Libraries Installation

echo "=== بدء إعداد قاعدة البيانات SQLite / Starting SQLite Database Setup ==="

# إنشاء مجلد المكتبات إذا لم يكن موجوداً
mkdir -p /app/lib

# تحميل مكتبة SQLite JDBC Driver
echo "تحميل مكتبة SQLite JDBC / Downloading SQLite JDBC Driver..."
wget -O /app/lib/sqlite-jdbc-3.43.0.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar

# التحقق من تحميل المكتبة
if [ -f "/app/lib/sqlite-jdbc-3.43.0.0.jar" ]; then
    echo "تم تحميل مكتبة SQLite بنجاح / SQLite JDBC Driver downloaded successfully"
else
    echo "فشل في تحميل المكتبة / Failed to download SQLite JDBC Driver"
fi

# إنشاء ملف ClassPath للكومبايل
echo "إعداد ClassPath / Setting up ClassPath..."
export CLASSPATH="/app/lib/sqlite-jdbc-3.43.0.0.jar:$CLASSPATH"

# إنشاء ملف الكومبايل
cat > /app/compile.sh << 'EOF'
#!/bin/bash
# ملف الكومبايل للمشروع / Project Compilation Script

echo "=== كومبايل المشروع / Compiling Project ==="

# إعداد ClassPath
export CLASSPATH="/app/lib/sqlite-jdbc-3.43.0.0.jar:/app:$CLASSPATH"

# كومبايل جميع ملفات Java
echo "كومبايل ملفات قاعدة البيانات / Compiling Database Files..."
javac -cp "$CLASSPATH" /app/DatabaseConnection.java
javac -cp "$CLASSPATH" /app/UserDAO.java
javac -cp "$CLASSPATH" /app/AccountDAO.java
javac -cp "$CLASSPATH" /app/TransactionDAO.java
javac -cp "$CLASSPATH" /app/AdminDAO.java
javac -cp "$CLASSPATH" /app/DatabaseInitializer.java

echo "كومبايل ملفات المشروع الأساسية / Compiling Core Project Files..."
javac -cp "$CLASSPATH" /app/user.java
javac -cp "$CLASSPATH" /app/client.java
javac -cp "$CLASSPATH" /app/account.java
javac -cp "$CLASSPATH" /app/transaction.java
javac -cp "$CLASSPATH" /app/admin.java

echo "كومبايل واجهات المستخدم / Compiling GUI Files..."
javac -cp "$CLASSPATH" /app/fpage.java
javac -cp "$CLASSPATH" /app/Login.java
javac -cp "$CLASSPATH" /app/signup.java
javac -cp "$CLASSPATH" /app/home.java
javac -cp "$CLASSPATH" /app/MainGUI.java
javac -cp "$CLASSPATH" /app/Banktest.java

echo "تم الانتهاء من الكومبايل / Compilation completed"
EOF

chmod +x /app/compile.sh

# إنشاء ملف تشغيل قاعدة البيانات
cat > /app/run_database_test.sh << 'EOF'
#!/bin/bash
# ملف تشغيل اختبار قاعدة البيانات / Database Test Runner Script

export CLASSPATH="/app/lib/sqlite-jdbc-3.43.0.0.jar:/app:$CLASSPATH"

echo "=== تشغيل اختبار قاعدة البيانات / Running Database Test ==="
java -cp "$CLASSPATH" bank.database.DatabaseInitializer
EOF

chmod +x /app/run_database_test.sh

# إنشاء ملف تشغيل المشروع الرئيسي
cat > /app/run_main.sh << 'EOF'
#!/bin/bash
# ملف تشغيل المشروع الرئيسي / Main Project Runner Script

export CLASSPATH="/app/lib/sqlite-jdbc-3.43.0.0.jar:/app:$CLASSPATH"

echo "=== تشغيل المشروع الرئيسي / Running Main Project ==="
java -cp "$CLASSPATH" BankGUi.MainGUI
EOF

chmod +x /app/run_main.sh

# إنشاء ملف README للتعليمات
cat > /app/DATABASE_README.md << 'EOF'
# دليل استخدام قاعدة البيانات SQLite / SQLite Database Usage Guide

## الملفات المضافة / Added Files:
1. **DatabaseConnection.java** - فئة الاتصال بقاعدة البيانات / Database connection class
2. **UserDAO.java** - عمليات قاعدة البيانات للمستخدمين / User database operations
3. **AccountDAO.java** - عمليات قاعدة البيانات للحسابات / Account database operations
4. **TransactionDAO.java** - عمليات قاعدة البيانات للمعاملات / Transaction database operations
5. **AdminDAO.java** - عمليات قاعدة البيانات للمديرين / Admin database operations
6. **DatabaseInitializer.java** - برنامج اختبار وتهيئة قاعدة البيانات / Database tester and initializer

## الملفات المحدثة / Updated Files:
1. **account.java** - فئة الحساب مع قاعدة البيانات / Account class with database
2. **transaction.java** - فئة المعاملات مع قاعدة البيانات / Transaction class with database
3. **admin.java** - فئة المدير مع قاعدة البيانات / Admin class with database

## طريقة الاستخدام / Usage Instructions:

### 1. تثبيت المكتبات / Install Libraries:
```bash
./setup_database.sh
```

### 2. كومبايل المشروع / Compile Project:
```bash
./compile.sh
```

### 3. تشغيل اختبار قاعدة البيانات / Run Database Test:
```bash
./run_database_test.sh
```

### 4. تشغيل المشروع الرئيسي / Run Main Project:
```bash
./run_main.sh
```

## هيكل قاعدة البيانات / Database Schema:

### جدول المستخدمين / Users Table:
- id (INTEGER PRIMARY KEY)
- username (TEXT UNIQUE)
- first_name (TEXT)
- last_name (TEXT)
- email (TEXT UNIQUE)
- password (TEXT)
- created_at (DATETIME)

### جدول الحسابات / Accounts Table:
- id (INTEGER PRIMARY KEY)
- user_id (INTEGER FOREIGN KEY)
- account_name (TEXT)
- account_type (TEXT)
- account_number (TEXT UNIQUE)
- currency (TEXT)
- balance (REAL)
- created_at (DATETIME)

### جدول المعاملات / Transactions Table:
- id (INTEGER PRIMARY KEY)
- account_id (INTEGER FOREIGN KEY)
- transaction_type (TEXT)
- amount (REAL)
- description (TEXT)
- balance_after (REAL)
- transaction_date (DATETIME)

### جدول المديرين / Admins Table:
- id (INTEGER PRIMARY KEY)
- username (TEXT UNIQUE)
- email (TEXT UNIQUE)
- password (TEXT)
- created_at (DATETIME)

## المميزات المتاحة / Available Features:

1. **إدارة المستخدمين / User Management:**
   - تسجيل مستخدمين جدد / Register new users
   - تسجيل الدخول / Login authentication
   - تحديث البيانات / Update user data
   - البحث عن المستخدمين / Search users

2. **إدارة الحسابات / Account Management:**
   - إنشاء حسابات جديدة / Create new accounts
   - عرض تفاصيل الحسابات / Display account details
   - إيداع وسحب الأموال / Deposit and withdraw money
   - التحويل بين الحسابات / Transfer between accounts

3. **إدارة المعاملات / Transaction Management:**
   - تسجيل جميع المعاملات / Record all transactions
   - عرض كشف الحساب / Display account statement
   - البحث في المعاملات / Search transactions
   - تحليل المعاملات / Transaction analysis

4. **إدارة النظام / System Administration:**
   - لوحة تحكم المديرين / Admin dashboard
   - مراقبة جميع العمليات / Monitor all operations
   - إحصائيات النظام / System statistics
   - إدارة المستخدمين والحسابات / Manage users and accounts

## ملاحظات مهمة / Important Notes:

1. قاعدة البيانات يتم إنشاؤها تلقائياً عند أول تشغيل / Database is created automatically on first run
2. مدير افتراضي يتم إنشاؤه: اسم المستخدم "admin" وكلمة المرور "admin123" / Default admin created: username "admin", password "admin123"
3. جميع المعاملات محمية بـ ACID transactions / All operations are ACID compliant
4. يدعم النظام اللغتين العربية والإنجليزية / System supports both Arabic and English

EOF

echo "=== انتهى إعداد قاعدة البيانات / Database Setup Completed ==="
echo "اقرأ ملف DATABASE_README.md للتعليمات التفصيلية / Read DATABASE_README.md for detailed instructions"