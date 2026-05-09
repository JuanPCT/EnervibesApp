package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ReportsService {
    @GET("reports/sales-by-product")
    suspend fun getSalesByProduct(
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null,
        @Query("branch_id") branchId: Int? = null
    ): Response<ApiResponse<List<Map<String, Any?>>>>

    @GET("reports/sales-by-employee")
    suspend fun getSalesByEmployee(
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null,
        @Query("branch_id") branchId: Int? = null
    ): Response<ApiResponse<List<Map<String, Any?>>>>
}
