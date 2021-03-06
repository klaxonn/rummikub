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

public class RemplacerJokerTest {

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
        Jeton jeton5 = new JetonNormal(5, Couleur.ROUGE);
        Joker joker = new Joker();
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, joker));
        plateau.creerSequence(Arrays.asList(jeton5));
        joueur.setPiocheInitiale(Arrays.asList(jeton3, jeton4));
    }

    @Test
    public void remplacerJoker() {
		commande = new RemplacerJoker(plateau, joueur, Arrays.asList(1, 1));
        commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge\n5rouge", plateau.toString());
        assertEquals("4rouge *", joueur.afficheJetonsJoueur());
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge*\n5rouge", plateau.toString());
        assertEquals("4rouge 3rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void remplacerJokerFail() {
		commande = new RemplacerJoker(plateau, joueur, Arrays.asList(2, 1));
		assertThrows(UnsupportedOperationException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge*\n5rouge", plateau.toString());
        assertEquals("3rouge 4rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void remplacerJokerMauvaisIndexJoueur() {
		commande = new RemplacerJoker(plateau, joueur, Arrays.asList(0, 1));
		assertThrows(IndexOutOfBoundsException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge*\n5rouge", plateau.toString());
        assertEquals("3rouge 4rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void pasdeJokerDansLaSequence() {
		commande = new RemplacerJoker(plateau, joueur, Arrays.asList(1, 2));
		assertThrows(UnsupportedOperationException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge*\n5rouge", plateau.toString());
        assertEquals("4rouge 3rouge", joueur.afficheJetonsJoueur());
    }
}
