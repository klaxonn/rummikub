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
import org.springframework.hateoas.EntityModel;

/**
 * Controleur de la partie.
 */
@RestController
public class ControleurPartie {

	private ListeParties listeParties;
	private ModeleControleurPartie modeleControleur;

	@Autowired
	public ControleurPartie(ListeParties listeParties, ModeleControleurPartie modeleControleur){
		this.listeParties = listeParties;
		this.modeleControleur = modeleControleur;
	}

    @GetMapping("{idPartie}/{idJoueur}/afficherPartie")
    public EntityModel<MessagePartie> afficherPartie(@PathVariable int idPartie, @PathVariable int idJoueur){
		return executerAction("afficherPartie", idPartie, idJoueur, null);
	}

	@PostMapping(value = "{idPartie}/{idJoueur}/creerSequence")
	public EntityModel<MessagePartie> creerSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("creerSequence", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/fusionnerSequence")
	public EntityModel<MessagePartie> fusionnerSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("fusionnerSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/couperSequence")
	public EntityModel<MessagePartie> couperSequence(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("couperSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/deplacerJeton")
	public EntityModel<MessagePartie> deplacerJeton(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("deplacerJeton", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/remplacerJoker")
	public EntityModel<MessagePartie> remplacerJoker(@PathVariable int idPartie, @PathVariable int idJoueur, @RequestBody List<Integer> indexes) {
		return executerAction("remplacerJoker", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/annulerDerniereAction")
	public EntityModel<MessagePartie> annulerDerniereAction(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("annulerDerniereAction", idPartie, idJoueur, null);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/terminerTour")
	public EntityModel<MessagePartie> terminerTour(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("terminerTour", idPartie, idJoueur, null);
    }

	private EntityModel<MessagePartie> executerAction(String action, int idPartie, int idJoueur, List<Integer> arg) {
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
				return modeleControleur.toModel(message);
			}
			catch(Exception e) {
				throw new IllegalArgumentException("Argument manquant");
			}
		}
		else {
			throw new IllegalArgumentException("La partie n'existe pas");
		}
	}
}
