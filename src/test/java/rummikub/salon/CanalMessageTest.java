package rummikub.salon;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.AbstractSubscribableChannel;

import java.util.ArrayList;
import java.util.List;

public class CanalMessageTest extends AbstractSubscribableChannel {

	private static List<Message<?>> messages = new ArrayList<>();

	public static List<Message<?>> getMessages() {
		return messages;
	}

	public static void effacerMessages() {
		messages.clear();
	}

	@Override
	protected boolean sendInternal(Message<?> message, long timeout) {
		messages.add(message);
		return true;
	}
}
