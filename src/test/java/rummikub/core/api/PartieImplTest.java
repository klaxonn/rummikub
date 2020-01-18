package rummikub.core.api;

import rummikub.core.jeu.Pioche;
import rummikub.core.jeu.Joueur;
import rummikub.core.jeu.commands.Historique;
import rummikub.core.plateau.Plateau;
import rummikub.core.plateau.PlateauImpl;
import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.JetonNormal;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartieImplTest {

	private Partie partie;
    private List<Jeton> jetonsPiochesJoueur1;
    private List<Jeton> jetonsPiochesJoueur2;
	private Pioche piocheMock;
	private Plateau plateauMock;
	private static final int JOUEUR1 = 1;
	private static final int JOUEUR2 = 2;
	private static final int JOUEUR_INCONNU =3;

	@BeforeEach
    private void initialisation() {
		List<Joueur> listejoueurs = initialiserJoueurs();
		jetonsPiochesJoueur1 = initialiserJoueur1();
		jetonsPiochesJoueur2 = initialiserJoueur2();
		piocheMock = mock(Pioche.class);
		plateauMock = mock(PlateauImpl.class);
		Historique historique = new Historique();
		partie = new PartieImpl(listejoueurs, piocheMock, plateauMock, historique);

    }

	private List<Joueur> initialiserJoueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		joueurs.add(new Joueur("Vincent"));
		joueurs.add(new Joueur("Katya"));
		return joueurs;
	}

	private List<Jeton> initialiserJoueur1() {
		List<Jeton> jetons = new ArrayList<>();
		jetons.add(new JetonNormal(10, Couleur.BLEU));
        jetons.add(new JetonNormal(11, Couleur.BLEU));
        jetons.add(new JetonNormal(12, Couleur.BLEU));
        jetons.add(new JetonNormal(13, Couleur.BLEU));
		return jetons;
	}

	private List<Jeton> initialiserJoueur2() {
		List<Jeton> jetons = new ArrayList<>();
        jetons.add(new JetonNormal(4, Couleur.JAUNE));
        jetons.add(new JetonNormal(9, Couleur.BLEU));
		return jetons;
	}

	@Test
    public void creerPartieVide() {
		when(plateauMock.toString()).thenReturn("");
		Historique historique = new Historique();
		Partie partie = new PartieImpl(new ArrayList<>(), piocheMock, plateauMock, historique);
		assertEquals("", partie.afficherJoueursPartie());
    }

	@Test
    public void creerPartieTropdeJoueurs() {
		when(plateauMock.toString()).thenReturn("");
		List<Joueur> listejoueurs = new ArrayList<>();
		for(int i=1; i<= Partie.NOMBRE_MAX_JOUEURS_PARTIE + 1; i++) {
			listejoueurs.add(new Joueur("Joueur-"+i));
		}
		Historique historique = new Historique();
		assertThrows(IllegalArgumentException.class, () -> {
			new PartieImpl(listejoueurs, piocheMock, plateauMock, historique);
        });
    }

	@Test
    public void avantCommencerPartieTest() {
		when(plateauMock.toString()).thenReturn("");
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", "", "Partie non commencée");
		MessagePartie message = partie.afficherPartie(JOUEUR1);
		MessagePartie message2 = partie.remplacerJoker(JOUEUR1, new ArrayList<Integer>());
		MessagePartie message3 = partie.annulerDerniereAction(JOUEUR1);
		MessagePartie message4 = partie.terminerTour(JOUEUR1);
		assertEquals(messageTest, message);
		assertEquals(messageTest, message2);
		assertEquals(messageTest, message3);
		assertEquals(messageTest, message4);
		assertFalse(partie.isPartieCommence());
		assertEquals("\"Vincent\", \"Katya\"", partie.afficherJoueursPartie());
    }

    @Test
    public void ajouterJoueurTest() {
		Joueur joueur3 = new Joueur("Bob");
		MessagePartie message = partie.ajouterJoueur(joueur3);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.AJOUTER_JOUEUR,
			0, 3, "Bob", "", "", "");
		assertEquals(messageTest, message);
		assertEquals("\"Vincent\", \"Katya\", \"Bob\"", partie.afficherJoueursPartie());
	}

    @Test
    public void ajouterTropJoueurFail() {
		for(int i=1; i<= Partie.NOMBRE_MAX_JOUEURS_PARTIE - 2; i++) {
			partie.ajouterJoueur(new Joueur("Joueur-"+i));
		}
        MessagePartie message = partie.ajouterJoueur(new Joueur("Joueur"));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", "", "Partie Pleine");
		assertEquals(messageTest, message);
	}

    @Test
    public void ajouterJoueurPartieCommenceFail() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
        MessagePartie message = partie.ajouterJoueur(new Joueur("Bob1"));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", "", "Partie déjà commencée");
		assertEquals(messageTest, message);
	}

	@Test
    public void commencerPartieTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		MessagePartie message = partie.commencerPartie();
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
			0, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", "", "");
		assertEquals(messageTest, message);
		assertEquals(1,partie.getIndexJoueurCourant());
		assertTrue(partie.isPartieCommence());
        message = partie.commencerPartie();
		messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", "", "Partie déjà commencée");
		assertEquals(messageTest, message);
	}

	@Test
    public void commencerPartiePasAssezJoueur() {
		when(plateauMock.toString()).thenReturn("");
		List<Joueur> listejoueurs = new ArrayList<>();
		listejoueurs.add(new Joueur("Vincent"));
		Historique historique = new Historique();
		partie = new PartieImpl(listejoueurs, piocheMock, plateauMock, historique);
        MessagePartie message = partie.commencerPartie();
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", "", "Nombre de joueurs insuffisant");
		assertEquals(messageTest, message);
    }

    @Test
    public void afficherPartieTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.afficherPartie(JOUEUR1);
		assertEquals(MessagePartie.TypeMessage.AFFICHER_PARTIE,message.getTypeMessage());
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.AFFICHER_PARTIE,
			0, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", "", "");
		assertEquals(messageTest, message);
    }

    @Test
    public void afficherPartieJoueurInconnuTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.afficherPartie(JOUEUR_INCONNU);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", "", "Joueur inexistant");
		assertEquals(messageTest, message);

    }

	@Test
    public void creerNouvelleSequenceTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		partie.commencerPartie();
		MessagePartie message = partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 1, "Vincent", "13bleu", "10bleu 11bleu 12bleu", "");
		assertEquals(messageTest, message);
    }

    @Test
    public void creerNouvelleSequenceTestMauvaisJoueur() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.creerNouvelleSequence(JOUEUR2, Arrays.asList(1,2));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 2, "Katya", "4jaune 9bleu", "", "Ce n'est pas votre tour");
		assertEquals(messageTest, message);
    }

	@Test
    public void finDeTourSansPiocheTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		MessagePartie message = partie.terminerTour(JOUEUR1);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
			0, 2, "Katya", "4jaune 9bleu", "10bleu 11bleu 12bleu", "");
		assertEquals(messageTest, message);
    }

    @Test
    public void finDeTourMauvaisJoueur() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.terminerTour(JOUEUR2);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 2, "Katya", "4jaune 9bleu", "", "Ce n'est pas votre tour");
		assertEquals(messageTest, message);
    }

	@Test
    public void ajouterJetonTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu\n4jaune");
		when(plateauMock.isValide()).thenReturn(true).thenReturn(false);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		MessagePartie message = partie.ajouterJeton(JOUEUR2, Arrays.asList(1,2));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "9bleu", "10bleu 11bleu 12bleu\n4jaune", "");
		assertEquals(messageTest, message);
    }

	@Test
    public void couperSequenceTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu\n11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		MessagePartie message = partie.couperSequence(JOUEUR2, Arrays.asList(1,2));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", "10bleu\n11bleu 12bleu", "");
		assertEquals(messageTest, message);
    }

	@Test
    public void deplacerJetonTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu\n12bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		MessagePartie message = partie.deplacerJeton(JOUEUR2, Arrays.asList(1,3,2));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", "10bleu 11bleu\n12bleu", "");
		assertEquals(messageTest, message);
    }

	@Test
    public void annulerActionTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		partie.deplacerJeton(JOUEUR2, Arrays.asList(1,3,2));
		MessagePartie message = partie.annulerDerniereAction(JOUEUR2);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", "10bleu 11bleu 12bleu", "");
		assertEquals(messageTest, message);
    }

    @Test
    public void annulerActionTestMauvaisJoueur() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		MessagePartie message = partie.annulerDerniereAction(JOUEUR2);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 2, "Katya", "4jaune 9bleu", "10bleu 11bleu 12bleu", "Ce n'est pas votre tour");
		assertEquals(messageTest, message);
    }

	@Test
    public void fusionnerSequenceTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		partie.deplacerJeton(JOUEUR2, Arrays.asList(1,3,2));
		MessagePartie message = partie.fusionnerSequence(JOUEUR2, Arrays.asList(1,4));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", "10bleu 11bleu 12bleu", "");
		assertEquals(messageTest, message);
    }

	@Test
    public void remplacerJokerFailTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		MessagePartie message = partie.remplacerJoker(JOUEUR2, Arrays.asList(3,1));
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 2, "Katya", "4jaune 9bleu", "10bleu 11bleu 12bleu", "Index jeton hors limite");
		assertEquals(messageTest, message);
    }

	@Test
    public void finDeTourInvalideTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu\n4jaune");
		when(plateauMock.isValide()).thenReturn(true).thenReturn(false);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		partie.ajouterJeton(JOUEUR2, Arrays.asList(1,2));
		MessagePartie message = partie.terminerTour(JOUEUR2);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 2, "Katya", "9bleu", "10bleu 11bleu 12bleu\n4jaune", "plateau non valide");
		assertEquals(messageTest, message);
    }

	@Test
    public void finDeTourPasAssezDePointsTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("9bleu 10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		partie.ajouterJeton(JOUEUR2, Arrays.asList(2,1));
		MessagePartie message = partie.terminerTour(JOUEUR2);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 2, "Katya", "4jaune", "9bleu 10bleu 11bleu 12bleu", "21 points restants nécessaires");
		assertEquals(messageTest, message);
    }

	@Test
    public void finDeTourAvecPiocheTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(piocheMock.piocher1Jeton()).thenReturn(new JetonNormal(10, Couleur.BLEU));
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		MessagePartie message = partie.terminerTour(JOUEUR2);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
			0, 1, "Vincent", "13bleu", "10bleu 11bleu 12bleu", "");
		assertEquals(messageTest, message);
    }

	@Test
    public void finDeTourAvecPiocheVideTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(piocheMock.piocher1Jeton()).thenThrow(UnsupportedOperationException.class);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		when(plateauMock.isValide()).thenReturn(true).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		partie.terminerTour(JOUEUR1);
		MessagePartie message = partie.terminerTour(JOUEUR2);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
			0, 1, "Vincent", "13bleu", "10bleu 11bleu 12bleu", "");
		assertEquals(messageTest, message);
    }

	@Test
    public void finDePartieTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu 13bleu");
		when(plateauMock.isValide()).thenReturn(true);
		partie.commencerPartie();
		partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3,4));
		MessagePartie message = partie.terminerTour(JOUEUR1);
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.FIN_DE_PARTIE,
			0, 1, "Vincent", "", "10bleu 11bleu 12bleu 13bleu", "");
    }
}
