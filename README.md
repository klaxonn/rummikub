Implémentation du serveur de jeu Rummikub.

Pour compiler : gradle build

Pour lancer le serveur : java -jar build/libs/Rummikub.jar

La documentation de l'API est disponible dans doc/api-docs.json

*Pour créer un certificat auto-signé :

Le serveur utilise uniquement une connexion https, il faut donc utiliser un certificat.

Pour créer une clé et le certificat :
	openssl req -newkey rsa:2048 -nodes -keyout rummikub.key -x509 -days 365 -out rummikub.crt
il faut répondre localhost à la première question

Il faut ensuite générer le keystore :
	openssl pkcs12 -inkey rummikub.key -name rummikub -in rummikub.crt -export -out keystore.p12

Puis ajouter la clé dans le keystore de java :
	sudo keytool -import -alias rummikub -keystore $JAVA_HOME/lib/security/cacerts -file rummikub.crt
le mot de passe par défaut est "changeit" ou "changeme"

Enfin il faut modifier application.properties pour ajouter le mot de passe choisi:
server.ssl.key-store-password: motdepasse
