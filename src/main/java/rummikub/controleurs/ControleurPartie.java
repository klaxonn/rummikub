package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import java.util.List;
import java.util.Arrays;
import java.lang.reflect.Method;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotEmpty;
import org.springframework.http.MediaType;

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
    public ResponseEntity<EntityModel> afficherPartie(@PathVariable int idPartie, @PathVariable int idJoueur){
		return executerAction("afficherPartie", idPartie, idJoueur, null);
	}

	@PostMapping(value = "{idPartie}/{idJoueur}/creerSequence", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel> creerSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @NotEmpty(message = "Au moins 1 jeton nécessaire")
	  List<Integer> indexes) {
		return executerAction("creerNouvelleSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/ajouterJeton/{indexJeton}/{indexSequence}")
	public ResponseEntity<EntityModel> ajouterJeton(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @PathVariable int indexJeton, @PathVariable int indexSequence){
		return executerAction("ajouterJeton", idPartie, idJoueur, Arrays.asList(indexJeton,indexSequence));
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/fusionnerSequence/{indexSequenceD}/{indexSequenceA}")
	public ResponseEntity<EntityModel> fusionnerSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @PathVariable int indexSequenceD, @PathVariable int indexSequenceA) {
		return executerAction("fusionnerSequence", idPartie, idJoueur, Arrays.asList(indexSequenceD, indexSequenceA));
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/couperSequence/{indexSequence}/{indexJeton}")
	public ResponseEntity<EntityModel> couperSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @PathVariable int indexSequence, @PathVariable int indexJeton) {
		return executerAction("couperSequence", idPartie, idJoueur, Arrays.asList(indexSequence, indexJeton));
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/deplacerJeton/{indexSequenceD}/{indexJeton}/{indexSequenceA}")
	public ResponseEntity<EntityModel> deplacerJeton(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @PathVariable int indexSequenceD, @PathVariable int indexJeton, @PathVariable int indexSequenceA) {
		return executerAction("deplacerJeton", idPartie, idJoueur, Arrays.asList(indexSequenceD, indexJeton, indexSequenceA));
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/remplacerJoker/{indexJeton}/{indexSequence}")
	public ResponseEntity<EntityModel> remplacerJoker(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @PathVariable int indexJeton, @PathVariable int indexSequence) {
		return executerAction("remplacerJoker", idPartie, idJoueur, Arrays.asList(indexJeton, indexSequence));
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/annulerDerniereAction")
	public ResponseEntity<EntityModel> annulerDerniereAction(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("annulerDerniereAction", idPartie, idJoueur, null);
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/terminerTour")
	public ResponseEntity<EntityModel> terminerTour(@PathVariable int idPartie, @PathVariable int idJoueur) {
		return executerAction("terminerTour", idPartie, idJoueur, null);
    }

	private ResponseEntity<EntityModel> executerAction(String action, int idPartie, int idJoueur, List<Integer> arg) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null){
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
				throw new ControleurErreurException(message, modeleControleur, HttpStatus.FORBIDDEN);
			}
			message.setIdPartie(idPartie);
			EntityModel<MessagePartie> body = modeleControleur.toModel(message);
			return new ResponseEntity<EntityModel>(body, HttpStatus.OK);
		}
		else {
			message.setMessageErreur("La partie n'existe pas");
			throw new ControleurErreurException(message, modeleControleur, HttpStatus.NOT_FOUND);
		}
	}
}
