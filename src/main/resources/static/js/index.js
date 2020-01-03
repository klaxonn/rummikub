var clientStomp = null;
var nomJoueur = null;
var canalConnexion = null;
var canalJoindre = null;
var canalPartie = null;
var nomJoueurRegEx = /^[a-z0-9\xC0-\xFF]+$/i;


function initialiser(){
	document.getElementById("connecter").addEventListener("click",seConnecter);
	document.getElementById("envoyer").addEventListener("click",envoyerMessageChat);
	document.getElementById("joindre").addEventListener("click",joindrePartie);
	document.getElementById("demarrer").addEventListener("click",demarrerPartie);
	document.getElementById("nomJoueur").addEventListener("keyup", 
		function(event){appuyerEntreeChampTexte(event,seConnecter);});
	document.getElementById("message").addEventListener("keyup", 
		function(event){appuyerEntreeChampTexte(event,envoyerMessageChat);});
}

function appuyerEntreeChampTexte(event,fonction) {
	//On appuie sur entrée
	if (event.keyCode === 13) {
    	fonction();
	}
}

function seConnecter(){
	nomJoueur = document.getElementById("nomJoueur").value.trim();
	if(valideNomJoueur(nomJoueur)) {
		var socket = new SockJS('/wss');
		clientStomp = Stomp.over(socket);
		clientStomp.connect({}, onConnexion, onErreur);
	}
	else {
		alert("Nom invalide");
		document.getElementById("nomJoueur").value = "";
	}
}

function valideNomJoueur(nomJoueur) {
	return 	nomJoueur.length > 0 && nomJoueur.length < 15
			&& nomJoueurRegEx.test(nomJoueur);
}

function onConnexion() {
    canalConnexion = clientStomp.subscribe('/connexionJoueur', onMessageRecuConnexion);
	envoyerMessageServeur("/salon/ajouterJoueurConnecte",'CONNEXION',"");
}

function envoyerMessageServeur(lien,typeMessage,message) {
	var messageServeur = {
			typeMessage: typeMessage,
            joueur: nomJoueur,
			message: message
        };
    clientStomp.send(lien, {}, JSON.stringify(messageServeur));
}

function onErreur() {
}

function onMessageRecuConnexion(payload) {
	var messageServeur = JSON.parse(payload.body);
	if(messageServeur.typeMessage === 'CONNEXION') {
		document.getElementById("connecter").disabled = true;
		//le serveur peut avoir changé le nom du joueur
		nomJoueur = messageServeur.joueur;

		var login = document.getElementById("login");
		effacerElements(login);
		login.appendChild(document.createTextNode("Vous êtes connecté en tant que " + nomJoueur));
		
		canalConnexion.unsubscribe();
		clientStomp.subscribe('/joueursConnectes', onMessageRecuConnectes);
		envoyerMessageServeur("/salon/mettreAJourJoueursConnectes",'CONNEXION',"");
	}
}

function onMessageRecuConnectes(payload) {
	var messageServeur = JSON.parse(payload.body);
	switch(messageServeur.typeMessage) {
		case 'CONNEXION':
			messageRecuConnexionDeconnexion(messageServeur);
			break;
		case 'MESSAGE_CHAT':
			messageRecuMessageChat(messageServeur);
			break;
		case 'DECONNEXION':
			messageRecuConnexionDeconnexion(messageServeur);
			break;
		case 'JOINDRE_PARTIE':
			messageRecuJoindrePartie(messageServeur);
			break;
		case 'ERREUR':
			alert(messageServeur.message);
			break;
		default:
			console.log('type message non reconnu');
	}
}

function messageRecuConnexionDeconnexion(messageServeur){
	var listeConnectes = document.getElementById("connectes");
	var listeJoueurs = document.getElementById("joueurs");

	
	var stringListesJoueurs = messageServeur.message.split(";");
	mettreAJourListeJoueurs(stringListesJoueurs[0],listeConnectes);

	effacerElements(listeJoueurs);

	var boutonJoindre = document.getElementById("joindre");
	var sectionJoueurs = document.getElementById("joueurs-section");
	if(stringListesJoueurs[1]){
		sectionJoueurs.style.display = "flex";
		boutonJoindre.value = 'Joindre partie';
		mettreAJourListeJoueurs(stringListesJoueurs[1],listeJoueurs);
	}
    else {
		sectionJoueurs.style.display = "none";
        boutonJoindre.value = 'Créer partie';
        boutonJoindre.disabled = false;
     }   
}

function messageRecuMessageChat(messageServeur){
	var listeMessagesElement = document.getElementById("chat");
	var listeMessages = document.getElementById("messages-section");
	var messageElement = document.createElement("li");
	var messageDansChat = messageServeur.joueur + ": " + messageServeur.message;
	messageElement.appendChild(document.createTextNode(messageDansChat));
	listeMessagesElement.appendChild(messageElement);
	//affiche toujours le dernier message envoyé
	listeMessages.scrollTop = listeMessages.scrollHeight;
}

function messageRecuJoindrePartie(messageServeur){
	document.getElementById("joueurs-section").style.display = "flex";
	document.getElementById("joindre").value = 'Joindre partie';
	var listeJoueurs = document.getElementById("joueurs");
	mettreAJourListeJoueurs(messageServeur.message,listeJoueurs);
}

function mettreAJourListeJoueurs(listeJoueurs,liste){
	
	effacerElements(liste);
	
	//On enlève les crochets
	listeJoueurs = listeJoueurs.slice(1,-1);
	var arrayListeJoueurs = listeJoueurs.split(",");
	for (const nomJoueurListe of arrayListeJoueurs){
		var joueurElement = document.createElement("li");
		joueurElement.appendChild(document.createTextNode(nomJoueurListe));
		liste.appendChild(joueurElement);
	}
}

function effacerElements(elementContenant){
	//On supprime les joueurs dans la liste
	while (elementContenant.firstChild) {
    	elementContenant.firstChild.remove();
	}
}

function envoyerMessageChat() {
    var message = document.getElementById("message").value;
    if(message && clientStomp) {
		envoyerMessageServeur("/salon/envoyerMessageChat",'MESSAGE_CHAT',message);
        document.getElementById("message").value = '';
    }
}

function joindrePartie(event) {
    if(clientStomp) {
		canalJoindre = clientStomp.subscribe('/JoueurAAjouter', onMessageRecuJoindrePartie);
		envoyerMessageServeur("/salon/joindrePartie",'JOINDRE_PARTIE',"");
    }
}

function onMessageRecuJoindrePartie(payload) {
	var messageServeur = JSON.parse(payload.body);
	//Le joueur a créé une partie, il a le pouvoir de la démarrer
	if(messageServeur.typeMessage === 'CREER_PARTIE') {
		var boutonDemarrer = document.getElementById("demarrer");
		boutonDemarrer.style.display = "flex";
	}
	document.getElementById("joindre").disabled = true;
	canalJoindre.unsubscribe();
	canalPartie = clientStomp.subscribe('/DemarrerPartie', onMessageRecuDemarrerPartie);
	envoyerMessageServeur("/salon/mettreAJourJoueursPartie",'JOINDRE_PARTIE',"");
}

function demarrerPartie(event) {
    if(clientStomp) {
        envoyerMessageServeur("/salon/demarrerPartie","DEMARRER_PARTIE","");
    }
}

function onMessageRecuDemarrerPartie(payload) {
	canalConnexion.unsubscribe();
	canalPartie.unsubscribe();
	clientStomp.disconnect();
	window.location.href="partie.html";
}

window.addEventListener("load",initialiser);
