package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.BranchModel
import co.com.enervibes.app.data.model.CategoryModel
import co.com.enervibes.app.data.model.BrandModel
import co.com.enervibes.app.data.model.FlavorModel
import retrofit2.Response
import retrofit2.http.*

interface ConfigurationService {
    // Configuration
    @GET("api/configuration")
    suspend fun getConfiguration(): Response<ApiResponse<Map<String, Any?>>>

    @PUT("api/configuration/company")
    suspend fun updateCompany(@Body body: Map<String, Any?>): Response<ApiResponse<Unit>>

    // Branches
    @POST("api/configuration/branches")
    suspend fun createBranch(@Body body: Map<String, Any?>): Response<ApiResponse<BranchModel>>

    @PUT("api/configuration/branches/{id}")
    suspend fun updateBranch(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<BranchModel>>

    @DELETE("api/configuration/branches/{id}")
    suspend fun deleteBranch(@Path("id") id: Int): Response<ApiResponse<Unit>>

    // Categories
    @GET("api/categories")
    suspend fun getCategories(): Response<ApiResponse<List<CategoryModel>>>

    @POST("api/categories")
    suspend fun createCategory(@Body body: Map<String, Any?>): Response<ApiResponse<CategoryModel>>

    @PUT("api/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<CategoryModel>>

    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Response<ApiResponse<Unit>>

    // Brands
    @GET("api/brands")
    suspend fun getBrands(): Response<ApiResponse<List<BrandModel>>>

    @POST("api/brands")
    suspend fun createBrand(@Body body: Map<String, Any?>): Response<ApiResponse<BrandModel>>

    @PUT("api/brands/{id}")
    suspend fun updateBrand(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<BrandModel>>

    @DELETE("api/brands/{id}")
    suspend fun deleteBrand(@Path("id") id: Int): Response<ApiResponse<Unit>>

    // Flavors
    @GET("api/flavors")
    suspend fun getFlavors(): Response<ApiResponse<List<FlavorModel>>>

    @POST("api/flavors")
    suspend fun createFlavor(@Body body: Map<String, Any?>): Response<ApiResponse<FlavorModel>>

    @PUT("api/flavors/{id}")
    suspend fun updateFlavor(
        @Path("id") id: Int,
        @Body body: Map<String, Any?>
    ): Response<ApiResponse<FlavorModel>>

    @DELETE("api/flavors/{id}")
    suspend fun deleteFlavor(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
