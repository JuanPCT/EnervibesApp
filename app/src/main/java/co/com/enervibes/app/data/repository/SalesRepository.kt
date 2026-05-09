package co.com.enervibes.app.data.repository

import co.com.enervibes.app.data.api.ServiceLocator
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.*

class SalesRepository(private val tokenManager: TokenManager) {
    private val service get() = ServiceLocator.salesService

    private suspend fun getBranchId(): Int = tokenManager.getBranchId()?.toIntOrNull() ?: 1

    suspend fun getSales(
        page: Int = 1,
        limit: Int = 20,
        dateFrom: String? = null,
        dateTo: String? = null
    ): Result<Pair<List<TransactionModel>, Int>> {
        return try {
            val branchId = getBranchId()
            val response = service.getSales(page, limit, dateFrom, dateTo, branchId)
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(Pair(body?.data ?: emptyList(), body?.total ?: 0))
            } else {
                Result.failure(Exception("Error al cargar ventas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSaleDetail(id: Int): Result<TransactionModel> {
        return try {
            val response = service.getSaleDetail(id)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Venta no encontrada"))
            } else {
                Result.failure(Exception("Error del servidor"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAsPaid(id: Int): Result<TransactionModel> {
        return try {
            val response = service.markAsPaid(id)
            if (response.isSuccessful) {
                val body = response.body()
                body?.data?.let { Result.success(it) }
                    ?: Result.failure(Exception("Error al marcar como pagada"))
            } else {
                Result.failure(Exception("Error del servidor"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSale(id: Int): Result<Unit> {
        return try {
            val response = service.deleteSale(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar venta"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
