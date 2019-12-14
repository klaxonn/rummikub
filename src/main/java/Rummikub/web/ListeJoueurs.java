package Rummikub.web;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import Rummikub.core.jeu.Joueur;

public class ListeJoueurs {

	private static final HashMap<String,Boolean> listeJoueurs = new HashMap<>();
	
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

