package Rummikub.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


	@EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String nomJoueur = (String) headerAccessor.getSessionAttributes().get("nomJoueur");
        if(nomJoueur != null) {
            logger.info("Joueur déconnecté : " + nomJoueur);

			ListeJoueurs.retirerJoueur(nomJoueur);

        	logger.info("Nombre de joueurs dans partie" + ListeJoueurs.nombreJoueursPrets());
            Message message = new Message();
            message.setTypeMessage(Message.TypeMessage.DECONNEXION);
            message.setJoueur(nomJoueur);
			String listeJoueurs = ListeJoueurs.getJoueursConnectes();
			message.setMessage(listeJoueurs);

            messagingTemplate.convertAndSend("/joueursConnectes", message);
        }
    }
}
