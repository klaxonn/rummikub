package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import java.util.List;

/**
 * Action représentant le déplacement d'un jeton d'une séquence à une autre.
 */
public class DeplacerJeton implements Command {

    private final Plateau plateau;
    private final List<Integer> indexes;
    private int indexSequenceDepart = 0;
    private int indexSequenceArrivee = 0;
    private int indexNouvellePositionJeton = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param indexes les indexes de la séquence de départ, du jeton d'arrivée
     * et de la séquence d'arrivée
     */
    public DeplacerJeton(Plateau plateau, List<Integer> indexes) {
        this.plateau = plateau;
        this.indexes = indexes;
    }

    @Override
    public void doCommand() {
        indexSequenceDepart = indexes.get(0);
        int indexJeton = indexes.get(1);
        indexSequenceArrivee = indexes.get(2);
        indexNouvellePositionJeton = plateau.deplacerJeton(indexSequenceDepart, indexJeton, indexSequenceArrivee);
    }

    @Override
    public void undoCommand() {
        plateau.deplacerJeton(indexSequenceArrivee, indexNouvellePositionJeton, indexSequenceDepart);
    }
}
