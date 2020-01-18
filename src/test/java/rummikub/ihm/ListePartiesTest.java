package rummikub.ihm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import rummikub.core.api.Partie;

public class ListePartiesTest {

	private ListeParties listeParties;
	Partie partie1;
	Partie partie2;

	@BeforeEach
	public void initialisation() {
		listeParties = new ListeParties();
		partie1 = listeParties.creerPartie(Arrays.asList("Vincent", "Kate"));
		partie2 = listeParties.creerPartie(Arrays.asList("Benoit", "Emie"));
	}

    @Test
	public void getIdTest(){
		assertEquals(partie1, listeParties.getPartie(1));
		assertEquals(partie2, listeParties.getPartie(2));
		assertEquals(null, listeParties.getPartie(3));
	}

    @Test
	public void listerPartiesDisposTest(){
		assertEquals("idPartie: 1 joueurs: [\"Vincent\", \"Kate\"]\n"
		+"idPartie: 2 joueurs: [\"Benoit\", \"Emie\"]", listeParties.listerPartiesDispos());
		partie1.commencerPartie();
		assertEquals("idPartie: 2 joueurs: [\"Benoit\", \"Emie\"]", listeParties.listerPartiesDispos());
	}
}
