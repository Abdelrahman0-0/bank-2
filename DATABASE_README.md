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

