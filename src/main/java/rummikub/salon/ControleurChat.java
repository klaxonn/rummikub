package rummikub.salon;

import rummikub.core.api.Partie;
import rummikub.core.api.PartieImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
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
    public MessageChat envoyerMessage(@Payload MessageChat message) {
		logger.info("Envoi message Chat");
        return message;
    }

	@MessageMapping("/ajouterJoueurConnecte")
    @SendTo("/connexionJoueur")
    public MessageChat ajouterJoueurConnecte(@Payload MessageChat message, 
                               SimpMessageHeaderAccessor headerAccessor) {
		String nomJoueur = ListeJoueurs.ajouterJoueurConnecte(message.getJoueur());	
        headerAccessor.getSessionAttributes().put("nomJoueur", nomJoueur);
		message.setJoueur(nomJoueur);
        return message;
    }


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

    @MessageMapping("/joindrePartie")
    @SendTo("/JoueurAAjouter")
    public MessageChat ajouterJoueurPartie(@Payload MessageChat message) {
		ListeJoueurs.ajouteJoueurPartie(message.getJoueur());
		if(ListeJoueurs.nombreJoueursPartie() == 1){
			message.setTypeMessage(MessageChat.TypeMessage.CREER_PARTIE);
			ListeJoueurs.setCreateurPartie(message.getJoueur());
		}
        return message;
    }

    @MessageMapping("/demarrerPartie")
    @SendTo("/DemarrerPartie")
    public MessageChat demarrerPartie(@Payload MessageChat message) {
		logger.info("Démarrage partie");
        Partie partie = new PartieImpl(ListeJoueurs.getJoueursPartie());
        partie.commencerPartie();
        return message;
    }
    
    @MessageMapping("/mettreAJourJoueursPartie")
    @SendTo("/joueursConnectes")
    public MessageChat mettreAJourJoueursPartie(@Payload MessageChat message) {
		String listeJoueurs = ListeJoueurs.getJoueursPartie().toString();
		message.setMessage(listeJoueurs);
		logger.info("Liste des joueurs dans la partie : " + listeJoueurs);
        return message;
    }
}

