package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.securite.ServiceJwt;
import rummikub.securite.JoueurConnecte;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.Size;

/**
 * Controleur de la partie.
 */
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ControleurParties {

	private ListeParties listeParties;
	private ListeJoueurs joueurs;
	private ModeleControleurParties modeleControleurParties;
	private ModeleControleurPartie modeleControleurPartie;
	private ModeleAfficherParties modeleAfficherParties;

	@Autowired
    private ServiceJwt serviceJwt;

	@Autowired
	public ControleurParties(ListeParties listeParties, ModeleControleurParties modeleControleurParties,
	  ModeleControleurPartie modeleControleurPartie, ModeleAfficherParties modeleAfficherParties,
	  ListeJoueurs joueurs){
		this.listeParties = listeParties;
		this.modeleControleurParties = modeleControleurParties;
		this.modeleControleurPartie = modeleControleurPartie;
		this.modeleAfficherParties = modeleAfficherParties;
		this.joueurs = joueurs;
	}

	@PostMapping(value = "/0/creerPartie")
	public ResponseEntity<EntityModel> creerPartie(
	  @Size(min=1, max = 15, message = "Le nom doit avoir entre 1 et 15 caractéres")
	  @RequestBody String nom) {
		int idPartie = listeParties.creerPartie();
		return ajouterJoueur(idPartie, nom);
    }

    @GetMapping(value = "/0/listerPartiesDispos")
	public ResponseEntity<CollectionModel> listerPartiesDispos() {
		List<PartieDispo> message = listeParties.listerPartiesDispos();
		CollectionModel<EntityModel<PartieDispo>> body = modeleAfficherParties.toCollectionModel(message);
		return new ResponseEntity<CollectionModel>(body, HttpStatus.OK);
    }

    @PostMapping(value = "{idPartie}/ajouterJoueur")
	public ResponseEntity<EntityModel> ajouterJoueur(@PathVariable int idPartie,
	  @Size(min=1, max = 15, message = "Le nom doit avoir entre 1 et 15 caractéres")
	  @RequestBody String nom) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			JoueurConnecte joueur = creerJoueur(nom, message);
			message = partie.ajouterJoueur(joueur);
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.AJOUTER_JOUEUR)) {
				message.setIdPartie(idPartie);
				return finaliserCreerJoueur(joueur, message);
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

    private JoueurConnecte creerJoueur(String nomJoueur, MessagePartie message) {
		try {
			return new JoueurConnecte(nomJoueur);
		}
		catch(IllegalArgumentException ex) {
			message.setMessageErreur(ex.getMessage());
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.FORBIDDEN);
		}
	}

	private ResponseEntity<EntityModel> finaliserCreerJoueur(JoueurConnecte joueur, MessagePartie message) {
		EntityModel<MessagePartie> reponseAjout = modeleControleurParties.toModel(message);
		joueur.setId(message.getIdJoueur());
		joueur.setIdPartie(message.getIdPartie());
		joueurs.ajouterJoueur(joueur);
		String token = serviceJwt.creerToken(joueur);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		return new ResponseEntity<EntityModel>(reponseAjout, headers, HttpStatus.CREATED);
	}

    @PostMapping(value = "{idPartie}/{idJoueur}/demarrerPartie")
	public ResponseEntity<EntityModel> demarrerPartie(@PathVariable int idPartie, @PathVariable int idJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			message = partie.commencerPartie(idJoueur);
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

	@DeleteMapping(value = "{idPartie}/{idJoueur}/quitterPartie")
	public ResponseEntity<EntityModel> quitterPartie(@PathVariable int idPartie, @PathVariable int idJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			message = partie.quitterPartie(idJoueur);
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.ERREUR)) {
				throw new ControleurErreurException(message, modeleControleurPartie, HttpStatus.FORBIDDEN);
			}
			joueurs.retirerJoueur(idPartie, idJoueur);
			EntityModel<MessagePartie> body = modeleControleurParties.toModel(message);
			return new ResponseEntity<EntityModel>(body, HttpStatus.OK);
		}
		else {
			message.setMessageErreur("La partie n'existe pas");
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.NOT_FOUND);
		}
	}
}
