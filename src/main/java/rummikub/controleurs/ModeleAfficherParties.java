package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Représentation HATEAOS pour l'affichage des parties disponibles.
 */
@Component
public class ModeleAfficherParties implements RepresentationModelAssembler<PartieDispo, EntityModel<PartieDispo>> {

	@Override
	public EntityModel<PartieDispo> toModel(PartieDispo partie) {
		int idPartie = partie.getId();

		return new EntityModel<>(partie,
		  linkTo(methodOn(ControleurParties.class).ajouterJoueur(idPartie, null, null)).withRel("ajouterJoueur"));
	}

	public CollectionModel<EntityModel<PartieDispo>> toCollectionModel(List<PartieDispo> liste) {
		List<EntityModel<PartieDispo>> listeEntity = liste.stream().map(element -> toModel(element))
															.collect(Collectors.toList());
		CollectionModel<EntityModel<PartieDispo>> collection = new CollectionModel<>(listeEntity);
		collection.add(linkTo(methodOn(ControleurParties.class).creerPartie(null, null)).withRel("creerPartie"),
						linkTo(methodOn(ControleurParties.class).listerPartiesDispos()).withRel("listerPartiesDispos"));
		return collection;
  }
}
