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
	private List<Integer> partiesTerminees;

	/**
	 * Construit une liste de parties.
	 */
	public ListeParties() {
		listeParties = new HashMap<>();
		partiesTerminees = new ArrayList<>();
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
	 * @param id l'id de la partie à récupérer
	 * @return la partie ou null si la partie n'existe pas
	 */
	public Partie getPartie(int id) {
		return listeParties.get(id);
	}

	/**
	 * Supprime la partie correspondant à l'id.
	 *
	 * @param id l'id de la partie à supprimer
	 */
	public void supprimerPartie(int id) {
		listeParties.remove(id);
		partiesTerminees.add(id);
	}

	/**
	 * Détermine si la partie a été supprimée.
	 *
	 * @param id l'id de la partie à tester
	 * @return true si elle a été supprimée
	 */
	public boolean isPartieSupprimee(int id) {
		return partiesTerminees.contains(id);
	}
}
