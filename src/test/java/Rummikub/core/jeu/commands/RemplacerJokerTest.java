package Rummikub.core.jeu.commands;

import Rummikub.core.plateau.Plateau;
import Rummikub.core.jeu.Joueur;
import Rummikub.core.pieces.*;
import Rummikub.ihm.ControleurAbstrait;
import Rummikub.ihm.ControleurTexte;
import java.util.List;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RemplacerJokerTest {

    private Plateau plateau;
    private Joueur joueur;
    private ControleurAbstrait controleur;
    private Command commande;
    private List<String> messages;

    @BeforeEach
    private void initialisation() {
        plateau = new Plateau();
        joueur = new Joueur("Kate");
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);
        Jeton jeton4 = new JetonNormal(4, Couleur.ROUGE);
        Joker joker = new Joker();
        controleur = mock(ControleurTexte.class);
        commande = new RemplacerJoker(plateau, joueur, controleur);
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, joker));
        joueur.setPiocheInitiale(Arrays.asList(jeton3, jeton4));
        messages = Arrays.asList("Numéro du jeton à uiliser : ",
                "Numéro de la séquence d'arrivée : ");
    }

    @Test
    public void remplacerJoker() {
        when(controleur.obtenirIndexes(messages)).thenReturn(Arrays.asList(1, 1));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge", plateau.toString());
        assertEquals("4rouge *", joueur.afficheJetonsJoueur());
        assertTrue(resultat);
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge*", plateau.toString());
        assertEquals("4rouge 3rouge", joueur.afficheJetonsJoueur());
    }

    @Test
    public void remplacerJokerFail() {
        when(controleur.obtenirIndexes(messages)).thenReturn(Arrays.asList(2, 1));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge*", plateau.toString());
        assertEquals("3rouge 4rouge", joueur.afficheJetonsJoueur());
        assertFalse(resultat);
    }

    @Test
    public void remplacerJokerMauvaisIndexJoueur() {
        when(controleur.obtenirIndexes(messages)).thenReturn(Arrays.asList(0, 1));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge*", plateau.toString());
        assertEquals("3rouge 4rouge", joueur.afficheJetonsJoueur());
        assertFalse(resultat);
    }
}
