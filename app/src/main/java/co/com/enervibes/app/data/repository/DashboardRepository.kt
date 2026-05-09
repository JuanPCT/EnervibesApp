package co.com.enervibes.app.data.repository

import co.com.enervibes.app.data.api.ServiceLocator
import co.com.enervibes.app.data.model.DashboardModel

class DashboardRepository {
    private val service get() = ServiceLocator.dashboardService

    suspend fun getDashboard(): Result<DashboardModel> {
        return try {
            val response = service.getDashboard()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Error al cargar dashboard"))
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
