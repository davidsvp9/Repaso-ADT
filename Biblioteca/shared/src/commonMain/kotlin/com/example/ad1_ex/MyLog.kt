package com.example.ad1_ex

// Si estan correctamente enlazados con las versiones especificas a la izquierda sale un simbolo que nos permite apuntar a esos códigos.
expect object MyLog {
    fun d(message: String)
    fun e(message: String, throwable: Throwable? = null)
}