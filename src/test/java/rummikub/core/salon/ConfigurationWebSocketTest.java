package rummikub.salon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import java.lang.reflect.Type;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import static java.util.Arrays.asList;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Disabled("Désactivé car trop lent")
public class ConfigurationWebSocketTest {

    static final String URL = "ws://localhost:8080/websocket/";
    static final String WEBSOCKET_TOPIC = "/connexionJoueur";

    private StompSession session;
	private StompSessionHandler sessionHandler;

    @BeforeEach
    public void initialisation() throws Exception {
		WebSocketClient client = new SockJsClient(asList(new WebSocketTransport(new StandardWebSocketClient())));
        WebSocketStompClient clientStomp = new WebSocketStompClient(client);
		sessionHandler = new MyStompSessionHandler();
		session = clientStomp.connect(URL, sessionHandler).get();
    }

    @Test
    public void isConnecteTest() {
		assertTrue(session.isConnected());
		session.disconnect();
    }

    @Test
    public void messageEnvoyeOK() throws Exception {
        session.subscribe(WEBSOCKET_TOPIC, sessionHandler);
		MessageChat message = nouveauMessage(MessageChat.TypeMessage.CONNEXION, "Vincent","");
		assertNotNull(session.send("/salon/envoyerMessageChat", message.toString().getBytes()));
		session.disconnect();
    }

	private MessageChat nouveauMessage(MessageChat.TypeMessage type, String joueur, String texteMessage){
		MessageChat message = new MessageChat();
		message.setTypeMessage(type);
		message.setMessage(texteMessage);
		message.setJoueur(joueur);
		return message;
	}

	public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    	@Override
    	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    	}

    	@Override
    	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    	}

    	@Override
    	public Type getPayloadType(StompHeaders headers) {
        	return MessageChat.class;
    	}

    	@Override
    	public void handleFrame(StompHeaders headers, Object payload) {
    	}
    }
}
