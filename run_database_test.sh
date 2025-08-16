#!/bin/bash
# ملف تشغيل اختبار قاعدة البيانات / Database Test Runner Script

export CLASSPATH="/app/lib/sqlite-jdbc-3.43.0.0.jar:/app:$CLASSPATH"

echo "=== تشغيل اختبار قاعدة البيانات / Running Database Test ==="
java -cp "$CLASSPATH" bank.database.DatabaseInitializer
