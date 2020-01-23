package rummikub.ihm;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.core.jeu.Joueur;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controleur de la partie.
 */
@RestController
public class ControleurParties {

	private ListeParties listeParties;

	@Autowired
	public ControleurParties(ListeParties listeParties){
		this.listeParties = listeParties;
	}

	/**
	 * Crée une partie.
	 *
	 * @param listeNomsJoueurs la liste des noms des joueurs
	 */
	@PostMapping(value = "/creerPartie")
	public MessagePartie creerPartie() {
		MessagePartie messageReponse = new MessagePartie();
		int idPartie = listeParties.creerPartie();
		messageReponse.setIdPartie(idPartie);
		return messageReponse;
    }

    @GetMapping(value = "/listerPartiesDispos")
	public String listerPartiesDispos() {
		return listeParties.listerPartiesDispos();
    }

    @PostMapping(value = "{idPartie}/ajouterJoueur")
	public MessagePartie ajouterJoueur(@PathVariable int idPartie, @RequestBody String nomJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie messageReponse = new MessagePartie();
		messageReponse.setIdPartie(idPartie);
		try {
			Joueur joueur = new Joueur(nomJoueur);
			messageReponse = partie.ajouterJoueur(joueur);
			if(messageReponse.getTypeMessage().equals(MessagePartie.TypeMessage.AJOUTER_JOUEUR)) {
				messageReponse.setIdPartie(idPartie);
				return messageReponse;
			}
		}
		catch(IllegalArgumentException e) {
			messageReponse.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
			messageReponse.setMessageErreur(e.getMessage());
		}
		return messageReponse;
    }

    @PostMapping(value = "{idPartie}/demarrerPartie")
	public MessagePartie demarrerPartie(@PathVariable int idPartie) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = partie.commencerPartie();
		message.setIdPartie(idPartie);
		return message;
	}
}
