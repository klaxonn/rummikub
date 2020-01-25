package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class ModeleControleurParties implements RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> {

  @Override
  public EntityModel<MessagePartie> toModel(MessagePartie message) {
	int idPartie = message.getIdPartie();

    return new EntityModel<>(message,
      linkTo(methodOn(ControleurParties.class).creerPartie(null)).withRel("creerPartie"),
      linkTo(methodOn(ControleurParties.class).listerPartiesDispos()).withRel("listerPartiesDispos"),
      linkTo(methodOn(ControleurParties.class).ajouterJoueur(idPartie, null)).withRel("ajouterJoueur"),
      linkTo(methodOn(ControleurParties.class).demarrerPartie(idPartie)).withRel("demarrerPartie"));
  }
}


