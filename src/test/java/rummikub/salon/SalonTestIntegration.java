package rummikub.salon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import static java.util.concurrent.TimeUnit.SECONDS;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SalonTestIntegration {

	private static WebSocketStompClient stompClient;
	private static final String URL = "http://localhost:8080/websocket";
	private CompletableFuture<MessageChat> completableFuture;

	@BeforeAll
	public static void initialisation() throws Exception {
		List<Transport> transports = new ArrayList<>(2);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		transports.add(new RestTemplateXhrTransport());
		WebSocketClient webSocketClient = new SockJsClient(transports);

		stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
	}

	@Test
	public void connexionTest() throws InterruptedException, ExecutionException, TimeoutException {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.CONNEXION, "Vincent", "");
		completableFuture = new CompletableFuture<>();
		StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);
        stompSession.subscribe("/user/queue/canalPersonel", new MessageStompFrameHandler());
        stompSession.send("/salon/ajouterJoueurConnecte", messageEnvoye);
        MessageChat messageRecu = completableFuture.get(10, SECONDS);
        testContenuMessage(messageRecu,MessageChat.TypeMessage.CONNEXION, "Vincent", "");

	}

	@Test
	public void deconnexionTest() throws InterruptedException, ExecutionException, TimeoutException {
		MessageChat messageEnvoye = new MessageChat(MessageChat.TypeMessage.CONNEXION, "Vincent", "");
		completableFuture = new CompletableFuture<>();
		StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);
        stompSession.subscribe("/user/queue/canalPersonel", new MessageStompFrameHandler());
        stompSession.send("/salon/ajouterJoueurConnecte", messageEnvoye);
		stompSession.disconnect();
		assertFalse(stompSession.isConnected());

	}

	private void testContenuMessage(MessageChat message, MessageChat.TypeMessage type, String joueur, String texteMessage){
		assertEquals(type, message.getTypeMessage());
		assertEquals(joueur, message.getJoueur());
		assertEquals(texteMessage, message.getMessage());
	}

	private class MessageStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            System.out.println(stompHeaders.toString());
            return MessageChat.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((MessageChat) o);
        }
    }
}


