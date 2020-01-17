let clientStomp = null;
let nomJoueur = null;
let canalPartie = null;
let canalSalon = null;
let nomJoueurRegEx = /^[a-z0-9\xC0-\xFF]+$/i;


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
		let socket = new SockJS('http://localhost:8080/websocket');
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
    clientStomp.subscribe('/user/queue/canalPersonel', onMessageRecuPersonnel);
	envoyerMessageServeur("/salon/ajouterJoueurConnecte",'CONNEXION',"");
}

function envoyerMessageServeur(lien,typeMessage,message) {
	let messageServeur = {
			typeMessage: typeMessage,
            joueur: nomJoueur,
			message: message
        };
    clientStomp.send(lien, {}, JSON.stringify(messageServeur));
}

function onErreur() {
}

function onMessageRecuPersonnel(payload) {
	let messageServeur = JSON.parse(payload.body);
	switch(messageServeur.typeMessage) {
		case 'CONNEXION':
			finalisationConnexion(messageServeur);
			break;
		//Pas de break : creer Partie traitement particulier
		case 'CREER_PARTIE':
			boutonDemarrer = document.getElementById("demarrer");
			boutonDemarrer.style.display = "flex";
		case 'JOINDRE_PARTIE':
			finalisationJoindrePartie();
			break;
		case 'ERREUR':
			alert(messageServeur.message);
			break;
		default:
			console.log('type message non reconnu');
	}

}

function finalisationConnexion(messageServeur) {
	document.getElementById("connecter").disabled = true;
	//le serveur peut avoir changé le nom du joueur
	nomJoueur = messageServeur.joueur;

	let login = document.getElementById("login");
	effacerElements(login);
	login.appendChild(document.createTextNode("Vous êtes connecté en tant que " + nomJoueur));

	canalSalon = clientStomp.subscribe('/topic/joueursConnectes', onMessageRecuConnectes);
	envoyerMessageServeur("/salon/mettreAJourJoueursConnectes",'CONNEXION',"");
}

function finalisationJoindrePartie() {
	document.getElementById("joindre").disabled = true;
	canalPartie = clientStomp.subscribe('/topic/joueursPartie', onMessageRecuDemarrerPartie);
	envoyerMessageServeur("/salon/mettreAJourJoueursPartie",'JOINDRE_PARTIE',"");
}

function onMessageRecuConnectes(payload) {
	let messageServeur = JSON.parse(payload.body);
	switch(messageServeur.typeMessage) {
		case 'CONNEXION':
			traitementConnexionDeconnexion(messageServeur);
			break;
		case 'MESSAGE_CHAT':
			miseAJourMessages(messageServeur);
			break;
		case 'DECONNEXION':
			traitementConnexionDeconnexion(messageServeur);
			break;
		case 'JOINDRE_PARTIE':
			miseAJourListeJoueursPartie(messageServeur);
			break;
		case 'ERREUR':
			alert(messageServeur.message);
			break;
		default:
			console.log('type message non reconnu');
	}
}

function traitementConnexionDeconnexion(messageServeur){
	let listeConnectes = document.getElementById("connectes");
	let listeJoueurs = document.getElementById("joueurs");


	let stringListesJoueurs = messageServeur.message.split(";");
	mettreAJourListeJoueurs(stringListesJoueurs[0],listeConnectes);

	effacerElements(listeJoueurs);

	let boutonJoindre = document.getElementById("joindre");
	let sectionJoueurs = document.getElementById("joueurs-section");
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

function miseAJourMessages(messageServeur){
	let listeMessagesElement = document.getElementById("chat");
	let listeMessages = document.getElementById("messages-section");
	let messageElement = document.createElement("li");
	let messageDansChat = messageServeur.joueur + ": " + messageServeur.message;
	messageElement.appendChild(document.createTextNode(messageDansChat));
	listeMessagesElement.appendChild(messageElement);
	//affiche toujours le dernier message envoyé
	listeMessages.scrollTop = listeMessages.scrollHeight;
}

function miseAJourListeJoueursPartie(messageServeur){
	document.getElementById("joueurs-section").style.display = "flex";
	document.getElementById("joindre").value = 'Joindre partie';
	let listeJoueurs = document.getElementById("joueurs");
	mettreAJourListeJoueurs(messageServeur.message,listeJoueurs);
}

function mettreAJourListeJoueurs(listeJoueurs,liste){

	effacerElements(liste);

	//On enlève les crochets
	listeJoueurs = listeJoueurs.slice(1,-1);
	let arrayListeJoueurs = listeJoueurs.split(",");
	for (const nomJoueurListe of arrayListeJoueurs){
		let joueurElement = document.createElement("li");
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
    let message = document.getElementById("message").value;
    if(message && clientStomp) {
		envoyerMessageServeur("/topic/joueursConnectes",'MESSAGE_CHAT',message);
        document.getElementById("message").value = '';
    }
}

function joindrePartie(event) {
    if(clientStomp) {
		envoyerMessageServeur("/salon/joindrePartie",'JOINDRE_PARTIE',"");
    }
}


function demarrerPartie(event) {
    if(clientStomp) {
        envoyerMessageServeur("/salon/demarrerPartie","DEMARRER_PARTIE","");
    }
}

function onMessageRecuDemarrerPartie(payload) {
	canalSalon.unsubscribe();
	canalPartie.unsubscribe();
	window.location.href="partie.html";
}

window.addEventListener("load",initialiser);
