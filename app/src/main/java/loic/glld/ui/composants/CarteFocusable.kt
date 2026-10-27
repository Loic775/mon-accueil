package loic.glld.ui.composants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.px

/** Rayon des coins de toutes les cartes (28 sur la maquette). */
@Composable
fun formeCarte(rayon: Dp = 28.px) = RoundedCornerShape(rayon)

/**
 * Élément cliquable à la télécommande : grossit à 1.05 et passe sur fond orange
 * quand il a le focus. Tout ce qui est cliquable dans l'appli passe par ici.
 */
@Composable
fun CarteFocusable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    rayon: Dp = 28.px,
    contenu: @Composable BoxScope.() -> Unit,
) {
    Surface(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(rayon)),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f, pressedScale = 1.0f),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Couleurs.Carte,
            contentColor = Couleurs.Texte,
            focusedContainerColor = Couleurs.Accent,
            focusedContentColor = Couleurs.TexteSurAccent,
            pressedContainerColor = Couleurs.Accent,
            pressedContentColor = Couleurs.TexteSurAccent,
        ),
        glow = ClickableSurfaceDefaults.glow(),
        content = contenu,
    )
}

/** Carte non cliquable (fond #161C25, coins arrondis). */
@Composable
fun Carte(modifier: Modifier = Modifier, contenu: @Composable BoxScope.() -> Unit) {
    Box(modifier.background(Couleurs.Carte, formeCarte()), content = contenu)
}
