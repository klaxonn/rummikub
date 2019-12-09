var clientStomp = null;
var nomJoueur = null;

function initialiser(){
	var bouton=document.getElementById("connecter");
	bouton.addEventListener("click",seConnecter);
}

function seConnecter(){
	nomJoueur = document.getElementById("nomJoueur").value.trim();
	if(nomJoueur) {
		//var socket = new WebSocket("wss://localhost:12345/chat/");
		var socket = new SockJS('/ws');
		clientStomp = Stomp.over(socket);
		clientStomp.connect({}, onConnecte, onErreur);
	}
}

function onConnecte() {
    clientStomp.subscribe('/topic/public', onMessageRecu);
    clientStomp.send("chat.ajouterJoueur",
        {},
        JSON.stringify({nomJoueur: nomJoueur, type: 'JOIN'})
    );
}

function onMessageRecu() {
}

function onErreur(){
}

window.addEventListener("load",initialiser);
