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

public class PlateauTestFail {

    private List<Jeton> suiteTest;
    private List<Jeton> suiteTest2;
    private List<Jeton> couleurSequenceTest;
    private Plateau plateau;

    @BeforeEach
    private void initialisation() {
        plateau = new Plateau();

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
    }

    @Test
    public void creerSequenceFail() {
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.BLEU);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.creerSequence(Arrays.asList(jeton1, jeton2));
        });
        assertEquals("", plateau.toString());
    }

    @Test
    public void supprimeSequenceMauvaisIndex() {
        int index = plateau.creerSequence(suiteTest);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.supprimerSequence(index + 1);
        });
    }

    @Test
    public void ajouteJetonFail() {
        plateau.creerSequence(suiteTest);
        Jeton jeton = new JetonNormal(7, Couleur.ROUGE);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.ajouterJeton(1, jeton);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void ajouteJetonMauvaisIndexSequence() {
        plateau.creerSequence(suiteTest);
        Jeton jeton = new JetonNormal(7, Couleur.ROUGE);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.ajouterJeton(2, jeton);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void supprime1JetonMauvaisIndexSequence() {
        plateau.creerSequence(suiteTest);
        Jeton jeton = new JetonNormal(6, Couleur.ROUGE);
        plateau.ajouterJeton(1, jeton);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.retirerJeton(3, 1);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge 6rouge", plateau.toString());
    }

    @Test
    public void supprime1JetonPasDansSequence() {
        plateau.creerSequence(suiteTest);
        Jeton jeton = new JetonNormal(5, Couleur.ROUGE);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.retirerJeton(1, 9);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void deplaceJetonVersNouvelleSequence() {
        plateau.creerSequence(suiteTest);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.deplacerJeton(1, 8, 2);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void deplaceJetonDeMauvaiseSequenceVersNouvelleSequence() {
        plateau.creerSequence(suiteTest);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.deplacerJeton(2, 8, 3);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void couperSequenceMauvaisIndexJeton() {
        plateau.creerSequence(suiteTest);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.couperSequence(1, 8);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void couperSequenceMauvaisIndexSequence() {
        plateau.creerSequence(suiteTest);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.couperSequence(2, 3);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void fusionnerSequence() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.fusionnerSequences(1, 2);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge\n"
                + "6bleu 6rouge 6vert 6jaune", plateau.toString());
    }

    @Test
    public void fusionnerSequenceMauvaisIndexSequence() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.fusionnerSequences(3, 3);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge\n"
                + "6bleu 6rouge 6vert 6jaune", plateau.toString());
    }

    @Test
    public void deplacerJeton() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.deplacerJeton(2, 1, 1);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge\n"
                + "6rouge 6vert 6jaune 6bleu", plateau.toString());
    }

    @Test
    public void deplacerJetonMauvaisIndexSequenceDepart() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.deplacerJeton(3, 1, 1);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge\n"
                + "6bleu 6rouge 6vert 6jaune", plateau.toString());
    }

    @Test
    public void deplacerJetonMauvaisIndexSequenceArrivee() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.deplacerJeton(1, 1, 4);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge\n"
                + "6bleu 6rouge 6vert 6jaune", plateau.toString());
    }

    @Test
    public void deplacerJetonMauvaisIndexJeton() {
        plateau.creerSequence(suiteTest);
        plateau.creerSequence(couleurSequenceTest);
        assertThrows(UnsupportedOperationException.class, () -> {
            plateau.deplacerJeton(1, 4, 2);
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge\n"
                + "6bleu 6rouge 6vert 6jaune", plateau.toString());
    }

    @Test
    public void remplacerJokerMauvaisIndex() throws UnsupportedOperationException {
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(3, Couleur.ROUGE);
        Joker joker = new Joker();

        plateau.creerSequence(Arrays.asList(jeton1, joker, jeton2));

        Jeton jeton3 = new JetonNormal(2, Couleur.ROUGE);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.remplacerJoker(2, jeton3);
        });
        assertEquals("1rouge 2rouge* 3rouge", plateau.toString());

    }

    @Test
    public void remplacerJetonMauvaisIndex() throws UnsupportedOperationException {
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);

        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3));
        Joker joker = new Joker();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.remplacerJetonParJoker(2, joker);
        });
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());

    }
}
