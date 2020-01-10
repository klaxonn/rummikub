package rummikub.salon;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import static org.mockito.Mockito.*;

public class ControleurChatTest {

	private ControleurChat controleur;
	private SimpMessageHeaderAccessor headerMock = mock(SimpMessageHeaderAccessor.class);

	@BeforeEach
	public void initialisation() {
		controleur = new ControleurChat();
		ListeJoueurs.retirerTousJoueurs();
	}

	private MessageChat nouveauMessage(MessageChat.TypeMessage type, String joueur, String texteMessage){
		MessageChat message = new MessageChat();
		message.setTypeMessage(type);
		message.setMessage(texteMessage);
		message.setJoueur(joueur);
		return message;
	}

	private boolean testContenuMessage(MessageChat message, MessageChat.TypeMessage type, String joueur, String texteMessage){
		return message.getTypeMessage().equals(type)
				&& message.getJoueur().equals(joueur)
				&& message.getMessage().equals(texteMessage);
	}

	@Test
    public void envoyerMessageTest() {
		MessageChat messageEnvoye = nouveauMessage(MessageChat.TypeMessage.MESSAGE_CHAT, "Vincent","");
		MessageChat messageReponse = controleur.envoyerMessage(messageEnvoye);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.MESSAGE_CHAT, "Vincent",""));
    }

	@Test
    public void ajouterJoueurConnecteTest() {
		MessageChat messageEnvoye = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		MessageChat messageReponse = controleur.ajouterJoueurConnecte(messageEnvoye,headerMock);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.CONNEXION, "Vincent",""));
    }

	@Test
    public void ajouterJoueurConnecteMemeNomTest() {
		MessageChat messageEnvoye = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		controleur.ajouterJoueurConnecte(messageEnvoye,headerMock);
		MessageChat messageReponse = controleur.ajouterJoueurConnecte(messageEnvoye,headerMock);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.CONNEXION, "Vincent-1",""));
    }

	@Test
    public void mettreAJourJoueursConnectesTest() {
		MessageChat messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Katya","");
		controleur.ajouterJoueurConnecte(messageEnvoye1,headerMock);
		controleur.ajouterJoueurConnecte(messageEnvoye2,headerMock);
		MessageChat messageReponse = controleur.mettreAJourJoueursConnectes(messageEnvoye2);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.CONNEXION, "Katya","[Vincent, Katya]"));
    }

	@Test
    public void ajouterPremierJoueurPartieTest() {
		MessageChat messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		controleur.ajouterJoueurConnecte(messageEnvoye1,headerMock);
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Vincent","");
		MessageChat messageReponse = controleur.ajouterJoueurPartie(messageEnvoye2);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.CREER_PARTIE, "Vincent",""));
    }

	@Test
    public void ajouterDeuxiemeJoueurPartieTest() {
		MessageChat messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Katya","");
		controleur.ajouterJoueurConnecte(messageEnvoye1,headerMock);
		controleur.ajouterJoueurConnecte(messageEnvoye2,headerMock);
		messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Vincent","");
		controleur.ajouterJoueurPartie(messageEnvoye1);
		messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya","");
		MessageChat messageReponse = controleur.ajouterJoueurPartie(messageEnvoye2);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya",""));
    }

	@Test
    public void ajouterPremierJoueurPartieTestFail() {
		MessageChat messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		controleur.ajouterJoueurConnecte(messageEnvoye1,headerMock);
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Tnecniv","");
		MessageChat messageReponse = controleur.ajouterJoueurPartie(messageEnvoye2);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.ERREUR, "Tnecniv","Le nom n'est pas un joueur connecté"));
    }

	@Test
    public void ajouterTropDeJoueursPartieTestFail() {
		for(int i=1; i<=controleur.NOMBRE_MAX_JOUEURS_PARTIE;i++){
			String nom = "Vincent-" + i;
			MessageChat messageEnvoye = nouveauMessage(MessageChat.TypeMessage.CONNEXION, nom,"");
			controleur.ajouterJoueurConnecte(messageEnvoye,headerMock);
			MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, nom,"");
			controleur.ajouterJoueurPartie(messageEnvoye2);
		}
		MessageChat messageEnvoye = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent-5","");
		controleur.ajouterJoueurConnecte(messageEnvoye,headerMock);
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Vincent-5","");
		MessageChat messageReponse = controleur.ajouterJoueurPartie(messageEnvoye2);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.ERREUR, "Vincent-5","La partie est complète"));
    }

	@Test
    public void mettreAJourJoueursPartieTest() {
		MessageChat messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Katya","");
		controleur.ajouterJoueurConnecte(messageEnvoye1,headerMock);
		controleur.ajouterJoueurConnecte(messageEnvoye2,headerMock);
		messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Vincent","");
		messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya","");
		controleur.ajouterJoueurPartie(messageEnvoye1);
		controleur.ajouterJoueurPartie(messageEnvoye2);
		MessageChat messageReponse = controleur.mettreAJourJoueursPartie(messageEnvoye2);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya","[Vincent, Katya]"));
    }

	@Test
    public void mettreAJourJoueursConnectesAvecPartieTest() {
		MessageChat messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		controleur.ajouterJoueurConnecte(messageEnvoye1,headerMock);
		messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Vincent","");
		controleur.ajouterJoueurPartie(messageEnvoye1);
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Katya","");
		controleur.ajouterJoueurConnecte(messageEnvoye2,headerMock);
		MessageChat messageReponse = controleur.mettreAJourJoueursConnectes(messageEnvoye2);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.CONNEXION, "Katya","[Vincent, Katya];[Vincent]"));
    }

	@Test
    public void demarrerPartie() {
		MessageChat messageEnvoye1 = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		controleur.ajouterJoueurConnecte(messageEnvoye1,headerMock);
		MessageChat messageEnvoye2 = nouveauMessage(MessageChat.TypeMessage.JOINDRE_PARTIE, "Vincent","");
		controleur.ajouterJoueurPartie(messageEnvoye2);
		MessageChat messageEnvoye3 = nouveauMessage(MessageChat.TypeMessage.DEMARRER_PARTIE, "Vincent","");
		MessageChat messageReponse = controleur.demarrerPartie(messageEnvoye3);
		assertTrue(testContenuMessage(messageReponse,MessageChat.TypeMessage.DEMARRER_PARTIE, "Vincent",""));
    }

}

