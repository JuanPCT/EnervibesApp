package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface PosService {
    @GET("api/pos/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("branch_id") branchId: Int
    ): Response<ApiResponse<List<ProductModel>>>

    @GET("api/pos/top-products")
    suspend fun getTopProducts(
        @Query("branch_id") branchId: Int
    ): Response<ApiResponse<List<ProductModel>>>

    @POST("api/pos/sale")
    suspend fun processSale(@Body sale: SaleRequest): Response<ApiResponse<TransactionModel>>
}
