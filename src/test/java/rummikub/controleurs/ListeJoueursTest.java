package rummikub.controleurs;

import rummikub.securite.JoueurConnecte;
import static rummikub.core.api.MessagePartie.TypeMessage.*;
import rummikub.securite.ServiceJwt;
import rummikub.core.api.MessagePartie;
import rummikub.core.api.Partie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListeJoueursTest {

	private ListeJoueurs listeJoueurs;
	private ServiceJwt serviceJwtMock;
	private ListeParties listePartiesMock;

	private static final String ADRESSE_IP_1 = "192.168.1.1";
	private static final String ADRESSE_IP_2 = "192.168.1.2";

	@BeforeEach
	public void initialisation() {
		serviceJwtMock = mock(ServiceJwt.class);
		listePartiesMock = mock(ListeParties.class);
		listeJoueurs = new ListeJoueurs(listePartiesMock, serviceJwtMock);
	}

	@Test
	public void ajouterJoueurTest() {
		MessagePartie messageTest = new MessagePartie(AJOUTER_JOUEUR,
			0, 1, "Vincent", "4jaune 9bleu", 0, "", "");
		Partie partieMock = mock(Partie.class);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(new JoueurConnecte("Vincent")))
		   .thenReturn(messageTest);
		when(serviceJwtMock.creerToken(new JoueurConnecte(1,"Vincent",1,ADRESSE_IP_1)))
		  .thenReturn("aa");

		MessagePartie message = listeJoueurs.ajouterJoueur("Vincent", 1,ADRESSE_IP_1);
		messageTest.setToken("aa");
		assertEquals(messageTest, message);
	}

	@Test
	public void ajouterJoueurPartiePleineFail() {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Partie Pleine");
		Partie partieMock = mock(Partie.class);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(new JoueurConnecte("Vincent")))
		   .thenReturn(messageTest);

		MessagePartie message = listeJoueurs.ajouterJoueur("Vincent", 1, ADRESSE_IP_1);
		assertEquals(messageTest, message);
	}

	@Test
	public void ajouterJoueurNomIncorrectFail() {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Nom non valide");
		Partie partieMock = mock(Partie.class);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);

		MessagePartie message = listeJoueurs.ajouterJoueur("Vincent!!!", 1, ADRESSE_IP_1);
		assertEquals(messageTest, message);
	}

	@Test
	public void retirerJoueurTest() {
		MessagePartie messageTest = new MessagePartie(AJOUTER_JOUEUR,
			0, 1, "Vincent", "4jaune 9bleu", 0, "", "");
		Partie partieMock = mock(Partie.class);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(new JoueurConnecte("Vincent")))
		   .thenReturn(messageTest);
		when(serviceJwtMock.creerToken(new JoueurConnecte(1,"Vincent",1,ADRESSE_IP_1)))
		  .thenReturn("aa");

		listeJoueurs.ajouterJoueur("Vincent", 1, ADRESSE_IP_1);
		assertFalse(listeJoueurs.retirerJoueur(1,2));
		assertFalse(listeJoueurs.retirerJoueur(2,1));
		assertTrue(listeJoueurs.isJoueurPartie(1,1));
		assertTrue(listeJoueurs.retirerJoueur(1,1));
		assertFalse(listeJoueurs.isJoueurPartie(1,1));
		assertFalse(listeJoueurs.retirerJoueur(1,1));
	}

	@Test
	public void retirerTousJoueursTest() {
		MessagePartie messageTest = new MessagePartie(AJOUTER_JOUEUR,
			0, 1, "Vincent", "4jaune 9bleu", 0, "", "");
		Partie partieMock = mock(Partie.class);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(new JoueurConnecte("Vincent")))
		   .thenReturn(messageTest);
		when(serviceJwtMock.creerToken(new JoueurConnecte(1,"Vincent",1,ADRESSE_IP_1)))
		  .thenReturn("aa");
		listeJoueurs.ajouterJoueur("Vincent", 1, ADRESSE_IP_1);

		MessagePartie messageTest2 = new MessagePartie(AJOUTER_JOUEUR,
			0, 2, "Katya", "6jaune", 0, "", "");
		when(partieMock.ajouterJoueur(new JoueurConnecte("Katya")))
		   .thenReturn(messageTest2);
		when(serviceJwtMock.creerToken(new JoueurConnecte(2,"Katya",1,ADRESSE_IP_2)))
		  .thenReturn("aa");
		listeJoueurs.ajouterJoueur("Katya", 1, ADRESSE_IP_2);

		assertTrue(listeJoueurs.isJoueurPartie(1,1));
		assertTrue(listeJoueurs.isJoueurPartie(1,2));
		assertFalse(listeJoueurs.retirerTousJoueurs(2));
		assertTrue(listeJoueurs.retirerTousJoueurs(1));
		assertFalse(listeJoueurs.retirerTousJoueurs(1));
		assertFalse(listeJoueurs.retirerJoueur(1,1));
		assertFalse(listeJoueurs.retirerJoueur(1,2));
		assertFalse(listeJoueurs.isJoueurPartie(1,1));
		assertFalse(listeJoueurs.isJoueurPartie(1,2));

	}

	@Test
	public void isJoueurPartieTest() {
		MessagePartie messageTest = new MessagePartie(AJOUTER_JOUEUR,
			0, 1, "Vincent", "4jaune 9bleu", 0, "", "");
		Partie partieMock = mock(Partie.class);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(listePartiesMock.getPartie(2)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(new JoueurConnecte("Vincent")))
		   .thenReturn(messageTest);
		when(serviceJwtMock.creerToken(new JoueurConnecte(1,"Vincent",1,ADRESSE_IP_1)))
		  .thenReturn("aa");
		listeJoueurs.ajouterJoueur("Vincent", 1, ADRESSE_IP_1);

		MessagePartie messageTest2 = new MessagePartie(AJOUTER_JOUEUR,
			0, 1, "Katya", "6jaune", 0, "", "");
		when(partieMock.ajouterJoueur(new JoueurConnecte("Katya")))
		   .thenReturn(messageTest2);
		when(serviceJwtMock.creerToken(new JoueurConnecte(1,"Katya",2,ADRESSE_IP_2)))
		  .thenReturn("aa");
		listeJoueurs.ajouterJoueur("Katya", 2, ADRESSE_IP_2);

		assertTrue(listeJoueurs.isJoueurPartie(1,1));
		assertTrue(listeJoueurs.isJoueurPartie(2,1));
	}
}
