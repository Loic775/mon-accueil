package loic.glld.ui.composants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.tv.material3.Text
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt

/** Demande confirmation avant de quitter. Le focus est mis sur « Rester » par sécurité. */
@Composable
fun DialogueQuitter(onRester: () -> Unit, onQuitter: () -> Unit) {
    val focusRester = remember { FocusRequester() }
    Dialog(onDismissRequest = onRester, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Carte(Modifier.width(820.px)) {
            Column(Modifier.padding(56.px), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Quitter Mon Accueil ?", fontSize = 48.pxt, fontWeight = FontWeight.SemiBold, color = Couleurs.Texte)
                Spacer(Modifier.height(16.px))
                Text(
                    "Le bouton Retour te ramène au tableau de bord.",
                    fontSize = 28.pxt, color = Couleurs.TexteSecondaire,
                )
                Spacer(Modifier.height(48.px))
                Row(horizontalArrangement = Arrangement.spacedBy(32.px)) {
                    BoutonTexte("Rester", onRester, Modifier.focusRequester(focusRester))
                    BoutonTexte("Quitter", onQuitter)
                }
            }
        }
    }
    LaunchedEffect(Unit) { runCatching { focusRester.requestFocus() } }
}

@Composable
fun BoutonTexte(texte: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    CarteFocusable(onClick = onClick, modifier = modifier, rayon = 20.px) {
        Text(
            texte,
            Modifier.padding(horizontal = 48.px, vertical = 22.px),
            fontSize = 32.pxt,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
