package co.com.enervibes.app.data.api

import co.com.enervibes.app.data.model.DashboardModel
import retrofit2.Response
import retrofit2.http.GET

interface DashboardService {
    @GET("api/dashboard")
    suspend fun getDashboard(): Response<DashboardModel>
}
