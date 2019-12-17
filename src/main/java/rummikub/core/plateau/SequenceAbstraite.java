package rummikub.core.plateau;

import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.Joker;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

/**
 * Représentation d'un ensemble de jetons.
 */
public abstract class SequenceAbstraite {

    protected final List<Jeton> sequence;

    /**
     * Détermine si la liste de jetons est une séquence valide.
     *
     * @param collectionJetons la liste à parcourir
     * @return <code>true</code> si c'est une séquence valide
     */
    public abstract boolean isCorrectSequence(List<Jeton> collectionJetons);

    /**
     * Détermine si on peut retirer le jeton à l'index donné.
     *
     * @param indexJetonAExtraire l'index du jeton à retirer (entre 1 et la
     * longueur de la séquence)
     * @return <code>true</code> si le jeton est retirable
     */
    public abstract boolean isPossibleRetirerJeton(int indexJetonAExtraire);

    /**
     * Crée une nouvelle séquence.
     *
     * @param collectionJetons la liste de jetons constituant la séquence
     * @throws UnsupportedOperationException si les jetons ne forment pas une
     * séquence valide.
     */
    protected SequenceAbstraite(List<Jeton> collectionJetons) {
        List<Jeton> copieCollectionJetons = new ArrayList<>(collectionJetons);
        if (isCorrectSequence(copieCollectionJetons)) {
            sequence = copieCollectionJetons;
        } else {
            SequenceAbstraite.reinitialiserJokersSiExiste(copieCollectionJetons);
            throw new UnsupportedOperationException("Impossible de créer la séquence");
        }
    }

    /**
     * Réinitialise les jokers d'un ensemble de jetons s'ils existent.
     *
     * @param collectionJetons la collection à parcourir
     */
    public static void reinitialiserJokersSiExiste(List<Jeton> collectionJetons) {
        int[] indexJokers = indexJokersSiExiste(collectionJetons);
        for (int indexJoker : indexJokers) {
            Joker joker = (Joker) collectionJetons.get(indexJoker);
            joker.reinitialiser();
        }
    }

    /**
     * Retire le jeton de la séquence à l'index donné.
     *
     * @param indexJetonAExtraire l'index du jeton à retirer (entre 1 et la
     * longueur de la séquence)
     * @return le jeton à l'index donné
     * @throws UnsupportedOperationException si le jeton n'est pas retirable
     */
    public Jeton retirerJeton(int indexJetonAExtraire) {
        if (isPossibleRetirerJeton(indexJetonAExtraire)) {
            Jeton jetonAExtraire = sequence.get(indexJetonAExtraire - 1);
            sequence.remove(jetonAExtraire);
            return jetonAExtraire;
        }
        throw new UnsupportedOperationException("Impossible d'extraire le jeton");
    }

    /**
     * Coupe la séquence en deux à l'index donné.
     *
     * @param indexDebutNouvelleSequence l'index du jeton qui débutera la
     * nouvelle séquence (entre 1 et la longueur de la séquence)
     * @return la nouvelle séquence coupée
     * @throws UnsupportedOperationException si la séquence ne peut être coupée
     */
    public SequenceAbstraite couperSequence(int indexDebutNouvelleSequence) {
        if (isPossibleCouperSequence(indexDebutNouvelleSequence)) {
            List<Jeton> listeJetons = new ArrayList<>();
            List<Jeton> sousListe = sequence.subList(indexDebutNouvelleSequence - 1, sequence.size());
            listeJetons.addAll(sousListe);
            sequence.removeAll(sousListe);
            return FabriqueSequence.creerNouvelleSequence(listeJetons);
        }
        throw new UnsupportedOperationException("Impossible de couper la suite");
    }

    /**
     * Détermine si la séquence peut être coupée à l'index donné.
     *
     * @param indexDebutNouvelleSequence l'index du jeton qui débutera la
     * nouvelle séquence (entre 1 et la longueur de la séquence)
     * @return <code>true</code> si la séquence peut être coupée
     */
    public boolean isPossibleCouperSequence(int indexDebutNouvelleSequence) {
        return sequence.size() > 1
                && indexDebutNouvelleSequence > 1
                && indexDebutNouvelleSequence <= sequence.size();
    }

    /**
     * Fusionne les deux séquences.
     *
     * @param sequenceAFusionner la séquence qui sera fusionnée
     * @return la nouvelle séquence résultant de la fusion des deux séquences
     * @throws UnsupportedOperationException si les deux séquences ne peuvent
     * être fusionnées
     */
    public SequenceAbstraite fusionnerSequence(SequenceAbstraite sequenceAFusionner) {
        return ajouterJetonOuFusionner(sequenceAFusionner.sequence);
    }

