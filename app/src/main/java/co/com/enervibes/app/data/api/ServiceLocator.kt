package co.com.enervibes.app.data.api

import android.content.Context
import retrofit2.Retrofit

object ServiceLocator {
    private var retrofit: Retrofit? = null

    val authService: AuthService by lazy { retrofit!!.create(AuthService::class.java) }
    val dashboardService: DashboardService by lazy { retrofit!!.create(DashboardService::class.java) }
    val posService: PosService by lazy { retrofit!!.create(PosService::class.java) }
    val salesService: SalesService by lazy { retrofit!!.create(SalesService::class.java) }
    val productsService: ProductsService by lazy { retrofit!!.create(ProductsService::class.java) }
    val purchasesService: PurchasesService by lazy { retrofit!!.create(PurchasesService::class.java) }
    val expensesService: ExpensesService by lazy { retrofit!!.create(ExpensesService::class.java) }
    val tercerosService: TercerosService by lazy { retrofit!!.create(TercerosService::class.java) }
    val transfersService: TransfersService by lazy { retrofit!!.create(TransfersService::class.java) }
    val temperaturesService: TemperaturesService by lazy { retrofit!!.create(TemperaturesService::class.java) }
    val reportsService: ReportsService by lazy { retrofit!!.create(ReportsService::class.java) }
    val configurationService: ConfigurationService by lazy { retrofit!!.create(ConfigurationService::class.java) }

    fun init(context: Context) {
        if (retrofit == null) {
            retrofit = ApiClient.create(context)
        }
    }
}
