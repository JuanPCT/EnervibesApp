package co.com.enervibes.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.ui.auth.LoginScreen
import co.com.enervibes.app.ui.auth.LoginViewModel
import co.com.enervibes.app.ui.components.AppScaffold
import co.com.enervibes.app.ui.components.AppScreen
import co.com.enervibes.app.ui.dashboard.DashboardScreen
import co.com.enervibes.app.ui.dashboard.DashboardViewModel
import co.com.enervibes.app.ui.pos.PosScreen
import co.com.enervibes.app.ui.pos.PosViewModel
import co.com.enervibes.app.ui.products.ProductsListScreen
import co.com.enervibes.app.ui.products.ProductsViewModel
import co.com.enervibes.app.ui.sales.SalesListScreen
import co.com.enervibes.app.ui.sales.SalesViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object NavRoutes {
    const val LOGIN = "login"
    const val MAIN = "main"
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val navController = rememberNavController()

    val token: String? = runBlocking { tokenManager.token.first() }
    val startDestination = if (!token.isNullOrBlank()) NavRoutes.MAIN else NavRoutes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return LoginViewModel(tokenManager) as T
                    }
                }
            )
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.MAIN) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }

        composable(NavRoutes.MAIN) {
            MainScreen(
                tokenManager = tokenManager,
                onLogout = {
                    runBlocking { tokenManager.clear() }
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.MAIN) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    tokenManager: TokenManager,
    onLogout: () -> Unit
) {
    var currentScreen by remember { mutableStateOf(AppScreen.Dashboard) }

    val dashboardViewModel: DashboardViewModel = viewModel()
    val posViewModel: PosViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return PosViewModel(tokenManager) as T
            }
        }
    )
    val salesViewModel: SalesViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return SalesViewModel(tokenManager) as T
            }
        }
    )
    val productsViewModel: ProductsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ProductsViewModel(tokenManager) as T
            }
        }
    )

    AppScaffold(
        currentScreen = currentScreen,
        onScreenChange = { currentScreen = it },
        onLogout = onLogout,
        tokenManager = tokenManager
    ) { _ ->
        when (currentScreen) {
            AppScreen.Dashboard -> DashboardScreen(dashboardViewModel)
            AppScreen.Pos -> PosScreen(posViewModel)
            AppScreen.Sales -> SalesListScreen(salesViewModel)
            AppScreen.Products -> ProductsListScreen(productsViewModel)
            else -> DashboardScreen(dashboardViewModel)
        }
    }
}
