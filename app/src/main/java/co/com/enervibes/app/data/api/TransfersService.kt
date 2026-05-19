package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface TransfersService {
    @GET("api/transfers")
    suspend fun getTransfers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<TransferModel>>>

    @GET("api/transfers/{id}")
    suspend fun getTransferDetail(@Path("id") id: Int): Response<TransferModel>

    @GET("api/transfers/stock")
    suspend fun getStockByBranch(
        @Query("productId") productId: Int,
        @Query("branchId") branchId: Int? = null
    ): Response<ApiResponse<List<StockByBranchResponse>>>

    @POST("api/transfers/create")
    suspend fun createTransfer(@Body body: TransferCreateRequest): Response<ApiResponse<TransferModel>>

    @PUT("api/transfers/{id}")
    suspend fun updateTransfer(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<TransferModel>>

    @DELETE("api/transfers/{id}")
    suspend fun deleteTransfer(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
