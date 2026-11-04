package com.example.ad1_ex

import java.io.PrintStream

actual object MyLog {
    private const val PREFIX = "[MyLog] "

    // Códigos ANSI para colores
    private const val ANSI_RESET = "\u001B[0m"
    private const val ANSI_CYAN = "\u001B[36m"
    private const val ANSI_RED = "\u001B[31m"
    private const val ANSI_GRAY = "\u001B[90m"     // Gris para metadatos (caller)
    private const val ANSI_WHITE = "\u001B[37m"    // Blanco brillante para el mensaje

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
        // Prefijo y caller en gris/cian tenue, mensaje en blanco/cyan claro
        stdout.println("$PREFIX$ANSI_GRAY${caller()}$ANSI_RESET: $ANSI_CYAN$message$ANSI_RESET")
    }

    actual fun e(message: String, throwable: Throwable?) {
        // En error, mantenemos el rojo para alertar, pero diferenciando el caller
        stderr.println("$PREFIX$ANSI_GRAY${caller()}$ANSI_RESET: $ANSI_RED$message$ANSI_RESET")
        throwable?.printStackTrace(stderr)
    }
}