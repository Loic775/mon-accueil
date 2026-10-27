package loic.glld.applis

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.provider.Settings
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AppliTv(
    val paquet: String,
    val nom: String,
    /** Bannière 16:9 si l'appli en a une, sinon son icône. */
    val image: ImageBitmap?,
    val estBanniere: Boolean,
)

/** Liste les applis installées et les lance. */
class AppsRepository(private val context: Context) {
    private val pm: PackageManager = context.packageManager

    suspend fun lister(): List<AppliTv> = withContext(Dispatchers.Default) {
        val tv = pm.queryIntentActivities(intentPrincipal(Intent.CATEGORY_LEANBACK_LAUNCHER), 0)
        val classiques = pm.queryIntentActivities(intentPrincipal(Intent.CATEGORY_LAUNCHER), 0)
        // Une entrée par appli ; la version TV (leanback) est prioritaire.
        val parPaquet = LinkedHashMap<String, ResolveInfo>()
        (tv + classiques).forEach { parPaquet.putIfAbsent(it.activityInfo.packageName, it) }
        parPaquet.remove(context.packageName)

        val applis = parPaquet.values.map { info ->
            val banniere: Drawable? = info.activityInfo.loadBanner(pm)
                ?: runCatching { pm.getApplicationBanner(info.activityInfo.packageName) }.getOrNull()
            val image = if (banniere != null) {
                banniere.versImage(LARGEUR_BANNIERE, HAUTEUR_BANNIERE)
            } else {
                info.loadIcon(pm)?.versImage(TAILLE_ICONE, TAILLE_ICONE)
            }
            AppliTv(
                paquet = info.activityInfo.packageName,
                nom = info.loadLabel(pm).toString(),
                image = image,
                estBanniere = banniere != null,
            )
        }.sortedBy { it.nom.lowercase() }

        listOf(AppliTv(PARAMETRES, "Paramètres", null, false)) + applis
    }

    /** Renvoie false si l'appli n'a pas pu être ouverte. */
    fun lancer(paquet: String): Boolean {
        val intent = if (paquet == PARAMETRES) {
            Intent(Settings.ACTION_SETTINGS)
        } else {
            pm.getLeanbackLaunchIntentForPackage(paquet) ?: pm.getLaunchIntentForPackage(paquet)
        } ?: return false
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return runCatching { context.startActivity(intent) }.isSuccess
    }

    private fun intentPrincipal(categorie: String) =
        Intent(Intent.ACTION_MAIN).addCategory(categorie)

    // Images réduites pour économiser la mémoire du Fire Stick.
    private fun Drawable.versImage(largeur: Int, hauteur: Int): ImageBitmap? =
        runCatching { toBitmap(largeur, hauteur).asImageBitmap() }.getOrNull()

    companion object {
        /** Pseudo-appli : ouvre les Paramètres de la Fire TV. */
        const val PARAMETRES = "__parametres__"
        private const val LARGEUR_BANNIERE = 320
        private const val HAUTEUR_BANNIERE = 180
        private const val TAILLE_ICONE = 128
    }
}
