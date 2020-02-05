package rummikub.securite;

import rummikub.joueurs.JoueurConnecte;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceJwtTest {

	@Test
	public void creerTokenTest() {
		JoueurConnecte joueur = new JoueurConnecte(1,"Vincent",1);
		ServiceJwt serviceJwt = new ServiceJwt();
		String token = serviceJwt.creerToken(joueur);
		JoueurConnecte joueur2 = serviceJwt.parseToken(token);
		assertEquals(joueur, joueur2);
	}

	@Test
	public void parseTokenFail() {
		ServiceJwt serviceJwt = new ServiceJwt();
		JoueurConnecte joueur2 = serviceJwt.parseToken("hbG.ciOi.JIU");
		assertNull(joueur2);
	}
}
