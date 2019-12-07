package Rummikub.web;

import Rummikub.core.jeu.Joueur;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ControleurChat {

    @MessageMapping("/chat.envoyerMessage")
    @SendTo("/topic/public")
    public Message envoyerMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/chat.ajouterJoueur")
    @SendTo("/topic/public")
    public Message ajouterJoueur(@Payload Message message, 
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("nomJoueur", message.getJoueur());
        return message;
    }

}

