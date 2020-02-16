package rummikub.core.pieces;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JokerTest {

	@Test
	public void jokerTests() {
		Jeton jeton = new Joker();
		assertTrue(jeton.isJoker());
		Joker joker = (Joker) jeton;
		assertFalse(joker.isUtilise());
		assertEquals("*", joker.toString());
		assertTrue(joker.setValeurAndCouleur(1, Couleur.ROUGE));
		assertEquals("1rouge*", joker.toString());
		assertTrue(joker.isUtilise());
		assertFalse(joker.setValeurAndCouleur(2, Couleur.BLEU));
		joker.reinitialiser();
		assertFalse(joker.isUtilise());
	}
}



