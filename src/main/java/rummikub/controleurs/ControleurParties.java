package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.core.jeu.Joueur;
import java.util.List;
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
	public ResponseEntity<CollectionModel> listerPartiesDispos() {
		List<PartieDispo> message = listeParties.listerPartiesDispos();
		CollectionModel<EntityModel<PartieDispo>> body = modeleAfficherParties.toCollectionModel(message);
		return new ResponseEntity<CollectionModel>(body, HttpStatus.OK);
    }

    @PostMapping(value = "{idPartie}/ajouterJoueur")
	public ResponseEntity<EntityModel> ajouterJoueur(@PathVariable int idPartie, @RequestBody String nomJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			message.setIdPartie(idPartie);
			Joueur joueur = creerJoueur(nomJoueur, message);
			message = partie.ajouterJoueur(joueur);
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.AJOUTER_JOUEUR)) {
				EntityModel<MessagePartie> reponseAjout = modeleControleurParties.toModel(message);
				return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.CREATED);
			}
			else {
				throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.FORBIDDEN);
			}
		}
		else {
			message.setMessageErreur("La partie n'existe pas");
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.NOT_FOUND);
		}
    }

    private Joueur creerJoueur(String nomJoueur, MessagePartie message) {
		try {
			return new Joueur(nomJoueur);
		}
		catch(IllegalArgumentException ex) {
			message.setMessageErreur(ex.getMessage());
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.FORBIDDEN);
		}
	}

    @PostMapping(value = "{idPartie}/demarrerPartie")
	public ResponseEntity<EntityModel> demarrerPartie(@PathVariable int idPartie) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			message = partie.commencerPartie();
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.ERREUR)) {
				throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.FORBIDDEN);
			}
			message.setIdPartie(idPartie);
			EntityModel<MessagePartie> body = modeleControleurPartie.toModel(message);
			return new ResponseEntity<EntityModel>(body, HttpStatus.OK);
		}
		else {
			message.setMessageErreur("La partie n'existe pas");
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.NOT_FOUND);
		}
	}
}
