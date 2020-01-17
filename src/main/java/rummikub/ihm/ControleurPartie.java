package rummikub.ihm;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import java.util.List;
import java.lang.reflect.Method;
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
public class ControleurPartie {

	private ListeParties listeParties;

	@Autowired
	public ControleurPartie(ListeParties listeParties){
		this.listeParties = listeParties;
	}

    @GetMapping("{idPartie}/{idJoueur}/afficherPartie")
    public ResponseEntity<MessagePartie> afficherPartie(@PathVariable int idPartie, @PathVariable int idJoueur){
		return executerAction("afficherPartie", idPartie, idJoueur, null);
	}

	@PostMapping(value = "{idPartie}/{idJoueur}/creerSequence")
	public ResponseEntity<MessagePartie> creerSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("creerSequence", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/fusionnerSequence")
	public ResponseEntity<MessagePartie> fusionnerSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("fusionnerSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/couperSequence")
	public ResponseEntity<MessagePartie> couperSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("couperSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/deplacerJeton")
	public ResponseEntity<MessagePartie> deplacerJeton(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("deplacerJeton", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/remplacerJoker")
	public ResponseEntity<MessagePartie> remplacerJoker(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("remplacerJoker", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/annulerDerniereAction")
	public ResponseEntity<MessagePartie> annulerDerniereAction(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("annulerDerniereAction", idPartie, idJoueur, null);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/terminerTour")
	public ResponseEntity<MessagePartie> terminerTour(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("terminerTour", idPartie, idJoueur, null);
    }

	private ResponseEntity<MessagePartie> executerAction(String action, int idPartie, int idJoueur, List<Integer> arg) {
		//test partie existe
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = null;
		try{
			Class<?> classePartie = Class.forName("Partie");
			if(arg == null) {
				Method methode = classePartie.getMethod(action, Integer.class);
				message = (MessagePartie) methode.invoke(partie, idJoueur);
			}
			else {
				Method methode = classePartie.getMethod(action, Integer.class, List.class);
				message = (MessagePartie) methode.invoke(partie, idJoueur, arg);
			}
		}
		catch(Exception e) {
		}
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