package rummikub.core.plateau;

import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.JetonNormal;
import rummikub.core.pieces.Joker;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

public class SequenceCouleurTest {

    private static FabriqueSequence fabrique;

    @BeforeAll
    public static void initialisation() {
		fabrique = FabriqueSequence.obtenirFabrique();
	}

    @Test
    public void nouvelleSequence() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.ROUGE);
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);
        assertEquals("1bleu 1rouge", sequence1.toString());
    }

    @Test
    public void nouvelleSequenceDifferenteValeur() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        assertThrows(UnsupportedOperationException.class, () -> {
            new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);
        });
    }

    @Test
    public void nouvelleSequenceDeuxJetonsMemeValeur() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);
        });
    }

    @Test
    public void nouvelleSequenceAvecJoker() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Joker joker = new Joker();
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1, joker), fabrique);
        assertEquals("1bleu 1rouge*", sequence1.toString());
    }

    @Test
    public void nouvelleSequenceFausseAvecJoker() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Joker joker = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            new SequenceCouleur(Arrays.asList(jeton1, jeton2, joker), fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void nouvelleSequenceAvecJokerAvecSequenceComplete() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.JAUNE);
        Jeton jeton3 = new JetonNormal(1, Couleur.VERT);
        Jeton jeton4 = new JetonNormal(1, Couleur.ROUGE);
        Joker joker = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            new SequenceCouleur(Arrays.asList(jeton1, jeton2, jeton3, jeton4, joker), fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void newSequenceJoker() {
        Joker joker = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            new SequenceCouleur(Arrays.asList(joker), fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void newSequence2Jokers() {
        Joker joker = new Joker();
        Joker joker2 = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            new SequenceCouleur(Arrays.asList(joker, joker2), fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void createWithEmptyCollection() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new SequenceCouleur(Arrays.asList(), fabrique);
        });
    }

    @Test
    public void fusion2ValidSequences() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(1, Couleur.JAUNE);
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1), fabrique);
        SequenceAbstraite sequence2 = new SequenceCouleur(Arrays.asList(jeton2, jeton3), fabrique);
        SequenceAbstraite sequence3 = sequence1.fusionnerSequence(sequence2);
        assertEquals("1bleu 1rouge 1jaune", sequence3.toString());
        assertEquals(3, sequence3.longueur());
    }

    @Test
    public void fusionCouleursCommunes() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton3 = new JetonNormal(1, Couleur.JAUNE);
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1), fabrique);
        SequenceAbstraite sequence2 = new SequenceCouleur(Arrays.asList(jeton2, jeton3), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            sequence1.fusionnerSequence(sequence2);
        });
        assertEquals("1bleu", sequence1.toString());
        assertEquals("1bleu 1jaune", sequence2.toString());
    }

    @Test
    public void fusionValeursDifferentes() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(2, Couleur.VERT);
        Jeton jeton3 = new JetonNormal(2, Couleur.JAUNE);
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1), fabrique);
        SequenceAbstraite sequence2 = new SequenceCouleur(Arrays.asList(jeton2, jeton3), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            sequence1.fusionnerSequence(sequence2);
        });
        assertEquals("1bleu", sequence1.toString());
        assertEquals("2vert 2jaune", sequence2.toString());
    }

    @Test
    public void retirerJetonHorsIndex() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.ROUGE);
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            sequence1.retirerJeton(0);
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            sequence1.retirerJeton(3);
        });

    }

    @Test
    public void retirerJeton() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.ROUGE);
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);
        Jeton jeton3 = sequence1.retirerJeton(2);
        assertEquals("1bleu", sequence1.toString());
        assertEquals("1rouge", jeton3.toString());
        assertEquals(jeton3, jeton2);
        sequence1.retirerJeton(1);
        assertTrue(sequence1.isVide());
    }

    @Test
    public void couperSequence() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.ROUGE);
        SequenceAbstraite suite1 = new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);
        SequenceAbstraite suite2 = suite1.couperSequence(2);
        assertEquals("1bleu", suite1.toString());
        assertEquals("1rouge", suite2.toString());
        assertThrows(UnsupportedOperationException.class, () -> {
            suite2.couperSequence(1);
        });
    }

    @Test
    public void couperSequenceHorsLimites() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.ROUGE);
        SequenceAbstraite suite1 = new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.couperSequence(0);
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.couperSequence(3);
        });
    }

    @Test
    public void remplacerJoker() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Joker joker = new Joker();
        joker.setValeurAndCouleur(1, Couleur.ROUGE);

        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1, joker), fabrique);
        assertEquals("1bleu 1rouge*", sequence1.toString());

        Jeton jeton3 = new JetonNormal(1, Couleur.ROUGE);
        Joker joker2 = sequence1.remplacerJoker(jeton3);
        assertEquals("1bleu 1rouge", sequence1.toString());
        assertFalse(joker2.isUtilise());

    }

    @Test
    public void remplacerJokerAvecValeurDifferente() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Joker joker = new Joker();
        joker.setValeurAndCouleur(1, Couleur.ROUGE);
        SequenceAbstraite sequence1 = new SequenceCouleur(Arrays.asList(jeton1, joker), fabrique);

        Jeton jeton2 = new JetonNormal(1, Couleur.VERT);
        assertThrows(UnsupportedOperationException.class, () -> {
            sequence1.remplacerJoker(jeton2);
        });
    }

    @Test
    public void remplacerJokerQuandPasDeJOker() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton2 = new JetonNormal(1, Couleur.ROUGE);
        SequenceCouleur sequence1 = new SequenceCouleur(Arrays.asList(jeton1, jeton2), fabrique);

        Jeton jeton3 = new JetonNormal(1, Couleur.BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            sequence1.remplacerJoker(jeton3);
        });
    }
}
