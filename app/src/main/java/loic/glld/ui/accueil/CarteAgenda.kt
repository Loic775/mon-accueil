package loic.glld.ui.accueil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.tv.material3.Text
import loic.glld.ui.composants.Carte
import loic.glld.ui.theme.CHIFFRES_FIXES
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.SpaceGrotesk
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt

/** Une ligne de l'agenda : « 09:30 » ou « Journée », puis le titre. */
data class LigneAgenda(val heure: String, val titre: String)

@Composable
fun CarteAgenda(lignes: List<LigneAgenda>, message: String?, modifier: Modifier = Modifier) {
    Carte(modifier.width(760.px).fillMaxHeight()) {
        Column(Modifier.padding(horizontal = 44.px, vertical = 36.px)) {
            Text("Aujourd'hui", fontSize = 36.pxt, fontWeight = FontWeight.SemiBold, color = Couleurs.Texte)
            Spacer(Modifier.height(12.px))
            if (lignes.isEmpty()) {
                Text(
                    message ?: "Rien de prévu aujourd'hui",
                    Modifier.padding(top = 20.px),
                    fontSize = 30.pxt,
                    color = Couleurs.TexteSecondaire,
                )
            }
            lignes.forEachIndexed { i, ligne ->
                if (i > 0) Separateur()
                Row(Modifier.padding(vertical = 18.px), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        ligne.heure,
                        Modifier.width(150.px),
                        style = TextStyle(
                            fontFamily = SpaceGrotesk, fontWeight = FontWeight.Medium,
                            fontSize = 32.pxt, fontFeatureSettings = CHIFFRES_FIXES, color = Couleurs.Accent,
                        ),
                    )
                    Text(
                        ligne.titre,
                        fontSize = 32.pxt, color = Couleurs.Texte,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun Separateur() {
    Spacer(Modifier.fillMaxWidth().height(2.px).background(Couleurs.Separateur))
}
