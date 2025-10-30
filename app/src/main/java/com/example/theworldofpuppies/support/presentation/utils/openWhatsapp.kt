package com.example.theworldofpuppies.support.presentation.utils

import android.content.Context
import android.content.Intent
import java.net.URLEncoder
import androidx.core.net.toUri

fun isAppInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: Exception) {
        false
    }
}

fun openWhatsappChatPreferBusiness(context: Context, phoneNumber: String, message: String? = null) {
    val encodedMessage = message?.let { URLEncoder.encode(it, "UTF-8") }
    val baseLink = "https://api.whatsapp.com/send?phone=$phoneNumber" + (encodedMessage?.let { "&text=$it" } ?: "")

    val whatsappPackage = when {
        isAppInstalled(context, "com.whatsapp.w4b") -> "com.whatsapp.w4b" // WhatsApp Business
        isAppInstalled(context, "com.whatsapp") -> "com.whatsapp" // regular
        else -> null
    }

    if (whatsappPackage != null) {
        val intent = Intent(Intent.ACTION_VIEW, baseLink.toUri()).apply { setPackage(whatsappPackage) }
        context.startActivity(intent)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, baseLink.toUri()))
    }
}
