var clientStomp = null;
var nomJoueur = null;

function initialiser(){
	var boutonConnecter=document.getElementById("connecter");
	var boutonEnvoyerMessage=document.getElementById("envoyer");
	boutonConnecter.addEventListener("click",seConnecter);
	boutonEnvoyerMessage.addEventListener("click",envoyerMessage);
}

function seConnecter(){
	nomJoueur = document.getElementById("nomJoueur").value.trim();
	if(nomJoueur) {
		var socket = new SockJS('/ws');
		clientStomp = Stomp.over(socket);
		clientStomp.connect({}, onConnecte, onErreur);
	}
}

function onConnecte() {
    clientStomp.subscribe('/topic/public', onMessageRecu);
    clientStomp.send("/app/chat.ajouterJoueur",
        {},
        JSON.stringify({joueur: nomJoueur, typeMessage: 'JOIN'})
    );
}

function onMessageRecu(payload) {
	var messageServeur = JSON.parse(payload.body);
	if(messageServeur.typeMessage === 'CHAT') {
		var listeMessagesElement = document.getElementById("chat");
		var messageElement = document.createElement("li");
		messageElement.appendChild(document.createTextNode(messageServeur.message));
		listeMessagesElement.appendChild(messageElement);

	}
	else if(messageServeur.typeMessage === 'JOIN') {
		var listeConnectes = document.getElementById("connectes");
		var joueurElement = document.createElement("li");
		joueurElement.appendChild(document.createTextNode(messageServeur.joueur));
		listeConnectes.appendChild(joueurElement);

	}
}

function onErreur(){
}

function envoyerMessage(event) {
    var message = document.getElementById("message").value;
    if(message && clientStomp) {
        var messageChat = {
            joueur: nomJoueur,
            message: message,
            typeMessage: 'CHAT'
        };
        clientStomp.send("/app/chat.envoyerMessage", {}, JSON.stringify(messageChat));
        message = '';
    }
    event.preventDefault();
}

window.addEventListener("load",initialiser);
