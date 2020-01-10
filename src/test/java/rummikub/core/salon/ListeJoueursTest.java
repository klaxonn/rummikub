package rummikub.salon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ListeJoueursTest {

	@BeforeEach
	public void initialisation() {
		ListeJoueurs.retirerTousJoueurs();
	}

    @Test
	public void ajouterJoueurConnecteTest(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		assertEquals("[Vincent]",ListeJoueurs.getJoueursConnectes().toString());
		assertEquals(1,ListeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void ajouterJoueurConnecteMemeNomTest(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		assertEquals("[Vincent, Vincent-1]",ListeJoueurs.getJoueursConnectes().toString());
		assertEquals(2,ListeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void retirerJoueurTest(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		ListeJoueurs.retirerJoueur("Vincent");
		assertEquals("[]",ListeJoueurs.getJoueursConnectes().toString());
		assertEquals(0,ListeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void retirerJoueurTestFail(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		assertThrows(UnsupportedOperationException.class, () -> {
            ListeJoueurs.retirerJoueur("Katya");
        });
		assertEquals("[Vincent]",ListeJoueurs.getJoueursConnectes().toString());
		assertEquals(1,ListeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void ajouterJoueurPartie(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		ListeJoueurs.ajouteJoueurPartie("Vincent");
		ListeJoueurs.ajouterJoueurConnecte("Katya");
		ListeJoueurs.ajouteJoueurPartie("Katya");
		assertEquals("[Vincent, Katya]",ListeJoueurs.getJoueursConnectes().toString());
		assertEquals(2,ListeJoueurs.nombreJoueursConnectes());
		assertEquals("[Vincent, Katya]",ListeJoueurs.getJoueursPartie().toString());
		assertEquals(2,ListeJoueurs.nombreJoueursPartie());
		assertEquals("Vincent",ListeJoueurs.getCreateurPartie());

	}

	@Test
	public void ajouterJoueurPartieFail(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		assertThrows(UnsupportedOperationException.class, () -> {
			ListeJoueurs.ajouteJoueurPartie("Katya");
        });
		assertEquals("[]",ListeJoueurs.getJoueursPartie().toString());
		assertEquals(0,ListeJoueurs.nombreJoueursPartie());
	}

	@Test
	public void supprimerJoueursPartieTest(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		ListeJoueurs.ajouteJoueurPartie("Vincent");
		ListeJoueurs.supprimerJoueursPartie();
		assertEquals("[]",ListeJoueurs.getJoueursPartie().toString());
		assertEquals(0,ListeJoueurs.nombreJoueursPartie());
		assertEquals("",ListeJoueurs.getCreateurPartie());
	}

	@Test
	public void retirerCreateurPartie(){
		ListeJoueurs.ajouterJoueurConnecte("Vincent");
		ListeJoueurs.ajouteJoueurPartie("Vincent");
		ListeJoueurs.ajouterJoueurConnecte("Katya");
		ListeJoueurs.ajouteJoueurPartie("Katya");
		ListeJoueurs.retirerJoueur("Vincent");
		assertEquals("[Katya]",ListeJoueurs.getJoueursConnectes().toString());
		assertEquals(1,ListeJoueurs.nombreJoueursConnectes());
		assertEquals("[]",ListeJoueurs.getJoueursPartie().toString());
		assertEquals(0,ListeJoueurs.nombreJoueursPartie());
		assertEquals("",ListeJoueurs.getCreateurPartie());
	}
}
