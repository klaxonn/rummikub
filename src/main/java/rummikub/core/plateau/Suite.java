package rummikub.core.plateau;

import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import rummikub.core.jeu.Pioche;
import rummikub.core.pieces.Joker;
import java.util.Collections;
import java.util.List;

/**
 * Représentation d'une suite.
 *
 * Une suite contient des jetons avec des couleurs identiques et des valeurs
 * avec une progression +1. Exemple : 5rouge - 6rouge -7rouge
 */
class Suite extends SequenceAbstraite {

    /**
     * Crée une nouvelle suite.
     *
     * Si la suite contient un joker libre, ses valeurs seront calculées pour
     * obtenir une suite valide si possible.
     *
     * @param collectionJetons la liste de jetons constituant la suite
     * @param fabrique la fabrique de séquences
     * @throws UnsupportedOperationException si les jetons ne forment pas une
     * suite valide
     */
    public Suite(List<Jeton> collectionJetons, FabriqueSequence fabrique) {
        super(collectionJetons, fabrique);
        Collections.sort(sequence);
    }

    @Override
    public boolean isCorrectSequence(List<Jeton> collectionJetons) {
        if (collectionJetons.isEmpty()) {
            return false;
        } else {
            Collections.sort(collectionJetons);
            parcoursJokers(collectionJetons);
            return isMemeCouleur(collectionJetons) && isValeursASuite(collectionJetons);
        }
    }

    private void parcoursJokers(List<Jeton> collectionJetons) {
        int[] indexJokers = SequenceAbstraite.indexJokersSiExiste(collectionJetons);
        for (int indexJoker : indexJokers) {
            Joker joker = (Joker) collectionJetons.get(indexJoker);
            if (!joker.isUtilise()) {
                collectionJetons.remove(joker);
                initialiserJoker(collectionJetons, joker);
                collectionJetons.add(joker);
                Collections.sort(collectionJetons);
            }
        }
    }

    private void initialiserJoker(List<Jeton> collectionJetons, Joker joker) {
        int valeurJoker = calculeValeurJoker(collectionJetons);
        joker.setValeurAndCouleur(valeurJoker, couleurDeSequence(collectionJetons));
    }

    private int calculeValeurJoker(List<Jeton> collectionJetons) {
        int valeurJoker = indexPremierTrouSiExiste(collectionJetons);
        if (valeurJoker == Joker.LIBRE) {
            valeurJoker = indexUneExtremitePossible(collectionJetons);
        }
        return valeurJoker;
    }

    private int indexPremierTrouSiExiste(List<Jeton> collectionJetons) {
        int valeurInitiale = valeurPremierElement(collectionJetons);
        int valeurJoker = Joker.LIBRE;
        for (int i = 1; i < collectionJetons.size(); i++) {
            if (collectionJetons.get(i).getValeur() != valeurInitiale + i) {
                valeurJoker = valeurInitiale + i;
                break;
            }
        }
        return valeurJoker;
    }

    private int indexUneExtremitePossible(List<Jeton> collectionJetons) {
        int valeurJoker = -1;
        if (valeurPremierElement(collectionJetons) > 1) {
            valeurJoker = valeurPremierElement(collectionJetons) - 1;
        } else if (valeurDernierElement(collectionJetons) < Pioche.VALEUR_MAX) {
            valeurJoker = valeurDernierElement(collectionJetons) + 1;
        }
        return valeurJoker;
    }

    private int valeurPremierElement(List<Jeton> collectionJetons) {
        return SequenceAbstraite.premierJetonNonJoker(collectionJetons).getValeur();
    }

    private int valeurDernierElement(List<Jeton> collectionJetons) {
        return collectionJetons.get(collectionJetons.size() - 1).getValeur();
    }

    private Couleur couleurDeSequence(List<Jeton> collectionJetons) {
        return SequenceAbstraite.premierJetonNonJoker(collectionJetons).getCouleur();
    }

    private boolean isMemeCouleur(List<Jeton> collectionJetons) {
        Couleur premiereCouleur = couleurDeSequence(collectionJetons);
        return collectionJetons.stream().allMatch(i -> i.getCouleur() == premiereCouleur);
    }

    private boolean isValeursASuite(List<Jeton> collectionJetons) {
        int valeurInitiale = collectionJetons.get(0).getValeur();
        for (int i = 1; i < collectionJetons.size(); i++) {
            if (collectionJetons.get(i).getValeur() != valeurInitiale + i) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isPossibleRetirerJeton(int indexJetonAExtraire) {
        return indexJetonAExtraire == 1 || indexJetonAExtraire == sequence.size();
    }
}
