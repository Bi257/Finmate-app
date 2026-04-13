package com.example.baitapdidongcuoiki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.baitapdidongcuoiki.domain.model.Transaction
import java.text.NumberFormat

@Composable
fun SummaryCard(
    transactions: List<Transaction>,
    formatter: NumberFormat
) {

    val totalIncome = transactions
        .filter { it.type == "income" }
        .sumOf { it.amount }

    val totalExpense = transactions
        .filter { it.type == "expense" }
        .sumOf { it.amount }

    val balance = totalIncome - totalExpense

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF5B5FEF), Color(0xFF8F94FB))
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(20.dp)
        ) {

            Column {

                //  TITLE
                Text(
                    "Ví của tôi",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                //  BALANCE
                Text(
                    formatter.format(balance),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                //  INCOME / EXPENSE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    InfoItem(
                        title = "Thu",
                        amount = "+${formatter.format(totalIncome)}",
                        color = Color(0xFF4CAF50)
                    )

                    InfoItem(
                        title = "Chi",
                        amount = "-${formatter.format(totalExpense)}",
                        color = Color(0xFFFF5252)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    amount: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .background(
                Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            title,
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            amount,
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}













































































