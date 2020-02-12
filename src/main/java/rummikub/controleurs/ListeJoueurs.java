package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static rummikub.core.api.MessagePartie.TypeMessage.*;
import rummikub.core.api.Partie;
import rummikub.securite.JoueurConnecte;
import rummikub.securite.ServiceJwt;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Gestion de l'ensemble des joueurs.
 */
@Component
public class ListeJoueurs {

	private List<JoueurConnecte> joueurs;
    private ServiceJwt serviceJwt;
    private ListeParties listeParties;

	/**
	 * Construit une liste de joueurs.
	 *
	 * @param listeParties la liste des parties
	 * @param serviceJwt le service de gestion des tokens
	 */
	@Autowired
	public ListeJoueurs(ListeParties listeParties, ServiceJwt serviceJwt) {
		joueurs = new ArrayList<>();
		this.serviceJwt = serviceJwt;
		this.listeParties = listeParties;
	}

	/**
	 * Ajoute un joueur dans la partie.
	 * Le message envoyé peut être de deux types :
	 * Le message envoyé est de type AJOUTER_JOUEUR en cas de réussite.
     *
     * Le message envoyé est de type ERREUR en cas d'échec.
	 *
	 * @param nomJoueur nom du joueur
	 * @param idPartie l'id de la partie que veut joindre le joueur
	 * @return le message contenant les informations
	 */
	public MessagePartie ajouterJoueur(String nomJoueur, int idPartie, String adresseIP) {
		MessagePartie message = new MessagePartie();
		try {
			JoueurConnecte joueur = new JoueurConnecte(nomJoueur);
			Partie partie = listeParties.getPartie(idPartie);
			message = partie.ajouterJoueur(joueur);
			if(message.getTypeMessage().equals(AJOUTER_JOUEUR)) {
				int idJoueur = message.getIdJoueur();
				message.setIdPartie(idPartie);
				String token = configJoueur(joueur, idPartie, idJoueur, adresseIP);
				message.setToken("Bearer " + token);
			}
			return message;
		}
		catch(IllegalArgumentException ex) {
			message.setTypeMessage(ERREUR);
			message.setMessageErreur(ex.getMessage());
			return message;
		}
	}

	private String configJoueur(JoueurConnecte joueur, int idPartie, int idJoueur, String adresseIP) {
		joueur.setId(idJoueur);
		joueur.setIdPartie(idPartie);
		joueur.setAdresseIP(adresseIP);
		joueurs.add(joueur);
		return serviceJwt.creerToken(joueur);
	}

	/**
	 * Supprime un joueur d'une partie.
	 *
	 * @param idPartie l'id de la partie
	 * @param idJoueur l'id du joueur
	 * @return <code>true</code> si le joueur a été supprimé
	 */
	public boolean retirerJoueur(int idPartie, int idJoueur) {
		return joueurs.stream().filter(i -> i.getId() == idJoueur &&
											i.getIdPartie() == idPartie)
							   .findFirst()
							   .map(i -> supprimeJoueur(i))
							   .isPresent();
	}

	private boolean supprimeJoueur(JoueurConnecte joueur) {
		joueur.desactive();
		return joueurs.remove(joueur);
	}

	/**
	 * Supprime tous les joueurs d'une partie.
	 *
	 * @param idPartie l'id de la partie
	 * @return <code>true</code> si tous les joueurs ont été supprimés
	 */
	public boolean retirerTousJoueurs(int idPartie) {
		joueurs.stream().filter(i -> i.getIdPartie() == idPartie)
						.forEach(i-> i.desactive());
		return joueurs.removeIf(i -> i.getIdPartie() == idPartie);
	}
}
