package com.example.baitapdidongcuoiki.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.OutputStream

object FileExportHelper {

    fun exportToFile(
        context: Context,
        content: String,
        fileName: String = "report_${System.currentTimeMillis()}.txt",
        mimeType: String = "text/plain"
    ): String {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveWithMediaStore(context, content, fileName, mimeType)
        } else {
            saveLegacy(context, content, fileName)
        }
    }

    // ===== ANDROID 10+ =====
    private fun saveWithMediaStore(
        context: Context,
        content: String,
        fileName: String,
        mimeType: String
    ): String {

        val resolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
            ?: return "Error"

        val outputStream: OutputStream? = resolver.openOutputStream(uri)

        outputStream?.use {
            it.write(content.toByteArray())
        }

        return uri.toString()
    }

    // ===== ANDROID < 10 =====
    private fun saveLegacy(
        context: Context,
        content: String,
        fileName: String
    ): String {

        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val file = File(downloadsDir, fileName)
        file.writeText(content)

        return file.absolutePath
    }
}