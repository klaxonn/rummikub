package rummikub.salon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


/**
 * Gestion des événements Web Socket.
 */
@Component
public class EvenementsWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(EvenementsWebSocket.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


	/**
	 * Appelé quand un client est déconnecté.
	 * Si le client déconnecté est le créateur de la partie,
	 * tous les joueurs sont retirés de la partie.
	 * Un message contenant les clients connectés et les joueurs
	 * est envoyé.
	 * @param evenement informations sur le client déconnecté.
	 */
	@EventListener
    public void deconnexionWebSocket(SessionDisconnectEvent evenement) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(evenement.getMessage());

        String nomJoueur = (String) headerAccessor.getSessionAttributes().get("nomJoueur");
        if(nomJoueur != null) {
            logger.info("Joueur déconnecté : " + nomJoueur);
			try{
				ListeJoueurs.retirerJoueur(nomJoueur);

				String listeAEnvoyer = ListeJoueurs.getJoueursConnectes().toString();
				if(ListeJoueurs.nombreJoueursPartie() > 0){
					listeAEnvoyer += ";" + ListeJoueurs.getJoueursPartie().toString();
				}
				logger.info("Liste des listes" + listeAEnvoyer);
				envoiMessage(nomJoueur,listeAEnvoyer);
			}
			catch(UnsupportedOperationException e){
				logger.error(e.getMessage());
			}
        }
    }

	private void envoiMessage(String nomJoueur, String listeJoueurs){
		MessageChat message = new MessageChat();
        message.setTypeMessage(MessageChat.TypeMessage.DECONNEXION);
        message.setJoueur(nomJoueur);
		message.setMessage(listeJoueurs);
		messagingTemplate.convertAndSend("/topic/joueursConnectes", message);
	}
}
