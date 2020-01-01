package rummikub.core.plateau;

import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.Joker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Représentation d'un plateau de jeu.
 *
 * Un plateau est composé des séquences créées par les joueurs.
 */
public class Plateau {

    private final List<SequenceAbstraite> plateau;

    /**
     * Nombre minimum de jetons pour qu'une séquence soit valide.
     */
    public static final int NOMBRE_JETONS_MINIMUM = 3;

	private static final String ERREUR_SEQUENCE = "Index séquence non correct";

    /**
     * Crée un nouveau plateau.
     */
    public Plateau() {
        plateau = new ArrayList<>();
    }

    /**
     * Crée une nouvelle séquence et l'ajoute au plateau.
     *
     * @param jetons la liste de jetons constituant la séquence
     * @return l'index de la séquence créée
     * @throws UnsupportedOperationException si les jetons ne forment pas une
     * séquence valide
     */
    public int creerSequence(List<Jeton> jetons) {
        SequenceAbstraite nouvelleSequenceCouleur = FabriqueSequence.creerNouvelleSequence(jetons);
        plateau.add(nouvelleSequenceCouleur);
        return plateau.size();
    }

    /**
     * Supprime une séquence du plateau.
     *
     * @param indexSequence l'index de la séquence créée
     * @return la liste de jetons constituant la séquence
     * @throws IndexOutOfBoundsException si l'index n'est pas compris entre 1 et
     * le nombre total de séquences
     */
    public List<Jeton> supprimerSequence(int indexSequence) {
        if (isIndexCorrect(indexSequence)) {
            SequenceAbstraite sequenceASupprimer = plateau.get(indexSequence - 1);
            List<Jeton> jetons = sequenceASupprimer.getJetons();
            SequenceAbstraite.reinitialiserJokersSiExiste(jetons);
            plateau.remove(sequenceASupprimer);
            return jetons;
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

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
    public int ajouterJeton(int indexSequence, Jeton jeton) {
        if (isIndexCorrect(indexSequence)) {
            SequenceAbstraite sequenceDepart = plateau.get(indexSequence - 1);
            SequenceAbstraite sequenceAvecJeton = sequenceDepart.ajouterJeton(jeton);
            mettreAJourSequence(indexSequence, sequenceDepart, sequenceAvecJeton);
            return sequenceAvecJeton.indexJeton(jeton);

        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

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
    public Jeton retirerJeton(int indexSequence, int indexJeton) {
        if (isIndexCorrect(indexSequence)) {
            SequenceAbstraite sequenceDepart = plateau.get(indexSequence - 1);
            return sequenceDepart.retirerJeton(indexJeton);
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

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
    public int fusionnerSequences(int indexSequenceDepart, int indexSequenceArrivee) {
        if (isIndexCorrect(indexSequenceDepart) && isIndexCorrect(indexSequenceArrivee)) {
            SequenceAbstraite sequenceDepart = plateau.get(indexSequenceDepart - 1);
            SequenceAbstraite sequenceArrivee = plateau.get(indexSequenceArrivee - 1);
            SequenceAbstraite sequenceFusionnee = sequenceDepart.fusionnerSequence(sequenceArrivee);
            mettreAJourSequence(indexSequenceDepart, sequenceDepart, sequenceFusionnee);
            plateau.remove(sequenceArrivee);
            return sequenceDepart.longueur() + 1;
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

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
    public int couperSequence(int indexSequenceDepart, int indexJeton) {
        if (isIndexCorrect(indexSequenceDepart)) {
            SequenceAbstraite sequenceDepart = plateau.get(indexSequenceDepart - 1);
            SequenceAbstraite nouvelleSequence = sequenceDepart.couperSequence(indexJeton);
            plateau.add(nouvelleSequence);
            return plateau.size();
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

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
    public int deplacerJeton(int indexSequenceDepart, int indexJeton, int indexSequenceArrivee) {
        if (isIndexCorrect(indexSequenceDepart)) {
            //Déplacement vers une nouvelle séquence
            if (indexSequenceArrivee == plateau.size() + 1) {
                return deplacerJetonVersNouvelleSequence(indexSequenceDepart, indexJeton);
            } else if (isIndexCorrect(indexSequenceArrivee)) {
                return deplacerJetonVersSequenceExistante(indexSequenceDepart, indexJeton, indexSequenceArrivee);
            } else {
                throw new IndexOutOfBoundsException("Index séquence arrivée non correct");
            }
        } else {
            throw new IndexOutOfBoundsException("Index séquence départ non correct");
        }
    }

    private int deplacerJetonVersSequenceExistante(int indexSequenceDepart, int indexJeton, int indexSequenceArrivee) {
        SequenceAbstraite sequenceDepart = plateau.get(indexSequenceDepart - 1);
        SequenceAbstraite sequenceArrivee = plateau.get(indexSequenceArrivee - 1);
        if (sequenceDepart.isPossibleRetirerJeton(indexJeton)) {
            Jeton jetonADeplacer = sequenceDepart.retirerJeton(indexJeton);
            try {
                SequenceAbstraite sequenceApresAjout = sequenceArrivee.ajouterJeton(jetonADeplacer);
                mettreAJourSequence(indexSequenceArrivee, sequenceArrivee, sequenceApresAjout);
                if (sequenceDepart.isVide()) {
                    plateau.remove(sequenceDepart);
                }
                return sequenceApresAjout.indexJeton(jetonADeplacer);
            } catch (UnsupportedOperationException e) {
                SequenceAbstraite sequenceApresAjout = sequenceDepart.ajouterJeton(jetonADeplacer);
                mettreAJourSequence(indexSequenceDepart, sequenceDepart, sequenceApresAjout);
                throw e;
            }
        } else {
            throw new UnsupportedOperationException("Impossible de déplacer ce jeton");
        }
    }

    private void mettreAJourSequence(int index, SequenceAbstraite ancienneSequence,
            SequenceAbstraite nouvelleSequence) {
        plateau.add(index, nouvelleSequence);
        plateau.remove(ancienneSequence);
    }

    private int deplacerJetonVersNouvelleSequence(int indexSequenceDepart, int indexJeton) {
        SequenceAbstraite sequenceDepart = plateau.get(indexSequenceDepart - 1);
        if (sequenceDepart.isPossibleRetirerJeton(indexJeton)) {
            Jeton jetonADeplacer = sequenceDepart.retirerJeton(indexJeton);
            List<Jeton> jetons = new ArrayList<>();
            jetons.add(jetonADeplacer);
            SequenceAbstraite nouvelleSequence = FabriqueSequence.creerNouvelleSequence(jetons);
            plateau.add(nouvelleSequence);
            return nouvelleSequence.longueur();
        } else {
            throw new UnsupportedOperationException("Impossible de déplacer ce jeton");
        }
    }

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
    public Joker remplacerJoker(int indexSequence, Jeton jeton) {
        if (isIndexCorrect(indexSequence)) {
            SequenceAbstraite sequence = plateau.get(indexSequence - 1);
            return sequence.remplacerJoker(jeton);
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

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
    public Jeton remplacerJetonParJoker(int indexSequence, Joker joker) {
        if (isIndexCorrect(indexSequence)) {
            SequenceAbstraite sequence = plateau.get(indexSequence - 1);
            return sequence.remplacerJetonParJoker(joker);
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

    private boolean isIndexCorrect(int index) {
        return index >= 1 && index <= plateau.size();
    }

    /**
     * Détermine si le plateau est valide.
     *
     * Un plateau est valide si toutes les séquences ont le nombre suffisant de
     * jetons.
     *
     * @return <code>true</code> si plateau est valide
     *
     */
    public boolean isValide() {
        return plateau.stream().allMatch(i -> i.longueur() >= NOMBRE_JETONS_MINIMUM);
    }

    /**
     * Renvoie la forme textuelle d'un plateau.
     *
     * Le format est l'ensemble des séquences séparées par un saut de ligne.
     */
    @Override
    public String toString() {
        return plateau.stream().map((sequence) -> sequence.toString())
							   .collect(Collectors.joining("\n"));
    }

}
