package rummikub.ihm;

import rummikub.core.api.MessagePartie;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class ModeleControleurPartie implements RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> {

  @Override
  public EntityModel<MessagePartie> toModel(MessagePartie message) {
	int idPartie = message.getIdPartie();
	int idJoueur = message.getIdJoueur();

    return new EntityModel<>(message,
      linkTo(methodOn(ControleurPartie.class).afficherPartie(idPartie, idJoueur)).withRel("afficherPartie"),
      linkTo(methodOn(ControleurPartie.class).creerSequence(idPartie, idJoueur, null)).withRel("creerSequence"),
      linkTo(methodOn(ControleurPartie.class).fusionnerSequence(idPartie, idJoueur, null)).withRel("fusionnerSequence"),
      linkTo(methodOn(ControleurPartie.class).couperSequence(idPartie, idJoueur, null)).withRel("couperSequence"),
      linkTo(methodOn(ControleurPartie.class).deplacerJeton(idPartie, idJoueur, null)).withRel("deplacerJeton"),
      linkTo(methodOn(ControleurPartie.class).remplacerJoker(idPartie, idJoueur, null)).withRel("remplacerJoker"),
      linkTo(methodOn(ControleurPartie.class).annulerDerniereAction(idPartie, idJoueur)).withRel("annulerDerniereAction"),
      linkTo(methodOn(ControleurPartie.class).terminerTour(idPartie, idJoueur)).withRel("terminerTour"));
  }
}

