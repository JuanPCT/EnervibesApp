package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.TransactionModel
import retrofit2.Response
import retrofit2.http.*

interface ExpensesService {
    @GET("api/expenses")
    suspend fun getExpenses(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("startDate") dateFrom: String? = null,
        @Query("endDate") dateTo: String? = null,
        @Query("category") category: String? = null
    ): Response<ApiResponse<List<TransactionModel>>>

    @GET("api/expenses/summary")
    suspend fun getExpensesSummary(
        @Query("period") period: String = "month"
    ): Response<ApiResponse<Map<String, Any?>>>

    @POST("api/expenses")
    suspend fun createExpense(@Body body: Map<String, Any?>): Response<ApiResponse<TransactionModel>>

    @GET("api/expenses/{id}")
    suspend fun getExpenseDetail(@Path("id") id: Int): Response<TransactionModel>

    @PUT("api/expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<TransactionModel>>

    @DELETE("api/expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
