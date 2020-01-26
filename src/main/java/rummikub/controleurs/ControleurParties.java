package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.core.jeu.Joueur;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * Controleur de la partie.
 */
@RestController
public class ControleurParties {

	private ListeParties listeParties;
	private ModeleControleurParties modeleControleurParties;
	private ModeleControleurPartie modeleControleurPartie;
	private ModeleAfficherParties modeleAfficherParties;

	@Autowired
	public ControleurParties(ListeParties listeParties, ModeleControleurParties modeleControleurParties,
	  ModeleControleurPartie modeleControleurPartie, ModeleAfficherParties modeleAfficherParties){
		this.listeParties = listeParties;
		this.modeleControleurParties = modeleControleurParties;
		this.modeleControleurPartie = modeleControleurPartie;
		this.modeleAfficherParties = modeleAfficherParties;
	}

	@PostMapping(value = "/creerPartie")
	public ResponseEntity<EntityModel> creerPartie(@RequestBody String nomJoueur) {
		int idPartie = listeParties.creerPartie();
		return ajouterJoueur(idPartie, nomJoueur);
    }

    @GetMapping(value = "/listerPartiesDispos")
	public CollectionModel<EntityModel<Map>> listerPartiesDispos() {
		List<Map> message = listeParties.listerPartiesDispos();
		return modeleAfficherParties.toCollectionModel(message);
    }

    @PostMapping(value = "{idPartie}/ajouterJoueur")
	public ResponseEntity<EntityModel> ajouterJoueur(@PathVariable int idPartie, @RequestBody String nomJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		if(partie != null) {
			Joueur joueur = new Joueur(nomJoueur);
			MessagePartie message = partie.ajouterJoueur(joueur);
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.AJOUTER_JOUEUR)) {
				message.setIdPartie(idPartie);
				EntityModel<MessagePartie> reponseAjout = modeleControleurParties.toModel(message);
				return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.CREATED);
			}
			else {
				throw new UnsupportedOperationException(message.getMessageErreur());
			}
		}
		else {
			throw new IndexOutOfBoundsException("La partie n'existe pas");
		}
    }

    @PostMapping(value = "{idPartie}/demarrerPartie")
	public EntityModel<MessagePartie> demarrerPartie(@PathVariable int idPartie) {
		Partie partie = listeParties.getPartie(idPartie);
		if(partie != null) {
			MessagePartie message = partie.commencerPartie();
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.ERREUR)) {
				throw new UnsupportedOperationException(message.getMessageErreur());
			}
			message.setIdPartie(idPartie);
			return modeleControleurPartie.toModel(message);
		}
		else {
			throw new IndexOutOfBoundsException("La partie n'existe pas");
		}
	}
}
