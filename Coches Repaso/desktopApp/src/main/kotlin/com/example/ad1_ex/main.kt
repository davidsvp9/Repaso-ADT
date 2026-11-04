package com.example.ad1_ex

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ad1_ex",
    ) {
        App()
    }
}