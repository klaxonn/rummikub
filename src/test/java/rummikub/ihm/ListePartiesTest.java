package rummikub.ihm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import rummikub.core.api.Partie;

public class ListePartiesTest {

	private ListeParties listeParties;
	int idPartie1;
	int idPartie2;

	@BeforeEach
	public void initialisation() {
		listeParties = new ListeParties();
		idPartie1 = listeParties.creerPartie(Arrays.asList("Vincent", "Kate"));
		idPartie2 = listeParties.creerPartie(Arrays.asList("Benoit", "Emie"));
	}

	@Test
	public void creationPartieFail(){
		assertEquals(idPartie1, 1);
		assertEquals(idPartie2, 2);
		assertThrows(IllegalArgumentException.class, () -> {
			listeParties.creerPartie(Arrays.asList("A", "B", "C", "D", "E"));
		});
	}

    @Test
	public void getIdTest(){
		assertEquals(idPartie1, 1);
		assertEquals(idPartie2, 2);
		assertEquals(null, listeParties.getPartie(3));
	}

    @Test
	public void listerPartiesDisposTest(){
		assertEquals("idPartie: 1 joueurs: [\"Vincent\", \"Kate\"]\n"
		+"idPartie: 2 joueurs: [\"Benoit\", \"Emie\"]", listeParties.listerPartiesDispos());
		listeParties.getPartie(idPartie1).commencerPartie();
		assertEquals("idPartie: 2 joueurs: [\"Benoit\", \"Emie\"]", listeParties.listerPartiesDispos());
	}
}
