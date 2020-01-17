package rummikub.salon;

import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import rummikub.ihm.ControleurParties;

@Disabled
public class ControleurChatTest {

	private HandlerPerso handlerMessages;
	private CanalMessageTest canalSortie;
	private CanalMessageTest canalEntree;
	private ListeJoueurs listeJoueursMock = mock(ListeJoueurs.class);

	@BeforeEach
	public void initialisation() {
		canalEntree = new CanalMessageTest();
		canalSortie = new CanalMessageTest();
		initialisationHandler();
	}

	private void initialisationHandler() {
		ControleurParties interfaceMock = mock(ControleurParties.class);
		handlerMessages = new HandlerPerso(
				canalEntree, canalSortie, new SimpMessagingTemplate(new CanalMessageTest()));
		handlerMessages.registerHandler(new ControleurChat(listeJoueursMock, interfaceMock));
		handlerMessages.setDestinationPrefixes(Arrays.asList("/salon", "/queue"));
		handlerMessages.setMessageConverter(new MappingJackson2MessageConverter());
		handlerMessages.afterPropertiesSet();
	}

	private void testContenuMessage(MessageChat message, MessageChat.TypeMessage type, String joueur, String texteMessage){
		assertEquals(type, message.getTypeMessage());
		assertEquals(joueur, message.getJoueur());
		assertEquals(texteMessage, message.getMessage());
	}

	private MessageChat transfertMessage(MessageChat messageAEnvoyer, String destination, String reception)
		throws Exception {
		byte[] payload = new ObjectMapper().writeValueAsBytes(messageAEnvoyer);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
		headers.setDestination(destination);
		headers.setSessionId("0");
		headers.setSessionAttributes(new HashMap<>());
		Message<byte[]> message = MessageBuilder.withPayload(payload).setHeaders(headers).build();

		canalSortie.effacerMessages();
        handlerMessages.handleMessage(message);

		Message<?> reponse = canalSortie.getMessages().get(0);

		StompHeaderAccessor headersReponse = StompHeaderAccessor.wrap(reponse);
		assertEquals("0", headersReponse.getSessionId());
		assertEquals(reception, headersReponse.getDestination());
		return (MessageChat) reponse.getPayload();
	}


	@Test
    public void ajouterJoueurConnecteTest() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		String destination = "/salon/ajouterJoueurConnecte";
		String reception = "/user/0/queue/canalPersonel";

		when(listeJoueursMock.ajouterJoueurConnecte("Vincent")).thenReturn("Vincent");
		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.CONNEXION,"Vincent","");
    }

	@Test
    public void ajouterJoueurConnecteMemeNomTest() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		String destination = "/salon/ajouterJoueurConnecte";
		String reception = "/user/0/queue/canalPersonel";

		when(listeJoueursMock.ajouterJoueurConnecte("Vincent")).thenReturn("Vincent-1");
		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.CONNEXION, "Vincent-1","");
    }

    @Test
    public void ajouterJoueurConnecteNomInvalide() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.CONNEXION, "*?*","");
		String destination = "/salon/ajouterJoueurConnecte";
		String reception = "/user/0/queue/canalPersonel";

		doThrow(new UnsupportedOperationException("Le nom n'est pas valide"))
		.when(listeJoueursMock).ajouterJoueurConnecte("*?*");
		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.ERREUR, "*?*","Le nom n'est pas valide");
    }

	@Test
    public void mettreAJourJoueursConnectesTest() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.CONNEXION, "Katya","");
		String destination = "/salon/mettreAJourJoueursConnectes";
		String reception = "/topic/joueursConnectes";
		when(listeJoueursMock.getJoueursConnectes()).thenReturn(Arrays.asList("Vincent", "Katya"));

		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.CONNEXION, "Katya","[Vincent, Katya]");
    }

	@Test
    public void ajouterPremierJoueurPartieTest() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.JOINDRE_PARTIE, "Vincent","");
		String destination = "/salon/joindrePartie";
		String reception = "/user/0/queue/canalPersonel";
		when(listeJoueursMock.nombreJoueursPartie()).thenReturn(1L);

		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.CREER_PARTIE, "Vincent","");
    }


	@Test
    public void ajouterDeuxiemeJoueurPartieTest() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya","");
		String destination = "/salon/joindrePartie";
		String reception = "/user/0/queue/canalPersonel";
		when(listeJoueursMock.nombreJoueursPartie()).thenReturn(2L);

		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya","");
    }

	@Test
    public void ajouterPremierJoueurPartieTestFail() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.JOINDRE_PARTIE, "Tnecniv","");
		String destination = "/salon/joindrePartie";
		String reception = "/user/0/queue/canalPersonel";
		when(listeJoueursMock.nombreJoueursPartie()).thenReturn(1L);
		doThrow(new UnsupportedOperationException("Le nom n'est pas un joueur connecté"))
		.when(listeJoueursMock).ajouterJoueurPartie("Tnecniv");

		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.ERREUR, "Tnecniv","Le nom n'est pas un joueur connecté");
    }

	@Test
    public void mettreAJourJoueursPartieTest() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya","");
		String destination = "/salon/mettreAJourJoueursPartie";
		String reception = "/topic/joueursConnectes";
		when(listeJoueursMock.getJoueursPartie()).thenReturn(Arrays.asList("Vincent", "Katya"));

		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.JOINDRE_PARTIE, "Katya","[Vincent, Katya]");
    }

	@Test
    public void mettreAJourJoueursConnectesAvecPartieTest() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.CONNEXION, "Katya","");
		String destination = "/salon/mettreAJourJoueursConnectes";
		String reception = "/topic/joueursConnectes";
		when(listeJoueursMock.getJoueursConnectes()).thenReturn(Arrays.asList("Vincent", "Katya"));
		when(listeJoueursMock.nombreJoueursPartie()).thenReturn(1L);
		when(listeJoueursMock.getJoueursPartie()).thenReturn(Arrays.asList("Vincent"));

		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.CONNEXION, "Katya","[Vincent, Katya];[Vincent]");
    }

	@Test
    public void demarrerPartie() throws Exception {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.DEMARRER_PARTIE, "Vincent","");
		String destination = "/salon/demarrerPartie";
		String reception = "/topic/joueursPartie";
		when(listeJoueursMock.getJoueursPartie()).thenReturn(Arrays.asList("Vincent"));

		MessageChat messageRecu = transfertMessage(messageEnvoye,destination,reception);
		testContenuMessage(messageRecu,MessageChat.TypeMessage.DEMARRER_PARTIE, "Vincent","");
    }

    private static class HandlerPerso extends SimpAnnotationMethodMessageHandler {

		public HandlerPerso(SubscribableChannel canalEntree, MessageChannel canalSortie,
				SimpMessageSendingOperations brokerTemplate) {

			super(canalEntree, canalSortie, brokerTemplate);
		}

		public void registerHandler(Object handler) {
			super.detectHandlerMethods(handler);
		}
	}
}

