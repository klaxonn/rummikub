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
    public MessagePartie afficherPartie(@PathVariable int idPartie, @PathVariable int idJoueur){
		return executerAction("afficherPartie", idPartie, idJoueur, null);
	}

	@PostMapping(value = "{idPartie}/{idJoueur}/creerSequence")
	public MessagePartie creerSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("creerSequence", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/fusionnerSequence")
	public MessagePartie fusionnerSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("fusionnerSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/couperSequence")
	public MessagePartie couperSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("couperSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/deplacerJeton")
	public MessagePartie deplacerJeton(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("deplacerJeton", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/remplacerJoker")
	public MessagePartie remplacerJoker(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("remplacerJoker", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/annulerDerniereAction")
	public MessagePartie annulerDerniereAction(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("annulerDerniereAction", idPartie, idJoueur, null);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/terminerTour")
	public MessagePartie terminerTour(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("terminerTour", idPartie, idJoueur, null);
    }

	private MessagePartie executerAction(String action, int idPartie, int idJoueur, List<Integer> arg) {
		Partie partie = listeParties.getPartie(idPartie);
		if(partie != null){
			try{
				Class<?> classePartie = Class.forName("Partie");
				MessagePartie message = null;
				if(arg == null) {
					Method methode = classePartie.getMethod(action, Integer.class);
					message = (MessagePartie) methode.invoke(partie, idJoueur);
				}
				else {
					Method methode = classePartie.getMethod(action, Integer.class, List.class);
					message = (MessagePartie) methode.invoke(partie, idJoueur, arg);
				}
				message.setIdPartie(idPartie);
				return message;
			}
			catch(Exception e) {
				throw new  NullPointerException("Argument manquant");
			}
		}
		else {
			throw new IllegalArgumentException("La partie n'existe pas");
		}
	}
}
