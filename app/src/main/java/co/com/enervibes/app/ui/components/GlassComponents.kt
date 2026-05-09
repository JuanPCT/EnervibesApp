package co.com.enervibes.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.com.enervibes.app.ui.theme.GlassBackground
import co.com.enervibes.app.ui.theme.GlassBorder

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderColor: Color = GlassBorder,
    backgroundColor: Color = GlassBackground,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius)),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        content = { Column(modifier = Modifier.fillMaxWidth()) { content() } }
    )
}

@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    startColor: Color = co.com.enervibes.app.ui.theme.Indigo500,
    endColor: Color = co.com.enervibes.app.ui.theme.Pink500,
) {
    val brush = Brush.linearGradient(
        colors = if (enabled) listOf(startColor, endColor) else listOf(Color.Gray, Color.Gray),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush, RoundedCornerShape(12.dp)),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = text,
                color = Color.White,
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(GlassBackground)
            .border(1.dp, GlassBorder, RoundedCornerShape(cornerRadius)),
        content = content
    )
}

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    startColor: Color = co.com.enervibes.app.ui.theme.Indigo500,
    endColor: Color = co.com.enervibes.app.ui.theme.Pink500,
    style: androidx.compose.ui.text.TextStyle = androidx.compose.material3.MaterialTheme.typography.titleLarge
) {
    val brush = Brush.linearGradient(listOf(startColor, endColor))
    androidx.compose.material3.Text(
        text = text,
        style = style.copy(brush = brush),
        modifier = modifier
    )
}
