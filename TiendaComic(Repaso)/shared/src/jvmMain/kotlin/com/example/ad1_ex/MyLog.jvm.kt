package com.example.ad1_ex

import java.io.PrintStream

actual object MyLog {
    private const val PREFIX = "[MyLog] "

    // Códigos ANSI para colores
    private const val ANSI_RESET = "\u001B[0m"
    private const val ANSI_CYAN = "\u001B[36m"
    private const val ANSI_RED = "\u001B[31m"

    // Compatible con cualquier versión de Java/JVM
    private val stdout = PrintStream(System.out, true, "UTF-8")
    private val stderr = PrintStream(System.err, true, "UTF-8")

    private fun caller(): String {
        val trace = Throwable().stackTrace
        val frame = trace.firstOrNull { element ->
            !element.className.contains("MyLog") &&
                    !element.className.startsWith("kotlin.") &&
                    !element.className.startsWith("java.")
        }
        val file = frame?.fileName ?: "Unknown.kt"
        val line = frame?.lineNumber ?: -1
        return "($file:$line)"
    }

    actual fun d(message: String) {
        stdout.println("${ANSI_CYAN}DEBUG / ${caller()}: $PREFIX$message$ANSI_RESET")
    }

    actual fun e(message: String, throwable: Throwable?) {
        stderr.println("${ANSI_RED}ERROR / ${caller()}: $PREFIX$message$ANSI_RESET")
        throwable?.printStackTrace(stderr)
    }
}