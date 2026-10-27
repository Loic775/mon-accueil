package loic.glld.meteo

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import loic.glld.util.Http
import org.json.JSONObject
import java.io.File

/** Télécharge la météo et garde la dernière réponse sur le disque pour le mode hors ligne. */
class MeteoRepository(context: Context) {
    private val cache = File(context.filesDir, "meteo.json")

    suspend fun lireCache(): Meteo? = withContext(Dispatchers.IO) {
        runCatching {
            val o = JSONObject(cache.readText())
            OpenMeteoApi.lire(o.getString("brut"), o.getLong("majLe"))
        }.getOrNull()
    }

    suspend fun telecharger(): Meteo {
        val brut = Http.texte(OpenMeteoApi.URL)
        val maintenant = System.currentTimeMillis()
        val meteo = OpenMeteoApi.lire(brut, maintenant) // vérifie que la réponse est valide avant de l'enregistrer
        withContext(Dispatchers.IO) {
            val tmp = File(cache.parentFile, "meteo.json.tmp")
            tmp.writeText(JSONObject().put("brut", brut).put("majLe", maintenant).toString())
            tmp.renameTo(cache)
        }
        return meteo
    }
}
