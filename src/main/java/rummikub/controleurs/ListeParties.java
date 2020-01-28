package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.FabriquePartie;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
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
	 *
	 * @return la liste.
	 */
	public List<PartieDispo> listerPartiesDispos() {
		int id = 1;
		List<PartieDispo> resultat = new ArrayList<>();
		for(Partie partie : listeParties.values()) {
			List<String> joueurs = partie.listeJoueursPrets();
			if(!joueurs.isEmpty()) {
				PartieDispo partieDispo = new PartieDispo(id, joueurs);
				resultat.add(partieDispo);
			}
			id++;
		}
		return resultat;
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
