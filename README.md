Implémentation du serveur de jeu Rummikub.

Pour compiler : gradle build

Pour lancer le serveur : java -jar build/libs/Rummikub.jar

La documentation de l'API est disponible dans doc/api-docs.json

*Pour créer un certificat auto-signé :

Le serveur utilise uniquement une connexion https, il faut donc utiliser un certificat.

Pour créer un keystore :
	keytool -genkeypair -alias rummikub -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore keystore.p12 -validity 3650 -storepass motdepasse
il faut répondre localhost à la première question

Il faut ensuite extraire la clé :
	keytool -exportcert -keystore keystore.p12 -alias rummikub -file rummikub.cer

Puis ajouter cette clé dans le keystore de java :
	sudo keytool -import -alias rummikub -keystore $JAVA_HOME/lib/security/cacerts -file rummikub.cer
le mot de passe par défaut est "changeit" ou "changeme"

Enfin il faut modifier application.properties pour ajouter le mot de passe choisi:
server.ssl.key-store-password: motdepasse
server.ssl.key-password: motdepasse
