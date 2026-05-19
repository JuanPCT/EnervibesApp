package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ReportsService {
    @GET("api/reports/sales-by-product")
    suspend fun getSalesByProduct(
        @Query("startDate") dateFrom: String? = null,
        @Query("endDate") dateTo: String? = null,
        @Query("allBranches") allBranches: Boolean? = null
    ): Response<ApiResponse<List<Map<String, Any?>>>>

    @GET("api/reports/sales-by-employee")
    suspend fun getSalesByEmployee(
        @Query("startDate") dateFrom: String? = null,
        @Query("endDate") dateTo: String? = null,
        @Query("allBranches") allBranches: Boolean? = null
    ): Response<ApiResponse<List<Map<String, Any?>>>>
}
