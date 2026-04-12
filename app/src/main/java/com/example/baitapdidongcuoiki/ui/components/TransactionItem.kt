package com.example.baitapdidongcuoiki.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.baitapdidongcuoiki.domain.model.Transaction
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionItem(
    transaction: Transaction,
    formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // HIỂN THỊ NỘI DUNG (Ví dụ: Ăn sáng bánh mì)
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                // HIỂN THỊ THÔNG TIN NGÂN HÀNG (Ví dụ: Tài khoản: 123456)
                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // HIỂN THỊ SỐ TIỀN PHÂN BIỆT MÀU SẮC
            val isIncome = transaction.type == "income"
            Text(
                text = (if (isIncome) "+" else "-") + formatter.format(transaction.amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}