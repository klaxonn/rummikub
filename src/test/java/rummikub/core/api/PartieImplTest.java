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
    public void commencerPartieTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		MessagePartie message = partie.commencerPartie();
		assertEquals(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,message.getTypeMessage());
		assertEquals("Vincent",message.getNomJoueur());
		assertEquals("10bleu 11bleu 12bleu 13bleu",message.getJeuJoueur());
		assertEquals("",message.getPlateau());
		assertEquals("",message.getMessageErreur());
		assertEquals(1,partie.getIndexJoueurCourant());
    }

    @Test
    public void afficherPartieTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.afficherPartie(JOUEUR1);
		assertEquals(MessagePartie.TypeMessage.AFFICHER_PARTIE,message.getTypeMessage());
		assertEquals("Vincent",message.getNomJoueur());
		assertEquals("10bleu 11bleu 12bleu 13bleu",message.getJeuJoueur());
		assertEquals("",message.getPlateau());
		assertEquals("",message.getMessageErreur());
    }

    @Test
    public void afficherPartieJoueurInconnuTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.afficherPartie(JOUEUR_INCONNU);
		assertEquals(MessagePartie.TypeMessage.ERREUR,message.getTypeMessage());
		assertEquals("",message.getNomJoueur());
		assertEquals("",message.getJeuJoueur());
		assertEquals("",message.getPlateau());
		assertEquals("Joueur inexistant",message.getMessageErreur());
    }

	@Test
    public void creerNouvelleSequenceTest() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("10bleu 11bleu 12bleu");
		partie.commencerPartie();
		MessagePartie message = partie.creerNouvelleSequence(JOUEUR1, Arrays.asList(1,2,3));
		assertEquals(MessagePartie.TypeMessage.RESULTAT_ACTION,message.getTypeMessage());
		assertEquals("Vincent",message.getNomJoueur());
		assertEquals("13bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
    }

    @Test
    public void creerNouvelleSequenceTestMauvaisJoueur() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.creerNouvelleSequence(JOUEUR2, Arrays.asList(1,2));
		assertEquals(MessagePartie.TypeMessage.ERREUR,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("",message.getPlateau());
		assertEquals("Ce n'est pas votre tour",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
    }

    @Test
    public void finDeTourMauvaisJoueur() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		partie.commencerPartie();
		MessagePartie message = partie.terminerTour(JOUEUR2);
		assertEquals(MessagePartie.TypeMessage.ERREUR,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("",message.getPlateau());
		assertEquals("Ce n'est pas votre tour",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.RESULTAT_ACTION,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu\n4jaune",message.getPlateau());
		assertEquals("",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.RESULTAT_ACTION,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("10bleu\n11bleu 12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.RESULTAT_ACTION,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu\n12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.RESULTAT_ACTION,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.ERREUR,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("Ce n'est pas votre tour",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.RESULTAT_ACTION,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.ERREUR,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune 9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("Index jeton hors limite",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.ERREUR,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("9bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu\n4jaune",message.getPlateau());
		assertEquals("plateau non valide",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.ERREUR,message.getTypeMessage());
		assertEquals("Katya",message.getNomJoueur());
		assertEquals("4jaune",message.getJeuJoueur());
		assertEquals("9bleu 10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("21 points restants nécessaires",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,message.getTypeMessage());
		assertEquals("Vincent",message.getNomJoueur());
		assertEquals("13bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,message.getTypeMessage());
		assertEquals("Vincent",message.getNomJoueur());
		assertEquals("13bleu",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
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
		assertEquals(MessagePartie.TypeMessage.FIN_DE_PARTIE,message.getTypeMessage());
		assertEquals("Vincent",message.getNomJoueur());
		assertEquals("",message.getJeuJoueur());
		assertEquals("10bleu 11bleu 12bleu 13bleu",message.getPlateau());
		assertEquals("",message.getMessageErreur());
    }
}
