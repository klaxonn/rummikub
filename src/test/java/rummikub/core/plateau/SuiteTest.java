package rummikub.core.plateau;

import static rummikub.core.pieces.Couleur.*;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.JetonNormal;
import rummikub.core.pieces.Joker;
import rummikub.core.api.FabriquePartie;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

public class SuiteTest {

	private static FabriqueSequence fabrique;

    @BeforeAll
    public static void initialisation() {
		fabrique = FabriqueSequence.obtenirFabrique();
	}

    @Test
    public void newSuite() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
		Jeton jeton3 = new JetonNormal(3, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2,jeton3), fabrique);
        assertEquals("1bleu 2bleu 3bleu", suite1.toString());
    }

    @Test
    public void newSuiteCouleurDifferente() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, ROUGE);
        assertThrows(UnsupportedOperationException.class, () -> {
            new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        });

    }

    @Test
    public void newSuiteValeurNonCroissante() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(1, BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        });
    }

    @Test
    public void newSuitewithWithListeVide() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new Suite(Arrays.asList(), fabrique);
        });
    }

    @Test
    public void newSuiteAvecJoker() {
        Jeton jeton1 = new JetonNormal(4, BLEU);
        Joker joker = new Joker();
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, joker), fabrique);
        assertEquals("3bleu* 4bleu", suite1.toString());
    }

    @Test
    public void newSuiteAvecJokerBorneInf() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Joker joker = new Joker();
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, joker), fabrique);
        assertEquals("1bleu 2bleu*", suite1.toString());
    }

    @Test
    public void newSuiteAvecJokerBorneSup() {
        Jeton jeton1 = new JetonNormal(FabriquePartie.VALEUR_MAX, ROUGE);
        Joker joker = new Joker();
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, joker), fabrique);
        assertEquals("12rouge* 13rouge", suite1.toString());
    }

    @Test
    public void newSuiteAvecJokerAvecTrou() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(3, BLEU);
        Joker joker = new Joker();
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2, joker), fabrique);
        assertEquals("1bleu 2bleu* 3bleu", suite1.toString());
    }

    @Test
    public void newSuiteAvecJokerAvecGrosTrou() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(4, BLEU);
        Joker joker = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            new Suite(Arrays.asList(jeton1, jeton2, joker), fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void newSuiteAvecJokerAvecSuiteComplete() {
        Joker joker = new Joker();
        List<Jeton> jetons1 = new ArrayList<>();
        jetons1.add(joker);
        for (int i = 1; i <= FabriquePartie.VALEUR_MAX; i++) {
            jetons1.add(new JetonNormal(i, BLEU));
        }
        assertThrows(UnsupportedOperationException.class, () -> {
            new Suite(jetons1, fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void newSuiteJoker() {
        Joker joker = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            new Suite(Arrays.asList(joker), fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void newSuite2Jokers() {
        Joker joker = new Joker();
        Joker joker2 = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            new Suite(Arrays.asList(joker, joker2), fabrique);
        });
        assertFalse(joker.isUtilise());
    }

    @Test
    public void ajoutJeton() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        Jeton jeton3 = new JetonNormal(3, BLEU);
        SequenceAbstraite suite2 = suite1.ajouterJeton(jeton3);
        assertEquals("1bleu 2bleu 3bleu", suite2.toString());
    }

    @Test
    public void ajoutMauvaisJeton() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        Jeton jeton3 = new JetonNormal(4, BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.ajouterJeton(jeton3);
        });
        assertEquals("1bleu 2bleu", suite1.toString());
    }

    @Test
    public void fusionSuite() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        Jeton jeton3 = new JetonNormal(3, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1), fabrique);
        SequenceAbstraite suite2 = new Suite(Arrays.asList(jeton2, jeton3), fabrique);
        SequenceAbstraite suite3 = suite1.fusionnerSequence(suite2);
        assertEquals("1bleu 2bleu 3bleu", suite3.toString());
    }

    @Test
    public void fusionSuiteDesordre() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        Jeton jeton3 = new JetonNormal(3, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton3), fabrique);
        SequenceAbstraite suite2 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        SequenceAbstraite suite3 = suite1.fusionnerSequence(suite2);
        assertEquals("1bleu 2bleu 3bleu", suite3.toString());
    }

    @Test
    public void fusionSuiteWithCouleursDifferentes() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, ROUGE);
        Jeton jeton3 = new JetonNormal(3, ROUGE);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1), fabrique);
        SequenceAbstraite suite2 = new Suite(Arrays.asList(jeton2, jeton3), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.fusionnerSequence(suite2);
        });
        assertEquals("1bleu", suite1.toString());
        assertEquals("2rouge 3rouge", suite2.toString());
    }

    @Test
    public void fusionSuiteWithValeurNonCroissante() {
        Jeton jeton1 = new JetonNormal(1, ROUGE);
        Jeton jeton2 = new JetonNormal(3, ROUGE);
        Jeton jeton3 = new JetonNormal(4, ROUGE);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1), fabrique);
        SequenceAbstraite suite2 = new Suite(Arrays.asList(jeton2, jeton3), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.fusionnerSequence(suite2);
        });
        assertEquals("1rouge", suite1.toString());
        assertEquals("3rouge 4rouge", suite2.toString());
    }

    @Test
    public void retirerJetonSup() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        Jeton jeton3 = suite1.retirerJeton(2);
        assertEquals("1bleu", suite1.toString());
        assertSame(jeton2, jeton3);
    }

    @Test
    public void retirerJetonInf() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        Jeton jeton3 = suite1.retirerJeton(1);
        assertEquals("2bleu", suite1.toString());
        assertSame(jeton1, jeton3);
    }

    @Test
    public void retirerJetonHorsLimite() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.retirerJeton(3);
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.retirerJeton(0);
        });
        assertEquals("1bleu 2bleu", suite1.toString());
    }

    @Test
    public void retirerJetonMilieu() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        Jeton jeton3 = new JetonNormal(3, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2, jeton3), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.retirerJeton(2);
        });
        assertEquals("1bleu 2bleu 3bleu", suite1.toString());
    }

    @Test
    public void couperSequence() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        Jeton jeton3 = new JetonNormal(3, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2, jeton3), fabrique);
        SequenceAbstraite suite2 = suite1.couperSequence(2);
        assertEquals("1bleu", suite1.toString());
        assertEquals("2bleu 3bleu", suite2.toString());
    }

    @Test
    public void couperSequenceHorsLimites() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.couperSequence(0);
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.couperSequence(3);
        });
        assertEquals("1bleu 2bleu", suite1.toString());
    }

    @Test
    public void remplacerJoker() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Joker joker = new Joker();
        joker.setValeurAndCouleur(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, joker), fabrique);
        assertEquals("1bleu 2bleu*", suite1.toString());

        Jeton jeton3 = new JetonNormal(2, BLEU);
        Joker joker2 = suite1.remplacerJoker(jeton3);
        assertEquals("1bleu 2bleu", suite1.toString());
        assertSame(joker, joker2);
        assertFalse(joker2.isUtilise());

    }

    @Test
    public void remplacerJeton() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        assertEquals("1bleu 2bleu", suite1.toString());

        Joker joker = new Joker();
        joker.setValeurAndCouleur(2, BLEU);
        Jeton jeton3 = suite1.remplacerJetonParJoker(joker);
        assertEquals("1bleu 2bleu*", suite1.toString());
        assertSame(jeton2, jeton3);
    }

    @Test
    public void remplacerJetonJokerNonInitialise() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        assertEquals("1bleu 2bleu", suite1.toString());

        Joker joker = new Joker();
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.remplacerJetonParJoker(joker);
        });
        assertEquals("1bleu 2bleu", suite1.toString());
    }

    @Test
    public void remplacerJetonFail() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);
        assertEquals("1bleu 2bleu", suite1.toString());

        Joker joker = new Joker();
        joker.setValeurAndCouleur(3, BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.remplacerJetonParJoker(joker);
        });
        assertEquals("1bleu 2bleu", suite1.toString());
    }

    @Test
    public void remplacerJokerAvecValeurDifferente() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Joker joker = new Joker();
        joker.setValeurAndCouleur(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, joker), fabrique);
        assertEquals("1bleu 2bleu*", suite1.toString());

        Jeton jeton2 = new JetonNormal(3, BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.remplacerJoker(jeton2);
        });
        assertEquals("1bleu 2bleu*", suite1.toString());

    }

    @Test
    public void remplacerJokerQuandPasDeJoker() {
        Jeton jeton1 = new JetonNormal(1, BLEU);
        Jeton jeton2 = new JetonNormal(2, BLEU);
        SequenceAbstraite suite1 = new Suite(Arrays.asList(jeton1, jeton2), fabrique);

        Jeton jeton3 = new JetonNormal(2, BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            suite1.remplacerJoker(jeton3);
        });
        assertEquals("1bleu 2bleu", suite1.toString());
    }

}
