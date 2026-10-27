package loic.glld.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

@Composable
fun ThemeMonAccueil(fondAnime: Boolean = true, contenu: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Couleurs.Cyan,
            background = Couleurs.Fond,
            surface = Couleurs.Fond,
            onSurface = Couleurs.Texte,
            onBackground = Couleurs.Texte,
        ),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides Couleurs.Texte,
            LocalTextStyle provides TextStyle(fontFamily = Inter),
        ) {
            Box(Modifier.fillMaxSize()) {
                FondAurore(anime = fondAnime)
                FournisseurEchelle(contenu)
            }
        }
    }
}
