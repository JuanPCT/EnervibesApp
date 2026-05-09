package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.TransactionModel
import retrofit2.Response
import retrofit2.http.*

interface ExpensesService {
    @GET("expenses")
    suspend fun getExpenses(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null,
        @Query("category") category: String? = null
    ): Response<ApiResponse<List<TransactionModel>>>

    @GET("expenses/api/summary")
    suspend fun getExpensesSummary(): Response<ApiResponse<Map<String, Any?>>>

    @FormUrlEncoded
    @POST("expenses/create")
    suspend fun createExpense(
        @Field("amount") amount: Double,
        @Field("description") description: String?,
        @Field("category") category: String?,
        @Field("branch_id") branchId: Int,
        @Field("date") date: String?
    ): Response<ApiResponse<TransactionModel>>

    @GET("expenses/{id}")
    suspend fun getExpenseDetail(@Path("id") id: Int): Response<TransactionModel>

    @FormUrlEncoded
    @POST("expenses/{id}/edit")
    suspend fun updateExpense(
        @Path("id") id: Int,
        @Field("amount") amount: Double,
        @Field("description") description: String?,
        @Field("category") category: String?,
        @Field("branch_id") branchId: Int,
        @Field("date") date: String?
    ): Response<ApiResponse<TransactionModel>>

    @FormUrlEncoded
    @POST("expenses/{id}/delete")
    suspend fun deleteExpense(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
