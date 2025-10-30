package com.example.theworldofpuppies.support.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri

fun openPhoneDialer(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }
        val chooser = Intent.createChooser(intent, "Open With")
        context.startActivity(chooser)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open dialer", Toast.LENGTH_SHORT).show()
    }
}
