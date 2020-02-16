package rummikub.core.pieces;

import lombok.Getter;
import lombok.EqualsAndHashCode;

/**
 * Représentation d'un jeton, brique élémentaire du jeu.
 */
@Getter
@EqualsAndHashCode
public abstract class Jeton implements Comparable<Jeton> {

    /**
     * Valeur du jeton
     */
    protected int valeur;

    /**
     * Couleur du jeton
     */
    protected Couleur couleur;

    /**
     * Détermine si le jeton est un joker.
     *
     * @return <code>true</code> si le jeton est un joker
     */
    public abstract boolean isJoker();

    @Override
    public int compareTo(Jeton jeton) {
        return Integer.compare(this.getValeur(), jeton.getValeur());

    }
}
