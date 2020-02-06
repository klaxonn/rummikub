package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.securite.ServiceJwt;
import rummikub.securite.JoueurConnecte;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.hateoas.MediaTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.Charset;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skyscreamer.jsonassert.JSONAssert;

@Import({ ModeleControleurPartie.class,
		  ModeleControleurParties.class})
@WebMvcTest(ControleurPartie.class)
public class ControleurPartieTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ListeParties listePartiesMock;

	private Partie partieMock = mock(Partie.class);

	@MockBean
	private ServiceJwt serviceJwtMock;

	@Test
	public void afficherPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.AFFICHER_PARTIE,
			1, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 1, "", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.afficherPartie(1)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		MvcResult resultat = mockMvc.perform(get("/1/1/afficherPartie")
										.contentType("application/json")
										.header("Authorization", "Bearer aa"))
										.andDo(print())
										.andExpect(status().isOk())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void afficherPartieFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "La partie n'existe pas");
		when(listePartiesMock.getPartie(1)).thenReturn(null);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));


		MvcResult resultat = mockMvc.perform(get("/1/1/afficherPartie")
									.contentType("application/json")
									.header("Authorization", "Bearer aa"))
									.andExpect(status().isNotFound())
									.andReturn();
		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void creerNouvelleSequenceTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 1, "Vincent", "13bleu", 1, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,2,3))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		String argument = asJsonString(Arrays.asList(1,2,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer aa"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void creerNouvelleSequenceTableauVideFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "creerSequence.indexes: Au moins 1 jeton nécessaire");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		String argument = asJsonString(new ArrayList<Integer>());

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer aa"))
									.andExpect(status().isBadRequest())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);

	}

	@Test
	public void ajouterJetonTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 2, "Katya", "9bleu", 2, "10bleu 11bleu 12bleu\n4jaune", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJeton(2, Arrays.asList(1,2))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		ParametresAction param = new ParametresAction();
		param.paramAjouterJeton(1,2);
		String argument = asJsonString(param);
		MvcResult resultat = mockMvc.perform(post("/1/2/ajouterJeton")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void fusionnerSequenceTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.fusionnerSequence(2, Arrays.asList(1,4))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		ParametresAction param = new ParametresAction();
		param.paramFusionner(1,4);
		String argument = asJsonString(param);

		MvcResult resultat = mockMvc.perform(post("/1/2/fusionnerSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isOk())
									.andReturn();

		String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void couperSequenceTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 2, "Katya", "4jaune 9bleu", 2, "10bleu\n11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.couperSequence(2, Arrays.asList(1,2))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		ParametresAction param = new ParametresAction();
		param.paramCouper(1,2);
		String argument = asJsonString(param);

		MvcResult resultat = mockMvc.perform(post("/1/2/couperSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void deplacerJetonTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu\n12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.deplacerJeton(2, Arrays.asList(1,3,2))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		ParametresAction param = new ParametresAction();
		param.paramDeplacer(1,3,2);
		String argument = asJsonString(param);

		MvcResult resultat = mockMvc.perform(post("/1/2/deplacerJeton")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void remplacerJokerTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.remplacerJoker(2, Arrays.asList(2,1))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		ParametresAction param = new ParametresAction();
		param.paramRemplacerJoker(2,1);
		String argument = asJsonString(param);

		MvcResult resultat = mockMvc.perform(post("/1/2/remplacerJoker")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void annulerActionTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.annulerDerniereAction(2)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		MvcResult resultat = mockMvc.perform(post("/1/2/annulerDerniereAction")
									.contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void finDeTourTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
			1, 2, "Katya", "4jaune 9bleu 10bleu", 1, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.terminerTour(2)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		MvcResult resultat = mockMvc.perform(post("/1/2/terminerTour")
									.contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void finDePartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.FIN_DE_PARTIE,
			2, 1, "Vincent", "", 1, "10bleu 11bleu 12bleu 13bleu", "");
		when(listePartiesMock.getPartie(2)).thenReturn(partieMock);
		when(partieMock.terminerTour(1)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",2));

		mockMvc.perform(post("/2/1/terminerTour")
				.contentType("application/json")
				.header("Authorization", "Bearer aa"))
				.andExpect(status().isOk())
				.andReturn();

        messageTest = new MessagePartie(MessagePartie.TypeMessage.FIN_DE_PARTIE,
			0, 0, "", "", 0, "", "La partie est terminée");
		when(listePartiesMock.getPartie(2)).thenReturn(partieMock);
		when(listePartiesMock.isPartieSupprimee(2)).thenReturn(true);
		when(partieMock.afficherPartie(2)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",2));

		MvcResult resultat = mockMvc.perform(get("/2/2/afficherPartie")
									.contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isForbidden())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}

	@Test
	public void creerSequenceSansArgument() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "Argument manquant ou de mauvais type");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
			.content("").contentType("application/json")
			.header("Authorization", "Bearer aa"))
			.andExpect(status().isBadRequest())
			.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void creerSequenceMauvaisTypeArgument() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "Argument manquant ou de mauvais type");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		String argument = asJsonString("blabla");

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
			.content("").contentType("application/json")
			.header("Authorization", "Bearer aa"))
			.andExpect(status().isBadRequest())
			.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void ajouterJetonArgumentIncorrectTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "Argument incorrect");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		ParametresAction param = new ParametresAction();
		param.paramFusionner(1,2);
		String argument = asJsonString(param);

		MvcResult resultat = mockMvc.perform(post("/1/1/ajouterJeton")
			.content(argument).contentType("application/json")
			.header("Authorization", "Bearer aa"))
			.andExpect(status().isBadRequest())
			.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void creerSequenceFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			1, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 1, "", "Aucune séquence possible");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,3))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		String argument = asJsonString(Arrays.asList(1,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
			.content(argument).contentType("application/json")
			.header("Authorization", "Bearer aa"))
			.andExpect(status().isForbidden())
			.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}


	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
