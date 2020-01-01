package rummikub.core.jeu;

import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.JetonNormal;
import rummikub.core.pieces.Joker;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class JoueurTest {

    Jeton jeton5;
    Joueur joueur;

    @BeforeEach
    public void initialisation() {
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(10, Couleur.BLEU);
        Jeton jeton3 = new JetonNormal(11, Couleur.JAUNE);
        Jeton jeton4 = new JetonNormal(12, Couleur.VERT);
        jeton5 = new JetonNormal(1, Couleur.ROUGE);
        Jeton joker = new Joker();
        joueur = new Joueur("Kate");
        joueur.setPiocheInitiale(Arrays.asList(
                jeton1, jeton2, jeton3, joker, jeton4, jeton5));
    }

    @Test
    public void nouveauJoueur() {
        assertEquals("Kate", joueur.getNom());
        assertEquals("1rouge 10bleu 11jaune * 12vert 1rouge", joueur.afficheJetonsJoueur());
        assertEquals("Kate\n1rouge 10bleu 11jaune * 12vert 1rouge", joueur.toString());
        assertFalse(joueur.aJoueAuMoins1Jeton());
    }

    @Test
    public void utiliseJeton() {
        Jeton jeton = joueur.utiliseJeton(5);
        assertEquals("1rouge 10bleu 11jaune * 1rouge", joueur.afficheJetonsJoueur());
        assertEquals("12vert", jeton.toString());
        assertEquals(23, joueur.getScore());
    }

    @Test
    public void utiliseJetonMauvaisIndexes() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            joueur.utiliseJeton(0);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            joueur.utiliseJeton(7);
        });
        assertEquals("1rouge 10bleu 11jaune * 12vert 1rouge", joueur.afficheJetonsJoueur());
        assertEquals(35, joueur.getScore());
    }

    @Test
    public void utiliseJetons() {
        List<Jeton> jetons = joueur.utiliseJetons(Arrays.asList(1, 4, 5));
        assertEquals("10bleu 11jaune 1rouge", joueur.afficheJetonsJoueur());
        assertEquals("[12vert, *, 1rouge]", jetons.toString());
        assertEquals(22, joueur.getScore());
    }

    @Test
    public void utiliseJetonsMauvaisIndexes() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            joueur.utiliseJetons(Arrays.asList(1, 0, 7));
        });
        assertEquals("1rouge 10bleu 11jaune * 12vert 1rouge", joueur.afficheJetonsJoueur());
        assertEquals(35, joueur.getScore());
    }

    @Test
    public void ajouteJeton() {
        Jeton jeton = new JetonNormal(7, Couleur.ROUGE);
        joueur.ajouteJeton(jeton);
        assertEquals("1rouge 10bleu 11jaune * 12vert 1rouge 7rouge", joueur.afficheJetonsJoueur());
        assertEquals(42, joueur.getScore());
    }

    @Test
    public void ajouteJetons() {
        Jeton jeton1 = new JetonNormal(7, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(12, Couleur.BLEU);
        List<Jeton> jetons = new ArrayList<>(Arrays.asList(jeton1, jeton2));
        joueur.ajouteJetons(jetons);
        assertEquals("1rouge 10bleu 11jaune * 12vert 1rouge 7rouge 12bleu", joueur.afficheJetonsJoueur());
        assertEquals(54, joueur.getScore());
    }

    @Test
    public void getScore() {
        assertEquals(35, joueur.getScore());
    }

    @Test
    public void aGagne() {
        joueur.utiliseJetons(Arrays.asList(1, 2, 3, 4, 5, 6));
        assertTrue(joueur.aGagne());
        assertEquals(0, joueur.getScore());
    }

    @Test
    public void estAutoriseATerminerleTour() {
        assertFalse(joueur.isAutoriseAterminerLeTour());
        joueur.utiliseJetons(Arrays.asList(2, 3, 5));
        assertTrue(joueur.isAutoriseAterminerLeTour());
        assertTrue(joueur.aJoueAuMoins1Jeton());
    }

    @Test
    public void pasAutoriseATerminerleTour() {
        joueur.utiliseJetons(Arrays.asList(2, 3));
        assertFalse(joueur.isAutoriseAterminerLeTour());
        assertEquals(9, joueur.pointsRestantsNecessaires());
    }

    @Test
    public void plusBesoinAutorisation() {
        joueur.utiliseJetons(Arrays.asList(2, 3, 5));
        assertTrue(joueur.aJoueAuMoins1Jeton());
        assertTrue(joueur.isAutoriseAterminerLeTour());
        joueur.initialiserNouveauTour();
        assertFalse(joueur.aJoueAuMoins1Jeton());
        assertEquals(0, joueur.pointsRestantsNecessaires());
        joueur.utiliseJeton(1);
        assertTrue(joueur.isAutoriseAterminerLeTour());
    }
}
