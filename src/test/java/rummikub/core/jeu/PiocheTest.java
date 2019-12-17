package Rummikub.core.jeu;

import Rummikub.core.pieces.Couleur;
import Rummikub.core.pieces.Jeton;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PiocheTest {

    Pioche pioche;
    final static int NB_COULEURS = Couleur.values().length;
    final static int NOMBRE_TOTAL_JETONS = Pioche.VALEUR_MAX * Pioche.NB_JEUX_JETONS * NB_COULEURS + Pioche.NB_JOKERS;

    @BeforeEach
    private void initialisation() {
        pioche = new Pioche();
    }

    @Test
    public void piocheInitiale() {
        assertEquals(NOMBRE_TOTAL_JETONS, pioche.nombreJetonsRestants());
        List<Jeton> piocheJoueur = pioche.piocheInitiale();
        assertEquals(Pioche.NB_INITIAL_JETONS, piocheJoueur.size());
        assertFalse(pioche.isVide());
        assertEquals(NOMBRE_TOTAL_JETONS - Pioche.NB_INITIAL_JETONS, pioche.nombreJetonsRestants());

    }
}
