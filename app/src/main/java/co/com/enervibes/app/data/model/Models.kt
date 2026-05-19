package co.com.enervibes.app.data.model

import com.google.gson.annotations.SerializedName

// ===== Auth Models =====
data class LoginRequest(
    val email: String,
    val password: String,
    val remember: Boolean = false
)

data class LoginResponse(
    val token: String?,
    val user: UserModel?,
    val message: String?,
    val error: String?
)

data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    @SerializedName("job_title") val jobTitle: String?,
    @SerializedName("CompanyId") val companyId: Int,
    @SerializedName("defaultBranchId") val defaultBranchId: Int,
    @SerializedName("currentBranchId") val currentBranchId: Int?,
    val Company: CompanyModel?,
    val Branch: BranchModel?
)

data class CompanyModel(
    val id: Int,
    val name: String,
    val plan: String?,
    val status: String?,
    val logo: String?
)

data class BranchModel(
    val id: Int,
    val name: String,
    val location: String?,
    @SerializedName("isMain") val isMain: Boolean?
)

// ===== Product Models =====
data class ProductModel(
    val id: Int,
    val name: String,
    val descripcion: String?,
    val barcode: String?,
    val price: Double,
    val cost: Double,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("useBatches") val useBatches: Boolean?,
    @SerializedName("stockMinimo") val stockMinimo: Int?,
    val porciones: Int?,
    @SerializedName("esProductoPreparado") val esProductoPreparado: Boolean?,
    @SerializedName("esIngrediente") val esIngrediente: Boolean?,
    @SerializedName("precioPorPorcion") val precioPorPorcion: Double?,
    @SerializedName("grupoIngrediente") val grupoIngrediente: String?,
    @SerializedName("CompanyId") val companyId: Int?,
    @SerializedName("BrandId") val brandId: Int?,
    @SerializedName("CategoryId") val categoryId: Int?,
    val Brand: BrandModel?,
    val Category: CategoryModel?,
    val Flavor: FlavorModel?,
    val Flavors: List<FlavorModel>?,
    val Stock: List<StockModel>?,
    val Batches: List<BatchModel>?
)

data class StockModel(
    val id: Int,
    val quantity: Double,
    @SerializedName("minStockLevel") val minStockLevel: Double?,
    @SerializedName("FlavorId") val flavorId: Int?,
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("BranchId") val branchId: Int,
    val Branch: BranchModel?
)

data class BatchModel(
    val id: Int,
    @SerializedName("batchNumber") val batchNumber: String,
    @SerializedName("expirationDate") val expirationDate: String?,
    val quantity: Double,
    @SerializedName("initialQuantity") val initialQuantity: Double?,
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("BranchId") val branchId: Int
)

data class CategoryModel(
    val id: Int,
    val name: String,
    val description: String?
)

data class BrandModel(
    val id: Int,
    val name: String
)

data class FlavorModel(
    val id: Int,
    val name: String
)

data class ProductFlavorModel(
    val id: Int,
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("FlavorId") val flavorId: Int
)

data class ProductRecipeModel(
    val id: Int,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("cantidadPorciones") val cantidadPorciones: Double,
    val orden: Int?,
    @SerializedName("esOpcional") val esOpcional: Boolean?,
    val grupo: String?,
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("IngredientId") val ingredientId: Int?,
    val Ingredient: ProductModel?
)

// ===== Transaction Models =====
data class TransactionModel(
    val id: Int,
    val type: String, // sale, expense, purchase, adjustment, transfer
    val amount: Double,
    val date: String?,
    val details: Map<String, Any?>?,
    @SerializedName("referenceNumber") val referenceNumber: String?,
    @SerializedName("paymentStatus") val paymentStatus: String?, // paid, pending
    @SerializedName("UserId") val userId: Int?,
    @SerializedName("BranchId") val branchId: Int?,
    @SerializedName("TerceroId") val terceroId: Int?,
    @SerializedName("FiscalYearId") val fiscalYearId: Int?,
    val User: UserModel?,
    val Branch: BranchModel?,
    val Tercero: TerceroModel?,
    @SerializedName("TransactionDetails") val transactionDetails: List<TransactionDetailModel>?
)

data class TransactionDetailModel(
    val id: Int,
    val quantity: Double,
    @SerializedName("unitPrice") val unitPrice: Double,
    val subtotal: Double,
    val discount: Double?,
    @SerializedName("FlavorId") val flavorId: Int?,
    val metadata: Map<String, Any?>?,
    @SerializedName("TransactionId") val transactionId: Int,
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("BatchId") val batchId: Int?,
    val Product: ProductModel?,
    val Batch: BatchModel?
)

data class SaleRequest(
    @SerializedName("branch_id") val branchId: Int,
    val items: List<SaleItemRequest>,
    val payment: SalePaymentRequest,
    @SerializedName("tercero_id") val terceroId: Int? = null,
    val reference: String? = null
)

