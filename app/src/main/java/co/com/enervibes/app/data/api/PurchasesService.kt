package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface PurchasesService {
    @GET("api/purchases/history")
    suspend fun getPurchaseHistory(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("startDate") dateFrom: String? = null,
        @Query("endDate") dateTo: String? = null
    ): Response<ApiResponse<List<TransactionModel>>>

    @GET("api/purchases/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("branch_id") branchId: Int
    ): Response<ApiResponse<List<ProductModel>>>

    @GET("api/purchases/batches/{productId}")
    suspend fun getProductBatches(
        @Path("productId") productId: Int
    ): Response<ApiResponse<List<BatchModel>>>

    @POST("api/purchases")
    suspend fun processPurchase(@Body purchase: PurchaseRequest): Response<ApiResponse<TransactionModel>>
}
