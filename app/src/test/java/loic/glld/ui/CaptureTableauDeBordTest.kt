package loic.glld.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import loic.glld.applis.AppliTv
import loic.glld.meteo.Meteo
import loic.glld.meteo.Prevision
import loic.glld.ui.accueil.EtatMeteo
import loic.glld.ui.accueil.PARIS
import loic.glld.ui.accueil.TableauDeBord
import loic.glld.ui.theme.ThemeMonAccueil
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.time.ZonedDateTime

/** Rendu du tableau de bord sur un écran de Fire TV (1920×1080, xhdpi) pour vérifier le design. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w960dp-h540dp-land-xhdpi")
class CaptureTableauDeBordTest {

    @get:Rule
    val compose = createComposeRule()

    private val meteo = Meteo(
        temperature = 13, ressenti = 11, code = 2, jour = true, majLe = 1_793_119_020_000L,
        previsions = listOf(
            Prevision("+3 h", 11, null, 3, false),
            Prevision("+6 h", 9, null, 61, false),
            Prevision("+9 h", 8, null, 0, false),
            Prevision("Demain", 15, 7, 95, true),
        ),
        maxJour = 15, minJour = 7, lever = "08:12", coucher = "17:46",
    )

    /** Fausse bannière : fond uni et nom de l'appli. */
    private fun banniere(fond: Int, texte: String, couleur: Int): ImageBitmap {
        val b = Bitmap.createBitmap(320, 180, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.drawColor(fond)
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = couleur; textSize = 46f; typeface = Typeface.DEFAULT_BOLD; textAlign = Paint.Align.CENTER
        }
        c.drawText(texte, 160f, 106f, p)
        return b.asImageBitmap()
    }

    private val tuiles by lazy {
        listOf(
            AppliTv("com.netflix.ninja", "Netflix", banniere(0xFF000000.toInt(), "NETFLIX", 0xFFE50914.toInt()), true),
            AppliTv("com.amazon.firetv.youtube", "YouTube", banniere(0xFFFFFFFF.toInt(), "YouTube", 0xFF282828.toInt()), true),
            AppliTv("com.amazon.avod", "Prime Video", banniere(0xFF0F79AF.toInt(), "prime video", 0xFFFFFFFF.toInt()), true),
            null,
            AppliTv("com.spotify.tv.android", "Spotify", banniere(0xFF121212.toInt(), "Spotify", 0xFF1ED760.toInt()), true),
            AppliTv("com.disney.disneyplus", "Disney+", banniere(0xFF0B1B4D.toInt(), "Disney+", 0xFFFFFFFF.toInt()), true),
        )
    }

    private fun capture(nom: String, focus: Int?) {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            ThemeMonAccueil(fondAnime = false) {
                TableauDeBord(
                    maintenant = ZonedDateTime.of(2026, 10, 27, 18, 58, 42, 0, PARIS),
                    meteo = EtatMeteo(meteo, horsLigne = false),
                    tuiles = tuiles,
                    onLancer = {},
                    onChoisir = {},
                    onParametres = {},
                    tuileFocaliseeApercu = focus,
                )
            }
        }
        compose.mainClock.advanceTimeBy(1500)
        compose.onRoot().captureRoboImage("build/captures/$nom.png")
    }

    @Test
    fun tableauDeBord() = capture("tableau-de-bord", focus = null)

    @Test
    fun tableauDeBordAvecFocus() = capture("tableau-de-bord-focus", focus = 0)
}
