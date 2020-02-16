package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import static rummikub.core.api.MessagePartie.TypeMessage.*;
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
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.Size;
import javax.servlet.http.HttpServletRequest;

/**
 * Controleur qui s'occupe des actions avant et après une partie.
 */
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ControleurParties {

	private ListeParties listeParties;
	private ListeJoueurs listeJoueurs;
	private ModeleControleurParties modeleControleurParties;
	private ModeleControleurPartie modeleControleurPartie;
	private ModeleAfficherParties modeleAfficherParties;

	@Autowired
	public ControleurParties(ListeParties listeParties, ModeleControleurParties modeleControleurParties,
	  ModeleControleurPartie modeleControleurPartie, ModeleAfficherParties modeleAfficherParties,
	  ListeJoueurs listeJoueurs){
		this.listeParties = listeParties;
		this.modeleControleurParties = modeleControleurParties;
		this.modeleControleurPartie = modeleControleurPartie;
		this.modeleAfficherParties = modeleAfficherParties;
		this.listeJoueurs = listeJoueurs;
	}

	@PostMapping("/0/creerPartie")
	public ResponseEntity<EntityModel> creerPartie(
	  @Size(min=1, max = 15, message = "Le nom doit avoir entre 1 et 15 caractéres")
	  @RequestBody String nom, HttpServletRequest requete) {
		int idPartie = listeParties.creerPartie();
		return ajouterJoueur(idPartie, nom, requete);
    }

    @GetMapping(value = "/0/listerPartiesDispos", consumes = "*/*")
	public ResponseEntity<CollectionModel> listerPartiesDispos() {
		List<PartieDispo> message = listeParties.listerPartiesDispos();
		CollectionModel<EntityModel<PartieDispo>> body = modeleAfficherParties.toCollectionModel(message);
		return new ResponseEntity<CollectionModel>(body, HttpStatus.OK);
    }

    @PostMapping("{idPartie}/ajouterJoueur")
	public ResponseEntity<EntityModel> ajouterJoueur(@PathVariable int idPartie,
	  @Size(min=1, max = 15, message = "Le nom doit avoir entre 1 et 15 caractéres")
	  @RequestBody String nom, HttpServletRequest requete) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			String adresseIP = requete.getLocalAddr();
			message = listeJoueurs.ajouterJoueur(nom, idPartie, adresseIP);
			if(message.getTypeMessage().equals(AJOUTER_JOUEUR)) {
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

    @PostMapping("{idPartie}/{idJoueur}/demarrerPartie")
	public ResponseEntity<EntityModel> demarrerPartie(@PathVariable int idPartie, @PathVariable int idJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			message = partie.commencerPartie(idJoueur);
			if(message.getTypeMessage().equals(ERREUR)) {
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

	@DeleteMapping("{idPartie}/{idJoueur}/quitterPartie")
	public ResponseEntity<EntityModel> quitterPartie(@PathVariable int idPartie, @PathVariable int idJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			message = partie.quitterPartie(idJoueur);
			if(message.getTypeMessage().equals(ERREUR)) {
				throw new ControleurErreurException(message, modeleControleurPartie, HttpStatus.FORBIDDEN);
			}
			listeJoueurs.retirerJoueur(idPartie, idJoueur);
			EntityModel<MessagePartie> body = modeleControleurParties.toModel(message);
			return new ResponseEntity<EntityModel>(body, HttpStatus.OK);
		}
		else {
			message.setMessageErreur("La partie n'existe pas");
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{idPartie}/{idJoueur}/arreterPartie")
	public ResponseEntity<EntityModel> arreterPartie(@PathVariable int idPartie, @PathVariable int idJoueur) {
		MessagePartie message = new MessagePartie();
		Partie partie = listeParties.getPartie(idPartie);
		if(partie != null) {
			if(listeJoueurs.isJoueurPartie(idPartie, idJoueur)) {
				if(listeParties.arreterPartie(idPartie)) {
					listeJoueurs.retirerTousJoueurs(idPartie);
					message.setTypeMessage(FIN_DE_PARTIE);
					EntityModel<MessagePartie> body = modeleControleurParties.toModel(message);
					return new ResponseEntity<EntityModel>(body, HttpStatus.OK);
				}
				else {
					message = partie.afficherPartie(idJoueur);
					message.setTypeMessage(ERREUR);
					message.setMessageErreur("Trop de joueurs pour supprimer la partie");
					message.setIdPartie(idPartie);
					throw new ControleurErreurException(message, modeleControleurPartie, HttpStatus.FORBIDDEN);
				}
			}
			else {
				message.setMessageErreur("Opération non autorisée");
				throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.UNAUTHORIZED);
			}
		}
		else {
			message.setMessageErreur("La partie n'existe pas");
			throw new ControleurErreurException(message, modeleControleurParties, HttpStatus.NOT_FOUND);
		}
	}
}
