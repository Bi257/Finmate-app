package com.example.baitapdidongcuoiki.export

import android.content.Context
import android.util.Log
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
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

    // Định nghĩa các loại báo cáo để tránh nhầm lẫn
    enum class ReportType { TAX, ANALYSIS }

    fun export(
        context: Context,
        transactions: List<Transaction>,
        dependentCount: Int = 0,
        type: ReportType = ReportType.TAX // Mặc định là Thuế
    ): File {

        // 1. Chuẩn bị dữ liệu và File
        val money = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        // Tên file thay đổi dựa theo loại báo cáo
        val fileName = if (type == ReportType.TAX) "BaoCao_Thue.pdf" else "BaoCao_BienDong.pdf"
        val dir = context.getExternalFilesDir(null) ?: context.filesDir
        val file = File(dir, fileName)

        val writer = PdfWriter(FileOutputStream(file))
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        // 2. Load Font tiếng Việt (Arial)
        val font = loadVietnameseFont(context)

        // 3. Header chung cho các báo cáo
        val titleText = if (type == ReportType.TAX) "BÁO CÁO QUYẾT TOÁN THUẾ" else "BÁO CÁO PHÂN TÍCH BIẾN ĐỘNG"
        document.add(
            Paragraph(titleText)
                .setFont(font)
                .setBold()
                .setFontSize(18f)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.MAGENTA)
        )
        document.add(Paragraph("Ngày xuất: ${sdf.format(Date())}").setFont(font).setTextAlignment(TextAlignment.RIGHT).setFontSize(10f))
        document.add(Paragraph("\n"))

        // 4. Vẽ nội dung dựa theo Loại báo cáo
        if (type == ReportType.TAX) {
            drawTaxContent(document, font, transactions, dependentCount, money)
        } else {
            drawAnalysisContent(document, font, transactions, money)
        }

        // 5. Footer: Chi tiết giao dịch (Dùng chung)
        document.add(Paragraph("\n"))
        document.add(Paragraph("DANH SÁCH GIAO DỊCH GẦN ĐÂY").setFont(font).setBold())
        if (transactions.isEmpty()) {
            document.add(Paragraph("Chưa có giao dịch.").setFont(font))
        } else {
            transactions.sortedByDescending { it.date }.take(15).forEachIndexed { index, t ->
                val prefix = if (t.type.equals("income", true)) "[+]" else "[-]"
                val color = if (t.type.equals("income", true)) ColorConstants.GREEN else ColorConstants.RED
                document.add(
                    Paragraph("${index + 1}. $prefix ${t.title}: ${money.format(t.amount)}")
                        .setFont(font).setFontSize(10f).setFontColor(color)
                )
            }
        }

        document.close()
        Log.d("PdfExporter", "Đã xuất file: ${file.absolutePath}")
        return file
    }

    // --- HÀM HỖ TRỢ VẼ NỘI DUNG THUẾ ---
    private fun drawTaxContent(doc: Document, font: PdfFont?, list: List<Transaction>, dependents: Int, nf: NumberFormat) {
        val income = list.filter { it.type.equals("income", true) }.sumOf { it.amount }
        val giamTruBanThan = 11000000.0
        val giamTruPhuThuoc = dependents * 4400000.0
        val thuNhapTinhThue = maxOf(0.0, income - giamTruBanThan - giamTruPhuThuoc)
        val thueDuKien = thuNhapTinhThue * 0.05 // Tạm tính 5%

        doc.add(Paragraph("1. TỔNG QUAN THU NHẬP").setFont(font).setBold())
        doc.add(Paragraph("- Tổng thu nhập: ${nf.format(income)}").setFont(font))
        doc.add(Paragraph("- Giảm trừ bản thân: ${nf.format(giamTruBanThan)}").setFont(font))
        doc.add(Paragraph("- Giảm trừ người phụ thuộc ($dependents người): ${nf.format(giamTruPhuThuoc)}").setFont(font))
        doc.add(Paragraph("- Thu nhập tính thuế: ${nf.format(thuNhapTinhThue)}").setFont(font).setBold())
        doc.add(Paragraph("=> THUẾ TNCN DỰ KIẾN: ${nf.format(thueDuKien)}").setFont(font).setBold().setFontSize(14f).setFontColor(ColorConstants.BLUE))
    }

    // --- HÀM HỖ TRỢ VẼ NỘI DUNG BIẾN ĐỘNG ---
    private fun drawAnalysisContent(doc: Document, font: PdfFont?, list: List<Transaction>, nf: NumberFormat) {
        val income = list.filter { it.type.equals("income", true) }.sumOf { it.amount }
        val expense = list.filter { it.type.equals("expense", true) }.sumOf { it.amount }

        doc.add(Paragraph("1. PHÂN TÍCH THU CHI").setFont(font).setBold())
        doc.add(Paragraph("- Tổng thu: ${nf.format(income)}").setFont(font).setFontColor(ColorConstants.GREEN))
        doc.add(Paragraph("- Tổng chi: ${nf.format(expense)}").setFont(font).setFontColor(ColorConstants.RED))
        doc.add(Paragraph("- Thặng dư: ${nf.format(income - expense)}").setFont(font))

        val percent = if (income > 0) (expense / income * 100) else 0.0
        doc.add(Paragraph("- Tỷ lệ chi tiêu/thu nhập: ${String.format("%.1f", percent)}%").setFont(font))
    }

    // --- HÀM LOAD FONT ---
    private fun loadVietnameseFont(context: Context): PdfFont? {
        return try {
            val fontStream = context.assets.open("fonts/arial.ttf")
            val fontBytes = fontStream.readBytes()
            fontStream.close()
            val fontProgram = FontProgramFactory.createFont(fontBytes)
            PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED)
        } catch (e: Exception) {
            Log.e("PdfExporter", "Font error: ${e.message}")
            null
        }
    }
}