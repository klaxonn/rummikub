package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.Jeton;
import java.util.List;

/**
 * Action représentant l'ajout d'une séquence au plateau.
 */
public class CreerNouvelleSequence implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final List<Integer> listeIndexJetons;
    private int indexSequence = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param listeIndexJetons les indexes des jetons constituant la nouvelle séquence
     */
    public CreerNouvelleSequence(Plateau plateau, Joueur joueur, List<Integer> listeIndexJetons) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.listeIndexJetons = listeIndexJetons;
        
    }

    @Override
    public boolean doCommand() {
        List<Jeton> listeJetons;
        try {
            listeJetons = joueur.utiliseJetons(listeIndexJetons);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        try {
            indexSequence = plateau.creerSequence(listeJetons);
            return true;
        } catch (Exception e) {
            joueur.ajouteJetons(listeJetons);
            return false;
        }
    }

    @Override
    public void undoCommand() {
        List<Jeton> jetons = plateau.supprimerSequence(indexSequence);
        joueur.ajouteJetons(jetons);
    }
}
