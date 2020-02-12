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

	EntityModel<MessagePartie> resultat = new EntityModel<>(message,
      linkTo(methodOn(ControleurParties.class).creerPartie(null, null)).withRel("creerPartie"),
      linkTo(methodOn(ControleurParties.class).listerPartiesDispos()).withRel("listerPartiesDispos"),
	  linkTo(methodOn(ControleurParties.class).demarrerPartie(idPartie, idJoueur)).withRel("demarrerPartie"));

	if(idPartie > 0) {
		resultat.add(linkTo(methodOn(ControleurParties.class).quitterPartie(idPartie, idJoueur)).withRel("quitterPartie"));
	}
	return resultat;
  }
}
