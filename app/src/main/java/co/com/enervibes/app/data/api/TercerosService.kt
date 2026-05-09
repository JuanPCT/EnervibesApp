package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.TerceroModel
import co.com.enervibes.app.data.model.TerceroCreateRequest
import retrofit2.Response
import retrofit2.http.*

interface TercerosService {
    @GET("terceros/api/search")
    suspend fun searchClientes(@Query("q") query: String): Response<ApiResponse<List<TerceroModel>>>

    @POST("terceros/api/quick-create")
    suspend fun quickCreateCliente(@Body body: TerceroCreateRequest): Response<ApiResponse<TerceroModel>>

    @GET("terceros/api/default")
    suspend fun getDefaultCliente(): Response<ApiResponse<TerceroModel>>

    @GET("terceros")
    suspend fun getTerceros(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("tipo") tipo: String? = null,
        @Query("search") search: String? = null
    ): Response<ApiResponse<List<TerceroModel>>>

    @FormUrlEncoded
    @POST("terceros/create")
    suspend fun createTercero(
        @Field("identificacion") identificacion: String?,
        @Field("nombre") nombre: String,
        @Field("tipo") tipo: String,
        @Field("telefono") telefono: String?,
        @Field("email") email: String?,
        @Field("direccion") direccion: String?,
        @Field("notas") notas: String?
    ): Response<ApiResponse<TerceroModel>>

    @FormUrlEncoded
    @POST("terceros/{id}/edit")
    suspend fun updateTercero(
        @Path("id") id: Int,
        @Field("identificacion") identificacion: String?,
        @Field("nombre") nombre: String,
        @Field("tipo") tipo: String?,
        @Field("telefono") telefono: String?,
        @Field("email") email: String?,
        @Field("direccion") direccion: String?,
        @Field("notas") notas: String?
    ): Response<ApiResponse<TerceroModel>>

    @FormUrlEncoded
    @POST("terceros/{id}/delete")
    suspend fun deleteTercero(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
