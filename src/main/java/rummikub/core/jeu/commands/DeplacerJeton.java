package Rummikub.core.jeu.commands;

import Rummikub.core.plateau.Plateau;
import Rummikub.core.jeu.Joueur;
import Rummikub.ihm.ControleurAbstrait;
import java.util.List;
import java.util.Arrays;

/**
 * Action représentant le déplacement d'un jeton d'une séquence à une autre.
 */
public class DeplacerJeton implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final ControleurAbstrait controleur;
    private int indexSequenceDepart = 0;
    private int indexSequenceArrivee = 0;
    private int indexNouvellePositionJeton = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param controleur le controleur qui s'occupe de l'IHM
     */
    public DeplacerJeton(Plateau plateau, Joueur joueur, ControleurAbstrait controleur) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.controleur = controleur;
    }

    @Override
    public boolean doCommand() {
        List<String> messages = Arrays.asList("Numéro de la séquence qui contient le jeton : ",
                "Numéro du jeton à déplacer : ",
                "Numéro de la séquence d'arrivée : ");
        List<Integer> indexes = controleur.obtenirIndexes(messages);
        indexSequenceDepart = indexes.get(0);
        int indexJeton = indexes.get(1);
        indexSequenceArrivee = indexes.get(2);
        try {
            indexNouvellePositionJeton = plateau.deplacerJeton(indexSequenceDepart, indexJeton, indexSequenceArrivee);
            return true;
        } catch (Exception e) {
            controleur.afficherMessage(e.getMessage());
            return false;
        }
    }

    @Override
    public void undoCommand() {
        plateau.deplacerJeton(indexSequenceArrivee, indexNouvellePositionJeton, indexSequenceDepart);
    }
}
