package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import java.util.List;
import java.lang.reflect.Method;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Controleur de la partie.
 */
@Validated
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
	public EntityModel<MessagePartie> creerSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @NotEmpty(message = "Au moins 1 jeton nécessaire")
	  List<Integer> indexes) {
		return executerAction("creerNouvelleSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/ajouterJeton")
	public EntityModel<MessagePartie> ajouterJeton(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @Size(min = 2, max = 2, message = "2 valeurs attendues")
	  List<Integer> indexes) {
		return executerAction("ajouterJeton", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/fusionnerSequence")
	public EntityModel<MessagePartie> fusionnerSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @Size(min = 2, max = 2, message = "2 valeurs attendues")
	  List<Integer> indexes) {
		return executerAction("fusionnerSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/couperSequence")
	public EntityModel<MessagePartie> couperSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @Size(min = 2, max = 2, message = "2 valeurs attendues")
	  List<Integer> indexes) {
		return executerAction("couperSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/deplacerJeton")
	public EntityModel<MessagePartie> deplacerJeton(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @Size(min = 3, max = 3, message = "3 valeurs attendues")
	  List<Integer> indexes) {
		return executerAction("deplacerJeton", idPartie, idJoueur, indexes);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/remplacerJoker")
	public EntityModel<MessagePartie> remplacerJoker(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @Size(min = 2, max = 2, message = "2 valeurs attendues")
	  List<Integer> indexes) {
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
			MessagePartie message = null;
			try{
				Class<?> classePartie = Class.forName("rummikub.core.api.Partie");
				if(arg == null) {
					Method methode = classePartie.getMethod(action, int.class);
					message = (MessagePartie) methode.invoke(partie, idJoueur);
				}
				else {
					Method methode = classePartie.getMethod(action, int.class, List.class);
					message = (MessagePartie) methode.invoke(partie, idJoueur, arg);
				}
			}
			catch(Exception e) {
			}

			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.ERREUR)) {
				throw new UnsupportedOperationException(message.getMessageErreur());
			}
			message.setIdPartie(idPartie);
			return modeleControleur.toModel(message);
		}
		else {
			throw new IndexOutOfBoundsException("La partie n'existe pas");
		}
	}
}
