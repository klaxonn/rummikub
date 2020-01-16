package rummikub.ihm;

import rummikub.core.api.Partie;
import rummikub.core.api.FabriquePartie;
import rummikub.core.api.MessagePartie;
import java.util.Set;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Controleur de la partie.
 */
@RestController
public class ControleurWeb {
	
	private Partie partie;	
	private List<String> listeNomsJoueurs;
	
	/**
	 * Crée une partie.
	 * 
	 * @param listeNomsJoueurs la liste des noms des joueurs
	 */
	@PostMapping(value = "/creerPartie")
	public ResponseEntity<MessagePartie> creerPartie(@RequestBody List<String> listeNomsJoueurs) {
		this.listeNomsJoueurs = listeNomsJoueurs;
		partie = FabriquePartie.creerNouvellePartie(listeNomsJoueurs);
        partie.commencerPartie();
        return new ResponseEntity<MessagePartie>(HttpStatus.OK);
    }
    
    @GetMapping("{nom}/afficherPartie")
    public ResponseEntity<MessagePartie> afficherPartie(@PathVariable String nom){
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.afficherPartie(id);
		return traitementActions(message);
	}
	
	@PostMapping(value = "{nom}/creerNouvelleSequence")
	public ResponseEntity<MessagePartie> creerNouvelleSequence(@PathVariable String nom, @RequestBody List<Integer> indexes) {
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.creerNouvelleSequence(id, indexes);
		return traitementActions(message);
    }
    
    @PostMapping(value = "{nom}/fusionnerSequence")
	public ResponseEntity<MessagePartie> fusionnerSequence(@PathVariable String nom, @RequestBody List<Integer> indexes) {
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.fusionnerSequence(id, indexes);
		return traitementActions(message);
    }
    
	@PostMapping(value = "{nom}/couperSequence")
	public ResponseEntity<MessagePartie> couperSequence(@PathVariable String nom, @RequestBody List<Integer> indexes) {
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.couperSequence(id, indexes);
		return traitementActions(message);
    }
    
	@PostMapping(value = "{nom}/deplacerJeton")
	public ResponseEntity<MessagePartie> deplacerJeton(@PathVariable String nom, @RequestBody List<Integer> indexes) {
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.deplacerJeton(id, indexes);
		return traitementActions(message);
    }
    
    @PostMapping(value = "{nom}/remplacerJoker")
	public ResponseEntity<MessagePartie> remplacerJoker(@PathVariable String nom, @RequestBody List<Integer> indexes) {
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.remplacerJoker(id, indexes);
		return traitementActions(message);
    }
    
    @PostMapping(value = "{nom}/annulerDerniereAction")
	public ResponseEntity<MessagePartie> annulerDerniereAction(@PathVariable String nom) {
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.annulerDerniereAction(id);
		return traitementActions(message);
    }
    
    @PostMapping(value = "{nom}/terminerTour")
	public ResponseEntity<MessagePartie> terminerTour(@PathVariable String nom) {
		int id = listeNomsJoueurs.indexOf(nom);
		MessagePartie message = partie.terminerTour(id);
		return traitementActions(message);
    }
    
    private ResponseEntity<MessagePartie> traitementActions(MessagePartie message) {
		switch(message.getTypeMessage()) {
			case AFFICHER_PARTIE:
				return new ResponseEntity<MessagePartie>(message, HttpStatus.OK);
			case RESULTAT_ACTION:
				return new ResponseEntity<MessagePartie>(message, HttpStatus.OK);
			case DEBUT_NOUVEAU_TOUR:
				return new ResponseEntity<MessagePartie>(message, HttpStatus.OK);
			case FIN_DE_PARTIE:
				return new ResponseEntity<MessagePartie>(message, HttpStatus.OK);
			default:
				return new ResponseEntity<MessagePartie>(message, HttpStatus.FORBIDDEN);
		}
	}
}
