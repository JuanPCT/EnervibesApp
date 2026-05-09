package co.com.enervibes.app.data.repository

import co.com.enervibes.app.data.api.ServiceLocator
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.*

class PosRepository(private val tokenManager: TokenManager) {
    private val service get() = ServiceLocator.posService

    private suspend fun getBranchId(): Int = tokenManager.getBranchId()?.toIntOrNull() ?: 1

    suspend fun searchProducts(query: String): Result<List<ProductModel>> {
        return try {
            val branchId = getBranchId()
            val response = service.searchProducts(query, branchId)
            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Error al buscar productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopProducts(): Result<List<ProductModel>> {
        return try {
            val branchId = getBranchId()
            val response = service.getTopProducts(branchId)
            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun processSale(request: SaleRequest): Result<TransactionModel> {
        return try {
            val response = service.processSale(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: body?.error ?: "Error al procesar venta"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error del servidor"
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
