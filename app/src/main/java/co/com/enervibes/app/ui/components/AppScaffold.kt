package co.com.enervibes.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.UserModel
import co.com.enervibes.app.ui.theme.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class AppScreen(
    val title: String,
    val icon: ImageVector,
    val roles: List<String> = listOf("superadmin", "tenant_admin", "cajero")
) {
    Dashboard("Dashboard", Icons.Default.Dashboard),
    Pos("Punto de Venta", Icons.Default.PointOfSale),
    Sales("Ventas", Icons.Default.Receipt),
    Purchases("Compras", Icons.Default.ShoppingCart, listOf("superadmin", "tenant_admin")),
    Products("Productos", Icons.Default.Inventory, listOf("superadmin", "tenant_admin")),
    Expenses("Gastos", Icons.Default.Money, listOf("superadmin", "tenant_admin")),
    Transfers("Traslados", Icons.Default.SwapHoriz, listOf("superadmin", "tenant_admin")),
    Terceros("Terceros", Icons.Default.People, listOf("superadmin", "tenant_admin")),
    Temperatures("Temperaturas", Icons.Default.Thermostat, listOf("superadmin", "tenant_admin")),
    Reports("Reportes", Icons.Default.Analytics, listOf("superadmin", "tenant_admin")),
    Configuration("Configuración", Icons.Default.Settings, listOf("superadmin", "tenant_admin")),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    currentScreen: AppScreen,
    onScreenChange: (AppScreen) -> Unit,
    onLogout: () -> Unit,
    tokenManager: TokenManager,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val userName = remember { runBlocking { tokenManager.userName.first() } ?: "Usuario" }
    val userEmail = remember { runBlocking { tokenManager.userEmail.first() } ?: "" }
    val userRole = remember { runBlocking { tokenManager.userRole.first() } ?: "cajero" }

    val availableScreens = AppScreen.entries.filter { userRole in it.roles }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Slate800,
                drawerContentColor = Color.White,
                modifier = Modifier.width(280.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(Indigo600, Pink600))
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            "ENERVIBES",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            userEmail,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            when (userRole) {
                                "superadmin" -> "Super Admin"
                                "tenant_admin" -> "Administrador"
                                "cajero" -> "Cajero"
                                else -> userRole
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Navigation items
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    availableScreens.forEach { screen ->
                        val isSelected = currentScreen == screen
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    screen.icon,
                                    null,
                                    tint = if (isSelected) Indigo400 else Slate400
                                )
                            },
                            label = {
                                Text(
                                    screen.title,
                                    color = if (isSelected) Color.White else Slate300
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                onScreenChange(screen)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 2.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Indigo500.copy(alpha = 0.2f),
                                unselectedContainerColor = Color.Transparent
                            )
                        )
                    }
                }

                // Logout
                HorizontalDivider(color = GlassBorder)
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Red400) },
                    label = { Text("Cerrar Sesión", color = Red400) },
                    selected = false,
                    onClick = onLogout,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            currentScreen.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menú", tint = Slate300)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Slate900.copy(alpha = 0.95f)
                    ),
                    actions = {
                        Text(
                            userName.split(" ").first(),
                            modifier = Modifier.padding(end = 16.dp),
                            color = Slate300,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            },
            containerColor = Slate900,
            content = content
        )
    }
}
