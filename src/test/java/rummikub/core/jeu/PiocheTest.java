package rummikub.core.jeu;

import rummikub.core.api.FabriquePartie;
import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.JetonNormal;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PiocheTest {

    private Pioche pioche;
    private static final int NOMBRE_TOTAL_JETONS = FabriquePartie.VALEUR_MAX * FabriquePartie.NB_JEUX_JETONS;

	@BeforeEach
    private void initialisation() {
		List<Jeton> jetons = new ArrayList<>();
        for (int i = 1; i <= FabriquePartie.VALEUR_MAX; i++) {
			for (int j = 1; j <= FabriquePartie.NB_JEUX_JETONS; j++) {
				Jeton jeton = new JetonNormal(i, Couleur.BLEU);
                jetons.add(jeton);
			}
		}
		pioche = new Pioche(jetons);
	}

    @Test
    public void piocheInitialeTest() {
        assertEquals(NOMBRE_TOTAL_JETONS, pioche.nombreJetonsRestants());
        List<Jeton> piocheJoueur = pioche.piocheInitiale();
        assertEquals(Pioche.NB_INITIAL_JETONS, piocheJoueur.size());
        assertFalse(pioche.isVide());
        assertEquals(NOMBRE_TOTAL_JETONS - Pioche.NB_INITIAL_JETONS, pioche.nombreJetonsRestants());
    }

    @Test
    public void piocher1Jeton() {
        assertEquals(NOMBRE_TOTAL_JETONS, pioche.nombreJetonsRestants());
        pioche.piocher1Jeton();
        assertFalse(pioche.isVide());
        assertEquals(NOMBRE_TOTAL_JETONS - 1, pioche.nombreJetonsRestants());
    }

    @Test
    public void piocher1JetonFail() {
		for (int i = 1; i<= NOMBRE_TOTAL_JETONS; i++) {
			pioche.piocher1Jeton();
		}
		assertTrue(pioche.isVide());
        assertThrows(UnsupportedOperationException.class, () -> {
			pioche.piocher1Jeton();
        });
    }
}
