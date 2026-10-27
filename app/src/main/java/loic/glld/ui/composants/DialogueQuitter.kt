package loic.glld.ui.composants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
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
        Box(Modifier.width(860.px).fenetre()) {
            Column(Modifier.padding(64.px), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Quitter Mon Accueil ?", fontSize = 50.pxt, fontWeight = FontWeight.SemiBold, color = Couleurs.Texte)
                Spacer(Modifier.height(16.px))
                Text(
                    "Le bouton Retour te ramène au tableau de bord.",
                    fontSize = 28.pxt, color = Couleurs.TexteSecondaire, textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(52.px))
                Row(horizontalArrangement = Arrangement.spacedBy(36.px)) {
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
    ElementFocusable(onClick = onClick, modifier = modifier, rayon = 999.px, echelleFocus = 1.08f) {
        Text(
            texte,
            Modifier.padding(horizontal = 60.px, vertical = 24.px),
            fontSize = 32.pxt,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
