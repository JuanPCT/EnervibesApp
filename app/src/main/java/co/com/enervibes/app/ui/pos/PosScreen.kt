package co.com.enervibes.app.ui.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
fun PosScreen(viewModel: PosViewModel) {
    val state by viewModel.uiState.collectAsState()
    val numberFormat = remember { NumberFormat.getNumberInstance(Constants.ES_CO_LOCALE) }

    LaunchedEffect(state.saleSuccess) {
        if (state.saleSuccess != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearSaleSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Success banner
            state.saleSuccess?.let { sale ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Green600
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(sale, color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Error banner
            state.error?.let { error ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Red600
                ) {
                    Text(error, modifier = Modifier.padding(12.dp), color = Color.White)
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                // Left: Products
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(12.dp)
                ) {
                    // Search bar
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        placeholder = { Text("Buscar producto...") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = Slate400) },
                        modifier = Modifier.fillMaxWidth(),
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

                    Spacer(modifier = Modifier.height(8.dp))

                    if (state.searchMode && state.searchResults.isNotEmpty()) {
                        // Search results
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(state.searchResults) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = { viewModel.addToCart(product) }
                                )
                            }
                        }
                    } else {
                        // Top products grid
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                Text(
                                    "Productos Populares",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Slate300,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            items(state.topProducts.chunked(2)) { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    row.forEach { product ->
                                        ProductCard(
                                            product = product,
                                            onClick = { viewModel.addToCart(product) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (row.size < 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                // Vertical divider
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = GlassBorder
                )

                // Right: Cart
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                        .background(Slate800)
                ) {
                    // Cart header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(listOf(Indigo600, Pink600))
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Venta Actual",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "${viewModel.cartCount} items",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Tercero selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, null, tint = Slate400, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            state.selectedTercero?.nombre ?: "Consumidor Final",
                            color = Slate200,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        if (state.selectedTercero != null) {
                            IconButton(
                                onClick = viewModel::clearTercero,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, null, tint = Red400, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    HorizontalDivider(color = GlassBorder)

                    // Cart items
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        itemsIndexed(state.cartItems) { index, item ->
                            CartItemRow(
                                item = item,
                                index = index,
                                viewModel = viewModel,
                                numberFormat = numberFormat
                            )
                        }
                    }

                    // Cart footer
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate700)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", color = Slate300)
                            Text(
                                "$${numberFormat.format(viewModel.cartTotal)}",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        if ((state.discount.toDoubleOrNull() ?: 0.0) > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Descuento", color = Red300)
                                Text("$${numberFormat.format(state.discount.toDoubleOrNull() ?: 0.0)}", color = Red300)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "$${numberFormat.format((viewModel.cartTotal) - (state.discount.toDoubleOrNull() ?: 0.0))}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        GradientButton(
                            text = "Cobrar",
                            onClick = { viewModel.showPayment() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = viewModel.cartCount > 0
                        )
                    }
                }
            }
        }

        // Payment dialog
        if (state.showPaymentDialog) {
            PaymentDialog(state = state, viewModel = viewModel, numberFormat = numberFormat)
        }
    }
}

@Composable
fun ProductCard(
    product: ProductModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GlassBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageUrl = product.imageUrl?.let {
                if (it.startsWith("http")) it else "${ApiConfig.BASE_URL}uploads/${it}"
            }
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(60.dp)
                        .background(Slate700, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                product.name,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                "$${NumberFormat.getNumberInstance(Constants.ES_CO_LOCALE).format(product.price)}",
                style = MaterialTheme.typography.labelMedium,
                color = Green400,
                fontWeight = FontWeight.Bold
            )
            if (product.esProductoPreparado == true) {
                Text(
                    "PREPARADO",
                    style = MaterialTheme.typography.labelSmall,
                    color = Pink400,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    index: Int,
    viewModel: PosViewModel,
    numberFormat: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Slate700)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.product.name,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "$${numberFormat.format(item.unitPrice)} c/u",
                    color = Slate400,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.updateQuantity(index, -1.0) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Remove, null, tint = Slate300, modifier = Modifier.size(16.dp))
                }
                Text(
                    item.quantity.toInt().toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.widthIn(min = 24.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { viewModel.updateQuantity(index, 1.0) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Add, null, tint = Indigo400, modifier = Modifier.size(16.dp))
                }
            }

            Text(
                "$${numberFormat.format(item.subtotal)}",
                color = Green400,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDialog(
    state: PosUiState,
    viewModel: PosViewModel,
    numberFormat: NumberFormat
) {
    val discount = state.discount.toDoubleOrNull() ?: 0.0
    val total = viewModel.cartTotal - discount
    val change = if (state.paymentMethod == "cash") {
        (state.amountReceived.toDoubleOrNull() ?: 0.0) - total
    } else 0.0

    AlertDialog(
        onDismissRequest = { if (!state.isProcessing) viewModel.dismissPayment() },
        containerColor = Slate800,
        title = {
            Text("Cobrar", color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                // Payment method
                Text("Método de pago", color = Slate300, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentMethodChip("Efectivo", state.paymentMethod == "cash") { viewModel.onPaymentMethodChange("cash") }
                    PaymentMethodChip("Transferencia", state.paymentMethod == "transfer") { viewModel.onPaymentMethodChange("transfer") }
                    PaymentMethodChip("Crédito", state.paymentMethod == "credit") { viewModel.onPaymentMethodChange("credit") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total a pagar:", color = Slate300, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "$${numberFormat.format(total)}",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Discount
                OutlinedTextField(
                    value = state.discount,
                    onValueChange = viewModel::onDiscountChange,
                    label = { Text("Descuento") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Slate100,
                        cursorColor = Indigo500,
                        focusedBorderColor = Indigo500,
                        unfocusedBorderColor = Slate600
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Amount received (cash only)
                if (state.paymentMethod == "cash") {
                    OutlinedTextField(
                        value = state.amountReceived,
                        onValueChange = viewModel::onAmountReceivedChange,
                        label = { Text("Monto recibido") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Slate100,
                            cursorColor = Indigo500,
                            focusedBorderColor = Indigo500,
                            unfocusedBorderColor = Slate600
                        )
                    )

                    if (change > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Cambio: $${numberFormat.format(change)}",
                            color = Green400,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.processSale {} },
                enabled = !state.isProcessing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green600,
                    disabledContainerColor = Slate600
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Confirmar Venta")
            }
        },
        dismissButton = {
            if (!state.isProcessing) {
                TextButton(onClick = viewModel::dismissPayment) {
                    Text("Cancelar", color = Slate400)
                }
            }
        }
    )
}

@Composable
fun PaymentMethodChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) Indigo500 else Slate700,
        contentColor = Color.White
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
