package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.core.jeu.Joueur;
import rummikub.securite.ServiceJwt;
import rummikub.joueurs.JoueurConnecte;
import rummikub.joueurs.RepertoireJoueurConnecte;
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
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import javax.validation.constraints.Size;

/**
 * Controleur de la partie.
 */
@RestController
public class ControleurParties {

	private ListeParties listeParties;
	private ModeleControleurParties modeleControleurParties;
	private ModeleControleurPartie modeleControleurPartie;
	private ModeleAfficherParties modeleAfficherParties;
    private RepertoireJoueurConnecte repertoireJoueurConnecte;
	@Autowired
    private ServiceJwt serviceJwt;


	@Autowired
	public ControleurParties(ListeParties listeParties, ModeleControleurParties modeleControleurParties,
	  ModeleControleurPartie modeleControleurPartie, ModeleAfficherParties modeleAfficherParties,
	  RepertoireJoueurConnecte repertoireJoueurConnecte){
		this.listeParties = listeParties;
		this.modeleControleurParties = modeleControleurParties;
		this.modeleControleurPartie = modeleControleurPartie;
		this.modeleAfficherParties = modeleAfficherParties;
		this.repertoireJoueurConnecte = repertoireJoueurConnecte;
	}

	@PostMapping(value = "/0/creerPartie", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "{idPartie}/ajouterJoueur", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityModel> ajouterJoueur(@PathVariable int idPartie,
	  @Size(min=1, max = 15, message = "Le nom doit avoir entre 1 et 15 caractéres")
	  @RequestBody String nom) {
		Partie partie = listeParties.getPartie(idPartie);
		MessagePartie message = new MessagePartie();
		if(partie != null) {
			Joueur joueur = creerJoueur(nom, message);
			message = partie.ajouterJoueur(joueur);
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.AJOUTER_JOUEUR)) {
				message.setIdPartie(idPartie);
				return finaliserCreerJoueur(message);
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

	private ResponseEntity<EntityModel> finaliserCreerJoueur(MessagePartie message) {
		EntityModel<MessagePartie> reponseAjout = modeleControleurParties.toModel(message);
		JoueurConnecte joueurConnecte = new JoueurConnecte(message.getIdJoueur(),
														   message.getNomJoueur(),
														   message.getIdPartie());
		repertoireJoueurConnecte.save(joueurConnecte);
		String token = serviceJwt.creerToken(joueurConnecte);
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
}
