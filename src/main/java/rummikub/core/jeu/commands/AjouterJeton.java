package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.Joker;
import rummikub.core.pieces.Jeton;
import java.util.List;

/**
 * Action représentant l'ajout d'un jeton à une séquence.
 */
public class AjouterJeton implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final List<Integer> indexes;
    private int indexArrivee = 0;
    private int indexJeton = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param indexes les indexes du jeton à ajouter et de la séquence
     */
    public AjouterJeton(Plateau plateau, Joueur joueur, List<Integer> indexes) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.indexes = indexes;
    }

    @Override
    public void doCommand() {
        int indexJetonJoueur = indexes.get(0);
        indexArrivee = indexes.get(1);

        Jeton jetonAAjouter = null;
        jetonAAjouter = joueur.utiliseJeton(indexJetonJoueur);
        try {
            indexJeton = plateau.ajouterJeton(indexArrivee, jetonAAjouter);
        } catch (Exception e) {
            joueur.ajouteJeton(jetonAAjouter);
           	throw e;
        }
    }

    @Override
    public void undoCommand() {
        Jeton jeton = plateau.retirerJeton(indexArrivee, indexJeton);
        if (jeton.isJoker()) {
            ((Joker) jeton).reinitialiser();
        }
        joueur.ajouteJeton(jeton);
    }
}
