package loic.glld.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Toutes les cotes de la maquette sont exprimées en pixels sur un écran 1920×1080.
 * Cette échelle les convertit pour l'écran réel, quelle que soit sa densité.
 */
class Echelle(private val facteur: Float, private val fontScale: Float) {
    fun dp(px: Number): Dp = (px.toFloat() * facteur).dp
    fun sp(px: Number): TextUnit = (px.toFloat() * facteur / fontScale).sp
}

val LocalEchelle = staticCompositionLocalOf { Echelle(0.5f, 1f) }

/** Taille de maquette (px 1920×1080) → dp. */
val Number.px: Dp
    @Composable @ReadOnlyComposable get() = LocalEchelle.current.dp(this)

/** Taille de police de maquette (px 1920×1080) → sp. */
val Number.pxt: TextUnit
    @Composable @ReadOnlyComposable get() = LocalEchelle.current.sp(this)

/**
 * Donne à un très grand texte la hauteur voulue en rognant les marges vides de la police
 * au-dessus et en dessous des chiffres (le texte reste centré verticalement).
 */
fun Modifier.hauteurVisuelle(hauteur: Dp): Modifier = layout { mesurable, contraintes ->
    val p = mesurable.measure(contraintes.copy(minHeight = 0, maxHeight = Constraints.Infinity))
    val h = hauteur.roundToPx()
    layout(p.width, h) { p.place(0, (h - p.height) / 2) }
}

@Composable
fun FournisseurEchelle(contenu: @Composable () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val facteur = minOf(maxWidth.value / 1920f, maxHeight.value / 1080f)
        val echelle = Echelle(facteur, LocalDensity.current.fontScale)
        CompositionLocalProvider(LocalEchelle provides echelle) { contenu() }
    }
}
