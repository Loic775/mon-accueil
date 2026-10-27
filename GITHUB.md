# Mettre le projet sur GitHub (à faire une seule fois)

GitHub va compiler l'appli tout seul à chaque modification et te donner un **lien fixe vers l'APK**,
à taper dans Downloader sur la télé. Compte une quinzaine de minutes.

> Le dépôt sera **public** (sinon Downloader ne pourrait pas télécharger l'APK).
> Ce n'est pas un problème : il ne contient ni mot de passe, ni clé, ni adresse d'agenda.
> Ces informations restent sur ton PC ou sur la télé.

## 1. Créer un compte GitHub
1. Va sur <https://github.com/signup> et crée un compte gratuit.
2. Choisis un **pseudo** court (il apparaîtra dans le lien de l'APK), par exemple `loicglld`.

## 2. Créer le dépôt (le « repo »)
1. Une fois connecté, va sur <https://github.com/new>.
2. **Repository name** : `mon-accueil`
3. Coche **Public**.
4. Ne coche **rien** d'autre (pas de README, pas de .gitignore, pas de licence).
5. Clique sur **Create repository**.

## 3. Ajouter la clé de signature dans les « secrets »
GitHub a besoin de la clé pour signer l'APK. Elle est rangée sur ton PC, dans
`C:\Users\lglld\dev\cle-signature\` :

| Fichier | Rôle |
|---|---|
| `mon-accueil.jks` | la clé elle-même |
| `keystore-base64.txt` | la même clé, convertie en texte (« base64 ») pour pouvoir la coller dans GitHub |
| `A-GARDER-mot-de-passe.txt` | le mot de passe et le nom de la clé |

Sur la page de ton dépôt :

1. Clique sur **Settings** (l'onglet avec un engrenage, tout à droite).
2. Dans le menu de gauche, ouvre **Secrets and variables**, puis **Actions**.
3. Clique sur **New repository secret** et crée ces 3 secrets, un par un :

   | Name (à recopier exactement) | Secret (la valeur) |
   |---|---|
   | `KEYSTORE_BASE64` | ouvre `keystore-base64.txt` avec le Bloc-notes, fais **Ctrl+A** puis **Ctrl+C**, et colle |
   | `KEYSTORE_PASSWORD` | le mot de passe écrit dans `A-GARDER-mot-de-passe.txt` |
   | `KEY_ALIAS` | `monaccueil` |

4. Clique sur **Add secret** après chacun.

> ⚠️ **Sauvegarde le dossier `cle-signature`** sur une clé USB ou ton cloud perso, dans un endroit
> privé. Si tu le perds, les futures versions ne pourront plus s'installer par-dessus l'ancienne :
> il faudrait désinstaller l'appli et refaire tes réglages.

## 4. Envoyer le code
Donne-moi ton pseudo GitHub et je m'occupe de l'envoi. Une fenêtre **« Connect to GitHub »** va
s'ouvrir : clique sur **Sign in with your browser**, puis sur **Authorize**. Il faut le faire
une seule fois, Windows s'en souviendra ensuite.

(Pour info, voici les commandes que je lance :)
```bash
git remote add origin https://github.com/<pseudo>/mon-accueil.git
git push -u origin main
```

## 5. Vérifier que l'APK est prêt
1. Sur la page du dépôt, clique sur l'onglet **Actions**. Un travail « APK » tourne (environ 5 minutes).
2. Une coche verte ✅ veut dire que c'est terminé. Une croix rouge ❌ veut dire qu'il y a un problème :
   clique dessus et préviens-moi. La cause la plus fréquente est un secret mal recopié.
3. Ton lien direct, qui ne change jamais :
   ```
   https://github.com/<pseudo>/mon-accueil/releases/download/latest/MonAccueil.apk
   ```

## 6. Bonus : un code court pour Downloader
Taper ce long lien à la télécommande est pénible. Le site d'AFTVnews (les créateurs de Downloader) le transforme en un code de quelques chiffres :

1. Va sur <https://go.aftvnews.com/> depuis ton PC.
2. Colle ton lien direct et valide.
3. Note le **code** obtenu (par exemple `123456`).

Dans Downloader, tape simplement ce code à la place de l'URL. Comme le lien ne change jamais,
**le même code fonctionne pour toutes les mises à jour**.
