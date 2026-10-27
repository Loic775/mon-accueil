package loic.glld.meteo

import org.junit.Assert.assertEquals
import org.junit.Test

class OpenMeteoApiTest {

    private val heures = (0 until 48).map { h -> "2026-10-${27 + h / 24}T%02d:00".format(h % 24) }

    private val reponse = """
        {
          "current": {"time": "2026-10-27T17:15", "temperature_2m": 12.6, "apparent_temperature": 10.2, "weather_code": 61, "is_day": 1},
          "hourly": {
            "time": [${heures.joinToString { "\"$it\"" }}],
            "temperature_2m": [${(0 until 48).joinToString { "$it.4" }}],
            "weather_code": [${(0 until 48).joinToString { if (it == 20) "3" else "0" }}],
            "is_day": [${(0 until 48).joinToString { if (it % 24 in 8..18) "1" else "0" }}]
          },
          "daily": {
            "time": ["2026-10-27", "2026-10-28", "2026-10-29"],
            "weather_code": [61, 2, 0],
            "temperature_2m_max": [13.4, 15.6, 14.0],
            "temperature_2m_min": [7.1, 6.5, 5.0]
          }
        }
    """.trimIndent()

    @Test
    fun litLaMeteoActuelle() {
        val m = OpenMeteoApi.lire(reponse, 42L)
        assertEquals(13, m.temperature)
        assertEquals(10, m.ressenti)
        assertEquals(61, m.code)
        assertEquals(true, m.jour)
        assertEquals(42L, m.majLe)
    }

    @Test
    fun previsionsDecaleesDepuisLHeureCourante() {
        val p = OpenMeteoApi.lire(reponse, 0L).previsions
        assertEquals(listOf("+3 h", "+6 h", "+9 h", "Demain"), p.map { it.libelle })
        assertEquals(listOf(20, 23, 26), p.take(3).map { it.temperature }) // 20.4 → 20, etc.
        assertEquals(3, p[0].code)          // 20 h : couvert
        assertEquals(false, p[0].jour)      // 20 h : nuit
        assertEquals(16, p[3].temperature)  // max de demain
        assertEquals(7, p[3].temperatureMin)
    }

    @Test
    fun codesTraduitsEnFrancais() {
        assertEquals("Ciel dégagé", CodesMeteo.description(0))
        assertEquals("Orage avec grêle", CodesMeteo.description(99))
        assertEquals(IconeMeteo.LUNE, CodesMeteo.icone(0, jour = false))
        assertEquals(IconeMeteo.NEIGE, CodesMeteo.icone(85, jour = true))
        assertEquals(IconeMeteo.PLUIE, CodesMeteo.icone(81, jour = true))
    }
}
