package loic.glld.ui.accueil

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import loic.glld.ui.composants.DialogueQuitter
import loic.glld.ui.theme.px

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

    Column(
        Modifier.fillMaxSize().padding(horizontal = 88.px, vertical = 72.px),
        verticalArrangement = Arrangement.spacedBy(40.px),
    ) {
        // 1 + 2 : horloge à gauche, météo à droite
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            BlocHorloge(maintenant, Modifier.weight(1f))
            CarteMeteo(meteo)
        }
        // 3 + 4 : agenda à gauche, photo à droite
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(40.px)) {
            CarteAgenda(lignes = emptyList(), message = null)
            CadrePhoto(Modifier.weight(1f))
        }
        // 5 : tuiles d'applis
        RangeeApplis(
            tuiles = tuiles,
            onLancer = { appli ->
                if (!vm.lancer(appli)) Toast.makeText(contexte, "Impossible d'ouvrir ${appli.nom}", Toast.LENGTH_SHORT).show()
            },
            onChoisir = { tuileEnChoix = it },
            premiereTuile = Modifier.focusRequester(focusPremiereTuile),
        )
    }

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
