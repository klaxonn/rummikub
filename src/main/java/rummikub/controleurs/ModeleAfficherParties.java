package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
class ModeleAfficherParties implements RepresentationModelAssembler<Map, EntityModel<Map>> {

	@Override
	public EntityModel<Map> toModel(Map infoPartie) {
		int idPartie = Integer.parseInt((String)infoPartie.get("idPartie"));

		EntityModel<Map> resultat = new EntityModel<>(infoPartie,
		  linkTo(methodOn(ControleurParties.class).ajouterJoueur(idPartie, null)).withRel("ajouterJoueur"),
		  linkTo(methodOn(ControleurParties.class).demarrerPartie(idPartie)).withRel("demarrerPartie"));

		return resultat;
	}

	public CollectionModel<EntityModel<Map>> toCollectionModel(List<Map> liste) {
		CollectionModel<EntityModel<Map>> collection = CollectionModel.wrap(liste);
		collection.add(linkTo(methodOn(ControleurParties.class).creerPartie(null)).withRel("creerPartie"),
						linkTo(methodOn(ControleurParties.class).listerPartiesDispos()).withRel("listerPartiesDispos"));
		return collection;

  }
}


