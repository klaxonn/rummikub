package rummikub.core.plateau;

import rummikub.core.pieces.Jeton;
import java.util.List;

/**
 * Fabrique de séquences.
 */
final public class FabriqueSequence {

    private static final String cheminModule = "rummikub.core.plateau.";
    private static FabriqueSequence fabrique = null;

    private FabriqueSequence() {
    }

	/**
	 * Retourne une instance de la fabrique.
	 *
	 * @return la séquence obtenue
	 */
	public static FabriqueSequence obtenirFabrique()
    {
        if (fabrique == null) {
            fabrique = new FabriqueSequence();
		}
        return fabrique;
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
    public SequenceAbstraite creerNouvelleSequence(List<Jeton> jetons) {
        for (TypeSequence type : TypeSequence.values()) {
            try {
                Class<?> c = Class.forName(cheminModule + type.toString());
                return (SequenceAbstraite) c.getConstructor(List.class, FabriqueSequence.class).newInstance(jetons, this);
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
