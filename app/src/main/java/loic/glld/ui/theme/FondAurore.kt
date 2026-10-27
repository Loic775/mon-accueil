package loic.glld.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Fond vivant : trois halos de lumière (cyan, violet, rose) qui dérivent très lentement,
 * une trame de points discrète et un vignettage. Animé à ~15 images/s pour ménager le Fire Stick.
 */
@Composable
fun FondAurore(anime: Boolean = true) {
    var phase by remember { mutableFloatStateOf(0.6f) }
    if (anime) {
        LaunchedEffect(Unit) {
            val debut = System.nanoTime()
            while (true) {
                // Un tour complet toutes les 90 s
                phase = (((System.nanoTime() - debut) / 1e9 / 90.0) * 2 * PI).toFloat() + 0.6f
                delay(66)
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Couleurs.Fond)
            val w = size.width
            val h = size.height
            halo(Couleurs.Cyan, 0.34f, Offset(w * (0.18f + 0.07f * sin(phase)), h * (0.12f + 0.10f * cos(phase * 0.8f))), w * 0.50f)
            halo(Couleurs.Violet, 0.38f, Offset(w * (0.86f + 0.05f * cos(phase)), h * (0.28f + 0.12f * sin(phase * 1.3f))), w * 0.55f)
            halo(Couleurs.Rose, 0.16f, Offset(w * (0.50f + 0.15f * sin(phase * 0.6f)), h * 1.08f), w * 0.45f)
        }
        // Couche statique : jamais redessinée par l'animation
        Canvas(Modifier.fillMaxSize()) {
            trame()
            drawRect(
                Brush.radialGradient(
                    0.6f to Color.Transparent,
                    1f to Color(0x80000000),
                    center = center,
                    radius = size.maxDimension * 0.62f,
                ),
            )
        }
    }
}

private fun DrawScope.halo(couleur: Color, alpha: Float, centre: Offset, rayon: Float) {
    drawCircle(
        Brush.radialGradient(
            0f to couleur.copy(alpha = alpha),
            0.45f to couleur.copy(alpha = alpha * 0.4f),
            1f to Color.Transparent,
            center = centre,
            radius = rayon,
        ),
        radius = rayon,
        center = centre,
    )
}

private fun DrawScope.trame() {
    val pas = size.width / 48f
    val points = ArrayList<Offset>()
    var y = pas / 2
    while (y < size.height) {
        var x = pas / 2
        while (x < size.width) {
            points += Offset(x, y)
            x += pas
        }
        y += pas
    }
    drawPoints(points, PointMode.Points, Color(0x14FFFFFF), strokeWidth = size.width / 960f, cap = StrokeCap.Round)
}
