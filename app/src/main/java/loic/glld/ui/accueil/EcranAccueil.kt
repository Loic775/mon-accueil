package loic.glld.ui.accueil

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import loic.glld.applis.AppliTv
import loic.glld.ui.composants.DialogueQuitter
import loic.glld.ui.theme.px
import java.time.ZonedDateTime

@Composable
fun EcranAccueil(onQuitter: () -> Unit, vm: AccueilViewModel = viewModel()) {
    val contexte = LocalContext.current
    val meteo by vm.meteo.collectAsStateWithLifecycle()
    val tuiles by vm.tuiles.collectAsStateWithLifecycle()
    val applis by vm.applis.collectAsStateWithLifecycle()
    val maintenant = rememberMaintenant()

    var confirmerQuitter by remember { mutableStateOf(false) }
    var tuileEnChoix by rememberSaveable { mutableStateOf<Int?>(null) }
    val focusPremiereTuile = remember { FocusRequester() }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { vm.auRetour() }

    // Retour ne ferme jamais l'appli directement : on demande confirmation.
    BackHandler(enabled = !confirmerQuitter && tuileEnChoix == null) { confirmerQuitter = true }

    TableauDeBord(
        maintenant = maintenant,
        meteo = meteo,
        tuiles = tuiles,
        onLancer = { appli ->
            if (!vm.lancer(appli)) Toast.makeText(contexte, "Impossible d'ouvrir ${appli.nom}", Toast.LENGTH_SHORT).show()
        },
        onChoisir = { tuileEnChoix = it },
        premiereTuile = Modifier.focusRequester(focusPremiereTuile),
    )

    LaunchedEffect(Unit) { runCatching { focusPremiereTuile.requestFocus() } }

    tuileEnChoix?.let { index ->
        SelecteurAppli(
            numeroTuile = index + 1,
            applis = applis,
            actuelle = tuiles.getOrNull(index),
            onChoix = { appli ->
                vm.choisir(index, appli)
                tuileEnChoix = null
            },
            onFermer = { tuileEnChoix = null },
        )
    }

    if (confirmerQuitter) {
        DialogueQuitter(onRester = { confirmerQuitter = false }, onQuitter = onQuitter)
    }
}

/** La mise en page du tableau de bord, sans logique (affichable aussi dans les tests). */
@Composable
fun TableauDeBord(
    maintenant: ZonedDateTime,
    meteo: EtatMeteo,
    tuiles: List<AppliTv?>,
    onLancer: (AppliTv) -> Unit,
    onChoisir: (Int) -> Unit,
    premiereTuile: Modifier = Modifier,
    /** Pour les captures de test uniquement : simule le focus sur une tuile. */
    tuileFocaliseeApercu: Int? = null,
) {
    Column(Modifier.fillMaxSize().padding(start = 96.px, end = 96.px, top = 80.px, bottom = 40.px)) {
        // Horloge à gauche, météo à droite, centrées dans l'espace au-dessus du dock
        Row(
            Modifier.fillMaxWidth().weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BlocHorloge(maintenant, Modifier.weight(1f))
            CarteMeteo(meteo)
        }
        Spacer(Modifier.height(56.px))
        // Dock d'applis
        RangeeApplis(
            tuiles = tuiles,
            onLancer = onLancer,
            onChoisir = onChoisir,
            premiereTuile = premiereTuile,
            tuileFocaliseeApercu = tuileFocaliseeApercu,
        )
    }
}
