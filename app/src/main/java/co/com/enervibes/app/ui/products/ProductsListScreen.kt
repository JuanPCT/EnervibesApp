package co.com.enervibes.app.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.com.enervibes.app.data.api.ApiConfig
import co.com.enervibes.app.data.model.ProductModel
import co.com.enervibes.app.ui.components.GlassCard
import co.com.enervibes.app.ui.components.GradientButton
import co.com.enervibes.app.ui.theme.*
import java.text.NumberFormat
import co.com.enervibes.app.util.Constants

@Composable
fun ProductsListScreen(viewModel: ProductsViewModel) {
    val state by viewModel.uiState.collectAsState()
    val numberFormat = remember { NumberFormat.getNumberInstance(Constants.ES_CO_LOCALE) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Productos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                GradientButton(
                    text = "Nuevo Producto",
                    onClick = viewModel::openCreate,
                    modifier = Modifier.height(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::onSearchChange,
                    placeholder = { Text("Buscar producto...") },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Slate400) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Slate100,
                        cursorColor = Indigo500,
                        focusedBorderColor = Indigo500,
                        unfocusedBorderColor = Slate600,
                        focusedContainerColor = Slate800,
                        unfocusedContainerColor = Slate800
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = viewModel::search) {
                    Icon(Icons.Default.Search, "Buscar", tint = Indigo400)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.products) { product ->
                    ProductListItem(
                        product = product,
                        numberFormat = numberFormat,
                        onEdit = { viewModel.openEdit(product) },
                        onDelete = { viewModel.deleteProduct(product) }
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
    }

    if (state.showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = viewModel::cancelDelete,
            containerColor = Slate800,
            title = { Text("Eliminar Producto", color = Color.White) },
            text = { Text("¿Estás seguro de eliminar \"${state.productToDelete?.name}\"?", color = Slate300) },
            confirmButton = {
                TextButton(onClick = viewModel::confirmDelete) {
                    Text("Eliminar", color = Red400)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelDelete) {
                    Text("Cancelar", color = Slate400)
                }
            }
        )
    }
}

@Composable
fun ProductListItem(
    product: ProductModel,
    numberFormat: NumberFormat,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = product.imageUrl?.let {
                if (it.startsWith("http")) it else "${ApiConfig.BASE_URL}uploads/${it}"
            }
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(56.dp)
                        .background(Slate700, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Slate700, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Inventory, null, tint = Slate500, modifier = Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        product.name,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (product.esProductoPreparado == true) {
                        Text("PREPARADO", color = Pink400, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
                Text(
                    "${product.Category?.name ?: ""} ${product.Brand?.name?.let { "• $it" } ?: ""}",
                    color = Slate400,
                    style = MaterialTheme.typography.bodySmall
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        "Precio: $${numberFormat.format(product.price)}",
                        color = Green400,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        "Costo: $${numberFormat.format(product.cost)}",
                        color = Slate400,
                        style = MaterialTheme.typography.labelMedium
                    )
                    val margin = if (product.price > 0) ((product.price - product.cost) / product.price) * 100 else 0.0
                    Text(
                        "${margin.toInt()}%",
                        color = when {
                            margin > 30 -> Green400
                            margin >= 0 -> Yellow400
                            else -> Red400
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                product.Stock?.firstOrNull()?.let { stock ->
                    Text(
                        "Stock: ${stock.quantity.toInt()}",
                        color = if (product.stockMinimo != null && stock.quantity <= product.stockMinimo) Red400 else Slate400,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Column {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, "Editar", tint = Indigo400, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, "Eliminar", tint = Red400, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
