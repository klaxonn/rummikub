package Rummikub.core.pieces;

import java.util.Objects;

/**
 * Représentation d'un jeton, brique élémentaire du jeu.
 */
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
     * Renvoie la valeur du jeton.
     *
     * @return valeur du jeton
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * Renvoie la couleur du jeton.
     *
     * @return valeur du jeton
     */
    public Couleur getCouleur() {
        return couleur;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.valeur;
        hash = 89 * hash + Objects.hashCode(this.couleur);
        return hash;
    }

    /**
     * Renvoie true si les jetons sont égaux.
     *
     * Deux jetons sont égaux s'ils ont la même couleur et la même valeur.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Jeton)) {
            return false;
        }
        final Jeton other = (Jeton) obj;
        return this.valeur == other.valeur
                && this.couleur.equals(other.couleur);
    }

    @Override
    public int compareTo(Jeton jeton) {
        return Integer.compare(this.getValeur(), jeton.getValeur());

    }

    /**
     * Détermine si le jeton est un joker.
     *
     * @return <code>true</code> si le jeton est un joker
     */
    public abstract boolean isJoker();

    /**
     * Renvoie la forme textuelle d'un jeton.
     *
     * le format est valeurcouleur Exemple : "7rouge".
     */
    @Override
    abstract public String toString();

}
