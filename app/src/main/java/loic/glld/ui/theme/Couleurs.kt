package loic.glld.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Palette « néon de nuit » : noir profond, verre dépoli, cyan et violet lumineux. */
object Couleurs {
    val Fond = Color(0xFF04060C)
    val Texte = Color(0xFFF5F7FA)
    val TexteSecondaire = Color(0xFF8E97AB)
    val TexteDiscret = Color(0xFF5A6377)

    val Cyan = Color(0xFF38E1FF)
    val Violet = Color(0xFF8B7CFF)
    val Rose = Color(0xFFFF5FA2)
    val Ambre = Color(0xFFFFB547)

    /** Panneaux en verre : blanc très transparent par-dessus le fond animé. */
    val Verre = Color(0x0DFFFFFF)
    val VerreFocus = Color(0x1FFFFFFF)
    val VerreBord = Color(0x1AFFFFFF)
    /** Fond opaque des fenêtres (sélecteur, confirmation). */
    val Fenetre = Color(0xF20A0E18)

    val Lune = Color(0xFFD9D3FF)
    val Nuage = Color(0xFFDDE6F3)
    val Pluie = Color(0xFF5CC8FF)
}

object Degrades {
    val Accent = Brush.linearGradient(listOf(Couleurs.Cyan, Couleurs.Violet))
    val AccentHorizontal = Brush.horizontalGradient(listOf(Couleurs.Cyan, Couleurs.Violet))
    /** Chiffres de l'horloge : blanc qui glisse vers un bleu glacier. */
    val Chiffres = Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFB9ECFF)))
    val Ligne = Brush.horizontalGradient(listOf(Color(0x00FFFFFF), Color(0x33FFFFFF), Color(0x00FFFFFF)))
}
