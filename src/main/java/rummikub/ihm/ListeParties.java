package rummikub.ihm;

import rummikub.core.api.Partie;
import rummikub.core.api.FabriquePartie;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Gestion de l'ensemble des parties.
 */
@Component("listeParties")
public class ListeParties {

	private Map<Integer, Partie> listeParties;

	/**
	 * Construit une liste de parties.
	 *
	 */
	public ListeParties() {
		listeParties = new HashMap<>();
	}

	/**
	 * Crée une partie.
	 *
	 * @param listeNomsJoueurs la liste des noms des joueurs
	 * @return la partie
	 */
	public Partie creerPartie(List<String> listeNomsJoueurs) {
		Partie partie = FabriquePartie.creerNouvellePartie(listeNomsJoueurs);
		int indexNouvellePartie = listeParties.size() + 1;
		listeParties.put(indexNouvellePartie, partie);
		return partie;
	}

	/**
	 * Retourne la liste des parties non commencées.
	 * Format : "idPartie: id joueurs: ["joueur1", "joueur2",...]"
	 *
	 * @return le texte.
	 */
	public String listerPartiesDispos() {
		int id = 1;
		String resultat = "";
		for(Partie partie : listeParties.values()) {
			if(!partie.isPartieCommence()) {
				resultat += "idPartie: " + id + " joueurs: [" + partie.afficherJoueursPartie() + "]\n";
			}
			id++;
		}
		return resultat.trim();
	}

	/**
	 * Retourne la partie correspondant à l'id.
	 *
	 * @return le partie.
	 */
	public Partie getPartie(int id) {
		return listeParties.get(id);
	}
}
