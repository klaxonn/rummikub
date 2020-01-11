package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.plateau.PlateauImpl;
import rummikub.core.plateau.FabriqueSequence;
import rummikub.core.pieces.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

public class CouperSequenceTest {

    private Plateau plateau;
    private Command commande;
    private static FabriqueSequence fabrique;

    @BeforeAll
    public static void initialisation() {
		fabrique = FabriqueSequence.obtenirFabrique();
	}

    @BeforeEach
    private void initialisationTest() {
        plateau = new PlateauImpl(fabrique);
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
        commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge\n4rouge 5rouge", plateau.toString());
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }

    @Test
    public void couperSequenceFail() {
		commande = new CouperSequence(plateau, Arrays.asList(2, 4));
		assertThrows(IndexOutOfBoundsException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
    }
}
