package rummikub.core.plateau;

import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.Joker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation d'un plateau de jeu.
 */
public class PlateauImpl implements Plateau{

    private final List<SequenceAbstraite> plateau;
    private final FabriqueSequence fabrique;

	private static final String ERREUR_SEQUENCE = "Index séquence non correct";

    /**
     * Crée un nouveau plateau.
     *
     * @param fabrique la fabrique de sequences
     */
    public PlateauImpl(FabriqueSequence fabrique) {
        plateau = new ArrayList<>();
        this.fabrique = fabrique;
    }

	@Override
    public int creerSequence(List<Jeton> jetons) {
        SequenceAbstraite nouvelleSequenceCouleur = fabrique.creerNouvelleSequence(jetons);
        plateau.add(nouvelleSequenceCouleur);
        return plateau.size();
    }

	@Override
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

	@Override
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

	@Override
    public Jeton retirerJeton(int indexSequence, int indexJeton) {
        if (isIndexCorrect(indexSequence)) {
            SequenceAbstraite sequenceDepart = plateau.get(indexSequence - 1);
            return sequenceDepart.retirerJeton(indexJeton);
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

	@Override
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

	@Override
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

	@Override
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
            SequenceAbstraite nouvelleSequence = fabrique.creerNouvelleSequence(jetons);
            plateau.add(nouvelleSequence);
            return nouvelleSequence.longueur();
        } else {
            throw new UnsupportedOperationException("Impossible de déplacer ce jeton");
        }
    }

	@Override
    public Joker remplacerJoker(int indexSequence, Jeton jeton) {
        if (isIndexCorrect(indexSequence)) {
            SequenceAbstraite sequence = plateau.get(indexSequence - 1);
            return sequence.remplacerJoker(jeton);
        } else {
            throw new IndexOutOfBoundsException(ERREUR_SEQUENCE);
        }
    }

	@Override
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

	@Override
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
