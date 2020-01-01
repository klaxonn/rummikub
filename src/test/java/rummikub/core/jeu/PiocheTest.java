package rummikub.core.jeu;

import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PiocheTest {

    Pioche pioche;
    final static int NB_COULEURS = Couleur.values().length;
    final static int NOMBRE_TOTAL_JETONS = Pioche.VALEUR_MAX * Pioche.NB_JEUX_JETONS * NB_COULEURS + Pioche.NB_JOKERS;

    @Test
    public void piocheInitiale() {
		pioche = new Pioche();
        assertEquals(NOMBRE_TOTAL_JETONS, pioche.nombreJetonsRestants());
        List<Jeton> piocheJoueur = pioche.piocheInitiale();
        assertEquals(Pioche.NB_INITIAL_JETONS, piocheJoueur.size());
        assertFalse(pioche.isVide());
        assertEquals(NOMBRE_TOTAL_JETONS - Pioche.NB_INITIAL_JETONS, pioche.nombreJetonsRestants());

    }
}
