package loic.glld.ui.accueil

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.tv.material3.Text
import loic.glld.ui.composants.Carte
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.pxt

/** Cadre photo (le diaporama arrive en phase 3). */
@Composable
fun CadrePhoto(modifier: Modifier = Modifier) {
    Carte(modifier.fillMaxSize()) {
        Text(
            "Tes photos s'afficheront ici",
            Modifier.align(Alignment.Center),
            fontSize = 30.pxt,
            color = Couleurs.TexteSecondaire,
        )
    }
}
