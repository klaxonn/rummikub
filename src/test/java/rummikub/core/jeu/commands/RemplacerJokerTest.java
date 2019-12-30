package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RemplacerJokerTest {

    private Plateau plateau;
    private Joueur joueur;
    private Command commande;

    @BeforeEach
    private void initialisation() {
        plateau = new Plateau();
        joueur = new Joueur("Kate");
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);
        Jeton jeton4 = new JetonNormal(4, Couleur.ROUGE);
        Joker joker = new Joker();
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, joker));
        joueur.setPiocheInitiale(Arrays.asList(jeton3, jeton4));
    }

    @Test
    public void remplacerJoker() {
		commande = new RemplacerJoker(plateau, joueur, Arrays.asList(1, 1));
        commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());
        assertEquals("4rouge *", joueur.afficheJetonsJoueur());
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge*", plateau.toString());
        assertEquals("4rouge 3rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void remplacerJokerFail() {
		commande = new RemplacerJoker(plateau, joueur, Arrays.asList(2, 1));
		assertThrows(UnsupportedOperationException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge*", plateau.toString());
        assertEquals("3rouge 4rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void remplacerJokerMauvaisIndexJoueur() {
		commande = new RemplacerJoker(plateau, joueur, Arrays.asList(0, 1));
		assertThrows(IndexOutOfBoundsException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge*", plateau.toString());
        assertEquals("3rouge 4rouge", joueur.afficheJetonsJoueur());
    }
}
