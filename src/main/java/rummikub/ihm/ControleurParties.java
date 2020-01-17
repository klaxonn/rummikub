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
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<MessagePartie> creerPartie(@RequestBody List<String> listeNomsJoueurs) {
		try {
			Partie partie = listeParties.creerPartie(listeNomsJoueurs);
			return new ResponseEntity<MessagePartie>(HttpStatus.OK);
		}
		catch(IllegalArgumentException e) {
			return new ResponseEntity<MessagePartie>(HttpStatus.FORBIDDEN);
		}
    }

    @GetMapping(value = "/listerPartiesDispos")
	public ResponseEntity<String> listerPartiesDispos() {
		String resultat = listeParties.listerPartiesDispos();
		return new ResponseEntity<String>(resultat, HttpStatus.OK);
    }

    @PostMapping(value = "{idPartie}/ajouterJoueur")
	public ResponseEntity<MessagePartie> ajouterJoueur(@PathVariable int idPartie, @RequestBody String nomJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		try {
			Joueur joueur = new Joueur(nomJoueur);
			partie.ajouterJoueur(joueur);
		}
		catch(Exception e) {
			return new ResponseEntity<MessagePartie>(HttpStatus.FORBIDDEN);
		}
        return new ResponseEntity<MessagePartie>(HttpStatus.OK);
    }

    @PostMapping(value = "{idPartie}/demarrerPartie")
	public ResponseEntity<MessagePartie> demarrerPartie(@PathVariable int idPartie) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = partie.commencerPartie();
		if(message.getTypeMessage().equals(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR)) {
			return new ResponseEntity<MessagePartie>(message, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<MessagePartie>(message, HttpStatus.FORBIDDEN);
		}
    }
}
