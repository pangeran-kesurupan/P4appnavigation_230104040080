package id.antasari.p4appnavigation_230104040079.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Sesuaikan skema warna jika perlu
private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF0066CC),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    background = androidx.compose.ui.graphics.Color(0xFFF6F6F6),
    surface = androidx.compose.ui.graphics.Color.White,
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF88B4FF),
    onPrimary = androidx.compose.ui.graphics.Color.Black,
)

@Composable
fun P4appnavigation_230104040079Theme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(), // default typography
        shapes = androidx.compose.material3.Shapes(), // default shapes
        content = content
    )
}
