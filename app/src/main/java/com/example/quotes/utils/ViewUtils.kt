package com.example.quotes.utils

import android.content.Context
import android.widget.Toast


// Defining Kotlin Extension function for Toasts HeadAch
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}