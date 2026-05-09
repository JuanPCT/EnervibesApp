package co.com.enervibes.app.ui.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.*
import co.com.enervibes.app.data.repository.PosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartItem(
    val product: ProductModel,
    val quantity: Double = 1.0,
    val batchId: Int? = null,
    val flavorId: Int? = null,
    val unitPrice: Double
) {
    val subtotal: Double get() = quantity * unitPrice
}

data class PosUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<ProductModel> = emptyList(),
    val topProducts: List<ProductModel> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val searchMode: Boolean = false,
    val error: String? = null,
    val saleSuccess: String? = null,
    val paymentMethod: String = "cash",
    val showPaymentDialog: Boolean = false,
    val amountReceived: String = "",
    val discount: String = "",
    val isProcessing: Boolean = false,
    val terceroSearchQuery: String = "",
    val terceroSearchResults: List<TerceroModel> = emptyList(),
    val selectedTercero: TerceroModel? = null,
    val showTerceroSearch: Boolean = false
)

class PosViewModel(tokenManager: TokenManager) : ViewModel() {
    private val repository = PosRepository(tokenManager)
    private val _uiState = MutableStateFlow(PosUiState())
    val uiState: StateFlow<PosUiState> = _uiState.asStateFlow()

    init { loadTopProducts() }

    fun loadTopProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getTopProducts().fold(
                onSuccess = { products ->
                    _uiState.update { it.copy(isLoading = false, topProducts = products) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, searchMode = query.isNotEmpty()) }
        if (query.isNotEmpty()) searchProducts(query)
        else _uiState.update { it.copy(searchResults = emptyList()) }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch {
            repository.searchProducts(query).fold(
                onSuccess = { products ->
                    _uiState.update { it.copy(searchResults = products) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
            )
        }
    }

    fun addToCart(product: ProductModel) {
        _uiState.update { state ->
            val price = product.price
            val existingIndex = state.cartItems.indexOfFirst {
                it.product.id == product.id && it.flavorId == null
            }
            if (existingIndex >= 0) {
                val updated = state.cartItems.toMutableList()
                val item = updated[existingIndex]
                updated[existingIndex] = item.copy(quantity = item.quantity + 1)
                state.copy(cartItems = updated, searchQuery = "", searchMode = false)
            } else {
                state.copy(
                    cartItems = state.cartItems + CartItem(product = product, unitPrice = price),
                    searchQuery = "",
                    searchMode = false
                )
            }
        }
    }

    fun updateQuantity(index: Int, delta: Double) {
        _uiState.update { state ->
            val updated = state.cartItems.toMutableList()
            val item = updated[index]
            val newQty = (item.quantity + delta).coerceAtLeast(0.0)
            if (newQty <= 0) {
                updated.removeAt(index)
            } else {
                updated[index] = item.copy(quantity = newQty)
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeFromCart(index: Int) {
        _uiState.update { state ->
            state.copy(cartItems = state.cartItems.toMutableList().also { it.removeAt(index) })
        }
    }

    fun showPayment() {
        _uiState.update { it.copy(showPaymentDialog = true, saleSuccess = null, error = null) }
    }

    fun dismissPayment() {
        _uiState.update { it.copy(showPaymentDialog = false) }
    }

    fun onPaymentMethodChange(method: String) {
        _uiState.update { it.copy(paymentMethod = method) }
    }

    fun onAmountReceivedChange(value: String) {
        _uiState.update { it.copy(amountReceived = value) }
    }

    fun onDiscountChange(value: String) {
        _uiState.update { it.copy(discount = value) }
    }

    val cartTotal: Double
        get() = _uiState.value.cartItems.sumOf { it.subtotal }

    val cartCount: Int
        get() = _uiState.value.cartItems.size

    fun processSale(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) {
            _uiState.update { it.copy(error = "Agrega productos al carrito") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }
            val discount = state.discount.toDoubleOrNull() ?: 0.0
            val subtotal = cartTotal
            val amount = if (state.paymentMethod == "cash") {
                state.amountReceived.toDoubleOrNull() ?: subtotal
            } else subtotal

            val request = SaleRequest(
                branchId = 1, // Will be overridden by server based on JWT
                items = state.cartItems.map { item ->
                    SaleItemRequest(
                        productId = item.product.id,
                        quantity = item.quantity,
                        unitPrice = item.unitPrice,
                        discount = 0.0,
                        batchId = item.batchId,
                        flavorId = item.flavorId
                    )
                },
                payment = SalePaymentRequest(
                    method = state.paymentMethod,
                    amount = amount,
                    subtotal = subtotal,
                    discount = discount
                ),
                terceroId = state.selectedTercero?.id
            )

            repository.processSale(request).fold(
                onSuccess = { transaction ->
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            showPaymentDialog = false,
                            cartItems = emptyList(),
                            saleSuccess = "Venta ${transaction.referenceNumber} procesada",
                            selectedTercero = null
                        )
                    }
                    onSuccess()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isProcessing = false, error = e.message) }
                }
            )
        }
    }

    fun clearSaleSuccess() {
        _uiState.update { it.copy(saleSuccess = null) }
    }

    fun onTerceroSearchChange(query: String) {
        _uiState.update { it.copy(terceroSearchQuery = query, showTerceroSearch = query.isNotEmpty()) }
    }

    fun selectTercero(tercero: TerceroModel) {
        _uiState.update { it.copy(selectedTercero = tercero, terceroSearchQuery = "", showTerceroSearch = false) }
    }

    fun clearTercero() {
        _uiState.update { it.copy(selectedTercero = null) }
    }
}
