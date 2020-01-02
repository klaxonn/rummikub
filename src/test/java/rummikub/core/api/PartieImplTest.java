package rummikub.core.api;

import rummikub.core.jeu.Pioche;
import rummikub.core.plateau.Plateau;
import rummikub.core.jeu.commands.Historique;
import rummikub.core.pieces.Couleur;
import rummikub.core.pieces.Jeton;
import rummikub.core.pieces.JetonNormal;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartieImplTest {

	private Partie partie;
    private List<Jeton> jetonsPiochesJoueur1;
    private List<Jeton> jetonsPiochesJoueur2;
	private Pioche piocheMock;
	private Plateau plateauMock;
	private Historique historique;

	@BeforeEach
    private void initialisation() {
		Set<String> listejoueurs = initialiserJoueurs();
		jetonsPiochesJoueur1 = initialiserJoueur1();
		jetonsPiochesJoueur2 = initialiserJoueur2();
		piocheMock = mock(Pioche.class);
		plateauMock = mock(Plateau.class);
		historique = new Historique();
		partie = new PartieImpl(listejoueurs, piocheMock, plateauMock, historique);

    }

	private Set<String> initialiserJoueurs() {
		Set<String> joueurs = new HashSet<>();
		joueurs.add("Vincent");
		joueurs.add("Katya");
		return joueurs;
	}

	private List<Jeton> initialiserJoueur1() {
		List<Jeton> jetons = new ArrayList<>();
		jetons.add(new JetonNormal(6, Couleur.BLEU));
        jetons.add(new JetonNormal(2, Couleur.ROUGE));
		return jetons;
	}

	private List<Jeton> initialiserJoueur2() {
		List<Jeton> jetons = new ArrayList<>();
        jetons.add(new JetonNormal(4, Couleur.JAUNE));
        jetons.add(new JetonNormal(10, Couleur.VERT));
		return jetons;
	}

	@Test
    public void commencerPartie() {
		when(piocheMock.piocheInitiale()).thenReturn(jetonsPiochesJoueur1)
										 .thenReturn(jetonsPiochesJoueur2);
		when(plateauMock.toString()).thenReturn("");
		MessagePartie message = partie.commencerPartie();
		assertEquals(MessagePartie.TypeMessage.DEBUT_PARTIE,message.getTypeMessage());
		assertEquals("Vincent,Katya",message.getNomJoueur());
		assertEquals("6bleu 2rouge,4jaune 10vert",message.getJeuJoueur());
		assertEquals("",message.getPlateau());
		assertEquals("",message.getMessageErreur());
    }
}
