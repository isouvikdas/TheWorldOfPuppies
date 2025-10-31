package com.example.theworldofpuppies.support.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun openEmailApp(
    context: Context,
    emailAddress: String,
    subject: String? = null,
    body: String? = null
) {
    try {
        val uri = Uri.Builder().apply {
            scheme("mailto")
            opaquePart(emailAddress)
            subject?.let { appendQueryParameter("subject", it) }
            body?.let { appendQueryParameter("body", it) }
        }.build()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = uri
        }
        val chooser = Intent.createChooser(intent, "Open With")
        context.startActivity(chooser)
    } catch (e: Exception) {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}
