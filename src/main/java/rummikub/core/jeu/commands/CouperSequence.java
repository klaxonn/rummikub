package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import java.util.List;
import java.util.Arrays;

/**
 * Action représentant le découpage d'une séquence.
 */
public class CouperSequence implements Command {

    private final Plateau plateau;
    private final List<Integer> indexes;
    private int indexSequenceACouper = 0;
    private int indexNouvelleSequence = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param indexes les indexes de la séquence à couper et du jeton
     */
    public CouperSequence(Plateau plateau, List<Integer> indexes) {
        this.plateau = plateau;
        this.indexes = indexes;
    }

    @Override
    public void doCommand() {
        /*List<String> messages = Arrays.asList("Numéro de la séquence à couper : ",
                "Numéro du jeton où couper : ");*/
        indexSequenceACouper = indexes.get(0);
        int indexJeton = indexes.get(1);

        indexNouvelleSequence = plateau.couperSequence(indexSequenceACouper, indexJeton);
    }

    @Override
    public void undoCommand() {
        plateau.fusionnerSequences(indexSequenceACouper, indexNouvelleSequence);
    }
}
