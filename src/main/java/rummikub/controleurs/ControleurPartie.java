package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.core.api.FabriquePartie;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Method;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.Size;
import org.springframework.http.MediaType;

/**
 * Controleur de la partie.
 */
@Validated
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ControleurPartie {

	private ListeParties listeParties;
	private ModeleControleurPartie modeleControleurPartie;
	private ModeleControleurParties modeleControleurParties;
	private static final String CHEMIN_CLASSE_PARTIE = "rummikub.core.api.Partie";

	@Autowired
	public ControleurPartie(ListeParties listeParties, ModeleControleurPartie modeleControleurPartie,
	  ModeleControleurParties modeleControleurParties){
		this.listeParties = listeParties;
		this.modeleControleurPartie = modeleControleurPartie;
		this.modeleControleurParties = modeleControleurParties;
	}

    @GetMapping("{idPartie}/{idJoueur}/afficherPartie")
    public ResponseEntity<EntityModel> afficherPartie(@PathVariable int idPartie, @PathVariable int idJoueur){
		return executerAction("afficherPartie", idPartie, idJoueur, null);
	}

	@PostMapping(value = "{idPartie}/{idJoueur}/creerSequence")
	public ResponseEntity<EntityModel> creerSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody
	  @Size(min=1, max = FabriquePartie.VALEUR_MAX, message = "Au moins 1 jeton nécessaire")
	  List<Integer> indexes) {
		return executerAction("creerNouvelleSequence", idPartie, idJoueur, indexes);
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/ajouterJeton")
	public ResponseEntity<EntityModel> ajouterJeton(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody ParametresAction param) {
		return executerAction("ajouterJeton", idPartie, idJoueur,
		  Arrays.asList(param.getIndexJeton(), param.getIndexSequenceArrivee()));
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/fusionnerSequence")
	public ResponseEntity<EntityModel> fusionnerSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody ParametresAction param) {
		return executerAction("fusionnerSequence", idPartie, idJoueur,
		  Arrays.asList(param.getIndexSequenceDepart(), param.getIndexSequenceArrivee()));
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/couperSequence")
	public ResponseEntity<EntityModel> couperSequence(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody ParametresAction param) {
		return executerAction("couperSequence", idPartie, idJoueur,
		  Arrays.asList(param.getIndexSequenceDepart(), param.getIndexJeton()));
    }

	@PostMapping(value = "{idPartie}/{idJoueur}/deplacerJeton")
	public ResponseEntity<EntityModel> deplacerJeton(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody ParametresAction param) {
		return executerAction("deplacerJeton", idPartie, idJoueur,
		  Arrays.asList(param.getIndexSequenceDepart(), param.getIndexJeton(), param.getIndexSequenceArrivee()));
    }

    @PostMapping(value = "{idPartie}/{idJoueur}/remplacerJoker")
	public ResponseEntity<EntityModel> remplacerJoker(@PathVariable int idPartie, @PathVariable int idJoueur,
	  @RequestBody ParametresAction param) {
		return executerAction("remplacerJoker", idPartie, idJoueur,
		  Arrays.asList(param.getIndexJeton(), param.getIndexSequenceArrivee()));
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
		MessagePartie message = new MessagePartie();
		if(listeParties.isPartieSupprimee(idPartie)) {
			message.setMessageErreur("La partie est terminée");
			message.setTypeMessage(MessagePartie.TypeMessage.FIN_DE_PARTIE);
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.FORBIDDEN);
		}
		Partie partie = listeParties.getPartie(idPartie);
		if(partie != null){
			try{
				Class<?> classePartie = Class.forName(CHEMIN_CLASSE_PARTIE);
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
				throw new ControleurErreurException(message, modeleControleurPartie, HttpStatus.FORBIDDEN);
			}
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.FIN_DE_PARTIE)) {
				listeParties.supprimerPartie(idPartie);
			}
			message.setIdPartie(idPartie);
			EntityModel<MessagePartie> body = modeleControleurPartie.toModel(message);
			return new ResponseEntity<EntityModel>(body, HttpStatus.OK);
		}
		else {
			message.setMessageErreur("La partie n'existe pas");
			throw new ControleurErreurException(message, modeleControleurPartie, HttpStatus.NOT_FOUND);
		}
	}
}
