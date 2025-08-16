#!/bin/bash
# ملف تشغيل المشروع الرئيسي / Main Project Runner Script

export CLASSPATH="/app/lib/sqlite-jdbc-3.43.0.0.jar:/app:$CLASSPATH"

echo "=== تشغيل المشروع الرئيسي / Running Main Project ==="
java -cp "$CLASSPATH" BankGUi.MainGUI
