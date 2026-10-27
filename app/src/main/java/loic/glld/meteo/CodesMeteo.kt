package loic.glld.meteo

enum class IconeMeteo { SOLEIL, LUNE, ECLAIRCIES_JOUR, ECLAIRCIES_NUIT, NUAGES, BROUILLARD, BRUINE, PLUIE, NEIGE, ORAGE }

/** Traduction des codes météo WMO renvoyés par Open-Meteo. */
object CodesMeteo {

    fun description(code: Int): String = when (code) {
        0 -> "Ciel dégagé"
        1 -> "Plutôt dégagé"
        2 -> "Partiellement nuageux"
        3 -> "Couvert"
        45 -> "Brouillard"
        48 -> "Brouillard givrant"
        51 -> "Bruine légère"
        53 -> "Bruine"
        55 -> "Bruine dense"
        56, 57 -> "Bruine verglaçante"
        61 -> "Pluie faible"
        63 -> "Pluie"
        65 -> "Pluie forte"
        66, 67 -> "Pluie verglaçante"
        71 -> "Neige faible"
        73 -> "Neige"
        75 -> "Neige forte"
        77 -> "Grains de neige"
        80 -> "Averses faibles"
        81 -> "Averses"
        82 -> "Averses violentes"
        85 -> "Averses de neige"
        86 -> "Fortes averses de neige"
        95 -> "Orage"
        96, 99 -> "Orage avec grêle"
        else -> "Conditions inconnues"
    }

    fun icone(code: Int, jour: Boolean): IconeMeteo = when (code) {
        0 -> if (jour) IconeMeteo.SOLEIL else IconeMeteo.LUNE
        1, 2 -> if (jour) IconeMeteo.ECLAIRCIES_JOUR else IconeMeteo.ECLAIRCIES_NUIT
        3 -> IconeMeteo.NUAGES
        45, 48 -> IconeMeteo.BROUILLARD
        51, 53, 55, 56, 57 -> IconeMeteo.BRUINE
        61, 63, 65, 66, 67, 80, 81, 82 -> IconeMeteo.PLUIE
        71, 73, 75, 77, 85, 86 -> IconeMeteo.NEIGE
        95, 96, 99 -> IconeMeteo.ORAGE
        else -> IconeMeteo.NUAGES
    }
}
