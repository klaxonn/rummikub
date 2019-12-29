package rummikub.core.plateau;

import rummikub.core.pieces.Jeton;
import java.util.List;

/**
 * Fabrique de séquences.
 */
final class FabriqueSequence {

    private FabriqueSequence() {
    }

    /**
     * Trouve un type de séquence qui correspond à la liste de jetons et
     * construit la séquence.
     *
     * La méthode parcourt les types de séquence fournis et essaie de la
     * construire jusqu'à la réussite d'un type. D'abord une séquence de
     * couleur, puis une suite.
     *
     * @param jetons la liste de jetons constituant la séquence à construire
     * @return la séquence obtenue
     * @throws UnsupportedOperationException si les jetons ne forment aucun type
     * de séquence valide
     */
    public static SequenceAbstraite creerNouvelleSequence(List<Jeton> jetons) {
        final String cheminModule = "rummikub.core.plateau.";
        for (TypeSequence type : TypeSequence.values()) {
            try {
                Class<?> c = Class.forName(cheminModule + type.toString());
                return (SequenceAbstraite) c.getConstructor(List.class).newInstance(jetons);
            } catch (ReflectiveOperationException e) {
                //Ce type de sequence n'a pas pu etre créé, on essaie un autre type
                continue;
            }
        }
        throw new UnsupportedOperationException("Aucune séquence possible");
    }

	//enum contenant les classes implémentant SequenceAbstraite
    private enum TypeSequence {
        SequenceCouleur, Suite

    }
}
