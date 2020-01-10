package rummikub.salon;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * Configuration Web Socket.
 */
@Configuration
@EnableWebSocketMessageBroker
public class ConfigurationWebSocket implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
				.setAllowedOrigins("*")
				.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/salon");
        registry.enableSimpleBroker("/queue/", "/topic");
    }
}