    /**
     * Ajoute le jeton à la séquence.
     *
     * @param jetonAAjoute sequence qui sera fusionnée
     * @return la nouvelle séquence résultant de l'ajout du jeton
     * @throws UnsupportedOperationException si le jeton ne peut être ajouté
     */
    public SequenceAbstraite ajouterJeton(Jeton jetonAAjoute) throws UnsupportedOperationException {
        return ajouterJetonOuFusionner(Arrays.asList(jetonAAjoute));
    }

    private SequenceAbstraite ajouterJetonOuFusionner(List<Jeton> collectionJetons) {
        List<Jeton> jetonsNouvelleSequence = new ArrayList<>();
        jetonsNouvelleSequence.addAll(sequence);
        jetonsNouvelleSequence.addAll(collectionJetons);
        return FabriqueSequence.creerNouvelleSequence(jetonsNouvelleSequence);
    }

    /**
     * Remplace un joker par un jeton.
     *
     * @param jeton jeton qui prendra la place du joker
     * @return le joker remplacé dans la séquence. Il est réinitialisé
     * (considéré comme libre).
     * @throws UnsupportedOperationException s'il n'y a pas de joker, ou le
     * jeton ne peut pas remplacer le joker
     */
    public Joker remplacerJoker(Jeton jeton) {
        int[] indexJokers = indexJokersSiExiste(sequence);
        for (int indexJoker : indexJokers) {
            if (sequence.get(indexJoker).equals(jeton)) {
                Joker joker = (Joker) sequence.get(indexJoker);
                sequence.remove(joker);
                joker.reinitialiser();
                sequence.add(indexJoker, jeton);
                return joker;
            }
        }
        throw new UnsupportedOperationException("Impossible de remplacer le joker");
    }

    /**
     * Remplace un jeton par un joker.
     *
     * Le joker doit avoir été initialisé au préalable avec les valeurs et
     * couleurs du jeton à remplacer.
     *
     * @param joker joker qui prendra la place du jeton
     * @return le jeton remplacé dans la séquence
     * @throws UnsupportedOperationException si le joker n'est pas initialisé,
     * ou s'il ne peut pas remplacer le jeton.
     */
    public Jeton remplacerJetonParJoker(Joker joker) {
        Jeton jeton;
        if (joker.isUtilise()) {
            for (int i = 0; i < sequence.size(); i++) {
                jeton = sequence.get(i);
                if (jeton.equals(joker)) {
                    sequence.remove(i);
                    sequence.add(i, joker);
                    return jeton;
                }
            }
        }
        throw new UnsupportedOperationException("Remplacememnt impossible");
    }

    /**
     * Renvoie l'index des jokers d'un ensemble de jetons, s'ils existent.
     *
     * @param collectionJetons la collection à parcourir
     * @return l'array contenant les index des jokers. L'array est vide s'il n'y
     * pas de joker.
     */
    protected static int[] indexJokersSiExiste(List<Jeton> collectionJetons) {
        return collectionJetons.stream().filter(i -> i.isJoker())
                .mapToInt(i -> collectionJetons.indexOf(i))
                .toArray();
    }

    /**
     * Renvoie le premier jeton non joker de la séquence.
     *
     * Le jeton n'est pas retiré.
     *
     * @param collectionJetons la collection à parcourir
     * @return le premier jeton qui n'est pas un joker
     *
     */
    protected static Jeton premierJetonNonJoker(List<Jeton> collectionJetons) {
        Optional<Jeton> jeton = collectionJetons.stream().filter(i -> !i.isJoker())
                .findFirst();
        return jeton.get();
    }

    /**
     * Renvoie l'index du jeton en paramètre.
     *
     * @param jeton le jeton
     * @return l'index (compris entre 1 et la longueur de la séquence)
     *
     */
    public int indexJeton(Jeton jeton) {
        return sequence.indexOf(jeton) + 1;
    }

    /**
     * Détermine si la séquence est vide.
     *
     * @return <code>true</code> si la séquence est vide
     *
     */
    public boolean isVide() {
        return sequence.isEmpty();
    }

    /**
     * Renvoie la longueur de la séquence.
     *
     * @return la longueur
     *
     */
    public int longueur() {
        return sequence.size();
    }

    /**
     * Renvoie la liste des jetons contenus dans la séquence.
     *
     * @return la liste de jetons. Ils ne sont pas retirés de la liste.
     *
     */
    public List<Jeton> getJetons() {
        return sequence;
    }

    /**
     * Renvoie la forme textuelle d'une séquence.
     *
     * Le format est l'ensemble des jetons séparés par un espace. Exemple :
     * "7rouge 7bleu".
     */
    @Override
    public String toString() {
        String chaine = "";
        chaine = sequence.stream().map((jeton) -> jeton.toString() + " ").reduce(chaine, String::concat);
        return chaine.strip();
    }

}
