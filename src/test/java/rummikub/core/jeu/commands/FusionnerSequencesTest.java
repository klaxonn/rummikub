package rummikub.core.jeu.commands;

import rummikub.core.plateau.Plateau;
import rummikub.core.pieces.*;
import rummikub.ihm.ControleurAbstrait;
import rummikub.ihm.ControleurTexte;
import java.util.List;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class FusionnerSequencesTest {

    private Plateau plateau;
    private ControleurAbstrait controleur;
    private Command commande;
    private List<String> messages;

    @BeforeEach
    private void initialisation() {
        plateau = new Plateau();
        Jeton jeton1 = new JetonNormal(1, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton3 = new JetonNormal(3, Couleur.ROUGE);
        Jeton jeton4 = new JetonNormal(4, Couleur.ROUGE);
        Jeton jeton5 = new JetonNormal(5, Couleur.ROUGE);
        controleur = mock(ControleurTexte.class);
        commande = new FusionnerSequences(plateau, controleur);
        plateau.creerSequence(Arrays.asList(jeton1, jeton2, jeton3));
        plateau.creerSequence(Arrays.asList(jeton4, jeton5));
        messages = Arrays.asList("Numéro de la séquence de départ : ",
                "Numéro de la séquence d'arrivée : ");
    }

    @Test
    public void fusionnerSequences() {
        when(controleur.obtenirIndexes(messages)).thenReturn(Arrays.asList(1, 2));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", plateau.toString());
        assertTrue(resultat);
        commande.undoCommand();
        assertEquals("1rouge 2rouge 3rouge\n4rouge 5rouge", plateau.toString());
    }

    @Test
    public void fusionnerSequencesFail() {
        when(controleur.obtenirIndexes(messages)).thenReturn(Arrays.asList(1, 3));
        boolean resultat = commande.doCommand();
        assertEquals("1rouge 2rouge 3rouge\n4rouge 5rouge", plateau.toString());
        assertFalse(resultat);
    }
}
