package com.example.ad1_ex

import android.util.Log

actual object MyLog {
    private const val TAG = "MyLog"

    private fun caller(): String {
        val trace = Throwable().stackTrace
        val frame = trace.firstOrNull { element ->
            !element.className.contains("MyLog") &&
                    !element.className.startsWith("kotlin.") &&
                    !element.className.startsWith("java.") &&
                    !element.className.startsWith("android.")
        }
        val file = frame?.fileName ?: "Unknown.kt"
        val line = frame?.lineNumber ?: -1
        return "($file:$line)"
    }

    actual fun d(message: String) {
        // El caller va al principio del mensaje
        Log.d(TAG, "${caller()} $message")
    }

    actual fun e(message: String, throwable: Throwable?) {
        val fullMessage = "${caller()} $message"
        if (throwable != null) {
            Log.e(TAG, fullMessage, throwable)
        } else {
            Log.e(TAG, fullMessage)
        }
    }
}