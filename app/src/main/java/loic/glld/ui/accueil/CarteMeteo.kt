package loic.glld.ui.accueil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.tv.material3.Text
import loic.glld.meteo.CodesMeteo
import loic.glld.meteo.Meteo
import loic.glld.meteo.Prevision
import loic.glld.ui.composants.Carte
import loic.glld.ui.theme.CHIFFRES_FIXES
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.SpaceGrotesk
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt
import java.time.Instant
import java.time.format.DateTimeFormatter

private val FORMAT_MAJ = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun CarteMeteo(etat: EtatMeteo, modifier: Modifier = Modifier) {
    Carte(modifier.width(560.px)) {
        Column(Modifier.padding(36.px)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Météo · Chelles", fontSize = 28.pxt, color = Couleurs.TexteSecondaire, fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                etat.meteo?.let {
                    val heure = Instant.ofEpochMilli(it.majLe).atZone(PARIS).format(FORMAT_MAJ)
                    Text(
                        if (etat.horsLigne) "Hors ligne · $heure" else "Mise à jour $heure",
                        fontSize = 20.pxt,
                        color = if (etat.horsLigne) Couleurs.Accent else Couleurs.TexteSecondaire,
                    )
                }
            }
            val meteo = etat.meteo
            if (meteo == null) {
                Text(
                    if (etat.horsLigne) "Météo indisponible : pas de connexion" else "Chargement de la météo…",
                    Modifier.padding(vertical = 60.px),
                    fontSize = 30.pxt,
                    color = Couleurs.TexteSecondaire,
                )
            } else {
                MeteoActuelle(meteo)
                Spacer(Modifier.height(24.px))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.px)) {
                    meteo.previsions.forEach { MiniCase(it, Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun MeteoActuelle(meteo: Meteo) {
    Row(Modifier.padding(top = 12.px), verticalAlignment = Alignment.CenterVertically) {
        DessinMeteo(CodesMeteo.icone(meteo.code, meteo.jour), Modifier.size(130.px))
        Spacer(Modifier.width(24.px))
        Column {
            Text(
                "${meteo.temperature}°",
                style = TextStyle(
                    fontFamily = SpaceGrotesk, fontWeight = FontWeight.Medium,
                    fontSize = 104.pxt, lineHeight = 110.pxt,
                    fontFeatureSettings = CHIFFRES_FIXES, color = Couleurs.Texte,
                ),
            )
            Text(
                CodesMeteo.description(meteo.code),
                fontSize = 30.pxt, color = Couleurs.Texte,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MiniCase(prevision: Prevision, modifier: Modifier = Modifier) {
    Column(
        modifier
            .background(Couleurs.Fond, RoundedCornerShape(18.px))
            .padding(vertical = 14.px),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(prevision.libelle, fontSize = 22.pxt, color = Couleurs.TexteSecondaire, fontWeight = FontWeight.Medium)
        DessinMeteo(CodesMeteo.icone(prevision.code, prevision.jour), Modifier.size(56.px))
        val texte = prevision.temperatureMin?.let { "${prevision.temperature}°/$it°" } ?: "${prevision.temperature}°"
        Text(
            texte,
            style = TextStyle(
                fontFamily = SpaceGrotesk, fontWeight = FontWeight.Medium,
                fontSize = if (prevision.temperatureMin != null) 23.pxt else 28.pxt,
                fontFeatureSettings = CHIFFRES_FIXES, color = Couleurs.Texte,
            ),
            maxLines = 1,
        )
    }
}
