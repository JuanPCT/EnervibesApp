package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface PurchasesService {
    @GET("purchases/history")
    suspend fun getPurchaseHistory(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null
    ): Response<ApiResponse<List<TransactionModel>>>

    @GET("purchases/api/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("branch_id") branchId: Int
    ): Response<ApiResponse<List<ProductModel>>>

    @GET("purchases/api/batches/{productId}")
    suspend fun getProductBatches(
        @Path("productId") productId: Int
    ): Response<ApiResponse<List<BatchModel>>>

    @POST("purchases/api/purchase")
    suspend fun processPurchase(@Body purchase: PurchaseRequest): Response<ApiResponse<TransactionModel>>
}
