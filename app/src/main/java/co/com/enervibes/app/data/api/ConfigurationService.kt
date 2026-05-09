package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.ApiResponse
import co.com.enervibes.app.data.model.BranchModel
import co.com.enervibes.app.data.model.CategoryModel
import co.com.enervibes.app.data.model.BrandModel
import co.com.enervibes.app.data.model.FlavorModel
import retrofit2.Response
import retrofit2.http.*

interface ConfigurationService {
    // Company
    @FormUrlEncoded
    @POST("configuration/company")
    suspend fun updateCompany(
        @Field("name") name: String,
        @Field("plan") plan: String? = null
    ): Response<ApiResponse<Unit>>

    // Branches
    @FormUrlEncoded
    @POST("configuration/branch")
    suspend fun createBranch(
        @Field("name") name: String,
        @Field("location") location: String?,
        @Field("is_main") isMain: Boolean? = false
    ): Response<ApiResponse<BranchModel>>

    @FormUrlEncoded
    @POST("configuration/branch/update")
    suspend fun updateBranch(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("location") location: String?
    ): Response<ApiResponse<BranchModel>>

    @FormUrlEncoded
    @POST("configuration/branch/delete")
    suspend fun deleteBranch(@Field("id") id: Int): Response<ApiResponse<Unit>>

    // Categories
    @GET("categories")
    suspend fun getCategories(): Response<ApiResponse<List<CategoryModel>>>

    @FormUrlEncoded
    @POST("categories/create")
    suspend fun createCategory(
        @Field("name") name: String,
        @Field("description") description: String? = null
    ): Response<ApiResponse<CategoryModel>>

    @FormUrlEncoded
    @POST("categories/{id}/edit")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("description") description: String? = null
    ): Response<ApiResponse<CategoryModel>>

    @FormUrlEncoded
    @POST("categories/{id}/delete")
    suspend fun deleteCategory(@Path("id") id: Int): Response<ApiResponse<Unit>>

    // Brands
    @GET("brands")
    suspend fun getBrands(): Response<ApiResponse<List<BrandModel>>>

    @FormUrlEncoded
    @POST("brands/create")
    suspend fun createBrand(@Field("name") name: String): Response<ApiResponse<BrandModel>>

    @FormUrlEncoded
    @POST("brands/{id}/edit")
    suspend fun updateBrand(@Path("id") id: Int, @Field("name") name: String): Response<ApiResponse<BrandModel>>

    @FormUrlEncoded
    @POST("brands/{id}/delete")
    suspend fun deleteBrand(@Path("id") id: Int): Response<ApiResponse<Unit>>

    // Flavors
    @GET("flavors")
    suspend fun getFlavors(): Response<ApiResponse<List<FlavorModel>>>

    @FormUrlEncoded
    @POST("flavors/create")
    suspend fun createFlavor(@Field("name") name: String): Response<ApiResponse<FlavorModel>>

    @FormUrlEncoded
    @POST("flavors/{id}/edit")
    suspend fun updateFlavor(@Path("id") id: Int, @Field("name") name: String): Response<ApiResponse<FlavorModel>>

    @FormUrlEncoded
    @POST("flavors/{id}/delete")
    suspend fun deleteFlavor(@Path("id") id: Int): Response<ApiResponse<Unit>>
}
