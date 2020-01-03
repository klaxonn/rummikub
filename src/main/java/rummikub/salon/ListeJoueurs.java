package rummikub.salon;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gestion de la liste des clients du salon.
 */
public final class ListeJoueurs {

	private static final Map<String,Boolean> listeJoueurs = new HashMap<>();
	private static String createurPartie = "";
	
	private ListeJoueurs() {
	}

	/**
	 * Ajoute un client.
	 * Si le nom est déjà présent dans la liste, un nouveau nom est généré.
	 *
	 * @param nomJoueur le nom du joueur
	 * @return le nom du joueur éventuellement modifié
	 */
	public static String ajouterJoueurConnecte(String nomJoueur) {
		String nom = nomJoueur;
		if(listeJoueurs.containsKey(nomJoueur)){
			 nom = nomJoueur + "-1";
		}
		listeJoueurs.put(nomJoueur,false);
		return nom;
	}

	/**
	 * Retourne la liste des clients connectés.
	 *
	 * @return la liste
	 */
	public static Set<String> getJoueursConnectes() {
		return listeJoueurs.keySet();
	}

	/**
	 * Retire un client.
	 *
	 * @param nomJoueur le nom du joueur
     * @throws UnsupportedOperationException si le nom
	 * n'est pas un client reconnu.
	 */
	public static void retirerJoueur(String nomJoueur){
		if(listeJoueurs.containsKey(nomJoueur)){
			listeJoueurs.remove(nomJoueur);
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
	public static int nombreJoueursConnectes(){
		return listeJoueurs.size();
	}

	/**
	 * Ajoute un client dans la partie.
	 *
	 * @param nomJoueur le nom du joueur
     * @throws UnsupportedOperationException si le nom
	 * n'est pas un client reconnu.
	 */
	public static void ajouteJoueurPartie(String nomJoueur) {
		if(listeJoueurs.containsKey(nomJoueur)){
			listeJoueurs.replace(nomJoueur,true);
		}
		else {
            throw new UnsupportedOperationException("Le nom n'est pas un joueur connecté");
		}
	}

	/**
	 * Retourne la liste des clients qui ont joint la partie.
	 *
	 * @return la liste
	 */
	public static Set<String> getJoueursPartie(){
		return listeJoueurs.keySet().stream()
							 .filter(i ->listeJoueurs.get(i).equals(true))
							 .collect(Collectors.toSet());
	}

	/**
	 * Retire tous les clients de la partie.
	 */
	public static void supprimerJoueursPartie() {
		listeJoueurs.forEach((k,v) ->listeJoueurs.replace(k,false));
	}

	/**
	 * Retourne le nombre des joueurs de la partie.
	 *
	 * @return la nombre
	 */
	public static long nombreJoueursPartie(){
		return listeJoueurs.keySet().stream()
									.filter(i ->listeJoueurs.get(i).equals(true))
									.count();
	}

	/**
	 * Promeut le joueur en tant que créateur de la partie.
	 *
	 * @param nomJoueur le nom du joueur
     * @throws UnsupportedOperationException si le nom
	 * n'est pas un client reconnu.
	 */
	public static void setCreateurPartie(String nomJoueur) {
		if(listeJoueurs.containsKey(nomJoueur)){
			createurPartie = nomJoueur;
		}
		else {
            throw new UnsupportedOperationException("Le nom n'est pas un joueur connecté");
		}
	}

	/**
	 * Retourne le joueur créateur de la partie.
	 *
	 * @return le joueur
	 */
	public static String getCreateurPartie() {
		return createurPartie;
	}
}

