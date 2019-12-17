package Rummikub.core.jeu.commands;

import Rummikub.core.plateau.Plateau;
import Rummikub.core.jeu.Joueur;
import Rummikub.ihm.ControleurAbstrait;
import java.util.List;
import java.util.Arrays;

/**
 * Action représentant la fusion de deux séquences.
 */
public class FusionnerSequences implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final ControleurAbstrait controleur;
    private int indexSequenceAFusionner = 0;
    private int indexDebutSequenceFusionnee = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param controleur le controleur qui s'occupe de l'IHM
     */
    public FusionnerSequences(Plateau plateau, Joueur joueur, ControleurAbstrait controleur) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.controleur = controleur;
    }

    @Override
    public boolean doCommand() {
        List<String> messages = Arrays.asList("Numéro de la séquence de départ : ",
                "Numéro de la séquence d'arrivée : ");
        List<Integer> indexes = controleur.obtenirIndexes(messages);
        indexSequenceAFusionner = indexes.get(0);
        int indexSequence = indexes.get(1);

        try {
            indexDebutSequenceFusionnee = plateau.fusionnerSequences(indexSequenceAFusionner, indexSequence);
            return true;
        } catch (Exception e) {
            controleur.afficherMessage(e.getMessage());
            return false;
        }
    }

    @Override
    public void undoCommand() {
        plateau.couperSequence(indexSequenceAFusionner, indexDebutSequenceFusionnee);
    }
}
