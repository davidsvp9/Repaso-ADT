package com.example.ad1_ex

import android.util.Log

actual object MyLog {
    private const val PREFIX = "[MyLog] "

    private fun caller(): String {
        val trace = Throwable().stackTrace
        val frame = trace.firstOrNull { element ->
            element.className.startsWith("com.example") &&
                    !element.className.contains("MyLog")
        }
        val file = frame?.fileName ?: "Unknown.kt"
        val line = frame?.lineNumber ?: -1
        return "($file:$line)"
    }

    actual fun d(message: String) {
        Log.d(caller(), "$PREFIX$message")
    }

    actual fun e(message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(caller(), PREFIX + message, throwable)
        } else {
            Log.e(caller(), PREFIX + message)
        }
    }
}