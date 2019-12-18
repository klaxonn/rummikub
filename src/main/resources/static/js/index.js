var clientStomp = null;
var nomJoueur = null;
var canalConnexion = null;
var canalJoindre = null;
var canalPartie = null;


function initialiser(){
	var boutonConnecter=document.getElementById("connecter");
	var boutonEnvoyerMessage=document.getElementById("envoyer");
	var boutonJoindrePartie=document.getElementById("joindre");
	var boutonDemarrerPartie=document.getElementById("demarrer");
	boutonConnecter.addEventListener("click",seConnecter);
	boutonEnvoyerMessage.addEventListener("click",envoyerMessage);
	boutonJoindrePartie.addEventListener("click",joindrePartie);
	boutonDemarrerPartie.addEventListener("click",demarrerPartie);
}

function seConnecter(){
	nomJoueur = document.getElementById("nomJoueur").value.trim();
	if(nomJoueur) {
		var socket = new SockJS('/ws');
		clientStomp = Stomp.over(socket);
		clientStomp.connect({}, onConnexion, onErreur);
	}
}

function onConnexion() {
    canalConnexion = clientStomp.subscribe('/connexionJoueur', onMessageRecuConnexion);
    clientStomp.send("/salon/ajouterJoueurConnecte",
        {},
        JSON.stringify({joueur: nomJoueur, typeMessage: 'CONNEXION'})
    );
}

function onMessageRecuConnexion(payload) {
	var messageServeur = JSON.parse(payload.body);
	if(messageServeur.typeMessage === 'CONNEXION') {
		//le serveur peut avoir changé le nom du joueur
		nomJoueur = messageServeur.joueur;
		canalConnexion.unsubscribe();
		clientStomp.subscribe('/joueursConnectes', onMessageRecuConnectes);
		clientStomp.send("/salon/mettreAJourJoueursConnectes",
        {},
        JSON.stringify({joueur: nomJoueur, typeMessage: 'CONNEXION'})
    );
	}
}


function onErreur() {
}

function onMessageRecuConnectes(payload) {
	var messageServeur = JSON.parse(payload.body);
	if(messageServeur.typeMessage === 'CONNEXION') {
		var listeConnectes = document.getElementById("connectes");
		var listeJoueurs = document.getElementById("joueurs");
		var arrayListesJoueurs = messageServeur.message.split(";");
		mettreAJourListeJoueurs(arrayListesJoueurs[0],listeConnectes);
		if(arrayListesJoueurs.length == 2){
			mettreAJourListeJoueurs(arrayListesJoueurs[1],listeJoueurs);
			document.getElementById("joindre").value = 'Joindre partie';
		}
	}
	else if(messageServeur.typeMessage === 'MESSAGE_CHAT') {
		var listeMessagesElement = document.getElementById("chat");
		var messageElement = document.createElement("li");
		var messageDansChat = messageServeur.joueur + ": " + messageServeur.message;
		messageElement.appendChild(document.createTextNode(messageDansChat));
		listeMessagesElement.appendChild(messageElement);
	}
	else if(messageServeur.typeMessage === 'DECONNEXION') {
		var listeConnectes = document.getElementById("connectes");
		var listeJoueurs = document.getElementById("joueurs");
		var arrayListesJoueurs = messageServeur.message.split(";");

		mettreAJourListeJoueurs(arrayListesJoueurs[0],listeConnectes);
		if(arrayListesJoueurs[1]){
			document.getElementById("joindre").value = 'Créer partie';
		}
		mettreAJourListeJoueurs(arrayListesJoueurs[1],listeJoueurs);
	}
	else if(messageServeur.typeMessage === 'JOINDRE_PARTIE') {
		document.getElementById("joindre").value = 'Joindre partie';
		var listeJoueurs = document.getElementById("joueurs");
		mettreAJourListeJoueurs(messageServeur.message,listeJoueurs);
	}
}

function mettreAJourListeJoueurs(listeJoueurs,liste){

	//On supprime les joueurs dans la liste
	while (liste.firstChild) {
    	liste.firstChild.remove();
	}

	//On ajoute la liste des joueurs reçus
	listeJoueurs = listeJoueurs.slice(1,-1);
	var arrayListeJoueurs = listeJoueurs.split(",");
	for (var i=0; i < arrayListeJoueurs.length; i++){
		var nomJoueurListe = arrayListeJoueurs[i];
		if(nomJoueurListe){
			var joueurElement = document.createElement("li");
			joueurElement.appendChild(document.createTextNode(nomJoueurListe));
			liste.appendChild(joueurElement);
		}
	}
}

function envoyerMessage(event) {
    var message = document.getElementById("message").value;
    if(message && clientStomp) {
        var messageServeurChat = {
            joueur: nomJoueur,
            message: message,
            typeMessage: 'MESSAGE_CHAT'
        };
        clientStomp.send("/salon/envoyerMessageChat", {}, JSON.stringify(messageServeurChat));
        document.getElementById("message").value = '';
    }
    event.preventDefault();
}

function joindrePartie(event) {
    if(clientStomp) {
		canalJoindre = clientStomp.subscribe('/JoueurAAjouter', onMessageRecuJoindrePartie);
        var messageServeurJoindre = {
            joueur: nomJoueur,
            typeMessage: 'JOINDRE_PARTIE'
        };
        clientStomp.send("/salon/joindrePartie", {}, JSON.stringify(messageServeurJoindre));
    }
    event.preventDefault();
}

function onMessageRecuJoindrePartie(payload) {
	var messageServeur = JSON.parse(payload.body);
	if(messageServeur.typeMessage === 'CREER_PARTIE') {
		var boutonDemarrer = document.getElementById("demarrer");
		boutonDemarrer.style.display = "flex";
	}
	canalJoindre.unsubscribe();
	canalPartie = clientStomp.subscribe('/DemarrerPartie', onMessageRecuDemarrerPartie);
	clientStomp.send("/salon/mettreAJourJoueursPrets",
		{},
        JSON.stringify({joueur: nomJoueur, typeMessage: 'JOINDRE_PARTIE'})
    );
}

function demarrerPartie(event) {
    if(clientStomp) {
        var messageServeurDemarrer = {
            joueur: nomJoueur
        };
        clientStomp.send("/salon/demarrerPartie", {}, JSON.stringify(messageServeurDemarrer));
    }
    event.preventDefault();
}

function onMessageRecuDemarrerPartie(payload) {
	canalConnexion.unsubscribe();
	canalPartie.unsubscribe();
	clientStomp.disconnect();
	window.location.href="partie.html";
}

window.addEventListener("load",initialiser);
