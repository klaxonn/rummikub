package rummikub.salon;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ListeJoueurs {

	private static final Map<String,Boolean> listeJoueurs = new HashMap<>();
	private static String createurPartie = "";
	
	private ListeJoueurs() {
	}

	public static String ajouterJoueurConnecte(String nomJoueur) {
		String nom = nomJoueur;
		if(listeJoueurs.containsKey(nomJoueur)){
			 nom = nomJoueur + "-1";
		}
		listeJoueurs.put(nomJoueur,false);
		return nom;
	}

	public static String getCreateurPartie() {
		return createurPartie;
	}

	public static void setCreateurPartie(String nom) {
		createurPartie = nom;
	}

	public static void supprimerJoueursPartie() {
		listeJoueurs.forEach((k,v) ->listeJoueurs.replace(k,false));
	}

	public static Set<String> getJoueursConnectes() {
		return listeJoueurs.keySet();
	}

	public static Set<String> getJoueursPartie(){
		return listeJoueurs.keySet().stream()
							 .filter(i ->listeJoueurs.get(i).equals(true))
							 .collect(Collectors.toSet());
	}

	public static void ajouteJoueurPartie(String nomJoueur) {
		listeJoueurs.replace(nomJoueur,true);
	}

	public static void retirerJoueur(String nomJoueur){
		listeJoueurs.remove(nomJoueur);
	}

	public static int nombreJoueursConnectes(){
		return listeJoueurs.size();
	}

	public static long nombreJoueursPartie(){
		return listeJoueurs.keySet().stream()
									.filter(i ->listeJoueurs.get(i).equals(true))
									.count();
	}
}

