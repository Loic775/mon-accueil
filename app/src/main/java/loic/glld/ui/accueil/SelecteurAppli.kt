package loic.glld.ui.accueil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import loic.glld.applis.AppliTv
import loic.glld.applis.AppsRepository
import loic.glld.ui.composants.Carte
import loic.glld.ui.composants.CarteFocusable
import loic.glld.ui.theme.Couleurs
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
        Carte(Modifier.width(1500.px).height(880.px)) {
            Column(Modifier.padding(48.px)) {
                Text("Tuile $numeroTuile : quelle appli ?", fontSize = 44.pxt, fontWeight = FontWeight.SemiBold, color = Couleurs.Texte)
                Text("Retour pour annuler", fontSize = 26.pxt, color = Couleurs.TexteSecondaire)
                Spacer(Modifier.height(28.px))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.px),
                    verticalArrangement = Arrangement.spacedBy(24.px),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.px),
                    initialFirstVisibleItemIndex = indexInitial,
                ) {
                    itemsIndexed(entrees) { i, appli ->
                        CarteFocusable(
                            onClick = { onChoix(appli) },
                            rayon = 20.px,
                            modifier = Modifier
                                .height(110.px)
                                .then(if (i == indexInitial) Modifier.focusRequester(focus) else Modifier),
                        ) {
                            EntreeAppli(appli)
                        }
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
    Row(Modifier.fillMaxSize().padding(12.px), verticalAlignment = Alignment.CenterVertically) {
        val image = appli?.image
        when {
            appli == null -> Spacer(Modifier.width(8.px))
            appli.paquet == AppsRepository.PARAMETRES -> Engrenage(Modifier.size(80.px))
            image != null && appli.estBanniere -> Image(
                image, null, Modifier.width(142.px).height(80.px).clip(RoundedCornerShape(10.px)),
                contentScale = ContentScale.Crop,
            )
            image != null -> Image(image, null, Modifier.size(80.px))
            else -> Spacer(Modifier.size(80.px))
        }
        Spacer(Modifier.width(16.px))
        Text(
            appli?.nom ?: "Laisser vide",
            fontSize = 24.pxt, fontWeight = FontWeight.Medium,
            maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 28.pxt,
        )
    }
}
