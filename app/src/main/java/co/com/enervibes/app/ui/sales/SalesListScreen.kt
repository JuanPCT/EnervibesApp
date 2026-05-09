package co.com.enervibes.app.ui.sales

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.com.enervibes.app.data.model.TransactionModel
import co.com.enervibes.app.ui.components.GlassCard
import co.com.enervibes.app.ui.theme.*
import java.text.NumberFormat
import co.com.enervibes.app.util.Constants

@Composable
fun SalesListScreen(viewModel: SalesViewModel) {
    val state by viewModel.uiState.collectAsState()
    val numberFormat = remember { NumberFormat.getNumberInstance(Constants.ES_CO_LOCALE) }

    LaunchedEffect(state.success) {
        if (state.success != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Ventas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${state.totalCount} registros",
                    color = Slate400,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.success?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Green600.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = Green400)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(msg, color = Green400)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.sales) { sale ->
                    SaleListItem(
                        sale = sale,
                        numberFormat = numberFormat,
                        onClick = { viewModel.loadSaleDetail(sale.id) },
                        onMarkPaid = { viewModel.markAsPaid(sale.id) }
                    )
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Indigo500
            )
        }

        if (state.showDetail && state.selectedSale != null) {
            SaleDetailDialog(
                sale = state.selectedSale!!,
                numberFormat = numberFormat,
                onDismiss = viewModel::closeDetail,
                onDelete = { viewModel.deleteSale(state.selectedSale!!.id) },
                onMarkPaid = { viewModel.markAsPaid(state.selectedSale!!.id) }
            )
        }
    }
}

@Composable
fun SaleListItem(
    sale: TransactionModel,
    numberFormat: NumberFormat,
    onClick: () -> Unit,
    onMarkPaid: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (sale.paymentStatus == "paid") Green500 else Yellow500,
                        RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    sale.referenceNumber ?: "VTA-${sale.id}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "${sale.transactionDetails?.size ?: 0} productos",
                    color = Slate400,
                    style = MaterialTheme.typography.bodySmall
                )
                sale.Tercero?.let { tercero ->
                    Text(
                        tercero.nombre,
                        color = Slate500,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$${numberFormat.format(sale.amount)}",
                    color = if (sale.paymentStatus == "paid") Green400 else Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    sale.date?.take(10) ?: "",
                    color = Slate400,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    if (sale.paymentStatus == "paid") "Pagado" else "Pendiente",
                    color = if (sale.paymentStatus == "paid") Green400 else Yellow500,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun SaleDetailDialog(
    sale: TransactionModel,
    numberFormat: NumberFormat,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onMarkPaid: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate800,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(sale.referenceNumber ?: "Detalle", color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    if (sale.paymentStatus == "paid") "Pagado" else "Pendiente",
                    color = if (sale.paymentStatus == "paid") Green400 else Yellow500,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        text = {
            Column {
                Text("Fecha: ${sale.date?.take(10) ?: "N/A"}", color = Slate300, style = MaterialTheme.typography.bodySmall)
                sale.Tercero?.let {
                    Text("Cliente: ${it.nombre}", color = Slate300, style = MaterialTheme.typography.bodySmall)
                }
                Text("Vendedor: ${sale.User?.name ?: "N/A"}", color = Slate300, style = MaterialTheme.typography.bodySmall)
                sale.Branch?.let {
                    Text("Sucursal: ${it.name}", color = Slate300, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = GlassBorder)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Productos", color = Color.White, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                sale.transactionDetails?.forEach { detail ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                detail.Product?.name ?: "Producto ${detail.productId}",
                                color = Slate200,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "${detail.quantity} x $${numberFormat.format(detail.unitPrice)}",
                                color = Slate400,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Text(
                            "$${numberFormat.format(detail.subtotal)}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = GlassBorder)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        "$${numberFormat.format(sale.amount)}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Row {
                if (sale.paymentStatus == "pending") {
                    TextButton(onClick = onMarkPaid) {
                        Text("Marcar Pagado", color = Green400)
                    }
                }
                TextButton(onClick = onDelete) {
                    Text("Eliminar", color = Red400)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Slate400)
            }
        }
    )
}
