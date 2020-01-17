package rummikub.salon;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import rummikub.core.api.Partie;
import rummikub.core.jeu.Joueur;


/**
 * Gestion de la liste des clients du salon.
 */
@Component("listeJoueurs")
public class ListeJoueurs {

	private Map<String,Boolean> listeJoueurs;
	private String createurPartie;

	public ListeJoueurs() {
		listeJoueurs = new HashMap<>();
		createurPartie = "";
	}

	/**
	 * Ajoute un client.
	 * Si le nom est déjà présent dans la liste, un nouveau nom est généré.
	 *
	 * @param nomJoueur le nom du joueur
	 * @return le nom du joueur éventuellement modifié
	 * @throws UnsupportedOperationException si le nom n'est pas valide.
	 */
	public String ajouterJoueurConnecte(String nomJoueur) {
		if(Joueur.isNomValide(nomJoueur)) {
			String nom = nomJoueur;
			if(listeJoueurs.containsKey(nomJoueur)){
				 nom = nomJoueur + "-1";
			}
			listeJoueurs.put(nom,false);
			return nom;
		}
		else {
			throw new UnsupportedOperationException("Le nom n'est pas valide");
		}
	}

	/**
	 * Retourne la liste des clients connectés.
	 *
	 * @return la liste
	 */
	public List<String> getJoueursConnectes() {
		return new ArrayList<String>(listeJoueurs.keySet());
	}

	/**
	 * Retire un client.
	 * Si le client est le créateur de la partie,
	 * tous les joueurs sont supprimés de la partie.
	 *
	 * @param nomJoueur le nom du joueur
     * @throws UnsupportedOperationException si le nom
	 * n'est pas un client reconnu.
	 */
	public void retirerJoueur(String nomJoueur) {
		if(listeJoueurs.containsKey(nomJoueur)){
			listeJoueurs.remove(nomJoueur);
			if(nomJoueur.equals(createurPartie)){
				supprimerJoueursPartie();
			}
		}
		else {
            throw new UnsupportedOperationException("Le nom n'est pas un joueur connecté");
		}
	}

	/**
	 * Retourne le nombre des clients connectés.
	 *
	 * @return la nombre
	 */
	public int nombreJoueursConnectes() {
		return listeJoueurs.size();
	}

	/**
	 * Ajoute un client dans la partie.
	 *
	 * @param nomJoueur le nom du joueur
     * @throws UnsupportedOperationException si le nom
	 * n'est pas un client reconnu, ou si la partie est pleine.
	 */
	public void ajouterJoueurPartie(String nomJoueur) {
		if(listeJoueurs.size() < Partie.NOMBRE_MAX_JOUEURS_PARTIE) {
			if(listeJoueurs.containsKey(nomJoueur)){
				listeJoueurs.replace(nomJoueur,true);
				if(nombreJoueursPartie() == 1){
					createurPartie = nomJoueur;
				}
			}
			else {
				throw new UnsupportedOperationException("Le nom n'est pas un joueur connecté");
			}
		}
		else {
			throw new UnsupportedOperationException("La partie est complète");
		}
	}

	/**
	 * Retourne la liste des clients qui ont joint la partie.
	 *
	 * @return la liste
	 */
	public List<String> getJoueursPartie() {
		return listeJoueurs.keySet().stream()
							 .filter(i ->listeJoueurs.get(i).equals(true))
							 .collect(Collectors.toList());
	}

	/**
	 * Retire tous les clients de la partie.
	 */
	public void supprimerJoueursPartie() {
		listeJoueurs.forEach((k,v) ->listeJoueurs.replace(k,false));
		createurPartie = "";
	}

	/**
	 * Retourne le nombre des joueurs de la partie.
	 *
	 * @return la nombre
	 */
	public long nombreJoueursPartie() {
		return listeJoueurs.keySet().stream()
									.filter(i ->listeJoueurs.get(i).equals(true))
									.count();
	}

	/**
	 * Retourne le joueur créateur de la partie.
	 *
	 * @return le joueur
	 */
	public String getCreateurPartie() {
		return createurPartie;
	}
}

