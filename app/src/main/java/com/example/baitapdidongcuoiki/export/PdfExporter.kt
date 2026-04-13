package com.example.baitapdidongcuoiki.export

import android.content.Context
import android.util.Log
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.itextpdf.kernel.colors.ColorConstants
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

    // Thêm tham số dependentCount để tính thuế
    fun export(context: Context, transactions: List<Transaction>, dependentCount: Int = 0): File {
        val income = transactions.filter { it.type.equals("income", true) }.sumOf { it.amount }
        val expense = transactions.filter { it.type.equals("expense", true) }.sumOf { it.amount }
        val balance = income - expense

        // --- Logic Tính Thuế TNCN ---
        val giamTruBanThan = 11000000.0
        val giamTruNguoiPhuThuoc = dependentCount * 4400000.0
        val thuNhapTinhThue = income - giamTruBanThan - giamTruNguoiPhuThuoc

        // Tính thuế lũy tiến đơn giản (Bậc 1: 5%)
        val thueDuKien = if (thuNhapTinhThue > 0) thuNhapTinhThue * 0.05 else 0.0

        val money = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        // Đổi tên file cho dễ tìm
        val fileName = "BaoCao_QuyetToanThue.pdf"
        val dir = context.getExternalFilesDir(null) ?: context.filesDir
        val file = File(dir, fileName)

        val writer = PdfWriter(FileOutputStream(file))
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        // Tiêu đề
        document.add(
            Paragraph("BÁO CÁO QUYẾT TOÁN THUẾ DỰ KIẾN")
                .setBold()
                .setFontSize(18f)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.MAGENTA)
        )
        document.add(Paragraph("Ngày xuất: ${sdf.format(Date())}").setTextAlignment(TextAlignment.RIGHT).setFontSize(10f))
        document.add(Paragraph(" "))

        // Phần 1: Tổng quan tài chính
        document.add(Paragraph("1. TỔNG QUAN TÀI CHÍNH").setBold())
        document.add(Paragraph("Tổng thu nhập: ${money.format(income)}").setFontColor(ColorConstants.GREEN))
        document.add(Paragraph("Tổng chi tiêu: ${money.format(expense)}").setFontColor(ColorConstants.RED))
        document.add(Paragraph("Số dư hiện tại: ${money.format(balance)}"))
        document.add(Paragraph(" "))

        // Phần 2: Chi tiết Thuế (Điểm nhấn đồ án)
        document.add(Paragraph("2. DỰ TOÁN THUẾ TNCN").setBold())
        document.add(Paragraph("- Giảm trừ gia cảnh (Bản thân): ${money.format(giamTruBanThan)}"))
        document.add(Paragraph("- Giảm trừ người phụ thuộc ($dependentCount người): ${money.format(giamTruNguoiPhuThuoc)}"))
        document.add(Paragraph("- Thu nhập tính thuế: ${money.format(if(thuNhapTinhThue > 0) thuNhapTinhThue else 0.0)}"))
        document.add(
            Paragraph("- THUẾ TNCN DỰ KIẾN (Tạm tính 5%): ${money.format(thueDuKien)}")
                .setBold()
                .setFontSize(14f)
                .setFontColor(ColorConstants.BLUE)
        )

        document.add(Paragraph("—".repeat(40)))
        document.add(Paragraph("3. CHI TIẾT GIAO DỊCH GẦN ĐÂY").setBold())

        if (transactions.isEmpty()) {
            document.add(Paragraph("Chưa có giao dịch."))
        } else {
            transactions.sortedByDescending { it.date }.take(20).forEachIndexed { index, t ->
                val type = if (t.type.equals("income", true)) "[Thu]" else "[Chi]"
                document.add(Paragraph("${index + 1}. $type ${t.title}: ${money.format(t.amount)} (${sdf.format(Date(t.date))})").setFontSize(10f))
            }
        }

        document.close()
        Log.d("PdfExporter", "PDF saved at: ${file.absolutePath}")
        return file
    }
}