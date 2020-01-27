package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

@WebMvcTest(ControleurPartie.class)
public class ControleurPartieTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ListeParties listePartiesMock;
	@MockBean
	private ModeleControleurPartie modeleControleurMock;

	private Partie partieMock = mock(Partie.class);

	@Test
	public void afficherPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.AFFICHER_PARTIE,
			0, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 1, "", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.afficherPartie(1)).thenReturn(messageTest);

		this.mockMvc.perform(get("/1/1/afficherPartie")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void afficherPartieFail() throws Exception {
		when(listePartiesMock.getPartie(1)).thenReturn(null);

		this.mockMvc.perform(get("/1/1/afficherPartie")).andExpect(status().isNotFound());
	}

	@Test
	public void creerNouvelleSequenceTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 1, "Vincent", "13bleu", 1, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,2,3))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(1,2,3));

		this.mockMvc.perform(post("/1/1/creerSequence").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isOk());
	}

	@Test
	public void ajouterJetonTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "9bleu", 2, "10bleu 11bleu 12bleu\n4jaune", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJeton(2, Arrays.asList(1,2))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(1,2));

		this.mockMvc.perform(post("/1/2/ajouterJeton").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isOk());
	}

	@Test
	public void fusionnerSequenceTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.fusionnerSequence(2, Arrays.asList(1,4))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(1,4));

		this.mockMvc.perform(post("/1/2/fusionnerSequence").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isOk());
	}

	@Test
	public void couperSequenceTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", 2, "10bleu\n11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.couperSequence(2, Arrays.asList(1,2))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(1,2));

		this.mockMvc.perform(post("/1/2/couperSequence").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isOk());
	}

	@Test
	public void deplacerJetonTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu\n12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.deplacerJeton(2, Arrays.asList(1,3,2))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(1,3,2));

		this.mockMvc.perform(post("/1/2/deplacerJeton").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isOk());
	}

	@Test
	public void remplacerJokerTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.remplacerJoker(2, Arrays.asList(2,1))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(2,1));

		this.mockMvc.perform(post("/1/2/remplacerJoker").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isOk());
	}

	@Test
	public void annulerActionTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 2, "Katya", "4jaune 9bleu", 2, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.annulerDerniereAction(2)).thenReturn(messageTest);

		this.mockMvc.perform(post("/1/2/annulerDerniereAction").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
	}

	@Test
	public void finDeTourTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
			0, 2, "Katya", "4jaune 9bleu 10bleu", 1, "10bleu 11bleu 12bleu", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.terminerTour(2)).thenReturn(messageTest);

		this.mockMvc.perform(post("/1/2/terminerTour").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
	}

	@Test
	public void creerSequenceSansArgument() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.RESULTAT_ACTION,
			0, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 1, "", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);

		this.mockMvc.perform(post("/1/1/creerSequence").contentType(MediaType.APPLICATION_JSON)
        .content(""))
        .andExpect(status().isBadRequest());
	}

	@Test
	public void creerSequenceMauvaisTypeArgument() throws Exception {
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);

		String argument = asJsonString("blabla");

		this.mockMvc.perform(post("/1/1/creerSequence").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isBadRequest());
	}

	@Test
	public void ajouterJetonTableauIncorrectTest() throws Exception {
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);

		String argument = asJsonString(Arrays.asList(1));

		this.mockMvc.perform(post("/1/1/ajouterJeton").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isBadRequest());
	}

	@Test
	public void creerSequenceFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 1, "", "Aucune séquence possible");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.creerNouvelleSequence(1, Arrays.asList(1,3))).thenReturn(messageTest);

		String argument = asJsonString(Arrays.asList(1,3));

		this.mockMvc.perform(post("/1/1/creerSequence").contentType(MediaType.APPLICATION_JSON)
        .content(argument))
        .andExpect(status().isForbidden());
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
