package loic.glld.ui.accueil

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import loic.glld.ui.theme.CHIFFRES_FIXES
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.Degrades
import loic.glld.ui.theme.SpaceGrotesk
import loic.glld.ui.theme.hauteurVisuelle
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val PARIS: ZoneId = ZoneId.of("Europe/Paris")
private val FORMAT_HEURE = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH)
private val FORMAT_SECONDES = DateTimeFormatter.ofPattern("ss", Locale.FRENCH)
private val FORMAT_DATE = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.FRENCH)

/** L'heure de Paris, mise à jour chaque seconde. */
@Composable
fun rememberMaintenant(): ZonedDateTime {
    var maintenant by remember { mutableStateOf(ZonedDateTime.now(PARIS)) }
    LaunchedEffect(Unit) {
        while (true) {
            maintenant = ZonedDateTime.now(PARIS)
            delay(1000L - System.currentTimeMillis() % 1000L)
        }
    }
    return maintenant
}

private val LARGEUR_HORLOGE = 900

@Composable
fun BlocHorloge(maintenant: ZonedDateTime, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PointVivant()
            Spacer(Modifier.width(16.px))
            Text("Bonjour Loïc", fontSize = 36.pxt, color = Couleurs.TexteSecondaire, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(40.px))
        Row {
            Text(
                maintenant.format(FORMAT_HEURE),
                modifier = Modifier.offset(x = (-10).px).hauteurVisuelle(250.px),
                style = TextStyle(
                    brush = Degrades.Chiffres,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Light,
                    fontSize = 300.pxt,
                    letterSpacing = (-12).pxt,
                    fontFeatureSettings = CHIFFRES_FIXES,
                ),
            )
            Text(
                maintenant.format(FORMAT_SECONDES),
                modifier = Modifier.padding(start = 14.px).hauteurVisuelle(56.px),
                style = TextStyle(
                    brush = Degrades.Accent,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Medium,
                    fontSize = 60.pxt,
                    fontFeatureSettings = CHIFFRES_FIXES,
                ),
            )
        }
        Spacer(Modifier.height(44.px))
        BarreSecondes(maintenant.second, Modifier.width(LARGEUR_HORLOGE.px).height(24.px))
        Spacer(Modifier.height(30.px))
        Text(
            maintenant.format(FORMAT_DATE).uppercase(Locale.FRENCH),
            style = TextStyle(
                brush = Degrades.AccentHorizontal,
                fontSize = 40.pxt,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 8.pxt,
            ),
        )
    }
}

/** Petit point cyan qui « respire » : l'écran est vivant. */
@Composable
private fun PointVivant() {
    val transition = rememberInfiniteTransition(label = "pouls")
    val pouls by transition.animateFloat(
        0.35f, 1f,
        infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Reverse),
        label = "pouls",
    )
    Box(contentAlignment = Alignment.Center) {
        Box(
            Modifier.size(30.px)
                .graphicsLayer { alpha = pouls * .35f; scaleX = .6f + pouls * .4f; scaleY = .6f + pouls * .4f }
                .background(Couleurs.Cyan, CircleShape),
        )
        Box(Modifier.size(12.px).background(Couleurs.Cyan, CircleShape))
    }
}

/** Fine ligne qui se remplit au fil de la minute, avec une tête lumineuse. */
@Composable
private fun BarreSecondes(seconde: Int, modifier: Modifier = Modifier) {
    val avance = remember { Animatable(seconde / 60f) }
    LaunchedEffect(seconde) {
        if (seconde == 0) avance.snapTo(0f)
        avance.animateTo((seconde + 1) / 60f, tween(1000, easing = LinearEasing))
    }
    Canvas(modifier) {
        val h = size.height
        val epaisseur = h / 6f
        val y = h / 2
        drawRoundRect(
            Color(0x1AFFFFFF),
            topLeft = Offset(0f, y - epaisseur / 2),
            size = Size(size.width, epaisseur),
            cornerRadius = CornerRadius(epaisseur),
        )
        val x = size.width * avance.value
        if (x > 0f) {
            drawRoundRect(
                Brush.horizontalGradient(listOf(Couleurs.Cyan.copy(alpha = .2f), Couleurs.Cyan, Couleurs.Violet), endX = size.width),
                topLeft = Offset(0f, y - epaisseur / 2),
                size = Size(x, epaisseur),
                cornerRadius = CornerRadius(epaisseur),
            )
            drawCircle(Couleurs.Violet.copy(alpha = .25f), h / 2, Offset(x, y))
            drawCircle(Color.White, epaisseur * 0.9f, Offset(x, y))
        }
    }
}
