package Rummikub.core.plateau;

import Rummikub.core.pieces.Couleur;
import Rummikub.core.pieces.Jeton;
import Rummikub.core.pieces.JetonNormal;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FabriqueSequenceTest {

    @Test
    public void creerSuite() {
        List<Jeton> liste1 = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Jeton jeton = new JetonNormal(i, Couleur.ROUGE);
            liste1.add(jeton);
        }
        SequenceAbstraite sequence = FabriqueSequence.creerNouvelleSequence(liste1);
        assertEquals("1rouge 2rouge 3rouge 4rouge 5rouge", sequence.toString());
    }

    @Test
    public void creerSequence() {
        List<Jeton> liste2 = new ArrayList<>();
        for (Couleur couleur : Couleur.values()) {
            Jeton jeton = new JetonNormal(2, couleur);
            liste2.add(jeton);
        }
        SequenceAbstraite sequence = FabriqueSequence.creerNouvelleSequence(liste2);
        assertEquals("2bleu 2rouge 2vert 2jaune", sequence.toString());
    }

    @Test
    public void creerInvalideSequence() {
        Jeton jeton = new JetonNormal(2, Couleur.ROUGE);
        Jeton jeton2 = new JetonNormal(1, Couleur.VERT);
        assertThrows(UnsupportedOperationException.class, () -> {
            FabriqueSequence.creerNouvelleSequence(Arrays.asList(jeton, jeton2));
        });
    }
}
