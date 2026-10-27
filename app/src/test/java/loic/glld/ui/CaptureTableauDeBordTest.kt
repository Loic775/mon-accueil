package loic.glld.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import loic.glld.applis.AppliTv
import loic.glld.applis.AppsRepository
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
        temperature = 12, ressenti = 10, code = 2, jour = true, majLe = 0L,
        previsions = listOf(
            Prevision("+3 h", 11, null, 3, false),
            Prevision("+6 h", 9, null, 61, false),
            Prevision("+9 h", 8, null, 0, false),
            Prevision("Demain", 15, 7, 95, true),
        ),
    )

    private val tuiles = listOf(
        AppliTv("com.netflix.ninja", "Netflix", null, false),
        AppliTv("com.amazon.firetv.youtube", "YouTube", null, false),
        null,
        AppliTv("com.spotify.tv.android", "Spotify", null, false),
        AppliTv(AppsRepository.PARAMETRES, "Paramètres", null, false),
    )

    @Test
    fun tableauDeBord() {
        compose.setContent {
            ThemeMonAccueil {
                TableauDeBord(
                    maintenant = ZonedDateTime.of(2026, 10, 27, 9, 41, 0, 0, PARIS),
                    meteo = EtatMeteo(meteo, horsLigne = false),
                    tuiles = tuiles,
                    onLancer = {},
                    onChoisir = {},
                )
            }
        }
        compose.onRoot().captureRoboImage("build/captures/tableau-de-bord.png")
    }
}
