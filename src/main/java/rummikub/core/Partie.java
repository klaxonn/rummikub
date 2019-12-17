package rummikub.core;

import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.commands.*;
import rummikub.core.plateau.Plateau;
import rummikub.ihm.ControleurAbstrait;
import java.util.ArrayList;
import java.util.List;

/**
 * Représentation d'une partie.
 *
 */
public class Partie {

    private final Pioche pioche;
    private final Plateau plateau;
    private List<Joueur> listeJoueurs;
    private Joueur joueurEnCours;
    private final ControleurAbstrait controleur;
    private boolean tourTermine;
    private final Historique historique;

    /**
     * Crée une nouvelle partie.
     *
     * @param controleur le controleur utilisé pour l'IHM
     */
    public Partie(ControleurAbstrait controleur) {
        pioche = new Pioche();
        plateau = new Plateau();
        listeJoueurs = new ArrayList<>();
        this.controleur = controleur;
        historique = new Historique();
        tourTermine = false;
    }

    /**
     * Démarre une partie et la poursuit jusqu'à sa fin.
     *
     */
    public void commencerPartie() {
        controleur.afficherIntroduction();
        initialiserJoueurs();
        int numJoueur = 0;
        do {
            joueurEnCours = listeJoueurs.get(numJoueur);
            joueurEnCours.initialiserNouveauTour();
            tourTermine = false;
            joueTour();
            numJoueur = (numJoueur + 1) % listeJoueurs.size();
            historique.reinitialiserHistorique();
        } while (!joueurEnCours.aGagne());
        controleur.afficherVictoireDe(joueurEnCours);
    }

    private void initialiserJoueurs() {
        listeJoueurs = controleur.listeDesJoueurs();
        listeJoueurs.forEach((joueur) -> {
            joueur.setPiocheInitiale(pioche.piocheInitiale());
        });
    }

    private void joueTour() {
        Actions action;
        do {
            controleur.changerJoueurCourant(joueurEnCours);
            controleur.afficherPartie(plateau.toString());
            action = controleur.obtenirAction();
            gestionChoixAction(action);
        } while (!tourTermine);
    }

    private void gestionChoixAction(Actions action) {
        switch (action) {
            case AFFICHER_AIDE:
                controleur.afficherAide();
                break;
            case NOUVELLE_SEQUENCE:
                executerAction(new CreerNouvelleSequence(plateau, joueurEnCours, controleur));
                break;
            case AJOUTER_JETON:
                executerAction(new AjouterJeton(plateau, joueurEnCours, controleur));
                break;
            case FUSIONNER_SEQUENCES:
                executerAction(new FusionnerSequences(plateau, controleur));
                break;
            case COUPER_SEQUENCE:
                executerAction(new CouperSequence(plateau, controleur));
                break;
            case DEPLACER_JETON:
                executerAction(new DeplacerJeton(plateau, controleur));
                break;
            case REMPLACER_JOKER:
                executerAction(new RemplacerJoker(plateau, joueurEnCours, controleur));
                break;
            case ANNULER_DERNIERE_ACTION:
                historique.annulerDerniereCommande();
                break;
            case TERMINER_TOUR:
                terminerTour();
                break;
            case ABANDONNER:
                controleur.aQuittePartie(joueurEnCours);
				break;
			default:
				controleur.afficherMessage("Commande non reconnue");
                break;
        }
    }

    private void executerAction(Command action) {
        boolean aReussi = action.doCommand();
        if (aReussi) {
            historique.ajouterCommande(action);
        }
    }

    private void terminerTour() {
        if (plateau.isValide()) {
            if (joueurEnCours.aJoueAuMoins1Jeton()) {
                joueurAJoue();
            } else {
                piocher();
            }
        } else {
            controleur.afficherMessage("Plateau non valide");
        }
    }

    private void joueurAJoue() {
        if (joueurEnCours.isAutoriseAterminerLeTour()) {
            tourTermine = true;
        } else {
            controleur.afficherMessage("Pas assez de points");
        }
    }

    private void piocher() {
        joueurEnCours.ajouteJeton(pioche.piocher1Jeton());
        tourTermine = true;
    }
}
