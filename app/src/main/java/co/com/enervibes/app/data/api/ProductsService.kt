package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductsService {
    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("search") search: String? = null,
        @Query("category_id") categoryId: Int? = null,
        @Query("branch_id") branchId: Int? = null,
        @Query("low_stock") lowStock: Boolean? = null
    ): Response<ApiResponse<List<ProductModel>>>

    @Multipart
    @POST("products/create")
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
    @POST("products/update")
    suspend fun updateProduct(
        @Part("id") id: RequestBody,
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

    @FormUrlEncoded
    @POST("products/delete")
    suspend fun deleteProduct(
        @Field("id") id: Int,
        @Field("_method") method: String = "DELETE"
    ): Response<ApiResponse<Unit>>

    @FormUrlEncoded
    @POST("products/set-initial-stock")
    suspend fun setInitialStock(
        @Field("product_id") productId: Int,
        @Field("branch_id") branchId: Int,
        @Field("quantity") quantity: Double,
        @Field("min_stock_level") minStockLevel: Double? = null
    ): Response<ApiResponse<Unit>>
}
