package rummikub.salon;

import rummikub.ihm.ControleurParties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controleur du salon du connexion et du chat.
 */
@Controller
public class ControleurChat {

	private static final Logger logger = LoggerFactory.getLogger(ControleurChat.class);
	private final ListeJoueurs listeJoueurs;
	private ControleurParties controleurParties;

	@Autowired
	public ControleurChat(ListeJoueurs listeJoueurs, ControleurParties controleurParties) {
		this.listeJoueurs = listeJoueurs;
		this.controleurParties = controleurParties;
	}

	/**
	 * Appelé quand un nouveau client se connecte au salon.
	 * Le message est transmis au nouveau client pour mettre à jour son nom.
	 * Si le nom du joueur n'est pas valide, le message sera de type ERREUR
	 *
	 * @param message le message reçu du client
	 * @param headerAccessor informations sur le client
	 * @return le même message avec son nom à jour
	 */
	@MessageMapping("/ajouterJoueurConnecte")
    @SendToUser("/queue/canalPersonel")
    public MessageChat ajouterJoueurConnecte(@Payload MessageChat message,
                               SimpMessageHeaderAccessor headerAccessor) {
		try {
			String nomJoueur = listeJoueurs.ajouterJoueurConnecte(message.getJoueur());
			logger.info("Ajout Joueur");
			headerAccessor.getSessionAttributes().put("nomJoueur", nomJoueur);
			message.setJoueur(nomJoueur);
		}
		catch(UnsupportedOperationException e){
			message = creerMessageErreur(message, e.getMessage());
		}
        return message;
    }

	/**
	 * Appelé pour mettre à jour la liste des clients connectés.
	 * Le message est retransmis à tous les clients.
	 *
	 * @param message le message reçu du client
	 * @return le même message avec la liste des clients connectés
	 */
	@MessageMapping("/mettreAJourJoueursConnectes")
    @SendTo("/topic/joueursConnectes")
    public MessageChat mettreAJourJoueursConnectes(@Payload MessageChat message) {
		String listeAEnvoyer = listeJoueurs.getJoueursConnectes().toString();
		if(listeJoueurs.nombreJoueursPartie() > 0){
			listeAEnvoyer += ";" + listeJoueurs.getJoueursPartie().toString();
		}
		message.setMessage(listeAEnvoyer);
        logger.info("Liste des listes" + listeAEnvoyer);
        return message;
    }

	/**
	 * Appelé quand un client joint une partie.
	 * Si aucune partie n'existe, le client est considéré comme créateur.
	 * et le type de message est CREER_PARTIE.
	 * Si la partie est complète ou le joueur non reconnu,
	 * le message sera de type ERREUR
	 * @param message le message reçu du client
	 * @return le message.
	 */
    @MessageMapping("/joindrePartie")
    @SendToUser("/queue/canalPersonel")
    public MessageChat ajouterJoueurPartie(@Payload MessageChat message) {
		try{
			listeJoueurs.ajouterJoueurPartie(message.getJoueur());
			if(listeJoueurs.nombreJoueursPartie() == 1){
				message.setTypeMessage(MessageChat.TypeMessage.CREER_PARTIE);
			}
		}
		catch(UnsupportedOperationException e){
			message = creerMessageErreur(message, e.getMessage());
		}
        return message;
    }

	private MessageChat creerMessageErreur(MessageChat message, String messageErreur){
		message.setTypeMessage(MessageChat.TypeMessage.ERREUR);
		message.setMessage(messageErreur);
		return message;
	}

	/**
	 * Appelé pour mettre à jour la liste des joueurs de la partie.
	 * Le message est retransmis à tous les clients.
	 *
	 * @param message le message reçu du client
	 * @return le même message avec la liste des joueurs
	 */
    @MessageMapping("/mettreAJourJoueursPartie")
    @SendTo("/topic/joueursConnectes")
    public MessageChat mettreAJourJoueursPartie(@Payload MessageChat message) {
		String listeJoueursTexte = listeJoueurs.getJoueursPartie().toString();
		message.setMessage(listeJoueursTexte);
		logger.info("Liste des joueurs dans la partie : " + listeJoueursTexte);
        return message;
    }

	/**
	 * Appelé quand une partie est démarrée.
	 *
	 * @param message le message reçu du client
	 * @return le même message
	 */
    @MessageMapping("/demarrerPartie")
    @SendTo("/topic/joueursPartie")
    public MessageChat demarrerPartie(@Payload MessageChat message) {
		logger.info("Démarrage partie");
		controleurParties.creerPartie(listeJoueurs.getJoueursPartie());
        return message;
    }

}

