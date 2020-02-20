package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Représentation HATEAOS pour les actions avant et après une partie.
 */
@Component
public class ModeleControleurParties implements RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> {

	@Override
	public EntityModel<MessagePartie> toModel(MessagePartie message) {
		int idPartie = message.getIdPartie();
		int idJoueur = message.getIdJoueur();

		if(idPartie > 0) {
			return new EntityModel<>(message,
			  linkTo(methodOn(ControleurParties.class).demarrerPartie(idPartie, idJoueur)).withRel("Démarrer la partie"),
			  linkTo(methodOn(ControleurParties.class).quitterPartie(idPartie, idJoueur)).withRel("Quitter la partie"),
			  linkTo(methodOn(ControleurParties.class).arreterPartie(idPartie, idJoueur)).withRel("Arrêter la partie"));
		}
		else {
			return new EntityModel<>(message,
			  linkTo(methodOn(ControleurParties.class).creerPartie(null, null)).withRel("Créer une partie"),
			  linkTo(methodOn(ControleurParties.class).listerPartiesDispos()).withRel("Lister les parties disponibles"));
		}
	}
}
