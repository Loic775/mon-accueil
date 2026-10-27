package loic.glld.ui.accueil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import loic.glld.applis.AppliTv
import loic.glld.applis.AppsRepository
import loic.glld.ui.composants.ElementFocusable
import loic.glld.ui.composants.fenetre
import loic.glld.ui.theme.Couleurs
import loic.glld.ui.theme.Degrades
import loic.glld.ui.theme.px
import loic.glld.ui.theme.pxt

/** Choix de l'appli d'une tuile. `null` passé à onChoix = laisser la tuile vide. */
@Composable
fun SelecteurAppli(
    numeroTuile: Int,
    applis: List<AppliTv>,
    actuelle: AppliTv?,
    onChoix: (AppliTv?) -> Unit,
    onFermer: () -> Unit,
) {
    // Entrée « Laisser vide » en premier, puis toutes les applis.
    val entrees: List<AppliTv?> = listOf<AppliTv?>(null) + applis
    val indexInitial = entrees.indexOfFirst { it?.paquet == actuelle?.paquet }.coerceAtLeast(0)
    val focus = remember { FocusRequester() }

    Dialog(onDismissRequest = onFermer, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(Modifier.width(1560.px).height(900.px).fenetre().padding(56.px)) {
            Text(
                "TUILE $numeroTuile",
                style = TextStyle(brush = Degrades.AccentHorizontal, fontSize = 24.pxt, letterSpacing = 6.pxt, fontWeight = FontWeight.SemiBold),
            )
            Spacer(Modifier.height(8.px))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("Quelle appli ?", fontSize = 52.pxt, fontWeight = FontWeight.SemiBold, color = Couleurs.Texte)
                Spacer(Modifier.weight(1f))
                Text("Retour pour annuler", fontSize = 24.pxt, color = Couleurs.TexteDiscret)
            }
            Spacer(Modifier.height(28.px))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(28.px),
                verticalArrangement = Arrangement.spacedBy(28.px),
                contentPadding = PaddingValues(20.px),
                state = rememberLazyGridState(initialFirstVisibleItemIndex = indexInitial),
            ) {
                itemsIndexed(entrees) { i, appli ->
                    ElementFocusable(
                        onClick = { onChoix(appli) },
                        rayon = 22.px,
                        modifier = Modifier
                            .height(116.px)
                            .then(if (i == indexInitial) Modifier.focusRequester(focus) else Modifier),
                    ) {
                        EntreeAppli(appli)
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        delay(100) // laisse la grille se mettre en place
        runCatching { focus.requestFocus() }
    }
}

@Composable
private fun EntreeAppli(appli: AppliTv?) {
    Row(Modifier.fillMaxSize().padding(14.px), verticalAlignment = Alignment.CenterVertically) {
        val image = appli?.image
        when {
            appli == null -> Box(Modifier.size(88.px), contentAlignment = Alignment.Center) {
                Text("–", fontSize = 44.pxt, color = Couleurs.TexteSecondaire)
            }
            appli.paquet == AppsRepository.PARAMETRES -> Engrenage(true, Modifier.size(88.px))
            image != null && appli.estBanniere -> Image(
                image, null, Modifier.width(154.px).height(87.px).clip(RoundedCornerShape(12.px)),
                contentScale = ContentScale.Crop,
            )
            image != null -> Image(image, null, Modifier.size(84.px))
            else -> Box(Modifier.size(72.px).background(Degrades.Accent, CircleShape), contentAlignment = Alignment.Center) {
                Text(appli.nom.take(1).uppercase(), fontSize = 34.pxt, fontWeight = FontWeight.SemiBold, color = Couleurs.Fond)
            }
        }
        Spacer(Modifier.width(18.px))
        Text(
            appli?.nom ?: "Laisser vide",
            fontSize = 24.pxt, fontWeight = FontWeight.Medium,
            maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 28.pxt,
        )
    }
}
