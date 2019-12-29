package rummikub.core;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.commands.*;
import rummikub.core.plateau.Plateau;
import java.util.List;

public class Partie {

    private final Pioche pioche;
    private final Plateau plateau;
    private List<Joueur> listeJoueurs;
    private Joueur joueurEnCours;
    private int numJoueur;
    private final Historique historique;

    public Partie(List<Joueur> listeJoueurs) {
        pioche = new Pioche();
        plateau = new Plateau();
        historique = new Historique();
        this.listeJoueurs = listeJoueurs;
        numJoueur = 0;
    }

    public void commencerPartie() {
        initialiserJoueurs();
		debutDuTour();
    }
    
    private void initialiserJoueurs() {
        listeJoueurs.forEach((joueur) -> {
            joueur.setPiocheInitiale(pioche.piocheInitiale());
        });
    }
    
    private void debutDuTour() {
		numJoueur = (numJoueur + 1) % listeJoueurs.size();
		joueurEnCours = listeJoueurs.get(numJoueur);
		joueurEnCours.initialiserNouveauTour();
        historique.reinitialiserHistorique();	
	}
	
	public Joueur getJoueurEnCours(){
		return joueurEnCours;
	}

	public void creerNouvelleSequence(List<Integer> listeIndexJetons) {
        executerAction(new CreerNouvelleSequence(plateau, joueurEnCours, listeIndexJetons));
    }
    
    public void ajouterJeton(List<Integer> indexes) {
        executerAction(new AjouterJeton(plateau, joueurEnCours, indexes));
    }
    
    public void fusionnerSequence(List<Integer> indexes) {
		executerAction(new FusionnerSequences(plateau, indexes));    
    }

    public void couperSequence(List<Integer> indexes) {
		executerAction(new CouperSequence(plateau, indexes));    
    }
    
    public void deplacerJeton(List<Integer> indexes) {
		executerAction(new DeplacerJeton(plateau, indexes));    
    }
    
    public void remplacerJoker(List<Integer> indexes) {
		executerAction(new RemplacerJoker(plateau, joueurEnCours, indexes));    
    }
    
    private void executerAction(Command action) {
        boolean aReussi = action.doCommand();
        if (aReussi) {
            historique.ajouterCommande(action);
        }
    }
    
    public void annulerDerniereAction() {
		historique.annulerDerniereCommande();    
    }
    
    public void terminerTour() {
		if (plateau.isValide()) {
            if (joueurEnCours.aJoueAuMoins1Jeton()) {
                joueurAJoue();
            } else {
                piocher();
            }
        } else {
            //plateau non valide
        }    
    }
    
    private void joueurAJoue() {
        if (joueurEnCours.isAutoriseAterminerLeTour()) {
			if(joueurEnCours.aGagne()){
				//fin de la partie
			}
			else {
				debutDuTour();
			}
        }  
        else {
           //pas assez de point pour terminer la partie
        }
    }

    private void piocher() {
        joueurEnCours.ajouteJeton(pioche.piocher1Jeton());
        debutDuTour();
    }

	public void abandonner() {
		//joueur abandonne    
    }
}

