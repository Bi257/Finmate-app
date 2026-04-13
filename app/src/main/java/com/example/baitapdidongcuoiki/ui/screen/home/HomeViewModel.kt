package com.example.baitapdidongcuoiki.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitapdidongcuoiki.data.repository.RemoteRepository
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: UseCases,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        // Lắng nghe dữ liệu ngay khi khởi tạo
        observeTransactions()
        syncWithBackend()
    }

    private fun observeTransactions() {
        _state.update { it.copy(isLoading = true) }

        // UseCase này gọi đến Repository (nơi mình đã sửa để lấy từ Firestore theo UserId)
        useCases.getTransactionsUseCase()
            .onEach { list ->
                // Sắp xếp dữ liệu mới nhất lên đầu
                val sortedList = list.sortedByDescending { it.date }
                val income = calculateIncome(sortedList)
                val expense = calculateExpense(sortedList)

                _state.update {
                    it.copy(
                        transactions = sortedList,
                        totalIncome = income,
                        totalExpense = expense,
                        balance = income - expense,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .catch { e ->
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    // Logic tính toán giữ nguyên nhưng dùng ignoreCase cho an toàn
    private fun calculateIncome(list: List<Transaction>): Double =
        list.filter { it.type.equals("income", ignoreCase = true) || it.type.contains("thu", ignoreCase = true) }
            .sumOf { it.amount }

    private fun calculateExpense(list: List<Transaction>): Double =
        list.filter { it.type.equals("expense", ignoreCase = true) || it.type.contains("chi", ignoreCase = true) }
            .sumOf { it.amount }

    private fun syncWithBackend() {
        viewModelScope.launch {
            try {
                remoteRepository.syncWithBackend()
                Log.d("HomeViewModel", "Đồng bộ API tỷ giá thành công")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Lỗi Sync API: ${e.message}")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            useCases.refreshTransactionsUseCase.invoke()
            // Sau đó observeTransactions sẽ tự động cập nhật vì Flow đang lắng nghe
        }
    }

    /*fun refresh() {
        syncWithBackend()
        // Gọi lại để đảm bảo dữ liệu mới nhất
        observeTransactions()
    }*/

    fun addTransaction(title: String, amount: Double, type: String, note: String = "") {
        viewModelScope.launch {
            val newTransaction = Transaction(
                title = title,
                amount = amount,
                type = type.trim(),
                date = System.currentTimeMillis(),
                note = note
            )

            try {
                // Chỉ cần gọi UseCase, vì observeTransactions() đang "lắng nghe"
                // nên khi Firebase có đồ mới, UI sẽ tự nhảy số theo.
                useCases.addTransactionUseCase(newTransaction)
            } catch (e: Exception) {
                _state.update { it.copy(error = "Không thể thêm giao dịch: ${e.message}") }
            }
        }
    }
}