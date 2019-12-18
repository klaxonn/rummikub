package rummikub.web;

import rummikub.ihm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ControleurChat {
		
	private static final Logger logger = LoggerFactory.getLogger(ControleurChat.class);

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
		String listeAEnvoyer = ListeJoueurs.getJoueursConnectes();
		if(ListeJoueurs.nombreJoueursPrets() > 0){
			listeAEnvoyer += ";" + ListeJoueurs.getJoueursPrets();
		}
		message.setMessage(listeAEnvoyer);
        logger.info("Liste des listes" + listeAEnvoyer);
        return message;
    }

    @MessageMapping("/joindrePartie")
    @SendTo("/JoueurAAjouter")
    public Message ajouterJoueurPartie(@Payload Message message) {
		ListeJoueurs.setJoueurPret(message.getJoueur());
		if(ListeJoueurs.nombreJoueursPrets() == 1){
			message.setTypeMessage(Message.TypeMessage.CREER_PARTIE);
		}
        return message;
    }

    @MessageMapping("/demarrerPartie")
    @SendTo("/DemarrerPartie")
    public Message demarrerPartie(@Payload Message message) {
		ControleurAbstrait controleur = new ControleurWeb(ListeJoueurs.creerListeJoueursPrets());
        return message;
    }
    
    @MessageMapping("/mettreAJourJoueursPrets")
    @SendTo("/joueursConnectes")
    public Message mettreAJourJoueursConnectes(@Payload Message message) {
		String listeJoueurs = ListeJoueurs.getJoueursPrets();
		message.setMessage(listeJoueurs);
		logger.info("Liste des joueurs prêts : " + listeJoueurs);
        return message;
    }
}

