package loic.glld.ui.composants

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.Degrades
import loic.glld.ui.theme.px

/**
 * Tout ce qui est cliquable à la télécommande passe par ici.
 * Au focus : l'élément grossit, s'éclaircit, prend un contour dégradé cyan → violet
 * et s'entoure d'un halo lumineux.
 */
@Composable
fun ElementFocusable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    rayon: Dp = 28.px,
    echelleFocus: Float = 1.06f,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    /** Pour les captures de test : affiche l'état « sélectionné » sans focus réel. */
    apercuFocus: Boolean = false,
    contenu: @Composable BoxScope.(focalise: Boolean) -> Unit,
) {
    val focalise = interactionSource.collectIsFocusedAsState().value || apercuFocus
    val progres by animateFloatAsState(
        if (focalise) 1f else 0f,
        spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow),
        label = "focus",
    )
    val forme = RoundedCornerShape(rayon)
    val epaisseurHalo = 26.px
    Box(
        modifier
            .graphicsLayer {
                val s = 1f + (echelleFocus - 1f) * progres
                scaleX = s
                scaleY = s
            }
            .drawBehind { if (progres > 0.01f) halo(rayon.toPx(), epaisseurHalo.toPx(), progres) },
        propagateMinConstraints = true,
    ) {
        Surface(
            onClick = onClick,
            onLongClick = onLongClick,
            interactionSource = interactionSource,
            shape = ClickableSurfaceDefaults.shape(forme),
            scale = ClickableSurfaceDefaults.scale(scale = 1f, focusedScale = 1f, pressedScale = 0.97f),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = Couleurs.Verre,
                contentColor = Couleurs.Texte,
                focusedContainerColor = Couleurs.VerreFocus,
                focusedContentColor = Couleurs.Texte,
                pressedContainerColor = Couleurs.VerreFocus,
                pressedContentColor = Couleurs.Texte,
            ),
            border = ClickableSurfaceDefaults.border(
                border = Border(BorderStroke(1.5.px, Couleurs.VerreBord), shape = forme),
                focusedBorder = Border(BorderStroke(3.px, Degrades.Accent), shape = forme),
                pressedBorder = Border(BorderStroke(3.px, Degrades.Accent), shape = forme),
            ),
        ) {
            contenu(focalise)
        }
    }
}

/** Halo flou simulé par plusieurs anneaux dégradés de plus en plus transparents. */
private fun DrawScope.halo(rayon: Float, epaisseur: Float, progres: Float) {
    val pinceau = Brush.linearGradient(
        listOf(Couleurs.Cyan, Couleurs.Violet),
        start = Offset.Zero,
        end = Offset(size.width, size.height),
    )
    val anneaux = 6
    for (i in 1..anneaux) {
        val d = epaisseur * i / anneaux
        val pas = epaisseur / anneaux
        drawRoundRect(
            pinceau,
            topLeft = Offset(-d + pas / 2, -d + pas / 2),
            size = Size(size.width + 2 * d - pas, size.height + 2 * d - pas),
            cornerRadius = CornerRadius(rayon + d),
            alpha = progres * 0.22f * (1f - (i - 1f) / anneaux) * (1f - (i - 1f) / anneaux),
            style = Stroke(pas),
        )
    }
}

/** Panneau en verre dépoli (non cliquable) : voile clair en dégradé et liseré lumineux. */
@Composable
fun Modifier.verre(rayon: Dp = 36.px): Modifier {
    val forme = RoundedCornerShape(rayon)
    return this
        .background(Brush.verticalGradient(listOf(Color(0x14FFFFFF), Color(0x08FFFFFF))), forme)
        .border(1.5.px, Brush.verticalGradient(listOf(Color(0x2EFFFFFF), Color(0x0AFFFFFF))), forme)
}

/** Fenêtre opaque (sélecteur, confirmation). */
@Composable
fun Modifier.fenetre(rayon: Dp = 40.px): Modifier {
    val forme = RoundedCornerShape(rayon)
    return this
        .background(Couleurs.Fenetre, forme)
        .border(1.5.px, Brush.verticalGradient(listOf(Couleurs.Cyan.copy(alpha = .5f), Couleurs.Violet.copy(alpha = .15f))), forme)
}
