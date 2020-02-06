package rummikub.controleurs;

import rummikub.securite.JoueurConnecte;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

/**
 * Gestion de l'ensemble des joueurs.
 */
@Component
public class ListeJoueurs {

	private Map<List, JoueurConnecte> joueurs;

	/**
	 * Construit une liste de joueurs.
	 */
	public ListeJoueurs() {
		joueurs = new HashMap<>();
	}

	/**
	 * Ajoute un joueur dans la liste.
	 */
	public void ajouterJoueur(JoueurConnecte joueur) {
		int idPartie = joueur.getIdPartie();
		int idJoueur = joueur.getId();
		joueurs.put(List.of(idPartie, idJoueur), joueur);
	}

	/**
	 * Supprime un joueur dans la liste.
	 *
	 * @param idPartie l'id de la partie
	 * @param idJoueur l'id du joueur
	 * @return le joueur supprimé ou null s'il n'existe pas
	 */
	public JoueurConnecte retirerJoueur(int idPartie, int idJoueur) {
		JoueurConnecte joueur =  joueurs.get(List.of(idPartie, idJoueur));
		if(joueur != null) {
			joueur.desactive();
			joueurs.remove(joueur);
		}
		return joueur;
	}
}
