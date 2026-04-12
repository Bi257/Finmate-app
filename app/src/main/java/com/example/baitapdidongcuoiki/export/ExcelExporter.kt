package com.example.baitapdidongcuoiki.export

import android.content.Context
import com.example.baitapdidongcuoiki.domain.model.Transaction
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExporter {

    fun export(context: Context, transactions: List<Transaction>): File {
        val income = transactions.filter { it.type.equals("income", true) }.sumOf { it.amount }
        val expense = transactions.filter { it.type.equals("expense", true) }.sumOf { it.amount }
        val balance = income - expense
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        XSSFWorkbook().use { workbook ->
            val sheet = workbook.createSheet("Bao cao")

            var rowIdx = 0
            sheet.createRow(rowIdx++).createCell(0).setCellValue("BÁO CÁO GIAO DỊCH")
            rowIdx++

            sheet.createRow(rowIdx++).apply {
                createCell(0).setCellValue("Tổng thu (VND)")
                createCell(1).setCellValue(income)
            }
            sheet.createRow(rowIdx++).apply {
                createCell(0).setCellValue("Tổng chi (VND)")
                createCell(1).setCellValue(expense)
            }
            sheet.createRow(rowIdx++).apply {
                createCell(0).setCellValue("Số dư (VND)")
                createCell(1).setCellValue(balance)
            }
            rowIdx++

            val header = sheet.createRow(rowIdx++)
            listOf("STT", "Tiêu đề", "Loại", "Số tiền (VND)", "Ngày", "Ghi chú").forEachIndexed { i, text ->
                header.createCell(i).setCellValue(text)
            }

            transactions
                .sortedByDescending { it.date }
                .forEachIndexed { index, t ->
                    val row = sheet.createRow(rowIdx++)
                    row.createCell(0).setCellValue((index + 1).toDouble())
                    row.createCell(1).setCellValue(t.title)
                    row.createCell(2).setCellValue(
                        if (t.type.equals("income", true)) "Thu nhập" else "Chi tiêu"
                    )
                    row.createCell(3).setCellValue(t.amount)
                    row.createCell(4).setCellValue(sdf.format(Date(t.date)))
                    row.createCell(5).setCellValue(t.note)
                }

            val widths = intArrayOf(6, 28, 14, 16, 20, 32)
            widths.forEachIndexed { i, chars -> sheet.setColumnWidth(i, chars * 256) }

            val fileName = "bao_cao_giao_dich_${System.currentTimeMillis()}.xlsx"
            val dir = context.getExternalFilesDir(null) ?: context.filesDir
            val file = File(dir, fileName)
            FileOutputStream(file).use { workbook.write(it) }
            return file
        }
    }
}
