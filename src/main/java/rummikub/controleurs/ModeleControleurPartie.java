package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Représentation HATEAOS pour les actions pendant une partie.
 */
@Component
public class ModeleControleurPartie implements RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> {

	@Override
	public EntityModel<MessagePartie> toModel(MessagePartie message) {
		int idPartie = message.getIdPartie();
		int idJoueur = message.getIdJoueur();

		EntityModel<MessagePartie> resultat = new EntityModel<>(message,
		  linkTo(methodOn(ControleurPartie.class).afficherPartie(idPartie, idJoueur)).withRel("Afficher la partie"));

		if(idJoueur == message.getIdJoueurCourant()) {
			resultat.add(linkTo(methodOn(ControleurPartie.class).creerSequence(idPartie, idJoueur, null)).withRel("Créer une séquence"),
			  linkTo(methodOn(ControleurPartie.class).fusionnerSequence(idPartie, idJoueur, null)).withRel("Fusionner deux séquences"),
			  linkTo(methodOn(ControleurPartie.class).couperSequence(idPartie, idJoueur, null)).withRel("Couper une séquence"),
			  linkTo(methodOn(ControleurPartie.class).deplacerJeton(idPartie, idJoueur, null)).withRel("Déplacer un jeton"),
			  linkTo(methodOn(ControleurPartie.class).remplacerJoker(idPartie, idJoueur, null)).withRel("Remplacer un joker"),
			  linkTo(methodOn(ControleurPartie.class).annulerDerniereAction(idPartie, idJoueur)).withRel("Annuler la dernière action"),
			  linkTo(methodOn(ControleurPartie.class).terminerTour(idPartie, idJoueur)).withRel("Terminer le tour"));
		}

		resultat.add(linkTo(methodOn(ControleurParties.class).quitterPartie(idPartie, idJoueur)).withRel("Quitter la partie"),
		  linkTo(methodOn(ControleurParties.class).arreterPartie(idPartie, idJoueur)).withRel("Arrêter la partie"));

		return resultat;
	}
}
