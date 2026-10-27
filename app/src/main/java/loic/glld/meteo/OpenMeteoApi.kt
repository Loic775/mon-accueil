package loic.glld.meteo

import org.json.JSONObject
import kotlin.math.roundToInt

/** Une mini-case de prévision (+3 h, +6 h, +9 h, demain). */
data class Prevision(
    val libelle: String,
    val temperature: Int,
    /** Renseigné seulement pour « demain » (min / max). */
    val temperatureMin: Int?,
    val code: Int,
    val jour: Boolean,
)

data class Meteo(
    val temperature: Int,
    val ressenti: Int,
    val code: Int,
    val jour: Boolean,
    val previsions: List<Prevision>,
    /** Heure de la mise à jour (millisecondes). */
    val majLe: Long,
)

object OpenMeteoApi {
    // Chelles (77), fuseau Europe/Paris. API gratuite, sans clé.
    const val URL = "https://api.open-meteo.com/v1/forecast" +
        "?latitude=48.8809&longitude=2.5944&timezone=Europe%2FParis" +
        "&current=temperature_2m,apparent_temperature,weather_code,is_day" +
        "&hourly=temperature_2m,weather_code,is_day" +
        "&daily=weather_code,temperature_2m_max,temperature_2m_min" +
        "&forecast_days=3"

    fun lire(brut: String, majLe: Long): Meteo {
        val racine = JSONObject(brut)
        val actuel = racine.getJSONObject("current")
        val horaire = racine.getJSONObject("hourly")
        val quotidien = racine.getJSONObject("daily")

        // « 2026-10-27T17:15 » → on cherche le créneau « 2026-10-27T17:00 »
        val heureActuelle = actuel.getString("time").take(13) + ":00"
        val heures = horaire.getJSONArray("time")
        var index = 0
        for (i in 0 until heures.length()) {
            if (heures.getString(i) == heureActuelle) { index = i; break }
        }
        val temps = horaire.getJSONArray("temperature_2m")
        val codes = horaire.getJSONArray("weather_code")
        val jours = horaire.getJSONArray("is_day")

        val previsions = mutableListOf<Prevision>()
        for (decalage in listOf(3, 6, 9)) {
            val i = index + decalage
            if (i < heures.length()) {
                previsions += Prevision(
                    libelle = "+$decalage h",
                    temperature = temps.getDouble(i).roundToInt(),
                    temperatureMin = null,
                    code = codes.getInt(i),
                    jour = jours.getInt(i) == 1,
                )
            }
        }
        if (quotidien.getJSONArray("time").length() > 1) {
            previsions += Prevision(
                libelle = "Demain",
                temperature = quotidien.getJSONArray("temperature_2m_max").getDouble(1).roundToInt(),
                temperatureMin = quotidien.getJSONArray("temperature_2m_min").getDouble(1).roundToInt(),
                code = quotidien.getJSONArray("weather_code").getInt(1),
                jour = true,
            )
        }

        return Meteo(
            temperature = actuel.getDouble("temperature_2m").roundToInt(),
            ressenti = actuel.getDouble("apparent_temperature").roundToInt(),
            code = actuel.getInt("weather_code"),
            jour = actuel.getInt("is_day") == 1,
            previsions = previsions,
            majLe = majLe,
        )
    }
}
