package loic.glld.ui.accueil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import loic.glld.ui.theme.CHIFFRES_FIXES
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.SpaceGrotesk
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val PARIS: ZoneId = ZoneId.of("Europe/Paris")
private val FORMAT_HEURE = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH)
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

@Composable
fun BlocHorloge(maintenant: ZonedDateTime, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text("Bonjour Loïc", fontSize = 36.pxt, color = Couleurs.TexteSecondaire, fontWeight = FontWeight.Medium)
        Text(
            maintenant.format(FORMAT_HEURE),
            // Compense l'espace vide au-dessus des chiffres
            modifier = Modifier.offset(x = (-8).px),
            style = TextStyle(
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Medium,
                fontSize = 230.pxt,
                lineHeight = 230.pxt,
                letterSpacing = (-6).pxt,
                fontFeatureSettings = CHIFFRES_FIXES,
                color = Couleurs.Texte,
                lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.Both),
            ),
        )
        Text(
            maintenant.format(FORMAT_DATE),
            fontSize = 48.pxt,
            fontWeight = FontWeight.SemiBold,
            color = Couleurs.Accent,
        )
    }
}
