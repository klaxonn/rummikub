package rummikub.salon;

import rummikub.core.api.Partie;
import rummikub.core.api.FabriquePartie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Controleur du salon du connexion et du chat.
 */
@Controller
public class ControleurChat {
		
	private static final Logger logger = LoggerFactory.getLogger(ControleurChat.class);

	/** Le nombre maximum de joueurs dans une partie */
	public static final int NOMBRE_MAX_JOUEURS_PARTIE = 4;

	/**
	 * Appelé quand un message de chat est envoyé.
	 * Le message est retransmis à tous les clients
	 *
	 * @param message le message reçu du client
	 * @return le même message
	 */
    @MessageMapping("/envoyerMessageChat")
    @SendTo("/joueursConnectes")
    public MessageChat envoyerMessage(@Payload MessageChat message) {
		logger.info("Envoi message Chat");
        return message;
    }

	/**
	 * Appelé quand un nouveau client se connecte au salon.
	 * Le message est transmis au nouveau client pour mettre à jour son nom.
	 *
	 * @param message le message reçu du client
	 * @param headerAccessor informations sur le client
	 * @return le même message avec son nom à jour
	 */
	@MessageMapping("/ajouterJoueurConnecte")
    @SendTo("/connexionJoueur")
    public MessageChat ajouterJoueurConnecte(@Payload MessageChat message, 
                               SimpMessageHeaderAccessor headerAccessor) {
		String nomJoueur = ListeJoueurs.ajouterJoueurConnecte(message.getJoueur());	
        headerAccessor.getSessionAttributes().put("nomJoueur", nomJoueur);
		message.setJoueur(nomJoueur);
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
    @SendTo("/joueursConnectes")
    public MessageChat mettreAJourJoueursConnectes(@Payload MessageChat message) {
		String listeAEnvoyer = ListeJoueurs.getJoueursConnectes().toString();
		if(ListeJoueurs.nombreJoueursPartie() > 0){
			listeAEnvoyer += ";" + ListeJoueurs.getJoueursPartie().toString();
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
    @SendTo("/JoueurAAjouter")
    public MessageChat ajouterJoueurPartie(@Payload MessageChat message) {
		if(ListeJoueurs.nombreJoueursPartie() < NOMBRE_MAX_JOUEURS_PARTIE) {
			try{
				ListeJoueurs.ajouteJoueurPartie(message.getJoueur());
				if(ListeJoueurs.nombreJoueursPartie() == 1){
					message.setTypeMessage(MessageChat.TypeMessage.CREER_PARTIE);
					ListeJoueurs.setCreateurPartie(message.getJoueur());
				}
			}
			catch(UnsupportedOperationException e){
				message = creerMessageErreur(message, e.getMessage());
			}
		}
		else {
			message = creerMessageErreur(message, "La partie est complète");
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
    @SendTo("/joueursConnectes")
    public MessageChat mettreAJourJoueursPartie(@Payload MessageChat message) {
		String listeJoueurs = ListeJoueurs.getJoueursPartie().toString();
		message.setMessage(listeJoueurs);
		logger.info("Liste des joueurs dans la partie : " + listeJoueurs);
        return message;
    }

	/**
	 * Appelé quand une partie est démarrée.
	 *
	 * @param message le message reçu du client
	 * @return le même message
	 */
    @MessageMapping("/demarrerPartie")
    @SendTo("/DemarrerPartie")
    public MessageChat demarrerPartie(@Payload MessageChat message) {
		logger.info("Démarrage partie");
		Partie partie = FabriquePartie.creerNouvellePartie(ListeJoueurs.getJoueursPartie());
        partie.commencerPartie();
        return message;
    }
    
}

