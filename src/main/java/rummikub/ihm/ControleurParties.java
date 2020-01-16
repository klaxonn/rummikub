package rummikub.ihm;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
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
		Partie partie = listeParties.creerPartie(listeNomsJoueurs);
        return new ResponseEntity<MessagePartie>(HttpStatus.OK);
    }

    @PostMapping(value = "{idPartie}/demarrerPartie")
	public ResponseEntity<MessagePartie> demarrerPartie(@PathVariable int idPartie, @RequestBody List<String> listeNomsJoueurs) {
		Partie partie = listeParties.getPartie(idPartie);
		partie.commencerPartie();
        return new ResponseEntity<MessagePartie>(HttpStatus.OK);
    }
}
