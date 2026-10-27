package loic.glld.ui.accueil

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import loic.glld.meteo.IconeMeteo
import loic.glld.ui.theme.Couleurs
import kotlin.math.cos
import kotlin.math.sin

/** Icônes météo en traits lumineux (style néon), dessinées directement : aucune image embarquée. */
@Composable
fun DessinMeteo(icone: IconeMeteo, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.minDimension
        val t = w * 0.05f
        when (icone) {
            IconeMeteo.SOLEIL -> soleil(Offset(w * .5f, w * .5f), w * .19f, t)
            IconeMeteo.LUNE -> lune(Offset(w * .5f, w * .5f), w * .27f, t)
            IconeMeteo.ECLAIRCIES_JOUR -> {
                soleil(Offset(w * .36f, w * .34f), w * .13f, t * .85f)
                nuage(w, t, dy = w * .08f, dx = w * .05f)
            }
            IconeMeteo.ECLAIRCIES_NUIT -> {
                lune(Offset(w * .36f, w * .32f), w * .17f, t * .85f)
                nuage(w, t, dy = w * .08f, dx = w * .05f)
            }
            IconeMeteo.NUAGES -> nuage(w, t)
            IconeMeteo.BROUILLARD -> {
                nuage(w, t, dy = -w * .1f)
                val p = Path()
                for (i in 0..1) {
                    val y = w * (.78f + i * .1f)
                    p.moveTo(w * (.2f + i * .08f), y); p.lineTo(w * (.8f - i * .08f), y)
                }
                neon(p, Couleurs.Nuage, t)
            }
            IconeMeteo.BRUINE -> {
                nuage(w, t, dy = -w * .1f)
                gouttes(w, t, longueur = .05f)
            }
            IconeMeteo.PLUIE -> {
                nuage(w, t, dy = -w * .1f)
                gouttes(w, t, longueur = .12f)
            }
            IconeMeteo.NEIGE -> {
                nuage(w, t, dy = -w * .1f)
                val p = Path()
                for (i in 0..2) {
                    val c = Offset(w * (.33f + i * .17f), w * (.8f + (i % 2) * .08f))
                    val r = w * .035f
                    for (k in 0..2) {
                        val a = Math.PI / 3 * k
                        p.moveTo(c.x - r * cos(a).toFloat(), c.y - r * sin(a).toFloat())
                        p.lineTo(c.x + r * cos(a).toFloat(), c.y + r * sin(a).toFloat())
                    }
                }
                neon(p, Color.White, t * .7f)
            }
            IconeMeteo.ORAGE -> {
                nuage(w, t, dy = -w * .1f)
                val eclair = Path().apply {
                    moveTo(w * .55f, w * .62f); lineTo(w * .43f, w * .8f); lineTo(w * .53f, w * .8f)
                    lineTo(w * .46f, w * .96f); lineTo(w * .63f, w * .74f); lineTo(w * .53f, w * .74f); close()
                }
                drawPath(eclair, Couleurs.Ambre.copy(alpha = .25f), style = Stroke(t * 3f, join = StrokeJoin.Round))
                drawPath(eclair, Couleurs.Ambre)
            }
        }
    }
}

/** Trait lumineux : un halo large et transparent, puis le trait net. */
private fun DrawScope.neon(path: Path, couleur: Color, t: Float) {
    drawPath(path, couleur.copy(alpha = .14f), style = Stroke(t * 3.6f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(path, couleur.copy(alpha = .22f), style = Stroke(t * 2f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawPath(path, couleur, style = Stroke(t, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.soleil(c: Offset, r: Float, t: Float) {
    val p = Path().apply { addOval(Rect(c, r)) }
    for (i in 0 until 8) {
        val a = Math.PI / 4 * i
        p.moveTo(c.x + r * 1.45f * cos(a).toFloat(), c.y + r * 1.45f * sin(a).toFloat())
        p.lineTo(c.x + r * 1.8f * cos(a).toFloat(), c.y + r * 1.8f * sin(a).toFloat())
    }
    neon(p, Couleurs.Ambre, t)
}

private fun DrawScope.lune(c: Offset, r: Float, t: Float) {
    val disque = Path().apply { addOval(Rect(c, r)) }
    val ombre = Path().apply { addOval(Rect(Offset(c.x + r * .55f, c.y - r * .38f), r * .9f)) }
    neon(Path.combine(PathOperation.Difference, disque, ombre), Couleurs.Lune, t)
}

private fun DrawScope.nuage(w: Float, t: Float, dy: Float = 0f, dx: Float = 0f) {
    fun cercle(x: Float, y: Float, r: Float) = Path().apply { addOval(Rect(Offset(w * x + dx, w * y + dy), w * r)) }
    var forme = Path.combine(PathOperation.Union, cercle(.5f, .47f, .17f), cercle(.33f, .58f, .12f))
    forme = Path.combine(PathOperation.Union, forme, cercle(.67f, .56f, .13f))
    forme = Path.combine(
        PathOperation.Union, forme,
        Path().apply {
            addRoundRect(RoundRect(Rect(w * .21f + dx, w * .56f + dy, w * .8f + dx, w * .7f + dy), CornerRadius(w * .07f)))
        },
    )
    // Masque ce qui est derrière (soleil, lune), puis le contour lumineux
    drawPath(forme, Color(0xFF0B1020))
    neon(forme, Couleurs.Nuage, t)
}

private fun DrawScope.gouttes(w: Float, t: Float, longueur: Float) {
    val p = Path()
    for (i in 0..2) {
        val x = w * (.37f + i * .14f)
        val y = w * .78f
        p.moveTo(x, y); p.lineTo(x - w * .04f, y + w * longueur)
    }
    neon(p, Couleurs.Pluie, t)
}
