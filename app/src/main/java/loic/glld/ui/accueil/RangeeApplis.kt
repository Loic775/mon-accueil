package loic.glld.ui.accueil

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.tv.material3.Text
import loic.glld.applis.AppliTv
import loic.glld.applis.AppsRepository
import loic.glld.ui.composants.ElementFocusable
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.Degrades
import loic.glld.ui.theme.SpaceGrotesk
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt
import kotlin.math.cos
import kotlin.math.sin

/** Hauteur des tuiles : format 16:9 des bannières Fire TV. */
private val HAUTEUR_TUILE = 182

/**
 * Le dock : 5 grandes tuiles. OK = lancer l'appli ;
 * appui long sur OK (ou touche Menu ≡) = choisir une autre appli.
 */
@Composable
fun RangeeApplis(
    tuiles: List<AppliTv?>,
    onLancer: (AppliTv) -> Unit,
    onChoisir: (Int) -> Unit,
    modifier: Modifier = Modifier,
    premiereTuile: Modifier = Modifier,
    tuileFocaliseeApercu: Int? = null,
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(36.px)) {
        tuiles.forEachIndexed { index, appli ->
            Tuile(
                appli = appli,
                onClick = { if (appli != null) onLancer(appli) else onChoisir(index) },
                onChoisir = { onChoisir(index) },
                modifier = Modifier.weight(1f),
                modifierTuile = if (index == 0) premiereTuile else Modifier,
                forcerFocus = tuileFocaliseeApercu == index,
            )
        }
    }
}

@Composable
private fun Tuile(
    appli: AppliTv?,
    onClick: () -> Unit,
    onChoisir: () -> Unit,
    modifier: Modifier,
    modifierTuile: Modifier,
    forcerFocus: Boolean,
) {
    val interaction = remember { MutableInteractionSource() }
    val focalise = interaction.collectIsFocusedAsState().value || forcerFocus
    val visibiliteNom by animateFloatAsState(if (focalise) 1f else 0f, tween(220), label = "nom")

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ElementFocusable(
            onClick = onClick,
            onLongClick = onChoisir,
            interactionSource = interaction,
            apercuFocus = forcerFocus,
            rayon = 26.px,
            echelleFocus = 1.09f,
            modifier = modifierTuile
                .fillMaxWidth()
                .height(HAUTEUR_TUILE.px)
                .onPreviewKeyEvent {
                    if (it.key == Key.Menu && it.type == KeyEventType.KeyUp) {
                        onChoisir(); true
                    } else false
                },
        ) {
            ContenuTuile(appli, focalise)
        }
        Spacer(Modifier.height(26.px))
        // Nom de l'appli : n'apparaît que sous la tuile sélectionnée
        Text(
            appli?.nom ?: "",
            Modifier.graphicsLayer { alpha = visibiliteNom; translationY = (1f - visibiliteNom) * -12f },
            fontSize = 26.pxt,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.pxt,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Couleurs.Texte,
        )
    }
}

@Composable
private fun ContenuTuile(appli: AppliTv?, focalise: Boolean) {
    val image = appli?.image
    val forme = RoundedCornerShape(26.px)
    when {
        appli == null -> Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "+",
                style = TextStyle(
                    brush = if (focalise) Degrades.Accent else Brush.linearGradient(listOf(Couleurs.TexteSecondaire, Couleurs.TexteSecondaire)),
                    fontFamily = SpaceGrotesk, fontWeight = FontWeight.Light, fontSize = 72.pxt, lineHeight = 72.pxt,
                ),
            )
            Text("Ajouter une appli", fontSize = 22.pxt, color = Couleurs.TexteSecondaire, letterSpacing = 1.pxt)
        }

        appli.paquet == AppsRepository.PARAMETRES -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Engrenage(focalise, Modifier.size(96.px))
        }

        image != null && appli.estBanniere -> Box(Modifier.fillMaxSize().clip(forme)) {
            Image(
                image, null,
                Modifier.fillMaxSize().graphicsLayer { alpha = if (focalise) 1f else 0.62f },
                contentScale = ContentScale.Crop,
            )
            // Reflet de verre en haut de la bannière
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color(0x26FFFFFF), Color.Transparent, Color(0x33000000))),
                ),
            )
        }

        else -> Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (image != null) {
                Image(image, null, Modifier.size(92.px).graphicsLayer { alpha = if (focalise) 1f else 0.75f })
            } else {
                Box(
                    Modifier.size(84.px).background(Degrades.Accent, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(appli.nom.take(1).uppercase(), fontSize = 40.pxt, fontWeight = FontWeight.SemiBold, color = Couleurs.Fond)
                }
            }
            Spacer(Modifier.height(10.px))
            Text(
                appli.nom, fontSize = 22.pxt, fontWeight = FontWeight.Medium,
                maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center,
                color = Couleurs.TexteSecondaire,
            )
        }
    }
}

/** Icône « Paramètres » : engrenage en traits lumineux. */
@Composable
fun Engrenage(focalise: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val c = Offset(size.width / 2, size.height / 2)
        val r = size.minDimension * .2f
        val t = size.minDimension * .055f
        val chemin = Path().apply {
            addOval(androidx.compose.ui.geometry.Rect(c, r))
            addOval(androidx.compose.ui.geometry.Rect(c, r * .38f))
            for (i in 0 until 8) {
                val a = Math.PI / 4 * i
                moveTo(c.x + r * 1.25f * cos(a).toFloat(), c.y + r * 1.25f * sin(a).toFloat())
                lineTo(c.x + r * 1.75f * cos(a).toFloat(), c.y + r * 1.75f * sin(a).toFloat())
            }
        }
        val pinceau = if (focalise) {
            Brush.linearGradient(listOf(Couleurs.Cyan, Couleurs.Violet), start = Offset.Zero, end = Offset(size.width, size.height))
        } else {
            Brush.linearGradient(listOf(Couleurs.TexteSecondaire, Couleurs.TexteSecondaire))
        }
        drawPath(chemin, pinceau, alpha = .2f, style = Stroke(t * 3f, cap = StrokeCap.Round))
        drawPath(chemin, pinceau, style = Stroke(t, cap = StrokeCap.Round))
    }
}
