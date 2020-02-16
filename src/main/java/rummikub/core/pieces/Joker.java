package rummikub.core.pieces;

/**
 * Représentation d'un joker.
 *
 * Un joker est un jeton qui peut prendre n'importe quelle valeur et couleur. Il
 * peut être libre ou utilisé.
 */
public class Joker extends Jeton {

    /**
     * Valeur du joker à l'état libre
     */
    public static final int LIBRE = 0;

    /**
     * Construit un joker.
     *
     * Les valeurs initiales du joker sont arbitraires.
     */
    public Joker() {
        super.valeur = LIBRE;
        super.couleur = Couleur.BLEU;
    }

    /**
     * Change la couleur et la valeur du joker.
     *
     * Après changement, le joker est considéré comme utilisé.
     *
     * @param valeur valeur à donner au joker
     * @param couleur couleur à donner au joker
     * @return <code>true</code> si l'action a été effectuée
     */
    public boolean setValeurAndCouleur(int valeur, Couleur couleur) {
        if (!isUtilise()) {
            super.valeur = valeur;
            super.couleur = couleur;
            return true;
        }
		return false;
    }

    /**
     * Renvoie l'état du joker.
     *
     * @return <code>true</code> si le joker est utilisé
     */
    public boolean isUtilise() {
        return super.valeur != LIBRE;
    }

    /**
     * Renvoie la forme textuelle d'un joker.
     *
     * S'il est libre : *
     * Sinon : valeurvouleur* ex: 5rouge*
     */
    @Override
    public String toString() {
        if (isUtilise()) {
            return "" + super.valeur + super.couleur.toString().toLowerCase() + '*';
        } else {
            return "*";
        }
    }

    @Override
    public boolean isJoker() {
        return true;
    }

    /**
     * Réinitialise la valeur et la couleur du joker.
     *
     * Il est considéré comme libre à nouveau.
     */
    public void reinitialiser() {
        super.valeur = LIBRE;
        super.couleur = Couleur.BLEU;
    }

}
