package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.Joueur;
import rummikub.core.pieces.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CouperSequenceTest {

    private Plateau plateau;
    private Command commande;

    @BeforeEach
    private void initialisation() {
        plateau = new Plateau();
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);
        Jeton jeton4 = new JetonNormal(4, Couleur.ROUGE);
        Jeton jeton5 = new JetonNormal(5, Couleur.ROUGE);
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3, jeton4, jeton5));
    }

    @Test
    public void couperSequence() {
        commande = new CouperSequence(plateau, Arrays.asList(1, 4));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge\n4rouge 5rouge", plateau.toString());
        assertTrue(resultat);
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void couperSequenceFail() {
		commande = new CouperSequence(plateau, Arrays.asList(2, 4));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
        assertFalse(resultat);
    }
}
