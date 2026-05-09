package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface PosService {
    @GET("pos/api/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("branch_id") branchId: Int
    ): Response<ApiResponse<List<ProductModel>>>

    @GET("pos/api/top-products")
    suspend fun getTopProducts(
        @Query("branch_id") branchId: Int
    ): Response<ApiResponse<List<ProductModel>>>

    @POST("pos/api/sale")
    suspend fun processSale(@Body sale: SaleRequest): Response<ApiResponse<TransactionModel>>
}
