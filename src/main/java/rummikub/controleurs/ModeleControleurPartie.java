package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ModeleControleurPartie implements RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> {

	@Override
	public EntityModel<MessagePartie> toModel(MessagePartie message) {
		int idPartie = message.getIdPartie();
		int idJoueur = message.getIdJoueur();

		if(idJoueur == 0) {
			return new EntityModel<>(message);
		}

		if(idJoueur == message.getIdJoueurCourant()) {
			return new EntityModel<>(message,
			  linkTo(methodOn(ControleurPartie.class).afficherPartie(idPartie, idJoueur)).withRel("afficherPartie"),
			  linkTo(methodOn(ControleurPartie.class).creerSequence(idPartie, idJoueur, null)).withRel("creerSequence"),
			  linkTo(methodOn(ControleurPartie.class).fusionnerSequence(idPartie, idJoueur, 0, 0)).withRel("fusionnerSequence"),
			  linkTo(methodOn(ControleurPartie.class).couperSequence(idPartie, idJoueur, 0, 0)).withRel("couperSequence"),
			  linkTo(methodOn(ControleurPartie.class).deplacerJeton(idPartie, idJoueur, 0, 0, 0)).withRel("deplacerJeton"),
			  linkTo(methodOn(ControleurPartie.class).remplacerJoker(idPartie, idJoueur, 0, 0)).withRel("remplacerJoker"),
			  linkTo(methodOn(ControleurPartie.class).annulerDerniereAction(idPartie, idJoueur)).withRel("annulerDerniereAction"),
			  linkTo(methodOn(ControleurPartie.class).terminerTour(idPartie, idJoueur)).withRel("terminerTour"));
		}
		else {
			return new EntityModel<>(message,
			linkTo(methodOn(ControleurPartie.class).afficherPartie(idPartie, idJoueur)).withRel("afficherPartie"));
		}
	}
}
