package loic.glld.ui.accueil

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Text
import loic.glld.applis.AppliTv
import loic.glld.applis.AppsRepository
import loic.glld.ui.composants.CarteFocusable
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt
import kotlin.math.cos
import kotlin.math.sin

/**
 * Les 5 grosses tuiles du bas. OK = lancer l'appli ;
 * appui long sur OK (ou touche Menu ≡) = choisir une autre appli.
 */
@Composable
fun RangeeApplis(
    tuiles: List<AppliTv?>,
    onLancer: (AppliTv) -> Unit,
    onChoisir: (Int) -> Unit,
    modifier: Modifier = Modifier,
    premiereTuile: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth().height(120.px), horizontalArrangement = Arrangement.spacedBy(32.px)) {
        tuiles.forEachIndexed { index, appli ->
            CarteFocusable(
                onClick = { if (appli != null) onLancer(appli) else onChoisir(index) },
                onLongClick = { onChoisir(index) },
                rayon = 24.px,
                modifier = (if (index == 0) premiereTuile else Modifier)
                    .weight(1f)
                    .fillMaxHeight()
                    .onPreviewKeyEvent {
                        if (it.key == Key.Menu && it.type == KeyEventType.KeyUp) {
                            onChoisir(index); true
                        } else false
                    },
            ) {
                ContenuTuile(appli)
            }
        }
    }
}

@Composable
private fun ContenuTuile(appli: AppliTv?) {
    Row(Modifier.fillMaxSize().padding(14.px), verticalAlignment = Alignment.CenterVertically) {
        when {
            appli == null -> Text(
                "+  Choisir une appli",
                Modifier.fillMaxWidth(),
                fontSize = 26.pxt, fontWeight = FontWeight.Medium,
                color = LocalContentColor.current.copy(alpha = .7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            else -> {
                val image = appli.image
                when {
                    appli.paquet == AppsRepository.PARAMETRES -> Engrenage(Modifier.padding(horizontal = 10.px).size(68.px))
                    image != null && appli.estBanniere -> Image(
                        image, null,
                        Modifier.width(144.px).height(81.px).clip(RoundedCornerShape(12.px)),
                        contentScale = ContentScale.Crop,
                    )
                    image != null -> Image(image, null, Modifier.size(84.px), contentScale = ContentScale.Fit)
                    else -> Spacer(Modifier.size(84.px))
                }
                Spacer(Modifier.width(16.px))
                Text(
                    appli.nom,
                    fontSize = 26.pxt, fontWeight = FontWeight.SemiBold,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    lineHeight = 32.pxt,
                )
            }
        }
    }
}

/** Icône de la tuile « Paramètres ». */
@Composable
fun Engrenage(modifier: Modifier = Modifier) {
    val couleur = LocalContentColor.current
    Box(modifier) {
        Canvas(Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2, size.height / 2)
            val r = size.minDimension * .26f
            drawCircle(couleur, r, c, style = Stroke(width = size.minDimension * .1f))
            for (i in 0 until 8) {
                val a = Math.PI / 4 * i
                drawLine(
                    couleur,
                    Offset(c.x + r * 1.2f * cos(a).toFloat(), c.y + r * 1.2f * sin(a).toFloat()),
                    Offset(c.x + r * 1.65f * cos(a).toFloat(), c.y + r * 1.65f * sin(a).toFloat()),
                    size.minDimension * .12f, StrokeCap.Round,
                )
            }
        }
    }
}
