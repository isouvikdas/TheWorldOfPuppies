package com.example.theworldofpuppies.core.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.primitives.Bytes


fun getBytes(uri: Uri, context: Context) : ByteArray? {
    return context.contentResolver.openInputStream(uri)?.readBytes()
}
fun getFileName(uri: Uri, context: Context): String? {
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    }
}

fun getMimeType(uri: Uri, context: Context): String? {
    return context.contentResolver.getType(uri)
}