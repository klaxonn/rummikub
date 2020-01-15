package rummikub.salon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ListeJoueursTest {

	private ListeJoueurs listeJoueurs;

	@BeforeEach
	public void initialisation() {
		listeJoueurs = new ListeJoueurs();
	}

    @Test
	public void ajouterJoueurConnecteTest(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		assertEquals("[Vincent]",listeJoueurs.getJoueursConnectes().toString());
		assertEquals(1,listeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void ajouterJoueurConnecteMemeNomTest(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		assertEquals("[Vincent, Vincent-1]",listeJoueurs.getJoueursConnectes().toString());
		assertEquals(2,listeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void retirerJoueurTest(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		listeJoueurs.retirerJoueur("Vincent");
		assertEquals("[]",listeJoueurs.getJoueursConnectes().toString());
		assertEquals(0,listeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void retirerJoueurTestFail(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		assertThrows(UnsupportedOperationException.class, () -> {
            listeJoueurs.retirerJoueur("Katya");
        });
		assertEquals("[Vincent]",listeJoueurs.getJoueursConnectes().toString());
		assertEquals(1,listeJoueurs.nombreJoueursConnectes());
	}

	@Test
	public void ajouterJoueurPartie(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		listeJoueurs.ajouterJoueurPartie("Vincent");
		listeJoueurs.ajouterJoueurConnecte("Katya");
		listeJoueurs.ajouterJoueurPartie("Katya");
		assertEquals("[Vincent, Katya]",listeJoueurs.getJoueursConnectes().toString());
		assertEquals(2,listeJoueurs.nombreJoueursConnectes());
		assertEquals("[Vincent, Katya]",listeJoueurs.getJoueursPartie().toString());
		assertEquals(2,listeJoueurs.nombreJoueursPartie());
		assertEquals("Vincent",listeJoueurs.getCreateurPartie());

	}

	@Test
	public void ajouterJoueurPartieFail(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		assertThrows(UnsupportedOperationException.class, () -> {
			listeJoueurs.ajouterJoueurPartie("Katya");
        });
		assertEquals("[]",listeJoueurs.getJoueursPartie().toString());
		assertEquals(0,listeJoueurs.nombreJoueursPartie());
	}

	@Test
	public void supprimerJoueursPartieTest(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		listeJoueurs.ajouterJoueurPartie("Vincent");
		listeJoueurs.supprimerJoueursPartie();
		assertEquals("[]",listeJoueurs.getJoueursPartie().toString());
		assertEquals(0,listeJoueurs.nombreJoueursPartie());
		assertEquals("",listeJoueurs.getCreateurPartie());
	}

	@Test
	public void retirerCreateurPartie(){
		listeJoueurs.ajouterJoueurConnecte("Vincent");
		listeJoueurs.ajouterJoueurPartie("Vincent");
		listeJoueurs.ajouterJoueurConnecte("Katya");
		listeJoueurs.ajouterJoueurPartie("Katya");
		listeJoueurs.retirerJoueur("Vincent");
		assertEquals("[Katya]",listeJoueurs.getJoueursConnectes().toString());
		assertEquals(1,listeJoueurs.nombreJoueursConnectes());
		assertEquals("[]",listeJoueurs.getJoueursPartie().toString());
		assertEquals(0,listeJoueurs.nombreJoueursPartie());
		assertEquals("",listeJoueurs.getCreateurPartie());
	}
}
