package loic.glld.applis

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.reglages by preferencesDataStore(name = "reglages")

/**
 * Mémorise l'appli choisie pour chacune des 5 tuiles.
 * null = jamais choisie (on utilise l'appli par défaut), "" = tuile laissée vide.
 */
class ChoixTuilesStore(private val context: Context) {

    val choix: Flow<List<String?>> = context.reglages.data.map { prefs ->
        List(NOMBRE_TUILES) { prefs[cle(it)] }
    }

    suspend fun definir(index: Int, paquet: String) {
        context.reglages.edit { it[cle(index)] = paquet }
    }

    private fun cle(index: Int) = stringPreferencesKey("tuile_$index")

    companion object {
        const val NOMBRE_TUILES = 6

        /** Applis par défaut, par tuile : on prend le premier paquet installé. */
        val PAR_DEFAUT: List<List<String>> = listOf(
            listOf("com.netflix.ninja", "com.netflix.mediaclient"),
            listOf("com.amazon.firetv.youtube", "com.google.android.youtube.tv", "com.google.android.youtube.googletv"),
            listOf("com.amazon.avod", "com.amazon.firebat", "com.amazon.amazonvideo.livingroom", "com.amazon.avod.thirdpartyclient"),
            listOf("com.spotify.tv.android", "com.spotify.music"),
            listOf("com.disney.disneyplus", "com.disney.disneyplus.prod"),
            listOf("com.canal.android.canal", "fr.francetv.pluzz", "com.molotov.app", "tv.twitch.android.app"),
        )
    }
}
