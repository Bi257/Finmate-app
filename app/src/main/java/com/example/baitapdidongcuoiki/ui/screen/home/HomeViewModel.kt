package com.example.baitapdidongcuoiki.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitapdidongcuoiki.data.repository.RemoteRepository
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
        observeTransactions()
        syncWithBackend()
    }

    private fun observeTransactions() {
        useCases.getTransactionsUseCase()
            .onEach { list ->
                val income = calculateIncome(list)
                val expense = calculateExpense(list)
                _state.update {
                    it.copy(
                        transactions = list,
                        totalIncome = income,
                        totalExpense = expense,
                        balance = income - expense,
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun calculateIncome(list: List<Transaction>): Double =
        list.filter { it.type.equals("income", ignoreCase = true) }.sumOf { it.amount }

    private fun calculateExpense(list: List<Transaction>): Double =
        list.filter { it.type.equals("expense", ignoreCase = true) }.sumOf { it.amount }

    private fun syncWithBackend() {
        viewModelScope.launch {
            try {
                remoteRepository.syncWithBackend()
                Log.d("HomeViewModel", "Đồng bộ tỷ giá / vàng (API) xong")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Sync API: ${e.message}")
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun refresh() {
        syncWithBackend()
    }

    fun addTransaction(title: String, amount: Double, type: String) {
        viewModelScope.launch {
            val newTransaction = Transaction(
                id = null,
                title = title,
                amount = amount,
                type = type.trim().lowercase(),
                date = System.currentTimeMillis()
            )

            val currentList = _state.value.transactions
            val updatedList = (currentList + newTransaction).sortedByDescending { it.date }

            val newIncome = calculateIncome(updatedList)
            val newExpense = calculateExpense(updatedList)

            _state.update {
                it.copy(
                    transactions = updatedList,
                    totalIncome = newIncome,
                    totalExpense = newExpense,
                    balance = newIncome - newExpense
                )
            }

            try {
                useCases.addTransactionUseCase(newTransaction)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}
