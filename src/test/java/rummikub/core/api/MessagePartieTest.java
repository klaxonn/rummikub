package rummikub.core.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessagePartieTest {

	@Test
    public void egaliteMessagesTest() {
		MessagePartie message1 = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
									"Vincent", "10bleu 11bleu", "13bleu", "Rien");
		MessagePartie message2 = new MessagePartie();
		message2.setTypeMessage(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR);
		message2.setNomJoueur("Vincent");
		message2.setJeuJoueur("10bleu 11bleu");
		message2.setPlateau("13bleu");
		message2.setMessageErreur("Rien");
		assertEquals(message1,message2);
    }
}
