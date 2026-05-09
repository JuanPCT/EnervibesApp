package co.com.enervibes.app.data.repository

import co.com.enervibes.app.data.api.ServiceLocator
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.*

class AuthRepository(private val tokenManager: TokenManager) {

    private val service get() = ServiceLocator.authService

    suspend fun login(email: String, password: String, rememberMe: Boolean): Result<UserModel> {
        return try {
            val response = service.login(LoginRequest(email, password, rememberMe))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.token != null && body.user != null) {
                    val user = body.user
                    val branchId = user.currentBranchId ?: user.defaultBranchId
                    val branchName = user.Branch?.name ?: "Sucursal"
                    tokenManager.saveSession(
                        token = body.token,
                        userId = user.id.toString(),
                        userName = user.name,
                        userEmail = user.email,
                        userRole = user.role,
                        companyId = user.companyId.toString(),
                        branchId = branchId.toString(),
                        branchName = branchName
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception(body?.message ?: body?.error ?: "Error al iniciar sesión"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error del servidor"
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        try {
            service.logout()
        } catch (_: Exception) {}
        tokenManager.clear()
    }

    suspend fun getBranches(): Result<List<BranchModel>> {
        return try {
            val response = service.getBranches()
            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener sucursales"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changeBranch(branchId: Int): Result<UserModel> {
        return try {
            val response = service.changeBranch(mapOf("branch_id" to branchId))
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(body?.data!!)
            } else {
                Result.failure(Exception("Error al cambiar de sucursal"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
