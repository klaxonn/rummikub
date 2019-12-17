package Rummikub.core.jeu.commands;

import Rummikub.core.plateau.Plateau;
import Rummikub.core.jeu.Joueur;
import Rummikub.core.pieces.Jeton;
import Rummikub.ihm.ControleurAbstrait;
import java.util.List;

/**
 * Action représentant l'ajout d'une séquence au plateau.
 */
public class CreerNouvelleSequence implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final ControleurAbstrait controleur;
    private int indexSequence = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param controleur le controleur qui s'occupe de l'IHM
     */
    public CreerNouvelleSequence(Plateau plateau, Joueur joueur, ControleurAbstrait controleur) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.controleur = controleur;
    }

    @Override
    public boolean doCommand() {
        List<Integer> listeIndexJetons = controleur.obtenirListeJetons();
        List<Jeton> listeJetons;
        try {
            listeJetons = joueur.utiliseJetons(listeIndexJetons);
        } catch (IndexOutOfBoundsException e) {
            controleur.afficherMessage(e.getMessage());
            return false;
        }
        try {
            indexSequence = plateau.creerSequence(listeJetons);
            return true;
        } catch (Exception e) {
            joueur.ajouteJetons(listeJetons);
            controleur.afficherMessage(e.getMessage());
            return false;
        }
    }

    @Override
    public void undoCommand() {
        List<Jeton> jetons = plateau.supprimerSequence(indexSequence);
        joueur.ajouteJetons(jetons);
    }
}
