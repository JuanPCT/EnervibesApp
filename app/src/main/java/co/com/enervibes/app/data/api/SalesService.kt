package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.TransactionModel
import retrofit2.Response
import retrofit2.http.*

interface SalesService {
    @GET("sales")
    suspend fun getSales(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null,
        @Query("branch_id") branchId: Int? = null,
        @Query("reference") reference: String? = null
    ): Response<ApiResponse<List<TransactionModel>>>

    @GET("sales/{id}")
    suspend fun getSaleDetail(@Path("id") id: Int): Response<TransactionModel>

    @PUT("sales/{id}")
    suspend fun updateSale(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<TransactionModel>>

    @POST("sales/{id}/mark-paid")
    suspend fun markAsPaid(@Path("id") id: Int): Response<ApiResponse<TransactionModel>>

    @DELETE("sales/{id}")
    suspend fun deleteSale(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
