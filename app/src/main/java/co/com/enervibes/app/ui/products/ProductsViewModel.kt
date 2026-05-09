package co.com.enervibes.app.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.BrandModel
import co.com.enervibes.app.data.model.CategoryModel
import co.com.enervibes.app.data.model.ProductModel
import co.com.enervibes.app.data.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductsUiState(
    val isLoading: Boolean = false,
    val products: List<ProductModel> = emptyList(),
    val totalCount: Int = 0,
    val currentPage: Int = 1,
    val searchQuery: String = "",
    val selectedCategory: Int? = null,
    val showLowStock: Boolean = false,
    val selectedProduct: ProductModel? = null,
    val showCreateEdit: Boolean = false,
    val isEditing: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    // Create/Edit form fields
    val formName: String = "",
    val formPrice: String = "",
    val formCost: String = "",
    val formBarcode: String = "",
    val formDescription: String = "",
    val formCategoryId: Int? = null,
    val formBrandId: Int? = null,
    val formStockMinimo: String = "",
    val formEsIngrediente: Boolean = false,
    val formEsPreparado: Boolean = false,
    val formPrecioPorPorcion: String = "",
    val formGrupoIngrediente: String = "",
    val formPorciones: String = "1",
    // Categories and Brands for dropdowns
    val categories: List<CategoryModel> = emptyList(),
    val brands: List<BrandModel> = emptyList(),
    // Delete dialog
    val showDeleteConfirm: Boolean = false,
    val productToDelete: ProductModel? = null,
    // Stock dialog
    val showStockDialog: Boolean = false,
    val stockProduct: ProductModel? = null,
    val stockQuantity: String = ""
)

class ProductsViewModel(tokenManager: TokenManager) : ViewModel() {
    private val repository = ProductsRepository()
    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init { loadProducts() }

    fun loadProducts(page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getProducts(
                page = page,
                search = _uiState.value.searchQuery.ifBlank { null },
                categoryId = _uiState.value.selectedCategory
            ).fold(
                onSuccess = { (products, total) ->
                    _uiState.update {
                        it.copy(isLoading = false, products = products, totalCount = total, currentPage = page)
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun onSearchChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun search() { loadProducts() }

    fun openCreate() {
        _uiState.update {
            it.copy(
                showCreateEdit = true,
                isEditing = false,
                formName = "",
                formPrice = "",
                formCost = "",
                formBarcode = "",
                formDescription = "",
                formCategoryId = null,
                formBrandId = null,
                formStockMinimo = "0",
                formEsIngrediente = false,
                formEsPreparado = false,
                formPrecioPorPorcion = "",
                formGrupoIngrediente = "",
                formPorciones = "1"
            )
        }
    }

    fun openEdit(product: ProductModel) {
        _uiState.update {
            it.copy(
                showCreateEdit = true,
                isEditing = true,
                selectedProduct = product,
                formName = product.name,
                formPrice = product.price.toString(),
                formCost = product.cost.toString(),
                formBarcode = product.barcode ?: "",
                formDescription = product.descripcion ?: "",
                formCategoryId = product.categoryId,
                formBrandId = product.brandId,
                formStockMinimo = (product.stockMinimo ?: 0).toString(),
                formEsIngrediente = product.esIngrediente ?: false,
                formEsPreparado = product.esProductoPreparado ?: false,
                formPrecioPorPorcion = product.precioPorPorcion?.toString() ?: "",
                formGrupoIngrediente = product.grupoIngrediente ?: "",
                formPorciones = (product.porciones ?: 1).toString()
            )
        }
    }

    fun closeForm() {
        _uiState.update { it.copy(showCreateEdit = false, selectedProduct = null) }
    }

    fun onFormFieldChange(field: String, value: Any) {
        _uiState.update {
            when (field) {
                "name" -> it.copy(formName = value as String)
                "price" -> it.copy(formPrice = value as String)
                "cost" -> it.copy(formCost = value as String)
                "barcode" -> it.copy(formBarcode = value as String)
                "description" -> it.copy(formDescription = value as String)
                "categoryId" -> it.copy(formCategoryId = value as? Int)
                "brandId" -> it.copy(formBrandId = value as? Int)
                "stockMinimo" -> it.copy(formStockMinimo = value as String)
                "esIngrediente" -> it.copy(formEsIngrediente = value as Boolean)
                "esPreparado" -> it.copy(formEsPreparado = value as Boolean)
                "precioPorPorcion" -> it.copy(formPrecioPorPorcion = value as String)
                "grupoIngrediente" -> it.copy(formGrupoIngrediente = value as String)
                "porciones" -> it.copy(formPorciones = value as String)
                else -> it
            }
        }
    }

    fun deleteProduct(product: ProductModel) {
        _uiState.update { it.copy(showDeleteConfirm = true, productToDelete = product) }
    }

    fun cancelDelete() {
        _uiState.update { it.copy(showDeleteConfirm = false, productToDelete = null) }
    }

    fun confirmDelete() {
        val product = _uiState.value.productToDelete ?: return
        viewModelScope.launch {
            repository.deleteProduct(product.id).fold(
                onSuccess = {
                    _uiState.update { it.copy(showDeleteConfirm = false, productToDelete = null, success = "Producto eliminado") }
                    loadProducts()
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

    fun openStockDialog(product: ProductModel) {
        _uiState.update { it.copy(showStockDialog = true, stockProduct = product, stockQuantity = "") }
    }

    fun closeStockDialog() {
        _uiState.update { it.copy(showStockDialog = false, stockProduct = null, stockQuantity = "") }
    }
}
