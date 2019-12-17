package rummikub.core.pieces;

/**
 * Représentation d'un jeton normal.
 *
 * Un jeton normal est un jeton qui n'est pas un joker. Sa couleur et valeur
 * sont non modifiables.
 */
public class JetonNormal extends Jeton {

    /**
     * Construit un jeton normal.
     *
     * @param valeur valeur à donner au jeton
     * @param couleur couleur à donner au jeton
     */
    public JetonNormal(int valeur, Couleur couleur) {
        super.valeur = valeur;
        super.couleur = couleur;
    }

    @Override
    public String toString() {
        return "" + super.valeur + super.couleur.toString().toLowerCase();
    }

    @Override
    public boolean isJoker() {
        return false;
    }
}
