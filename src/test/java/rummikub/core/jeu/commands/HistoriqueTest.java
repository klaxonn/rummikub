package Rummikub.core.jeu.commands;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class HistoriqueTest {

    private Historique historique;
    private Command commande1;
    private Command commande2;

    @BeforeEach
    private void initialisation() {
        historique = new Historique();
        commande1 = mock(CreerNouvelleSequence.class);
        commande2 = mock(CreerNouvelleSequence.class);
    }

    @Test
    public void ajouterCommande() {
        assertTrue(historique.isVide());
        historique.ajouterCommande(commande1);
        assertFalse(historique.isVide());
        historique.reinitialiserHistorique();
        assertTrue(historique.isVide());
    }

    @Test
    public void annulerDerniereCommande() {
        historique.ajouterCommande(commande1);
        historique.ajouterCommande(commande2);
        historique.annulerDerniereCommande();
        verify(commande2).undoCommand();
        historique.annulerDerniereCommande();
        verify(commande1).undoCommand();
        assertTrue(historique.isVide());
        historique.annulerDerniereCommande();
    }
}
