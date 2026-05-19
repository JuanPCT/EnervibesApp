package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.TerceroModel
import co.com.enervibes.app.data.model.TerceroCreateRequest
import retrofit2.Response
import retrofit2.http.*

interface TercerosService {
    @GET("api/terceros/search")
    suspend fun searchClientes(@Query("q") query: String): Response<ApiResponse<List<TerceroModel>>>

    @POST("api/terceros/quick-create")
    suspend fun quickCreateCliente(@Body body: TerceroCreateRequest): Response<ApiResponse<TerceroModel>>

    @GET("api/terceros/default")
    suspend fun getDefaultCliente(): Response<ApiResponse<TerceroModel>>

    @GET("api/terceros")
    suspend fun getTerceros(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("tipo") tipo: String? = null,
        @Query("search") search: String? = null
    ): Response<ApiResponse<List<TerceroModel>>>

    @POST("api/terceros")
    suspend fun createTercero(@Body body: TerceroCreateRequest): Response<ApiResponse<TerceroModel>>

    @PUT("api/terceros/{id}")
    suspend fun updateTercero(
        @Path("id") id: Int,
        @Body body: TerceroCreateRequest
    ): Response<ApiResponse<TerceroModel>>

    @DELETE("api/terceros/{id}")
    suspend fun deleteTercero(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
