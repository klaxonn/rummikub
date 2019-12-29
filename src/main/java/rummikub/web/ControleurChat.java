package rummikub.web;

import rummikub.core.jeu.Joueur;
import rummikub.core.Partie;
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
    public Message mettreAJourJoueursConnectes(@Payload Message message) {
		String listeAEnvoyer = ListeJoueurs.getJoueursConnectes();
		if(ListeJoueurs.nombreJoueursPartie() > 0){
			listeAEnvoyer += ";" + ListeJoueurs.getJoueursPartie();
		}
		message.setMessage(listeAEnvoyer);
        logger.info("Liste des listes" + listeAEnvoyer);
        return message;
    }

    @MessageMapping("/joindrePartie")
    @SendTo("/JoueurAAjouter")
    public Message ajouterJoueurPartie(@Payload Message message) {
		ListeJoueurs.ajouteJoueurPartie(message.getJoueur());
		if(ListeJoueurs.nombreJoueursPartie() == 1){
			message.setTypeMessage(Message.TypeMessage.CREER_PARTIE);
			ListeJoueurs.setCreateurPartie(message.getJoueur());
		}
        return message;
    }

    @MessageMapping("/demarrerPartie")
    @SendTo("/DemarrerPartie")
    public Message demarrerPartie(@Payload Message message) {
		logger.info("Démarrage partie");
		List<Joueur> listeJoueurs = Arrays.asList(ListeJoueurs.creerListeJoueursPartie());
        Partie partie = new Partie(listeJoueurs);
        partie.commencerPartie();
        return message;
    }
    
    @MessageMapping("/mettreAJourJoueursPartie")
    @SendTo("/joueursConnectes")
    public Message mettreAJourJoueursPartie(@Payload Message message) {
		String listeJoueurs = ListeJoueurs.getJoueursPartie();
		message.setMessage(listeJoueurs);
		logger.info("Liste des joueurs dans la partie : " + listeJoueurs);
        return message;
    }
}

