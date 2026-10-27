package loic.glld

import android.app.Application
import loic.glld.applis.AppsRepository
import loic.glld.applis.ChoixTuilesStore
import loic.glld.meteo.MeteoRepository

/** Point d'entrée de l'appli : crée une seule fois les objets partagés. */
class MonAccueilApp : Application() {
    val meteo by lazy { MeteoRepository(this) }
    val applis by lazy { AppsRepository(this) }
    val choixTuiles by lazy { ChoixTuilesStore(this) }
}
