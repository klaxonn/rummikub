package Rummikub.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class EvenementsWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(EvenementsWebSocket.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


	@EventListener
    public void deconnexionWebSocket(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String nomJoueur = (String) headerAccessor.getSessionAttributes().get("nomJoueur");
        if(nomJoueur != null) {
            logger.info("Joueur déconnecté : " + nomJoueur);

			ListeJoueurs.retirerJoueur(nomJoueur);

			String listeJoueursConnectes = ListeJoueurs.getJoueursConnectes();
			String listeJoueursPrets = ListeJoueurs.getJoueursPrets();
			String listeAEnvoyer = listeJoueursConnectes + ";" + listeJoueursPrets;
        	logger.info("Liste des listes" + listeAEnvoyer);
			envoiMessage(nomJoueur,listeAEnvoyer); 
        }
    }
	
	private void envoiMessage(String nomJoueur, String listeJoueurs){
		Message message = new Message();
        message.setTypeMessage(Message.TypeMessage.DECONNEXION);
        message.setJoueur(nomJoueur);
		message.setMessage(listeJoueurs);
		messagingTemplate.convertAndSend("/joueursConnectes", message);
	}
}
