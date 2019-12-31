package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import java.util.List;

/**
 * Action représentant la fusion de deux séquences.
 */
public class FusionnerSequences implements Command {

    private final Plateau plateau;
    private final List<Integer> indexes;
    private int indexSequenceAFusionner = 0;
    private int indexDebutSequenceFusionnee = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param indexes les indexes des séquences à fusionner
     */
    public FusionnerSequences(Plateau plateau, List<Integer> indexes) {
        this.plateau = plateau;
        this.indexes = indexes;
    }

    @Override
    public void doCommand() {
        indexSequenceAFusionner = indexes.get(0);
        int indexSequence = indexes.get(1);

        indexDebutSequenceFusionnee = plateau.fusionnerSequences(indexSequenceAFusionner, indexSequence);
    }

    @Override
    public void undoCommand() {
        plateau.couperSequence(indexSequenceAFusionner, indexDebutSequenceFusionnee);
    }
}
