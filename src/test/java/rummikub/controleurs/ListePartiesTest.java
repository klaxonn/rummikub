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
		assertNotEquals(null, partie1);
		assertNotEquals(null, partie1);
		assertEquals(null, listeParties.getPartie(3));
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
		listeParties.getPartie(idPartie1).commencerPartie();
		listePartiesDispos.remove(0);
		assertEquals(listePartiesDispos, listeParties.listerPartiesDispos());
	}
}
