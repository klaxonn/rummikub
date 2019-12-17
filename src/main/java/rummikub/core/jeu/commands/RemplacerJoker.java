package Rummikub.core.jeu.commands;

import Rummikub.core.plateau.Plateau;
import Rummikub.core.jeu.Joueur;
import Rummikub.core.pieces.Jeton;
import Rummikub.core.pieces.Joker;
import Rummikub.ihm.ControleurAbstrait;
import java.util.List;
import java.util.Arrays;

/**
 * Action représentant le remplacement d'un joker par un jeton.
 */
public class RemplacerJoker implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final ControleurAbstrait controleur;
    private Jeton jetonAAjouter = null;
    private int indexSequenceArrivee = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param controleur le controleur qui s'occupe de l'IHM
     */
    public RemplacerJoker(Plateau plateau, Joueur joueur, ControleurAbstrait controleur) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.controleur = controleur;
    }

    @Override
    public boolean doCommand() {
        List<String> messages = Arrays.asList("Numéro du jeton à uiliser : ",
                "Numéro de la séquence d'arrivée : ");
        List<Integer> indexes = controleur.obtenirIndexes(messages);
        int indexJeton = indexes.get(0);
        indexSequenceArrivee = indexes.get(1);

        try {
            jetonAAjouter = joueur.utiliseJeton(indexJeton);
        } catch (IndexOutOfBoundsException e) {
            controleur.afficherMessage(e.getMessage());
            return false;
        }
        try {
            Joker joker = plateau.remplacerJoker(indexSequenceArrivee, jetonAAjouter);
            joueur.ajouteJeton(joker);
            return true;
        } catch (Exception e) {
            joueur.ajouteJeton(jetonAAjouter);
            controleur.afficherMessage(e.getMessage());
            return false;
        }
    }

    @Override
    public void undoCommand() {
        //le joker est à la dernière position
        Joker joker = (Joker) joueur.utiliseJeton(joueur.nombreJetonsRestants());
        joker.setValeurAndCouleur(jetonAAjouter.getValeur(), jetonAAjouter.getCouleur());
        Jeton jeton = plateau.remplacerJetonParJoker(indexSequenceArrivee, joker);
        joueur.ajouteJeton(jeton);
    }
}
