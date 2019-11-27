package Rummikub.core.plateau;

import Rummikub.core.pieces.Couleur;
import Rummikub.core.pieces.Jeton;
import Rummikub.core.pieces.Joker;
import java.util.ArrayList;
import java.util.List;

/**
 * Représentation d'une séquence de couleurs.
 *
 * Une séquence de couleurs contient des jetons avec des valeurs identiques et
 * des couleurs différentes. Exemple : 5rouge - 5vert - 5jaune
 */
class SequenceCouleur extends SequenceAbstraite {

    /**
     * Crée une nouvelle séquence de couleurs.
     *
     * Si la séquence de couleurs contient un joker libre, ses valeurs seront
     * calculées pour obtenir une séquence valide si possible.
     *
     * @param collectionJetons la liste de jetons constituant la séquence
     * @throws UnsupportedOperationException si les jetons ne forment pas une
     * séquence de couleurs valide
     */
    public SequenceCouleur(List<Jeton> collectionJetons) {
        super(collectionJetons);
    }

    @Override
    public boolean IsCorrectSequence(List<Jeton> collectionJetons) {
        if (collectionJetons.size() > Couleur.values().length) {
            return false;
        } else if (collectionJetons.size() >= 1) {
            int[] indexJokers = SequenceAbstraite.indexJokersSiExiste(collectionJetons);
            for (int i = 0; i < indexJokers.length; i++) {
                int indexJoker = indexJokers[i];
                Joker joker = (Joker) collectionJetons.get(indexJoker);
                if (!joker.isUtilise()) {
                    initialiserJoker(collectionJetons, joker);
                }
            }
            return isMemeValeur(collectionJetons) && allColorsDifferent(collectionJetons);
        } else {
            return false;
        }
    }

    private void initialiserJoker(List<Jeton> collectionJetons, Joker joker) {
        Couleur couleurJoker = calculerCouleurJoker(collectionJetons, joker);
        joker.setValeurAndCouleur(valeurDeSequence(collectionJetons), couleurJoker);
    }

    private Couleur calculerCouleurJoker(List<Jeton> collectionJetons, Joker joker) {
        List<Couleur> couleursDansSequence = couleursDeLaSequence(collectionJetons);
        Couleur couleurJoker = null;
        for (Couleur couleurPossible : Couleur.values()) {
            if (!couleursDansSequence.contains(couleurPossible)) {
                couleurJoker = couleurPossible;
                break;
            }
        }
        return couleurJoker;
    }

    private List<Couleur> couleursDeLaSequence(List<Jeton> collectionJetons) {
        List<Couleur> couleurs = new ArrayList<>();
        collectionJetons.forEach((jeton) -> {
            couleurs.add(jeton.getCouleur());
        });
        return couleurs;
    }

    private int valeurDeSequence(List<Jeton> collectionJetons) {
        return SequenceAbstraite.premierJetonNonJoker(collectionJetons).getValeur();
    }

    private boolean isMemeValeur(List<Jeton> collectionJetons) {
        int premiereValeur = valeurDeSequence(collectionJetons);
        return collectionJetons.stream().allMatch(i -> i.getValeur() == premiereValeur);
    }

    private boolean allColorsDifferent(List<Jeton> collectionJetons) {
        List<Couleur> couleurs = couleursDeLaSequence(collectionJetons);
        return couleurs.stream().distinct().count() == collectionJetons.size();
    }

    @Override
    public boolean isPossibleRetirerJeton(int indexJetonAExtraire) {
        return indexJetonAExtraire >= 1 && indexJetonAExtraire <= sequence.size();
    }
}
