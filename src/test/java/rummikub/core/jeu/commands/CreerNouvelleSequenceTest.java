package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.plateau.PlateauImpl;
import rummikub.core.plateau.FabriqueSequence;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

public class CreerNouvelleSequenceTest {

    private Plateau plateau;
    private Joueur joueur;
    private Command commande;
    private static FabriqueSequence fabrique;

    @BeforeAll
    public static void initialisation() {
		fabrique = FabriqueSequence.obtenirFabrique();
	}

    @BeforeEach
    private void initialisationTest() {
        plateau = new PlateauImpl(fabrique);
        joueur = new Joueur("Kate");
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);
        Jeton jeton4 = new JetonNormal(4, Couleur.ROUGE);
        Jeton joker = new Joker();
        joueur.setPiocheInitiale(Arrays.asList(jeton1, jeton2, jeton3, jeton4, joker));
    }

    @Test
    public void creerSuite() {
        commande = new CreerNouvelleSequence(plateau, joueur, Arrays.asList(1, 2, 3));
        commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());
        assertEquals("4rouge *", joueur.afficheJetonsJoueur());
        commande.undoCommand();
        assertEquals("", plateau.toString());
        assertEquals("4rouge * 1rouge 2rouge 3rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void creerSuiteFail() {
        commande = new CreerNouvelleSequence(plateau, joueur, Arrays.asList(1, 3));
		assertThrows(UnsupportedOperationException.class, () -> {
            commande.doCommand();
        });
        assertEquals("", plateau.toString());
        assertEquals("2rouge 4rouge * 3rouge 1rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void creerSuiteMauvaisIndexJoueur() {
        commande = new CreerNouvelleSequence(plateau, joueur, Arrays.asList(0, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> {
            commande.doCommand();
        });
        assertEquals("", plateau.toString());
        assertEquals("1rouge 2rouge 4rouge * 3rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void creerSuiteAvecJoker() {
        commande = new CreerNouvelleSequence(plateau, joueur, Arrays.asList(1, 2, 3, 5));
        commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge*", plateau.toString());
        assertEquals("4rouge", joueur.afficheJetonsJoueur());
        commande.undoCommand();
        assertEquals("", plateau.toString());
        assertEquals("4rouge 1rouge 2rouge 3rouge *", joueur.afficheJetonsJoueur());
    }

    @Test
    public void creerSequence1Jeton() {
        Jeton jeton1 = new JetonNormal(1, Couleur.BLEU);
        joueur.ajouteJeton(jeton1);
        assertEquals("1rouge 2rouge 3rouge 4rouge * 1bleu", joueur.afficheJetonsJoueur());
        commande = new CreerNouvelleSequence(plateau, joueur, Arrays.asList(6));
        commande.doCommand();
        assertEquals("1bleu", plateau.toString());
        assertEquals("1rouge 2rouge 3rouge 4rouge *", joueur.afficheJetonsJoueur());
        commande.undoCommand();
        assertEquals("", plateau.toString());
        assertEquals("1rouge 2rouge 3rouge 4rouge * 1bleu", joueur.afficheJetonsJoueur());
    }

    @Test
    public void creerSuiteAvecJokerFail() {
		commande = new CreerNouvelleSequence(plateau, joueur, Arrays.asList(1, 4, 5));
		assertThrows(UnsupportedOperationException.class, () -> {
            commande.doCommand();
        });
        assertEquals("", plateau.toString());
        assertEquals("2rouge 3rouge * 4rouge 1rouge", joueur.afficheJetonsJoueur());
    }
}
