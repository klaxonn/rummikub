package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static rummikub.core.api.MessagePartie.TypeMessage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.hateoas.EntityModel;
import org.springframework.util.ResourceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import javax.net.ssl.SSLContext;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;

//@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControleursTestIntegration {

	@LocalServerPort
    private int port;

    @Value("${server.ssl.key-store-password}")
	private String mdp;

	@Value("${server.ssl.key-store}")
	private String keyStore;

    private static final String ADRESSE = "https://localhost:";
    private TestRestTemplate template;

    @BeforeEach
    public void init() throws Exception{
		SSLContext sslContext = SSLContextBuilder
								.create()
								.loadTrustMaterial(ResourceUtils.getFile(keyStore), mdp.toCharArray())
								.build();
		HttpClient client = HttpClients.custom()
                                .setSSLContext(sslContext)
                                .build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(client);
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		restTemplateBuilder.requestFactory(() -> {return requestFactory;});
		template = new TestRestTemplate(restTemplateBuilder);
	}

    @Test
	public void creerPartieTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("Vincent", headers);
		ResponseEntity<EntityModel> reponse = template.postForEntity(
		creerUrl("/0/creerPartie"), entity, EntityModel.class);

		assertEquals(HttpStatus.CREATED, reponse.getStatusCode());

		MessagePartie messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		int idPartie = messageReponse.getIdPartie();
		String tokenJoueur1 = messageReponse.getToken();
		MessagePartie messageTest = new MessagePartie(AJOUTER_JOUEUR,
			idPartie, 1, "Vincent", "", 0, "", "");
		messageTest.setToken(tokenJoueur1);

		messageTest.setJeuJoueur(messageReponse.getJeuJoueur());
		assertEquals(messageTest, messageReponse);
	}

	@Test
	public void ajouterJoueurTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("Vincent", headers);
		ResponseEntity<EntityModel> reponse = template.postForEntity(
		  creerUrl("/0/creerPartie"), entity, EntityModel.class);
		MessagePartie messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		int idPartie = messageReponse.getIdPartie();

		entity = new HttpEntity<String>("Katya", headers);
		reponse = template.postForEntity(
		  creerUrl("/"+idPartie+"/ajouterJoueur"), entity, EntityModel.class);

		assertEquals(HttpStatus.CREATED, reponse.getStatusCode());

		messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		String tokenJoueur2 = messageReponse.getToken();
		MessagePartie messageTest = new MessagePartie(AJOUTER_JOUEUR,
			idPartie, 2, "Katya", "", 0, "", "");
		messageTest.setToken(tokenJoueur2);
		messageTest.setJeuJoueur(messageReponse.getJeuJoueur());
		assertEquals(messageTest, messageReponse);
	}

	@Test
	public void demarrerPartieTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("Vincent", headers);
		ResponseEntity<EntityModel> reponse = template.postForEntity(
		  creerUrl("/0/creerPartie"), entity, EntityModel.class);
		MessagePartie messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		int idPartie = messageReponse.getIdPartie();
		String tokenJoueur1 = messageReponse.getToken();

		entity = new HttpEntity<String>("Katya", headers);
		template.postForEntity(creerUrl("/"+idPartie+"/ajouterJoueur"), entity, EntityModel.class);
		headers.set(HttpHeaders.AUTHORIZATION, tokenJoueur1);
		entity = new HttpEntity<String>("", headers);
		reponse = template.postForEntity(creerUrl("/"+idPartie+"/1/demarrerPartie"), entity, EntityModel.class);

		assertEquals(HttpStatus.OK, reponse.getStatusCode());

		messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		MessagePartie messageTest = new MessagePartie(DEBUT_NOUVEAU_TOUR,
			idPartie, 0, "", "",  1, "", "");
		assertEquals(messageTest, messageReponse);
	}

	@Test
	public void creerSequenceTest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("Vincent", headers);
		ResponseEntity<EntityModel> reponse = template.postForEntity(
		  creerUrl("/0/creerPartie"), entity, EntityModel.class);
		MessagePartie messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		int idPartie = messageReponse.getIdPartie();
		String tokenJoueur1 = messageReponse.getToken();

		entity = new HttpEntity<String>("Katya", headers);
		template.postForEntity(creerUrl("/"+idPartie+"/ajouterJoueur"), entity, EntityModel.class);
		headers.set(HttpHeaders.AUTHORIZATION, tokenJoueur1);
		entity = new HttpEntity<String>("", headers);
		reponse = template.postForEntity(creerUrl("/"+idPartie+"/1/demarrerPartie"), entity, EntityModel.class);

		HttpEntity<List> entity2 = new HttpEntity<>(Arrays.asList(1), headers);
		reponse = template.postForEntity(creerUrl("/"+idPartie+"/1/creerSequence"), entity2, EntityModel.class);
		assertEquals(HttpStatus.OK, reponse.getStatusCode());

		messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		MessagePartie messageTest = new MessagePartie(RESULTAT_ACTION,
			idPartie, 1, "Vincent", "", 1, "", "");
		messageTest.setJeuJoueur(messageReponse.getJeuJoueur());
		messageTest.setPlateau(messageReponse.getPlateau());
		assertEquals(messageTest, messageReponse);
	}

	@Test
	public void terminerTourMauvaisJoueurFail() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("Vincent", headers);
		ResponseEntity<EntityModel> reponse = template.postForEntity(
		  creerUrl("/0/creerPartie"), entity, EntityModel.class);
		MessagePartie messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		int idPartie = messageReponse.getIdPartie();
		String tokenJoueur1 = messageReponse.getToken();

		entity = new HttpEntity<String>("Katya", headers);
		reponse = template.postForEntity(creerUrl("/"+idPartie+"/ajouterJoueur"), entity, EntityModel.class);
		messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		String tokenJoueur2 = messageReponse.getToken();

		headers.set(HttpHeaders.AUTHORIZATION, tokenJoueur1);
		entity = new HttpEntity<String>("", headers);
		reponse = template.postForEntity(creerUrl("/"+idPartie+"/1/demarrerPartie"), entity, EntityModel.class);

		headers.set(HttpHeaders.AUTHORIZATION, tokenJoueur2);
		entity = new HttpEntity<>("", headers);
		reponse = template.postForEntity(creerUrl("/"+idPartie+"/1/terminerTour"), entity, EntityModel.class);
		assertEquals(HttpStatus.UNAUTHORIZED, reponse.getStatusCode());

		messageReponse = creerMessageReponse((Map) reponse.getBody().getContent());
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Opération non autorisée");
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
		message.setToken((String) valeurs.get("token"));
		return message;
	}
}
