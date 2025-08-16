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
