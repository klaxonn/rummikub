package rummikub.ihm;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.core.jeu.Joueur;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;

/**
 * Controleur de la partie.
 */
@RestController
public class ControleurParties {

	private ListeParties listeParties;
	private ModeleControleurParties modeleControleur;


	@Autowired
	public ControleurParties(ListeParties listeParties, ModeleControleurParties modeleControleur){
		this.listeParties = listeParties;
		this.modeleControleur = modeleControleur;
	}

	@PostMapping(value = "/creerPartie")
	public EntityModel<MessagePartie> creerPartie(@RequestBody String nomJoueur) {
		int idPartie = listeParties.creerPartie();
		return ajouterJoueur(idPartie, nomJoueur);
    }

    @GetMapping(value = "/listerPartiesDispos")
	public String listerPartiesDispos() {
		return listeParties.listerPartiesDispos();
    }

    @PostMapping(value = "{idPartie}/ajouterJoueur")
	public EntityModel<MessagePartie> ajouterJoueur(@PathVariable int idPartie, @RequestBody String nomJoueur) {
		Partie partie = listeParties.getPartie(idPartie);
		if(partie != null) {
			Joueur joueur = new Joueur(nomJoueur);
			MessagePartie message = partie.ajouterJoueur(joueur);
			if(message.getTypeMessage().equals(MessagePartie.TypeMessage.AJOUTER_JOUEUR)) {
				message.setIdPartie(idPartie);
				return modeleControleur.toModel(message);
			}
			else {
				throw new UnsupportedOperationException(message.getMessageErreur());
			}
		}
		else {
			throw new IllegalArgumentException("La partie n'existe pas");
		}
    }

    @PostMapping(value = "{idPartie}/demarrerPartie")
	public EntityModel<MessagePartie> demarrerPartie(@PathVariable int idPartie) {
		Partie partie = listeParties.getPartie(idPartie);
		if(partie != null) {
			MessagePartie message = partie.commencerPartie();
			message.setIdPartie(idPartie);
			return modeleControleur.toModel(message);
		}
		else {
			throw new IllegalArgumentException("La partie n'existe pas");
		}
	}
}
