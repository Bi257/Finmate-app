package com.example.baitapdidongcuoiki.export

import android.content.Context
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    fun export(context: Context, transactions: List<Transaction>): File {
        val income = transactions.filter { it.type.equals("income", true) }.sumOf { it.amount }
        val expense = transactions.filter { it.type.equals("expense", true) }.sumOf { it.amount }
        val balance = income - expense

        val money = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        val fileName = "bao_cao_giao_dich_${System.currentTimeMillis()}.pdf"
        val dir = context.getExternalFilesDir(null) ?: context.filesDir
        val file = File(dir, fileName)

        val writer = PdfWriter(FileOutputStream(file))
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        document.add(
            Paragraph("BÁO CÁO GIAO DỊCH")
                .setBold()
                .setFontSize(18f)
                .setTextAlignment(TextAlignment.CENTER)
        )
        document.add(Paragraph(" "))

        document.add(
            Paragraph("Tổng thu: ${money.format(income)}").setFontColor(
                com.itextpdf.kernel.colors.ColorConstants.GREEN
            )
        )
        document.add(
            Paragraph("Tổng chi: ${money.format(expense)}").setFontColor(
                com.itextpdf.kernel.colors.ColorConstants.RED
            )
        )
        document.add(
            Paragraph("Số dư: ${money.format(balance)}").setBold()
        )
        document.add(Paragraph("—".repeat(40)))
        document.add(Paragraph("Chi tiết giao dịch").setBold().setFontSize(14f))
        document.add(Paragraph(" "))

        if (transactions.isEmpty()) {
            document.add(Paragraph("Chưa có giao dịch."))
        } else {
            transactions
                .sortedByDescending { it.date }
                .forEachIndexed { index, transaction ->
                    val typeText =
                        if (transaction.type.equals("income", true)) "[Thu nhập]" else "[Chi tiêu]"
                    val line =
                        "${index + 1}. $typeText ${transaction.title}\n   " +
                            "${money.format(transaction.amount)} — ${sdf.format(Date(transaction.date))}" +
                            if (transaction.note.isNotBlank()) "\n   Ghi chú: ${transaction.note}" else ""
                    document.add(Paragraph(line))
                    document.add(Paragraph(" "))
                }
        }

        document.close()
        return file
    }
}
