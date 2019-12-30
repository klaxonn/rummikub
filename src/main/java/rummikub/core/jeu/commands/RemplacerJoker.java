package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.Joker;
import java.util.List;
import java.util.Arrays;

/**
 * Action représentant le remplacement d'un joker par un jeton.
 */
public class RemplacerJoker implements Command {

    private final Joueur joueur;
    private final Plateau plateau;
    private final List<Integer> indexes;
    private Jeton jetonAAjouter = null;
    private int indexSequenceArrivee = 0;

    /**
     * Crée une action.
     *
     * @param plateau le plateau de jeu
     * @param joueur le joueur qui fait l'action
     * @param indexes les indexes du jeton et de la séquence qui contient
     * le joker
     */
    public RemplacerJoker(Plateau plateau, Joueur joueur, List<Integer> indexes) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.indexes = indexes;
    }

    @Override
    public void doCommand() {
        /*List<String> messages = Arrays.asList("Numéro du jeton à uiliser : ",
                "Numéro de la séquence d'arrivée : ");*/
        int indexJeton = indexes.get(0);
        indexSequenceArrivee = indexes.get(1);

        jetonAAjouter = joueur.utiliseJeton(indexJeton);
        try {
            Joker joker = plateau.remplacerJoker(indexSequenceArrivee, jetonAAjouter);
            joueur.ajouteJeton(joker);
        } catch (Exception e) {
            joueur.ajouteJeton(jetonAAjouter);
            throw e;
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
