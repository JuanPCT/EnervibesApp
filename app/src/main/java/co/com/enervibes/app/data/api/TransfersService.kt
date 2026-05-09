package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface TransfersService {
    @GET("transfers")
    suspend fun getTransfers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<TransferModel>>>

    @GET("transfers/{id}")
    suspend fun getTransferDetail(@Path("id") id: Int): Response<TransferModel>

    @GET("transfers/api/stock")
    suspend fun getStockByBranch(
        @Query("product_id") productId: Int
    ): Response<ApiResponse<List<StockByBranchResponse>>>

    @POST("transfers/api/create")
    suspend fun createTransfer(@Body body: TransferCreateRequest): Response<ApiResponse<TransferModel>>

    @PUT("transfers/api/{id}")
    suspend fun updateTransfer(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<TransferModel>>

    @DELETE("transfers/api/{id}")
    suspend fun deleteTransfer(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
