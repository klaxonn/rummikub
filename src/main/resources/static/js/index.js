var clientStomp = null;
var nomJoueur = null;
var connexion = null;

function initialiser(){
	var boutonConnecter=document.getElementById("connecter");
	var boutonEnvoyerMessage=document.getElementById("envoyer");
	var boutonJoindrePartie=document.getElementById("joindre");
	boutonConnecter.addEventListener("click",seConnecter);
	boutonEnvoyerMessage.addEventListener("click",envoyerMessage);
	boutonJoindrePartie.addEventListener("click",joindrePartie);
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
    connexion = clientStomp.subscribe('/connexionJoueur', onMessageRecuConnexion);
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
		connexion.unsubscribe();
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
		mettreAJourListeJoueursConnectes(messageServeur);
	}
	else if(messageServeur.typeMessage === 'MESSAGE_CHAT') {
		var listeMessagesElement = document.getElementById("chat");
		var messageElement = document.createElement("li");
		var messageDansChat = messageServeur.joueur + ": " + messageServeur.message;
		messageElement.appendChild(document.createTextNode(messageDansChat));
		listeMessagesElement.appendChild(messageElement);
	}

	else if(messageServeur.typeMessage === 'DECONNEXION') {
		mettreAJourListeJoueursConnectes(messageServeur);
	}
}

function mettreAJourListeJoueursConnectes(messageServeur){

	var listeConnectes = document.getElementById("connectes");

	//On supprime les joueurs dans la liste
	while (listeConnectes.firstChild) {
    	listeConnectes.firstChild.remove();
	}

	//On ajoute la liste des joueurs reçus
	var listeJoueurs = messageServeur.message;
	listeJoueurs = listeJoueurs.slice(1,-1);
	var arrayListeJoueurs = listeJoueurs.split(",");
	for (var i=0; i < arrayListeJoueurs.length; i++){
		var nomJoueurListe = arrayListeJoueurs[i];
		var joueurElement = document.createElement("li");
		joueurElement.appendChild(document.createTextNode(nomJoueurListe));
		listeConnectes.appendChild(joueurElement);
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
        var messageServeurJoindre = {
            joueur: nomJoueur,
            typeMessage: 'JOINDRE_PARTIE'
        };
        clientStomp.send("/salon/joindrePartie", {}, JSON.stringify(messageServeurJoindre));
    }
    event.preventDefault();
}

window.addEventListener("load",initialiser);