data class SaleItemRequest(
    @SerializedName("product_id") val productId: Int,
    val quantity: Double,
    @SerializedName("unit_price") val unitPrice: Double,
    val discount: Double? = 0.0,
    @SerializedName("batch_id") val batchId: Int? = null,
    @SerializedName("flavor_id") val flavorId: Int? = null,
    val metadata: Map<String, Any?>? = null
)

data class SalePaymentRequest(
    val method: String, // cash, transfer, credit
    val amount: Double? = null,
    val subtotal: Double? = null,
    val discount: Double? = null
)

data class PurchaseRequest(
    @SerializedName("branch_id") val branchId: Int,
    val items: List<PurchaseItemRequest>,
    val payment: PurchasePaymentRequest? = null,
    @SerializedName("proveedor_id") val proveedorId: Int? = null
)

data class PurchaseItemRequest(
    @SerializedName("product_id") val productId: Int,
    val quantity: Double,
    @SerializedName("unit_price") val unitPrice: Double,
    @SerializedName("batch_number") val batchNumber: String? = null,
    @SerializedName("expiration_date") val expirationDate: String? = null
)

data class PurchasePaymentRequest(
    val method: String = "cash",
    val amount: Double = 0.0
)

data class ExpenseRequest(
    val amount: Double,
    val description: String?,
    val category: String?,
    @SerializedName("branch_id") val branchId: Int,
    @SerializedName("date") val date: String? = null,
    @SerializedName("reference_number") val referenceNumber: String? = null
)

// ===== Tercero Models =====
data class TerceroModel(
    val id: Int,
    val identificacion: String?,
    val nombre: String,
    val tipo: String, // usuario, cliente, proveedor
    val telefono: String?,
    val email: String?,
    val direccion: String?,
    val notas: String?,
    val activo: Boolean?,
    @SerializedName("UserId") val userId: Int?
)

data class TerceroCreateRequest(
    val identificacion: String?,
    val nombre: String,
    val tipo: String = "cliente",
    val telefono: String? = null,
    val email: String? = null,
    val direccion: String? = null,
    val notas: String? = null
)

// ===== Transfer Models =====
data class TransferModel(
    val id: Int,
    @SerializedName("referenceNumber") val referenceNumber: String?,
    val quantity: Double,
    val status: String?, // pending, completed, cancelled
    val notes: String?,
    @SerializedName("transferDate") val transferDate: String?,
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("fromBranchId") val fromBranchId: Int,
    @SerializedName("toBranchId") val toBranchId: Int,
    @SerializedName("UserId") val userId: Int?,
    val Product: ProductModel?,
    val FromBranch: BranchModel?,
    val ToBranch: BranchModel?,
    val User: UserModel?
)

data class TransferCreateRequest(
    @SerializedName("product_id") val productId: Int,
    val quantity: Double,
    @SerializedName("from_branch_id") val fromBranchId: Int,
    @SerializedName("to_branch_id") val toBranchId: Int,
    val notes: String? = null
)

// ===== Temperature Models =====
data class TemperatureModel(
    val id: Int,
    val date: String?,
    @SerializedName("morningTemp") val morningTemp: Double?,
    @SerializedName("afternoonTemp") val afternoonTemp: Double?,
    @SerializedName("BranchId") val branchId: Int
)

// ===== FiscalYear Models =====
data class FiscalYearModel(
    val id: Int,
    val year: Int,
    val status: String?,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?
)

// ===== Dashboard Models =====
data class DashboardModel(
    @SerializedName("todaySales") val todaySales: DashboardSalesModel?,
    @SerializedName("todayExpenses") val todayExpenses: Double?,
    @SerializedName("netIncome") val netIncome: Double?,
    @SerializedName("monthlySummary") val monthlySummary: MonthlySummaryModel?,
    @SerializedName("lowStock") val lowStock: List<ProductModel>?,
    @SerializedName("topProducts") val topProducts: List<TopProductModel>?,
    @SerializedName("topSellers") val topSellers: List<TopSellerModel>?,
    @SerializedName("salesTrend") val salesTrend: List<SalesTrendModel>?,
    @SerializedName("recentTransactions") val recentTransactions: List<TransactionModel>?
)

data class DashboardSalesModel(
    val total: Double?,
    val count: Int?
)

data class MonthlySummaryModel(
    val sales: Double?,
    val expenses: Double?
)

data class TopProductModel(
    val name: String?,
    @SerializedName("total_sold") val totalSold: Double?,
    @SerializedName("total_amount") val totalAmount: Double?,
    @SerializedName("Product") val product: ProductModel?
)

data class TopSellerModel(
    val name: String?,
    @SerializedName("total_sales") val totalSales: Double?
)

data class SalesTrendModel(
    val date: String?,
    val total: Double?
)

// ===== Generic API Response =====
data class ApiResponse<T>(
    val success: Boolean?,
    val data: T?,
    val message: String?,
    val error: String?,
    val total: Int?,
    val page: Int?,
    @SerializedName("totalPages") val totalPages: Int?
)

data class StockByBranchResponse(
    val product: ProductModel?,
    @SerializedName("stock_by_branch") val stockByBranch: List<StockModel>?
)
