package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.TransactionModel
import retrofit2.Response
import retrofit2.http.*

interface SalesService {
    @GET("api/sales")
    suspend fun getSales(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("startDate") dateFrom: String? = null,
        @Query("endDate") dateTo: String? = null,
        @Query("allBranches") allBranches: Boolean? = null,
        @Query("search") search: String? = null
    ): Response<ApiResponse<List<TransactionModel>>>

    @GET("api/sales/{id}")
    suspend fun getSaleDetail(@Path("id") id: Int): Response<TransactionModel>

    @PUT("api/sales/{id}")
    suspend fun updateSale(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<TransactionModel>>

    @POST("api/sales/{id}/mark-paid")
    suspend fun markAsPaid(@Path("id") id: Int): Response<ApiResponse<TransactionModel>>

    @DELETE("api/sales/{id}")
    suspend fun deleteSale(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
