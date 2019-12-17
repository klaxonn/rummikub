package rummikub.web;

import rummikub.core.jeu.Joueur;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public final class ListeJoueurs {

	private static final Map<String,Boolean> listeJoueurs = new HashMap<>();

	private ListeJoueurs() {
	}
	
	public static String ajouterJoueurConnecte(String nomJoueur) {
		if(listeJoueurs.containsKey(nomJoueur)){
			 nomJoueur = nomJoueur + "-1";
		}
		listeJoueurs.put(nomJoueur,false);
		return nomJoueur;
	}

	public static String getJoueursConnectes() {
		return listeJoueurs.keySet().toString();
	}

	public static String getJoueursPrets(){
		String chaine = "";
		chaine = listeJoueurs.keySet().stream()
							 .filter(i ->listeJoueurs.get(i).equals(true))
							 .collect(Collectors.toSet())
							 .toString();
		return chaine;
	}

	public static void setJoueurPret(String nomJoueur) {
		listeJoueurs.replace(nomJoueur,true);
	}

	public static int nombreJoueursConnectes(){
		return listeJoueurs.size();
	}

	public static long nombreJoueursPrets(){
		return listeJoueurs.keySet().stream()
									.filter(i ->listeJoueurs.get(i).equals(true))
									.count();
	}

	public static void retirerJoueur(String nomJoueur){
		listeJoueurs.remove(nomJoueur);
	}

	public static Joueur[] creerListeJoueursPrets(){
		return listeJoueurs.keySet().stream()
									.filter(i ->listeJoueurs.get(i).equals(true))
									.map(i -> new Joueur(i))
									.toArray(Joueur[]::new);
	}

}

