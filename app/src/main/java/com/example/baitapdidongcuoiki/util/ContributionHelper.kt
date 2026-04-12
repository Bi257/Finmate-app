package com.example.baitapdidongcuoiki.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object ContributionHelper {

    private const val TAG = "ContributionHelper"

    // ================= LOG =================
    fun logDebug(message: String) {
        Log.d(TAG, formatLog(message))
    }

    fun logError(message: String) {
        Log.e(TAG, formatLog(message))
    }

    private fun formatLog(message: String): String {
        return "[${getCurrentTime()}] $message"
    }

    // ================= TIME =================
    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
    }

    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    // ================= NUMBER =================
    fun sum(a: Int, b: Int): Int = a + b

    fun multiply(a: Int, b: Int): Int = a * b

    fun divide(a: Int, b: Int): Double {
        if (b == 0) return 0.0
        return a.toDouble() / b
    }

    fun average(numbers: List<Int>): Double {
        if (numbers.isEmpty()) return 0.0
        return numbers.sum().toDouble() / numbers.size
    }

    fun roundDouble(value: Double): Int {
        return value.roundToInt()
    }

    fun isEven(number: Int): Boolean = number % 2 == 0

    fun isOdd(number: Int): Boolean = number % 2 != 0

    // ================= STRING =================
    fun isNullOrEmpty(input: String?): Boolean {
        return input == null || input.trim().isEmpty()
    }

    fun capitalize(input: String): String {
        return input.replaceFirstChar { it.uppercase() }
    }

    fun reverseString(input: String): String {
        return input.reversed()
    }

    // ================= CURRENCY =================
    fun formatCurrency(amount: Double): String {
        return String.format("%,.0f VND", amount)
    }

    // ================= RANDOM =================
    fun randomInt(min: Int, max: Int): Int {
        return (min..max).random()
    }

    fun randomBoolean(): Boolean {
        return listOf(true, false).random()
    }

    // ================= LIST =================
    fun <T> getFirstItem(list: List<T>): T? {
        return if (list.isNotEmpty()) list[0] else null
    }

    fun <T> getLastItem(list: List<T>): T? {
        return if (list.isNotEmpty()) list[list.size - 1] else null
    }

    fun <T> isListEmpty(list: List<T>?): Boolean {
        return list == null || list.isEmpty()
    }

    // ================= DELAY =================
    fun simulateDelay(ms: Long) {
        try {
            Thread.sleep(ms)
        } catch (e: InterruptedException) {
            logError("Delay interrupted: ${e.message}")
        }
    }

    // ================= VALIDATION =================
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPhoneValid(phone: String): Boolean {
        return phone.length in 9..11 && phone.all { it.isDigit() }
    }

    // ================= DEBUG =================
    fun printAllInfo() {
        logDebug("App debug info:")
        logDebug("Time: ${getCurrentTime()}")
        logDebug("Random: ${randomInt(1, 100)}")
    }
}