package rummikub.salon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import static org.mockito.Mockito.*;

public class EvenementsWebSocketTest {

	private EvenementsWebSocket controleur;
	private SessionDisconnectEvent evenement = mock(SessionDisconnectEvent.class);

	@BeforeEach
	public void initialisation() {
		controleur = new EvenementsWebSocket();
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

	//@Test
    public void deconnexionWebSocketTest() {
		controleur.deconnexionWebSocket(evenement);
    }
}

