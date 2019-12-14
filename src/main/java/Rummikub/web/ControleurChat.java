package Rummikub.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ControleurChat {
		
	private static final Logger logger = LoggerFactory.getLogger(ControleurChat.class);
	private String nomJoueur;

    @MessageMapping("/envoyerMessageChat")
    @SendTo("/joueursConnectes")
    public Message envoyerMessage(@Payload Message message) {
		logger.info("Envoi message Chat");
        return message;
    }

	@MessageMapping("/ajouterJoueurConnecte")
    @SendTo("/connexionJoueur")
    public Message ajouterJoueurConnecte(@Payload Message message, 
                               SimpMessageHeaderAccessor headerAccessor) {
		String nomJoueur = ListeJoueurs.ajouterJoueurConnecte(message.getJoueur());	
        headerAccessor.getSessionAttributes().put("nomJoueur", nomJoueur);
		message.setJoueur(nomJoueur);
        return message;
    }


	@MessageMapping("/mettreAJourJoueursConnectes")
    @SendTo("/joueursConnectes")
    public Message mettreAJourJoueursConnectes(@Payload Message message, 
                               SimpMessageHeaderAccessor headerAccessor) {
		String listeJoueurs = ListeJoueurs.getJoueursConnectes();
		message.setMessage(listeJoueurs);
        logger.info("Liste des joueurs : " + listeJoueurs);
        return message;
    }

    @MessageMapping("/joindrePartie")
    @SendTo("/joueursConnectes")
    public Message ajouterJoueurPartie(@Payload Message message) {
		ListeJoueurs.setJoueurPret(message.getJoueur());
        return message;
    }
}

