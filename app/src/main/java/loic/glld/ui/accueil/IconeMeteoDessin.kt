package loic.glld.ui.accueil

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import loic.glld.meteo.IconeMeteo
import loic.glld.ui.theme.Couleurs
import kotlin.math.cos
import kotlin.math.sin

/** Icônes météo dessinées directement (aucune image à embarquer). */
@Composable
fun DessinMeteo(icone: IconeMeteo, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.minDimension
        when (icone) {
            IconeMeteo.SOLEIL -> soleil(Offset(w * .5f, w * .5f), w * .2f)
            IconeMeteo.LUNE -> lune(Offset(w * .5f, w * .5f), w * .28f)
            IconeMeteo.ECLAIRCIES_JOUR -> {
                soleil(Offset(w * .36f, w * .36f), w * .15f)
                nuage(w, dy = w * .06f)
            }
            IconeMeteo.ECLAIRCIES_NUIT -> {
                lune(Offset(w * .36f, w * .34f), w * .2f)
                nuage(w, dy = w * .06f)
            }
            IconeMeteo.NUAGES -> nuage(w)
            IconeMeteo.BROUILLARD -> {
                nuage(w, dy = -w * .08f)
                for (i in 0..2) {
                    val y = w * (.72f + i * .1f)
                    drawLine(Couleurs.Nuage.copy(alpha = .7f), Offset(w * (.18f + i * .04f), y), Offset(w * (.82f - i * .04f), y), w * .045f, StrokeCap.Round)
                }
            }
            IconeMeteo.BRUINE -> {
                nuage(w, dy = -w * .1f)
                gouttes(w, Couleurs.Pluie, longueur = .06f)
            }
            IconeMeteo.PLUIE -> {
                nuage(w, dy = -w * .1f)
                gouttes(w, Couleurs.Pluie, longueur = .13f)
            }
            IconeMeteo.NEIGE -> {
                nuage(w, dy = -w * .1f)
                for (i in 0..2) drawCircle(Color.White, w * .04f, Offset(w * (.32f + i * .18f), w * (.8f + (i % 2) * .07f)))
            }
            IconeMeteo.ORAGE -> {
                nuage(w, dy = -w * .1f)
                val eclair = Path().apply {
                    moveTo(w * .54f, w * .6f); lineTo(w * .4f, w * .8f); lineTo(w * .5f, w * .8f)
                    lineTo(w * .44f, w * .96f); lineTo(w * .62f, w * .73f); lineTo(w * .52f, w * .73f); close()
                }
                drawPath(eclair, Couleurs.Accent)
            }
        }
    }
}

private fun DrawScope.soleil(centre: Offset, rayon: Float) {
    drawCircle(Couleurs.Accent, rayon, centre)
    for (i in 0 until 8) {
        val a = Math.PI / 4 * i
        val d1 = rayon * 1.35f
        val d2 = rayon * 1.75f
        drawLine(
            Couleurs.Accent,
            Offset(centre.x + d1 * cos(a).toFloat(), centre.y + d1 * sin(a).toFloat()),
            Offset(centre.x + d2 * cos(a).toFloat(), centre.y + d2 * sin(a).toFloat()),
            rayon * .28f, StrokeCap.Round,
        )
    }
}

private fun DrawScope.lune(centre: Offset, rayon: Float) {
    val croissant = Path().apply {
        addOval(androidx.compose.ui.geometry.Rect(centre, rayon))
    }
    val ombre = Path().apply {
        addOval(androidx.compose.ui.geometry.Rect(Offset(centre.x + rayon * .55f, centre.y - rayon * .4f), rayon * .95f))
    }
    drawPath(Path.combine(androidx.compose.ui.graphics.PathOperation.Difference, croissant, ombre), Color(0xFFF6E3B4))
}

private fun DrawScope.nuage(w: Float, dy: Float = 0f) {
    val c = Couleurs.Nuage
    drawCircle(c, w * .17f, Offset(w * .52f, w * .48f + dy))
    drawCircle(c, w * .12f, Offset(w * .34f, w * .58f + dy))
    drawCircle(c, w * .13f, Offset(w * .68f, w * .58f + dy))
    drawRoundRect(c, Offset(w * .22f, w * .56f + dy), Size(w * .58f, w * .14f), CornerRadius(w * .07f))
}

private fun DrawScope.gouttes(w: Float, couleur: Color, longueur: Float) {
    for (i in 0..2) {
        val x = w * (.36f + i * .14f)
        val y = w * .76f
        drawLine(couleur, Offset(x, y), Offset(x - w * .04f, y + w * longueur), w * .045f, StrokeCap.Round)
    }
}
