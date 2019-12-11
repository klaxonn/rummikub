package Rummikub.web;

import Rummikub.core.jeu.Joueur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;

@Controller
public class ControleurChat {
	
	private static final ArrayList<Joueur> listeJoueurs = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(ControleurChat.class);

    @MessageMapping("/chat.envoyerMessage")
    @SendTo("/topic/public")
    public Message envoyerMessage(@Payload Message message) {
		logger.info("Envoi message");
        return message;
    }

    @MessageMapping("/chat.ajouterJoueur")
    @SendTo("/topic/public")
    public Message ajouterJoueur(@Payload Message message, 
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("nomJoueur", message.getJoueur());
		listeJoueurs.add(new Joueur(message.getJoueur()));
        logger.info("Nombre de joueurs " + listeJoueurs.size());
        return message;
    }

}

