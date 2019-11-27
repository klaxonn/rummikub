package Rummikub.core.jeu.commands;

import Rummikub.core.plateau.Plateau;
import Rummikub.core.jeu.Joueur;
import Rummikub.ihm.ControleurAbstrait;
import java.util.List;
import java.util.Arrays;

/**
 * Action représentant le découpage d'une séquence.
 */
public class CouperSequence implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final ControleurAbstrait controleur;
    private int indexSequenceACouper = 0;
    private int indexNouvelleSequence = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param controleur le controleur qui s'occupe de l'IHM
     */
    public CouperSequence(Plateau plateau, Joueur joueur, ControleurAbstrait controleur) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.controleur = controleur;
    }

    @Override
    public boolean doCommand() {
        List<String> messages = Arrays.asList("Numéro de la séquence à couper : ",
                "Numéro du jeton où couper : ");
        List<Integer> indexes = controleur.obtenirIndexes(messages);
        indexSequenceACouper = indexes.get(0);
        int indexJeton = indexes.get(1);

        try {
            indexNouvelleSequence = plateau.couperSequence(indexSequenceACouper, indexJeton);
            return true;
        } catch (Exception e) {
            controleur.afficherMessage(e.getMessage());
            return false;
        }
    }

    @Override
    public void undoCommand() {
        plateau.fusionnerSequences(indexSequenceACouper, indexNouvelleSequence);
    }
}
