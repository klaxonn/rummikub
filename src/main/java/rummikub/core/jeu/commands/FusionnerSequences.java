package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import java.util.List;
import java.util.Arrays;

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
    public boolean doCommand() {
        /*List<String> messages = Arrays.asList("Numéro de la séquence de départ : ",
                "Numéro de la séquence d'arrivée : ");*/
        indexSequenceAFusionner = indexes.get(0);
        int indexSequence = indexes.get(1);

        try {
            indexDebutSequenceFusionnee = plateau.fusionnerSequences(indexSequenceAFusionner, indexSequence);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void undoCommand() {
        plateau.couperSequence(indexSequenceAFusionner, indexDebutSequenceFusionnee);
    }
}
