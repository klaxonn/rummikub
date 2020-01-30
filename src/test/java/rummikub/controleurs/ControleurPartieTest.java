package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.MediaTypes;
import org.skyscreamer.jsonassert.JSONAssert;
import java.nio.charset.Charset;

@Import({ ModeleControleurPartie.class })
@WebMvcTest(ControleurPartie.class)
public class ControleurPartieTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ListeParties listePartiesMock;

	private Partie partieMock = mock(Partie.class);

	@Test
	public void afficherPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.AFFICHER_PARTIE,
			1, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 1, "", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.afficherPartie(1)).thenReturn(messageTest);

		MvcResult resultat = mockMvc.perform(get("/1/1/afficherPartie"))
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

		MvcResult resultat = mockMvc.perform(get("/1/1/afficherPartie"))
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

		String argument = asJsonString(Arrays.asList(1,2,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
									.content(argument).contentType("application/json"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void ajouterJetonTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			1, 2, "Katya", "9bleu", 2, "10bleu 11bleu 12bleu\n4jaune", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJeton(2, Arrays.asList(1,2))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(1,2));

		MvcResult resultat = mockMvc.perform(post("/1/2/ajouterJeton")
									.content(argument).contentType("application/json"))
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

		String argument = asJsonString(Arrays.asList(1,4));

		MvcResult resultat = mockMvc.perform(post("/1/2/fusionnerSequence")
									.content(argument).contentType("application/json"))
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

		String argument = asJsonString(Arrays.asList(1,2));

		MvcResult resultat = mockMvc.perform(post("/1/2/couperSequence")
									.content(argument).contentType("application/json"))
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

		String argument = asJsonString(Arrays.asList(1,3,2));

		MvcResult resultat = mockMvc.perform(post("/1/2/deplacerJeton")
									.content(argument).contentType("application/json"))
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

		String argument = asJsonString(Arrays.asList(2,1));

		MvcResult resultat = mockMvc.perform(post("/1/2/remplacerJoker")
									.content(argument).contentType("application/json"))
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

		MvcResult resultat = mockMvc.perform(post("/1/2/annulerDerniereAction")
									.contentType("application/json"))
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

		MvcResult resultat = mockMvc.perform(post("/1/2/terminerTour")
									.contentType("application/json"))
									.andExpect(status().isOk())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void creerSequenceSansArgument() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "Argument manquant ou de mauvais type");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
			.content("").contentType("application/json"))
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

		String argument = asJsonString("blabla");

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
			.content("").contentType("application/json"))
			.andExpect(status().isBadRequest())
			.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void ajouterJetonTableauIncorrectTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "ajouterJeton.indexes: 2 valeurs attendues");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);

		String argument = asJsonString(Arrays.asList(1));

		MvcResult resultat = mockMvc.perform(post("/1/1/ajouterJeton")
			.content(argument).contentType("application/json"))
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

		String argument = asJsonString(Arrays.asList(1,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
			.content(argument).contentType("application/json"))
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
