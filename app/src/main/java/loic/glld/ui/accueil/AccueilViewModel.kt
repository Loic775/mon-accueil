package loic.glld.ui.accueil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import loic.glld.MonAccueilApp
import loic.glld.applis.AppliTv
import loic.glld.applis.ChoixTuilesStore
import loic.glld.meteo.Meteo

data class EtatMeteo(
    val meteo: Meteo? = null,
    /** Vrai si la dernière tentative de mise à jour a échoué (on affiche le cache). */
    val horsLigne: Boolean = false,
)

class AccueilViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MonAccueilApp

    private val _meteo = MutableStateFlow(EtatMeteo())
    val meteo: StateFlow<EtatMeteo> = _meteo.asStateFlow()

    private val _applis = MutableStateFlow<List<AppliTv>>(emptyList())
    val applis: StateFlow<List<AppliTv>> = _applis.asStateFlow()

    /** Les 5 tuiles (null = vide). */
    val tuiles: StateFlow<List<AppliTv?>> = combine(app.choixTuiles.choix, _applis) { choix, installees ->
        resoudreTuiles(choix, installees)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, List(ChoixTuilesStore.NOMBRE_TUILES) { null })

    private var boucleMeteo: Job? = null

    init {
        viewModelScope.launch {
            app.meteo.lireCache()?.let { _meteo.value = EtatMeteo(it, horsLigne = false) }
        }
        demarrerMeteo()
    }

    private fun demarrerMeteo() {
        boucleMeteo?.cancel()
        boucleMeteo = viewModelScope.launch {
            while (true) {
                val ok = actualiserMeteo()
                delay(if (ok) TRENTE_MINUTES else CINQ_MINUTES)
            }
        }
    }

    private suspend fun actualiserMeteo(): Boolean = runCatching { app.meteo.telecharger() }
        .onSuccess { _meteo.value = EtatMeteo(it, horsLigne = false) }
        .onFailure { _meteo.value = _meteo.value.copy(horsLigne = true) }
        .isSuccess

    /** Appelé à chaque retour sur l'appli (une appli a pu être installée ou supprimée). */
    fun auRetour() {
        viewModelScope.launch { _applis.value = app.applis.lister() }
        val derniere = _meteo.value.meteo?.majLe ?: 0L
        if (System.currentTimeMillis() - derniere > TRENTE_MINUTES) demarrerMeteo()
    }

    fun lancer(appli: AppliTv): Boolean = app.applis.lancer(appli.paquet)

    fun choisir(index: Int, appli: AppliTv?) {
        viewModelScope.launch { app.choixTuiles.definir(index, appli?.paquet ?: "") }
    }

    private fun resoudreTuiles(choix: List<String?>, installees: List<AppliTv>): List<AppliTv?> {
        val parPaquet = installees.associateBy { it.paquet }
        val utilisees = choix.filterNotNull().toMutableSet()
        return choix.mapIndexed { i, paquet ->
            when {
                paquet == null -> ChoixTuilesStore.PAR_DEFAUT[i]
                    .firstOrNull { it in parPaquet && it !in utilisees }
                    ?.also { utilisees += it }
                    ?.let { parPaquet[it] }
                paquet.isEmpty() -> null
                else -> parPaquet[paquet]
            }
        }
    }

    private companion object {
        const val TRENTE_MINUTES = 30 * 60 * 1000L
        const val CINQ_MINUTES = 5 * 60 * 1000L
    }
}
