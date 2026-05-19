package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.TemperatureModel
import retrofit2.Response
import retrofit2.http.*

interface TemperaturesService {
    @GET("api/temperatures")
    suspend fun getTemperatures(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("branch_id") branchId: Int
    ): Response<ApiResponse<List<TemperatureModel>>>

    @GET("api/temperatures/check")
    suspend fun checkTemperatureNeeded(@Query("branch_id") branchId: Int): Response<ApiResponse<Map<String, Any?>>>

    @POST("api/temperatures/register")
    suspend fun registerTemperature(@Body body: Map<String, Any?>): Response<ApiResponse<TemperatureModel>>
}
