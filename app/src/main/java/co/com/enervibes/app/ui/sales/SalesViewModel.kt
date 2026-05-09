package co.com.enervibes.app.ui.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.TransactionModel
import co.com.enervibes.app.data.repository.SalesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SalesUiState(
    val isLoading: Boolean = false,
    val sales: List<TransactionModel> = emptyList(),
    val totalCount: Int = 0,
    val currentPage: Int = 1,
    val selectedSale: TransactionModel? = null,
    val showDetail: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

class SalesViewModel(tokenManager: TokenManager) : ViewModel() {
    private val repository = SalesRepository(tokenManager)
    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState: StateFlow<SalesUiState> = _uiState.asStateFlow()

    init { loadSales() }

    fun loadSales(page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getSales(page).fold(
                onSuccess = { (sales, total) ->
                    _uiState.update {
                        it.copy(isLoading = false, sales = sales, totalCount = total, currentPage = page)
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun loadSaleDetail(id: Int) {
        viewModelScope.launch {
            repository.getSaleDetail(id).fold(
                onSuccess = { sale ->
                    _uiState.update { it.copy(selectedSale = sale, showDetail = true) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }

    fun closeDetail() {
        _uiState.update { it.copy(showDetail = false, selectedSale = null) }
    }

    fun markAsPaid(id: Int) {
        viewModelScope.launch {
            repository.markAsPaid(id).fold(
                onSuccess = {
                    _uiState.update { it.copy(success = "Venta marcada como pagada") }
                    loadSales(_uiState.value.currentPage)
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }

    fun deleteSale(id: Int) {
        viewModelScope.launch {
            repository.deleteSale(id).fold(
                onSuccess = {
                    _uiState.update { it.copy(success = "Venta eliminada", showDetail = false, selectedSale = null) }
                    loadSales(_uiState.value.currentPage)
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, success = null) }
    }
}
