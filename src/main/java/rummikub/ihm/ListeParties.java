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
	 * @return l'id de partie
	 */
	public int creerPartie() {
		Partie partie = FabriquePartie.creerNouvellePartie();
		int indexNouvellePartie = listeParties.size() + 1;
		listeParties.put(indexNouvellePartie, partie);
		return indexNouvellePartie;
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
			String joueurs = partie.listeJoueursPrets();
			if(!joueurs.isEmpty()) {
				resultat += "idPartie: " + id + " joueurs: [" + joueurs + "]\n";
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
