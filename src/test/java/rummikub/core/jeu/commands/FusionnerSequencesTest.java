package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
<<<<<<< HEAD:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
import rummikub.core.pieces.*;
import rummikub.ihm.ControleurAbstrait;
import rummikub.ihm.ControleurTexte;
import java.util.List;
=======
import rummikub.core.plateau.PlateauImpl;
import rummikub.core.plateau.FabriqueSequence;
import rummikub.core.pieces.*;
>>>>>>> web:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

public class FusionnerSequencesTest {

    private Plateau plateau;
<<<<<<< HEAD:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
    private ControleurAbstrait controleur;
=======
>>>>>>> web:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
    private Command commande;
    private static FabriqueSequence fabrique;

    @BeforeAll
    public static void initialisation() {
		fabrique = FabriqueSequence.obtenirFabrique();
	}

    @BeforeEach
<<<<<<< HEAD:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
    private void initialisation() {
        plateau = new Plateau();
=======
    private void initialisationTest() {
        plateau = new PlateauImpl(fabrique);
>>>>>>> web:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);
        Jeton jeton4 = new JetonNormal(4, Couleur.ROUGE);
        Jeton jeton5 = new JetonNormal(5, Couleur.ROUGE);
<<<<<<< HEAD:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
        controleur = mock(ControleurTexte.class);
        commande = new FusionnerSequences(plateau, controleur);
=======
>>>>>>> web:src/test/java/rummikub/core/jeu/commands/FusionnerSequencesTest.java
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3));
        plateau.creerSequence(Arrays.asList(jeton4, jeton5));
    }

    @Test
    public void fusionnerSequences() {
		commande = new FusionnerSequences(plateau, Arrays.asList(1, 2));
        commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge\n4rouge 5rouge", plateau.toString());
    }

    @Test
    public void fusionnerSequencesFail() {
		commande = new FusionnerSequences(plateau, Arrays.asList(1, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> {
            commande.doCommand();
        });
        assertEquals("1rouge 2rouge 3rouge\n4rouge 5rouge", plateau.toString());
    }
}
