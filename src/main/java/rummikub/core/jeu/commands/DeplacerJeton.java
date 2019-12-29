package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import java.util.List;
import java.util.Arrays;

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
    public boolean doCommand() {
        /*List<String> messages = Arrays.asList("Numéro de la séquence qui contient le jeton : ",
                "Numéro du jeton à déplacer : ",
                "Numéro de la séquence d'arrivée : ");*/
        indexSequenceDepart = indexes.get(0);
        int indexJeton = indexes.get(1);
        indexSequenceArrivee = indexes.get(2);
        try {
            indexNouvellePositionJeton = plateau.deplacerJeton(indexSequenceDepart, indexJeton, indexSequenceArrivee);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void undoCommand() {
        plateau.deplacerJeton(indexSequenceArrivee, indexNouvellePositionJeton, indexSequenceDepart);
    }
}
