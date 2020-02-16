package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.Jeton;
<<<<<<< HEAD:src/main/java/rummikub/core/jeu/commands/CreerNouvelleSequence.java
import rummikub.ihm.ControleurAbstrait;
=======
>>>>>>> web:src/main/java/rummikub/core/jeu/commands/CreerNouvelleSequence.java
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
    public void doCommand() {
        List<Jeton> listeJetons;
        listeJetons = joueur.utiliseJetons(listeIndexJetons);
        try {
            indexSequence = plateau.creerSequence(listeJetons);
        } catch (Exception e) {
            joueur.ajouteJetons(listeJetons);
            throw e;
        }
    }

    @Override
    public void undoCommand() {
        List<Jeton> jetons = plateau.supprimerSequence(indexSequence);
        joueur.ajouteJetons(jetons);
    }
}
