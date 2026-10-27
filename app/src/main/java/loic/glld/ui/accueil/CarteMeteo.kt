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
import loic.glld.ui.composants.verre
import loic.glld.ui.theme.CHIFFRES_FIXES
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.Degrades
import loic.glld.ui.theme.SpaceGrotesk
import loic.glld.ui.theme.hauteurVisuelle
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt
import java.time.Instant
import java.time.format.DateTimeFormatter

private val FORMAT_MAJ = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun CarteMeteo(etat: EtatMeteo, modifier: Modifier = Modifier) {
    Column(modifier.width(640.px).verre().padding(horizontal = 44.px, vertical = 36.px)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "CHELLES",
                fontSize = 22.pxt,
                letterSpacing = 6.pxt,
                fontWeight = FontWeight.SemiBold,
                color = Couleurs.TexteSecondaire,
            )
            Spacer(Modifier.weight(1f))
            etat.meteo?.let {
                val heure = Instant.ofEpochMilli(it.majLe).atZone(PARIS).format(FORMAT_MAJ)
                Text(
                    if (etat.horsLigne) "HORS LIGNE · $heure" else "MAJ $heure",
                    fontSize = 20.pxt,
                    letterSpacing = 2.pxt,
                    fontWeight = FontWeight.Medium,
                    color = if (etat.horsLigne) Couleurs.Ambre else Couleurs.TexteDiscret,
                )
            }
        }
        val meteo = etat.meteo
        if (meteo == null) {
            Text(
                if (etat.horsLigne) "Météo indisponible : pas de connexion" else "Chargement de la météo…",
                Modifier.padding(vertical = 120.px),
                fontSize = 30.pxt,
                color = Couleurs.TexteSecondaire,
            )
        } else {
            MeteoActuelle(meteo)
            Separateur(haut = 26, bas = 20)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Statistique("Ressenti", "${meteo.ressenti}°")
                if (meteo.maxJour != null && meteo.minJour != null) {
                    Statistique("Max", "${meteo.maxJour}°")
                    Statistique("Min", "${meteo.minJour}°")
                }
                meteo.lever?.let { Statistique("Lever", it) }
                meteo.coucher?.let { Statistique("Coucher", it) }
            }
            Separateur(haut = 20, bas = 22)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                meteo.previsions.forEach { ColonnePrevision(it) }
            }
        }
    }
}

@Composable
private fun Separateur(haut: Int, bas: Int) {
    Spacer(Modifier.height(haut.px))
    Spacer(Modifier.fillMaxWidth().height(1.5.px).background(Degrades.Ligne))
    Spacer(Modifier.height(bas.px))
}

@Composable
private fun MeteoActuelle(meteo: Meteo) {
    Row(Modifier.padding(top = 14.px), verticalAlignment = Alignment.CenterVertically) {
        DessinMeteo(CodesMeteo.icone(meteo.code, meteo.jour), Modifier.size(156.px))
        Spacer(Modifier.width(26.px))
        Column {
            Text(
                "${meteo.temperature}°",
                style = TextStyle(
                    brush = Degrades.Chiffres,
                    fontFamily = SpaceGrotesk, fontWeight = FontWeight.Light,
                    fontSize = 140.pxt, letterSpacing = (-4).pxt,
                    fontFeatureSettings = CHIFFRES_FIXES,
                ),
                modifier = Modifier.hauteurVisuelle(116.px),
            )
            Spacer(Modifier.height(14.px))
            Text(
                CodesMeteo.description(meteo.code),
                fontSize = 32.pxt, fontWeight = FontWeight.Medium, color = Couleurs.Texte,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun Statistique(libelle: String, valeur: String) {
    Column {
        Text(libelle.uppercase(), fontSize = 18.pxt, letterSpacing = 2.pxt, color = Couleurs.TexteDiscret, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.px))
        Text(
            valeur,
            style = TextStyle(
                fontFamily = SpaceGrotesk, fontWeight = FontWeight.Medium, fontSize = 28.pxt,
                fontFeatureSettings = CHIFFRES_FIXES, color = Couleurs.Texte,
            ),
        )
    }
}

@Composable
private fun ColonnePrevision(prevision: Prevision) {
    Column(Modifier.width(118.px), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            prevision.libelle.uppercase(),
            fontSize = 20.pxt, letterSpacing = 2.pxt,
            color = Couleurs.TexteDiscret, fontWeight = FontWeight.SemiBold,
        )
        DessinMeteo(CodesMeteo.icone(prevision.code, prevision.jour), Modifier.padding(vertical = 6.px).size(70.px))
        val texte = prevision.temperatureMin?.let { "${prevision.temperature}°/$it°" } ?: "${prevision.temperature}°"
        Text(
            texte,
            style = TextStyle(
                fontFamily = SpaceGrotesk, fontWeight = FontWeight.Medium,
                fontSize = if (prevision.temperatureMin != null) 26.pxt else 32.pxt,
                fontFeatureSettings = CHIFFRES_FIXES, color = Couleurs.Texte,
            ),
            maxLines = 1,
        )
    }
}
