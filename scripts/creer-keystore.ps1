# Crée UNE SEULE FOIS la clé de signature de « Mon Accueil ».
# Tous les APK doivent être signés avec cette même clé pour s'installer par-dessus l'ancienne version.
#
# Résultat, dans C:\Users\<toi>\dev\cle-signature (EN DEHORS du projet, jamais envoyé sur GitHub) :
#   - mon-accueil.jks            : la clé (à sauvegarder sur une clé USB / un cloud perso)
#   - A-GARDER-mot-de-passe.txt  : le mot de passe et l'alias
#   - keystore-base64.txt        : la clé en texte, à coller dans le secret GitHub KEYSTORE_BASE64
# Et keystore.properties dans le projet (ignoré par Git) pour compiler sur ce PC.

param(
    [string]$Dossier = "$env:USERPROFILE\dev\cle-signature",
    [string]$Keytool = "keytool"
)
$ErrorActionPreference = "Stop"

$jks = Join-Path $Dossier "mon-accueil.jks"
if (Test-Path $jks) {
    Write-Host "La clé existe déjà ($jks). Ne la recrée surtout pas : les mises à jour ne s'installeraient plus."
    exit 1
}
New-Item -ItemType Directory -Force $Dossier | Out-Null

# Mot de passe aléatoire (lettres et chiffres, 24 caractères)
$car = [char[]]'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789'
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$octets = New-Object byte[] 24
$rng.GetBytes($octets)
$motDePasse = -join ($octets | ForEach-Object { $car[$_ % $car.Length] })
$alias = "monaccueil"

& $Keytool -genkeypair -v -keystore $jks -storetype PKCS12 -alias $alias `
    -keyalg RSA -keysize 2048 -validity 10000 `
    -storepass $motDePasse -keypass $motDePasse `
    -dname "CN=Mon Accueil, O=Loic, C=FR" | Out-Null
if ($LASTEXITCODE -ne 0) { throw "keytool a échoué" }

[IO.File]::WriteAllText((Join-Path $Dossier "keystore-base64.txt"), [Convert]::ToBase64String([IO.File]::ReadAllBytes($jks)))
@"
Clé de signature de Mon Accueil — À GARDER PRÉCIEUSEMENT (et à sauvegarder ailleurs que sur ce PC)

Secrets à créer sur GitHub (Settings > Secrets and variables > Actions) :
  KEYSTORE_BASE64   = tout le contenu du fichier keystore-base64.txt
  KEYSTORE_PASSWORD = $motDePasse
  KEY_ALIAS         = $alias
"@ | Out-File -Encoding utf8 (Join-Path $Dossier "A-GARDER-mot-de-passe.txt")

$projet = Split-Path $PSScriptRoot -Parent
@"
KEYSTORE_FILE=$($jks.Replace('\', '/'))
KEYSTORE_PASSWORD=$motDePasse
KEY_ALIAS=$alias
"@ | Out-File -Encoding ascii (Join-Path $projet "keystore.properties")

Write-Host "Clé créée dans $Dossier"
