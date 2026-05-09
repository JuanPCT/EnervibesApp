package co.com.enervibes.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.com.enervibes.app.ui.components.GlassCard
import co.com.enervibes.app.ui.theme.*
import co.com.enervibes.app.util.Constants
import java.text.NumberFormat

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.uiState.collectAsState()
    val numberFormat = remember { NumberFormat.getNumberInstance(Constants.ES_CO_LOCALE) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Indigo500
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(Indigo500, Pink500)),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            "Bienvenido",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Resumen del día",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Ventas Hoy",
                        value = "$${numberFormat.format(state.data?.todaySales?.total ?: 0)}",
                        subtitle = "${state.data?.todaySales?.count ?: 0} transacciones",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        color = Green500
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Gastos Hoy",
                        value = "$${numberFormat.format(state.data?.todayExpenses ?: 0)}",
                        icon = Icons.AutoMirrored.Filled.TrendingDown,
                        color = Red500
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Neto",
                        value = "$${numberFormat.format(state.data?.netIncome ?: 0)}",
                        icon = Icons.Default.AccountBalance,
                        color = Indigo500
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                GlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Resumen Mensual",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Ventas", color = Slate400, style = MaterialTheme.typography.bodySmall)
                                Text(
                                    "$${numberFormat.format(state.data?.monthlySummary?.sales ?: 0)}",
                                    color = Green500,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Gastos", color = Slate400, style = MaterialTheme.typography.bodySmall)
                                Text(
                                    "$${numberFormat.format(state.data?.monthlySummary?.expenses ?: 0)}",
                                    color = Red500,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                state.data?.lowStock?.let { lowStock ->
                    if (lowStock.isNotEmpty()) {
                        GlassCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Stock Bajo",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Yellow500,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                lowStock.take(5).forEach { product ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(product.name, color = Slate200, style = MaterialTheme.typography.bodyMedium)
                                        Text(
                                            "Stock: ${product.Stock?.firstOrNull()?.quantity?.toInt() ?: 0}",
                                            color = Red400,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    HorizontalDivider(color = GlassBorder)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                state.data?.topProducts?.let { top ->
                    if (top.isNotEmpty()) {
                        GlassCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Productos Más Vendidos",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                top.take(5).forEach { tp ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            tp.product?.name ?: tp.name ?: "Producto",
                                            color = Slate200,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            "$${numberFormat.format(tp.totalAmount ?: 0)}",
                                            color = Green400,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    HorizontalDivider(color = GlassBorder)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector,
    color: Color
) {
    GlassCard(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(title, color = Slate400, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            subtitle?.let {
                Text(it, color = Slate400, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
