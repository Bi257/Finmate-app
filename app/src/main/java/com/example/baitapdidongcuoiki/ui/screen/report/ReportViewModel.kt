package com.example.baitapdidongcuoiki.ui.screen.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitapdidongcuoiki.domain.model.Transaction
import com.example.baitapdidongcuoiki.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ReportState(
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val balance: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state.asStateFlow()

    init {
        observeTransactions()
    }

    private fun observeTransactions() {

        useCases.getTransactionsUseCase() //
            .onStart {
                _state.update { it.copy(isLoading = true) }
            }
            .map { list: List<Transaction> -> //

                val income = list
                    .filter { it.type.equals("income", true) }
                    .sumOf { it.amount }

                val expense = list
                    .filter { it.type.equals("expense", true) }
                    .sumOf { it.amount }

                ReportState(
                    income = income,
                    expense = expense,
                    balance = income - expense,
                    isLoading = false
                )
            }
            .catch {
                _state.update { it.copy(isLoading = false) }
            }
            .onEach { newState ->
                _state.value = newState
            }
            .launchIn(viewModelScope)
    }
}