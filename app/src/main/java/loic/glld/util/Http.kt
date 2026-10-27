package loic.glld.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/** Petit client HTTP sans librairie externe (garde l'APK léger). */
object Http {
    suspend fun texte(url: String, delaiMs: Int = 15_000): String = withContext(Dispatchers.IO) {
        val connexion = URL(url).openConnection() as HttpURLConnection
        try {
            connexion.connectTimeout = delaiMs
            connexion.readTimeout = delaiMs
            connexion.instanceFollowRedirects = true
            connexion.setRequestProperty("User-Agent", "MonAccueil/1.0 (Fire TV)")
            val code = connexion.responseCode
            if (code !in 200..299) throw IOException("HTTP $code")
            connexion.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        } finally {
            connexion.disconnect()
        }
    }
}
