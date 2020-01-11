package rummikub.core.plateau;

import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.JetonNormal;
import rummikub.core.pieces.Joker;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;


public class PlateauTest {

    private List<Jeton> suiteTest;
    private List<Jeton> suiteTest2;
    private List<Jeton> couleurSequenceTest;
    private Plateau plateau;
    private static FabriqueSequence fabrique;

    @BeforeAll
    public static void initialisation() {
		fabrique = FabriqueSequence.obtenirFabrique();
	}


    @BeforeEach
    public void initialisationTest() {
        plateau = new PlateauImpl(fabrique);

        suiteTest = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Jeton jeton = new JetonNormal(i, Couleur.ROUGE);
            suiteTest.add(jeton);
        }

        couleurSequenceTest = new ArrayList<>();
        for (Couleur couleur : Couleur.values()) {
            Jeton jeton = new JetonNormal(6, couleur);
            couleurSequenceTest.add(jeton);
        }

        suiteTest2 = new ArrayList<>();
        for (int i = 6; i <= 7; i++) {
            Jeton jeton = new JetonNormal(i, Couleur.ROUGE);
            suiteTest2.add(jeton);
        }
    }

    @Test
    public void ajouteSequence() {
        int index = plateau.creerSequence(suiteTest);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
        assertEquals(1, index);
    }

    @Test
    public void ajouteSequenceAvecJoker() {
        Joker joker = new Joker();
        suiteTest.add(joker);
        suiteTest.remove(0);
        int index = plateau.creerSequence(suiteTest);
        assertEquals("1rouge* 2rouge 3rouge 4rouge 5rouge", plateau.toString());
        assertEquals(1, index);
    }

    @Test
    public void ajouteSequenceCouleurAvecJoker() {
        Joker joker = new Joker();
        Jeton jeton1 = new JetonNormal(10, Couleur.VERT);
        Jeton jeton2 = new JetonNormal(10, Couleur.BLEU);
        int index = plateau.creerSequence(Arrays.asList(joker, jeton1, jeton2));
        assertEquals("10rouge* 10vert 10bleu", plateau.toString());
        assertEquals(1, index);
    }

    @Test
    public void supprimeSequence() {
        int index = plateau.creerSequence(suiteTest);
        List<Jeton> jetons = plateau.supprimerSequence(index);
        assertEquals(suiteTest, jetons);
    }

    @Test
    public void supprimeSequenceAvecJoker() {
        Joker joker = new Joker();
        suiteTest.add(joker);
        int index = plateau.creerSequence(suiteTest);
        List<Jeton> jetons = plateau.supprimerSequence(index);
        assertEquals(suiteTest, jetons);
        assertFalse(joker.isUtilise());
    }

    @Test
    public void ajoute1Jeton() {
        plateau.creerSequence(suiteTest);
        Jeton jeton = new JetonNormal(6, Couleur.ROUGE);
        plateau.ajouterJeton(1, jeton);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge 6rouge", plateau.toString());
    }

    @Test
    public void ajoute1Joker() {
        plateau.creerSequence(suiteTest);
        Joker joker = new Joker();
        plateau.ajouterJeton(1, joker);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge 6rouge*", plateau.toString());
    }

    @Test
    public void retire1Jeton() {
        plateau.creerSequence(suiteTest);
        Jeton jeton = new JetonNormal(6, Couleur.ROUGE);
        int index = plateau.ajouterJeton(1, jeton);
        plateau.retirerJeton(1, index);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void deplacerJeton() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        int index = plateau.deplacerJeton(2, 2, 1);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge 6rouge\n"
                + "6bleu 6vert 6jaune", plateau.toString());
        assertEquals(6, index);
    }

    @Test
    public void deplacerJetonSequence1Jeton() {
        plateau.creerSequence(suiteTest);
        Jeton jeton = new JetonNormal(6, Couleur.ROUGE);
        List<Jeton> liste = new ArrayList<>();
        liste.add(jeton);
        plateau.creerSequence(liste);
        int index = plateau.deplacerJeton(2, 1, 1);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge 6rouge", plateau.toString());
        assertEquals(6, index);
    }

    @Test
    public void deplaceJetonVersNouvelleSequence() {
        plateau.creerSequence(suiteTest);
        int index = plateau.deplacerJeton(1, 1, 2);
        assertEquals("2rouge 3rouge 4rouge 5rouge\n"
                + "1rouge", plateau.toString());
        assertEquals(1, index);
    }

    @Test
    public void couperSequence() {
        plateau.creerSequence(suiteTest);
        int index = plateau.couperSequence(1, 3);
        assertEquals("1rouge 2rouge\n"
                + "3rouge 4rouge 5rouge", plateau.toString());
        assertFalse(plateau.isValide());
        assertEquals(2, index);
    }

    @Test
    public void fusionnerSequence() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(suiteTest2);
        int index = plateau.fusionnerSequences(1, 2);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge 6rouge 7rouge", plateau.toString());
        assertEquals(6, index);
    }

    @Test
    public void remplacerJoker() {
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton3 = new JetonNormal(1, Couleur.JAUNE);
        Joker joker = new Joker();

        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3, joker));

        Jeton jeton4 = new JetonNormal(1, Couleur.VERT);
        Joker joker2 = plateau.remplacerJoker(1, jeton4);
        assertEquals("1rouge 1bleu 1jaune 1vert", plateau.toString());
        assertSame(joker, joker2);
    }

    @Test
    public void remplacerJeton() {
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(1, Couleur.BLEU);
        Jeton jeton3 = new JetonNormal(1, Couleur.JAUNE);
        Jeton jeton4 = new JetonNormal(1, Couleur.VERT);
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3, jeton4));

        Joker joker = new Joker();
        joker.setValeurAndCouleur(1, Couleur.BLEU);
        Jeton jeton5 = plateau.remplacerJetonParJoker(1, joker);
        assertEquals("1rouge 1bleu* 1jaune 1vert", plateau.toString());
        assertSame(jeton2, jeton5);
    }

    @Test
    public void plateauValide() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        assertTrue(plateau.isValide());
    }

}
