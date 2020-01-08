package rummikub.core.plateau;

import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.Joker;
import java.util.List;

/**
 * Représentation d'un plateau de jeu.
 *
 * Un plateau est composé des séquences créées par les joueurs.
 */
public interface Plateau {

    /**
     * Nombre minimum de jetons pour qu'une séquence soit valide.
     */
    static final int NOMBRE_JETONS_MINIMUM = 3;

    /**
     * Crée une nouvelle séquence et l'ajoute au plateau.
     *
     * @param jetons la liste de jetons constituant la séquence
     * @return l'index de la séquence créée
     * @throws UnsupportedOperationException si les jetons ne forment pas une
     * séquence valide
     */
    int creerSequence(List<Jeton> jetons);

    /**
     * Supprime une séquence du plateau.
     *
     * @param indexSequence l'index de la séquence créée
     * @return la liste de jetons constituant la séquence
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de séquences
     */
    List<Jeton> supprimerSequence(int indexSequence);

    /**
     * Ajoute un jeton à une séquence.
     *
     * @param indexSequence l'index de la séquence
     * @param jeton le jeton à ajouter
     * @return l'index du jeton ajouté dans la séquence
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de séquences
     * @throws UnsupportedOperationException si le jeton ne peut pas être ajouté
     */
    int ajouterJeton(int indexSequence, Jeton jeton);

    /**
     * Retire un jeton à une séquence.
     *
     * @param indexSequence l'index de la séquence
     * @param indexJeton l'index du jeton à retirer
     * @return le jeton qui a été retiré de la séquence
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de séquences
     * @throws UnsupportedOperationException si le jeton ne peut pas être retiré
     */
    Jeton retirerJeton(int indexSequence, int indexJeton);

    /**
     * Fusionne deux séquences.
     *
     * Les jetons de la séquence 2 seront ajoutés à la séquence 1. La séquence 2
     * sera supprimée du plateau.
     *
     * @param indexSequenceDepart l'index de la séquence 1
     * @param indexSequenceArrivee l'index de la séquence 2
     * @return l'index du jeton du point de fusion
     * @throws IndexOutOfBoundsException si les index ne sont pas compris entre
     * 1 et le nombre total de séquences
     * @throws UnsupportedOperationException si les séquences ne peuvent être
     * fusionnées
     */
    int fusionnerSequences(int indexSequenceDepart, int indexSequenceArrivee);

    /**
     * Coupe une séquence.
     *
     * La nouvelle séquence sera ajoutée au plateau.
     *
     * @param indexSequenceDepart l'index de la séquence à couper
     * @param indexJeton l'index du jeton qui commencera la nouvelle séquence
     * @return l'index de la nouvelle séquence
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de séquences
     * @throws UnsupportedOperationException si la séquence ne peut être coupée
     */
    int couperSequence(int indexSequenceDepart, int indexJeton);

    /**
     * Déplace un jeton d'une séquence à une autre.
     *
     * Si l'index de destination est égal au nombre de séquences + 1, alors une
     * nouvelle séquence est créée et on y ajoute le jeton.
     *
     * @param indexSequenceDepart l'index de la séquence contenant le jeton
     * @param indexJeton l'index du jeton à déplacer
     * @param indexSequenceArrivee l'index de la séquence destination
     * @return l'index du jeton dans la nouvelle séquence
     * @throws IndexOutOfBoundsException si les index ne sont pas compris entre
     * 1 et le nombre total de séquences
     * @throws UnsupportedOperationException si le jeton ne peut être déplacé
     */
    int deplacerJeton(int indexSequenceDepart, int indexJeton, int indexSequenceArrivee);

    /**
     * Remplace un joker par un jeton.
     *
     * @param indexSequence l'index de la séquence contenant le joker
     * @param jeton jeton qui prendra la place du joker
     * @return le joker remplacé dans la séquence. Il est réinitialisé
     * (considéré comme libre).
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de séquences
     * @throws UnsupportedOperationException s'il n'y a pas de joker, ou le
     * jeton ne peut pas remplacer le joker
     */
    Joker remplacerJoker(int indexSequence, Jeton jeton);

    /**
     * Remplace un jeton par un joker.
     *
     * Le joker doit avoir été initialisé au préalable avec les valeurs et
     * couleurs du jeton à remplacer.
     *
     * @param indexSequence l'index de la séquence contenant le jeton
     * @param joker joker qui prendra la place du jeton
     * @return le jeton remplacé dans la séquence
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de séquences
     * @throws UnsupportedOperationException si le joker n'est pas initialisé,
     * ou s'il ne peut pas remplacer le jeton.
     */
    Jeton remplacerJetonParJoker(int indexSequence, Joker joker);

    /**
     * Détermine si le plateau est valide.
     *
     * Un plateau est valide si toutes les séquences ont le nombre suffisant de
     * jetons.
     *
     * @return <code>true</code> si plateau est valide
     *
     */
    boolean isValide();
}
