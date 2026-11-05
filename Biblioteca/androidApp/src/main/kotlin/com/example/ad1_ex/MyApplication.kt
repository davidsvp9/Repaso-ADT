package com.example.ad1_ex

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicialización global única
        Log.d("cargaAndroid", "MyApplication: ¡Iniciando Firebase en MyApplication!")
        FirebaseApp.initializeApp(this)
    }
}