package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductsService {
    @GET("api/products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("name") search: String? = null,
        @Query("category") categoryId: Int? = null,
        @Query("branch") branchId: Int? = null,
        @Query("low_stock") lowStock: Boolean? = null
    ): Response<ApiResponse<List<ProductModel>>>

    @Multipart
    @POST("api/products")
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("cost") cost: RequestBody,
        @Part("barcode") barcode: RequestBody?,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("category_id") categoryId: RequestBody?,
        @Part("brand_id") brandId: RequestBody?,
        @Part("stock_minimo") stockMinimo: RequestBody?,
        @Part("es_ingrediente") esIngrediente: RequestBody?,
        @Part("es_producto_preparado") esProductoPreparado: RequestBody?,
        @Part("precio_por_porcion") precioPorPorcion: RequestBody?,
        @Part("grupo_ingrediente") grupoIngrediente: RequestBody?,
        @Part("porciones") porciones: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<ProductModel>>

    @Multipart
    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("cost") cost: RequestBody,
        @Part("barcode") barcode: RequestBody?,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("category_id") categoryId: RequestBody?,
        @Part("brand_id") brandId: RequestBody?,
        @Part("stock_minimo") stockMinimo: RequestBody?,
        @Part("es_ingrediente") esIngrediente: RequestBody?,
        @Part("es_producto_preparado") esProductoPreparado: RequestBody?,
        @Part("precio_por_porcion") precioPorPorcion: RequestBody?,
        @Part("grupo_ingrediente") grupoIngrediente: RequestBody?,
        @Part("porciones") porciones: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<ProductModel>>

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<ApiResponse<Unit>>

    @POST("api/products/set-initial-stock")
    suspend fun setInitialStock(
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<Unit>>
}
