package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.EntityModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.skyscreamer.jsonassert.JSONAssert;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControleursTestIntegration {
	
	@LocalServerPort
    private int port;
    
    private static final String ADRESSE = "http://localhost:";
    private TestRestTemplate template = new TestRestTemplate();
    
    @Test
	public void creerPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.AJOUTER_JOUEUR,
			1, 1, "Vincent", "", 0, "", "");

		ResponseEntity<EntityModel> reponse = template.postForEntity(
		creerUrl("/creerPartie"), "Vincent", EntityModel.class);
       
		assertEquals(HttpStatus.CREATED, reponse.getStatusCode());

		MessagePartie messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		messageTest.setJeuJoueur(messageReponse.getJeuJoueur());
		assertEquals(messageTest, messageReponse);
	}
	
	@Test
	public void demarrerPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
			1, 0, "", "",  1, "", "");

		template.postForEntity(creerUrl("/creerPartie"), "Vincent", EntityModel.class);
		template.postForEntity(creerUrl("/1/ajouterJoueur"), "Katya", EntityModel.class);
		ResponseEntity<EntityModel> reponse = template.postForEntity(
		  creerUrl("/1/demarrerPartie"), null, EntityModel.class);
       
		assertEquals(HttpStatus.OK, reponse.getStatusCode());

		MessagePartie messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		assertEquals(messageTest, messageReponse);
	}
	
	private String creerUrl(String uri) {
        return ADRESSE + port + uri;
    }
    
    private MessagePartie creerMessageReponse(Map valeurs) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(Enum.valueOf(MessagePartie.TypeMessage.class,(String) valeurs.get("typeMessage")));
		message.setIdPartie((int) valeurs.get("idPartie"));
		message.setIdJoueur((int) valeurs.get("idJoueur"));
		message.setNomJoueur((String) valeurs.get("nomJoueur"));
		message.setJeuJoueur((String) valeurs.get("jeuJoueur"));
		message.setIdJoueurCourant((int) valeurs.get("idJoueurCourant"));
		message.setPlateau((String) valeurs.get("plateau"));
		message.setMessageErreur((String) valeurs.get("messageErreur"));
		return message;
	}
}
