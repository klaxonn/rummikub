package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.Joker;
import rummikub.core.pieces.Jeton;
import java.util.List;
import java.util.Arrays;

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
    public boolean doCommand() {
        /*List<String> messages = Arrays.asList("Numéro du jeton à ajouter : ",
                "Numéro de la séquence d'arrivée : ");*/
        int indexJetonJoueur = indexes.get(0);
        indexArrivee = indexes.get(1);

        Jeton jetonAAjouter = null;
        try {
            jetonAAjouter = joueur.utiliseJeton(indexJetonJoueur);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        try {
            indexJeton = plateau.ajouterJeton(indexArrivee, jetonAAjouter);
            return true;
        } catch (Exception e) {
            joueur.ajouteJeton(jetonAAjouter);
            return false;
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
