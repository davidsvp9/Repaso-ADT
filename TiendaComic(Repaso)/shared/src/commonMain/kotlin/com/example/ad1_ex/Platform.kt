package com.example.ad1_ex

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform