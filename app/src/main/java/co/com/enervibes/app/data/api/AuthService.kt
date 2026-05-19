package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/auth/me")
    suspend fun me(): Response<LoginResponse>

    @GET("api/auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    @GET("api/auth/branches")
    suspend fun getBranches(): Response<ApiResponse<List<BranchModel>>>

    @POST("api/auth/change-branch")
    suspend fun changeBranch(@Body body: Map<String, Int>): Response<ApiResponse<UserModel>>
}
