package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class AjouterJetonTest {

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
        Jeton jeton5 = new JetonNormal(6, Couleur.ROUGE);
        Jeton joker = new Joker();
        joueur.setPiocheInitiale(Arrays.asList(jeton4, jeton5, joker));
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3));
    }

    @Test
    public void ajouterJeton() {
        commande = new AjouterJeton(plateau, joueur, Arrays.asList(1, 1));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge", plateau.toString());
        assertEquals("6rouge *", joueur.afficheJetonsJoueur());
        assertTrue(resultat);
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());
        assertEquals("6rouge * 4rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void ajouterJetonFail() {
        commande = new AjouterJeton(plateau, joueur, Arrays.asList(2, 1));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());
        assertEquals("4rouge * 6rouge", joueur.afficheJetonsJoueur());
        assertFalse(resultat);
    }

    @Test
    public void ajouterJetonMauvaisIndexJoueur() {
        commande = new AjouterJeton(plateau, joueur, Arrays.asList(0, 1));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());
        assertEquals("4rouge 6rouge *", joueur.afficheJetonsJoueur());
        assertFalse(resultat);
    }

    @Test
    public void ajouterJoker() {
        commande = new AjouterJeton(plateau, joueur, Arrays.asList(3, 1));
        commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge*", plateau.toString());
        assertEquals("4rouge 6rouge", joueur.afficheJetonsJoueur());
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());
        assertEquals("4rouge 6rouge *", joueur.afficheJetonsJoueur());
    }

}
