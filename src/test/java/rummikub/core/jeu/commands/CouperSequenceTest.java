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

public class CouperSequenceTest {

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
        Jeton jeton5 = new JetonNormal(5, Couleur.ROUGE);
        controleur = mock(ControleurTexte.class);
        commande = new CouperSequence(plateau, joueur, controleur);
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3, jeton4, jeton5));
        messages = Arrays.asList("Numéro de la séquence à couper : ",
                "Numéro du jeton où couper : ");
    }

    @Test
    public void couperSequence() {
        when(controleur.obtenirIndexes(messages)).thenReturn(Arrays.asList(1, 4));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge\n4rouge 5rouge", plateau.toString());
        assertTrue(resultat);
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void couperSequenceFail() {
        when(controleur.obtenirIndexes(messages)).thenReturn(Arrays.asList(2, 4));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
        assertFalse(resultat);
    }
}
