package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.pieces.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DeplacerJetonTest {

    private Plateau plateau;
    private Command commande;

    @BeforeEach
    private void initialisation() {
        plateau = new Plateau();
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);
        Jeton jeton4 = new JetonNormal(3, Couleur.JAUNE);
        Jeton jeton5 = new JetonNormal(3, Couleur.BLEU);
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3));
        plateau.creerSequence(Arrays.asList(jeton4, jeton5));
    }

    @Test
    public void deplacerJeton() {
		commande = new DeplacerJeton(plateau, Arrays.asList(1, 3, 2));
        commande.doCommand();
        assertEquals("1rouge 2rouge\n3jaune 3bleu 3rouge", plateau.toString());
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge\n3jaune 3bleu", plateau.toString());
    }

    @Test
    public void deplacerJetonFail() {
		commande = new DeplacerJeton(plateau, Arrays.asList(2, 2, 1));
		assertThrows(UnsupportedOperationException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge\n3jaune 3bleu", plateau.toString());
    }
}
