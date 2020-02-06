package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import static rummikub.core.api.MessagePartie.TypeMessage.*;
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
public class ControleurPartieTestSecurite {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ListeParties listePartiesMock;

	private Partie partieMock = mock(Partie.class);

	@MockBean
	private ServiceJwt serviceJwtMock;

	@Test
	public void creerNouvelleSequenceMauvaisJoueur() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Opération non autorisée");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,2,3))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("bb")).thenReturn(new JoueurConnecte(2,"Katya",1));

		String argument = asJsonString(Arrays.asList(1,2,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer bb"))
									.andExpect(status().isUnauthorized())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}

	@Test
	public void creerNouvelleSequenceMauvaisePartie() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Opération non autorisée");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,2,3))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",2));

		String argument = asJsonString(Arrays.asList(1,2,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer aa"))
									.andExpect(status().isUnauthorized())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}

	@Test
	public void creerNouvelleSequenceJoueurInconnu() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Opération non autorisée");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,2,3))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("cc")).thenReturn(null);

		String argument = asJsonString(Arrays.asList(1,2,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "Bearer cc"))
									.andExpect(status().isUnauthorized())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}

	@Test
	public void creerNouvelleSequenceMauvaisHeader() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Opération non autorisée");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,2,3))).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("cc")).thenReturn(null);

		String argument = asJsonString(Arrays.asList(1,2,3));

		MvcResult resultat = mockMvc.perform(post("/1/1/creerSequence")
									.content(argument).contentType("application/json")
									.header("Authorization", "cc"))
									.andExpect(status().isUnauthorized())
									.andReturn();

        String resultatTest = asJsonString(messageTest);
        JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}

	@Test
	public void afficherPartieJoueurSupprimeFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Opération non autorisée");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		JoueurConnecte joueur = new JoueurConnecte(1,"Vincent",1);
		joueur.desactive();
		when(serviceJwtMock.parseToken("aa")).thenReturn(joueur);

		MvcResult resultat = mockMvc.perform(get("/1/1/afficherPartie")
										.header("Authorization", "Bearer aa"))
										.andDo(print())
										.andExpect(status().isUnauthorized())
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
