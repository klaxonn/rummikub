package rummikub.controleurs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import rummikub.securite.JoueurConnecte;

public class ListeJoueursTest {

	private ListeJoueurs listeJoueurs;

	@BeforeEach
	public void initialisation() {
		listeJoueurs = new ListeJoueurs();
	}

	@Test
	public void retirerJoueurTest() {
		JoueurConnecte joueur = new JoueurConnecte(1, "Vincent", 1);
		listeJoueurs.ajouterJoueur(joueur);
		JoueurConnecte joueur2 = listeJoueurs.retirerJoueur(1,1);
		assertSame(joueur, joueur2);
	}

	@Test
	public void retirerJoueurFail() {
		JoueurConnecte joueur = new JoueurConnecte(1, "Vincent", 1);
		listeJoueurs.ajouterJoueur(joueur);
		JoueurConnecte joueur2 = listeJoueurs.retirerJoueur(1,2);
		assertNull(joueur2);
	}
}
