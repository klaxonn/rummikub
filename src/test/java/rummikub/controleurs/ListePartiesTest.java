package rummikub.controleurs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import rummikub.core.api.Partie;
import rummikub.core.jeu.Joueur;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListePartiesTest {

	private ListeParties listeParties;

	@BeforeEach
	public void initialisation() {
		listeParties = new ListeParties();
	}

    @Test
	public void creerPartieTest(){
		int idPartie1 = listeParties.creerPartie();
		assertEquals(1, idPartie1);
		int idPartie2 = listeParties.creerPartie();
		assertEquals(2, idPartie2);
	}

    @Test
	public void getPartieTest(){
		int idPartie1 = listeParties.creerPartie();
		int idPartie2 = listeParties.creerPartie();
		Partie partie1 = listeParties.getPartie(idPartie1);
		Partie partie2 = listeParties.getPartie(idPartie2);
		assertNotSame(partie1, partie2);
		assertNotNull(partie1);
		assertNotNull(partie1);
		assertNull(listeParties.getPartie(3));
	}

    @Test
	public void partieTermineeTest(){
		int idPartie = listeParties.creerPartie();
		Partie partie = listeParties.getPartie(idPartie);
		assertNotNull(listeParties.getPartie(idPartie));
		listeParties.setPartieTerminee(idPartie);
		partie = listeParties.getPartie(idPartie);
		assertNull(listeParties.getPartie(idPartie));
		assertTrue(listeParties.isPartieTerminee(idPartie));
	}

    @Test
	public void arreterPartieTest(){
		int idPartie = listeParties.creerPartie();
		Partie partie = listeParties.getPartie(idPartie);
		assertNotNull(listeParties.getPartie(idPartie));
		assertTrue(listeParties.arreterPartie(idPartie));
		partie = listeParties.getPartie(idPartie);
		assertNull(listeParties.getPartie(idPartie));
		assertTrue(listeParties.isPartieTerminee(idPartie));
	}

	@Test
	public void arreterPartieMauvaiseFail(){
		int idPartie = listeParties.creerPartie();
		assertFalse(listeParties.arreterPartie(-1));
	}

	@Test
	public void arreterPartieTropJoueursFail(){
		int idPartie = listeParties.creerPartie();
		Partie partie = listeParties.getPartie(idPartie);
		for(int i=1; i<= Partie.NOMBRE_MIN_JOUEURS_PARTIE + 1; i++) {
			partie.ajouterJoueur(new Joueur("Joueur"+i));
		}
		assertFalse(listeParties.arreterPartie(idPartie));
	}

    @Test
	public void listerPartiesDisposTest(){
		int idPartie1 = listeParties.creerPartie();
		int idPartie2 = listeParties.creerPartie();
		listeParties.getPartie(idPartie1).ajouterJoueur(new Joueur("Vincent"));
		listeParties.getPartie(idPartie1).ajouterJoueur(new Joueur("Kate"));
		listeParties.getPartie(idPartie2).ajouterJoueur(new Joueur("Benoit"));
		listeParties.getPartie(idPartie2).ajouterJoueur(new Joueur("Emie"));

		PartieDispo partie1 = new PartieDispo(1, Arrays.asList("Vincent", "Kate"));
		PartieDispo partie2 = new PartieDispo(2, Arrays.asList("Benoit", "Emie"));
		List<PartieDispo> listePartiesDispos = new ArrayList<>(Arrays.asList(partie1, partie2));

		assertEquals(listePartiesDispos, listeParties.listerPartiesDispos());
		listeParties.getPartie(idPartie1).commencerPartie(1);
		listePartiesDispos.remove(0);
		assertEquals(listePartiesDispos, listeParties.listerPartiesDispos());
	}
}
