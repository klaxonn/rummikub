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

	public String[] creerNouvelleSequence(List<Integer> listeIndexJetons) {
        return executerAction(new CreerNouvelleSequence(plateau, joueurEnCours, listeIndexJetons));
    }
    
    public String[] ajouterJeton(List<Integer> indexes) {
        return executerAction(new AjouterJeton(plateau, joueurEnCours, indexes));
    }
    
    public String[] fusionnerSequence(List<Integer> indexes) {
		return executerAction(new FusionnerSequences(plateau, indexes));
    }

    public String[] couperSequence(List<Integer> indexes) {
		return executerAction(new CouperSequence(plateau, indexes));
    }
    
    public String[] deplacerJeton(List<Integer> indexes) {
		return executerAction(new DeplacerJeton(plateau, indexes));
    }
    
    public String[] remplacerJoker(List<Integer> indexes) {
		return executerAction(new RemplacerJoker(plateau, joueurEnCours, indexes));
    }
    
    private String[] executerAction(Command action) {
        action.doCommand();
        historique.ajouterCommande(action);
		return informationsPartie();
    }

	private String[] informationsPartie() {
		return new String[]{plateau.toString(),joueurEnCours.afficheJetonsJoueur()};
	}
    
    public String[] annulerDerniereAction() {
		historique.annulerDerniereCommande();
		return informationsPartie();
    }
    
    public String[] terminerTour() throws UnsupportedOperationException {
		if (plateau.isValide()) {
            if (joueurEnCours.aJoueAuMoins1Jeton()) {
                joueurAJoue();
            } else {
                piocher();
            }
			return informationsPartie();
        } else {
            throw new UnsupportedOperationException("plateau non valide");
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
			throw new UnsupportedOperationException("Pas assez de point pour terminer la partie");
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

