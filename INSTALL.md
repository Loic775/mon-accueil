# Installer « Mon Accueil » sur la Fire TV

Il te faut :
- ton **code Downloader** : **`3771939`**
- (ou le lien direct : `https://github.com/Loic775/mon-accueil/releases/download/latest/MonAccueil.apk`)

## 1. Installer Downloader
1. Sur l'accueil de la Fire TV, va sur **Rechercher** (la loupe).
2. Tape `Downloader` et choisis l'appli **Downloader** (icône orange, éditeur AFTVnews).
3. Clique sur **Télécharger** (ou **Obtenir**). C'est gratuit.

## 2. Activer les Options pour les développeurs
1. Va dans **Paramètres** (l'engrenage), puis **Ma Fire TV**, puis **À propos**.
2. Mets-toi sur la première ligne (le nom de l'appareil, par exemple « Fire TV Stick 4K ») et
   **appuie 7 fois de suite sur le bouton central** de la télécommande.
3. Le message « Vous êtes maintenant développeur » s'affiche.
4. Reviens en arrière avec Retour : une nouvelle ligne **Options pour les développeurs** est apparue dans **Ma Fire TV**.

## 3. Autoriser Downloader à installer des applis
1. **Paramètres**, puis **Ma Fire TV**, puis **Options pour les développeurs**.
2. Ouvre **Installer des applis inconnues**.
3. Mets **Downloader** sur **ACTIVÉ**.

## 4. Télécharger et installer l'appli
1. Ouvre **Downloader**. Au premier lancement, autorise l'accès aux fichiers et ferme les messages d'accueil.
2. Dans l'onglet **Home**, sélectionne le champ d'adresse et tape **ton code**
   (ou le lien complet), puis **Go**.
3. Le téléchargement démarre (environ 1 Mo). L'écran d'installation s'ouvre ensuite : choisis **Installer**.
4. À la fin, choisis **Ouvrir** pour lancer « Mon Accueil ».
   De retour dans Downloader, choisis **Supprimer** (Delete) deux fois pour effacer le fichier
   téléchargé : l'appli reste installée et tu libères de la place.

**Mises à jour :** refais simplement l'étape 4 avec le même code. L'écran affiche « Voulez-vous
installer une mise à jour… ? ». Réponds **Installer**. Tes réglages (choix des tuiles, etc.) sont conservés.

## 5. Mettre « Mon Accueil » en premier dans la rangée d'applis
1. Maintiens le bouton **Accueil** (la maison) enfoncé 2 secondes, puis choisis **Applis**.
2. Sélectionne **Mon Accueil** (sans l'ouvrir) et appuie sur le bouton **Menu** (☰, trois traits).
3. Choisis **Déplacer** (ou **Déplacer au début**). Amène l'icône tout en haut à gauche avec les flèches, puis valide avec le bouton central.
4. L'appli apparaît maintenant dans la rangée d'applis de l'accueil Amazon. Il te suffit de la sélectionner pour l'ouvrir.

> Le bouton **Accueil** de la télécommande ramène toujours à l'accueil Amazon : c'est voulu par
> Amazon et l'appli ne cherche pas à l'empêcher.

## Utilisation à la télécommande
- **Flèches** : se déplacer. L'élément sélectionné grossit et devient orange.
- **Bouton central** sur une tuile : ouvre l'appli.
- **Appui long sur le bouton central** (ou bouton **☰ Menu**) sur une tuile : choisir quelle appli y mettre.
- **Retour** : une fenêtre demande si tu veux vraiment quitter.

---

## Alternative pour les mises à jour rapides : ADB en Wi-Fi depuis le PC
Pratique pour installer une nouvelle version en quelques secondes, sans passer par Downloader.

**Une seule fois, sur la télé :**
1. **Paramètres**, puis **Ma Fire TV**, puis **Options pour les développeurs** : mets **Débogage ADB** sur **ACTIVÉ**.
2. **Paramètres**, puis **Ma Fire TV**, puis **À propos**, puis **Réseau** : note l'**adresse IP** (par exemple `192.168.1.42`).

**Sur le PC** (l'outil `adb` est déjà installé dans `C:\Users\lglld\dev\outils\sdk\platform-tools`) :
```bash
C:\Users\lglld\dev\outils\sdk\platform-tools\adb.exe connect 192.168.1.42:5555
```
La première fois, la télé affiche « Autoriser le débogage USB ? ». Coche **Toujours autoriser
depuis cet ordinateur**, puis choisis **OK**. Ensuite, installe la mise à jour :
```bash
C:\Users\lglld\dev\outils\sdk\platform-tools\adb.exe install -r MonAccueil.apk
```
Tu peux aussi simplement me donner l'adresse IP : je lance ces commandes pour toi.
