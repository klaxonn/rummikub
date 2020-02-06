package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import static rummikub.core.api.MessagePartie.TypeMessage.*;
import rummikub.securite.ServiceJwt;
import rummikub.securite.JoueurConnecte;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import org.springframework.hateoas.MediaTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import java.nio.charset.Charset;

@Import({ 	ModeleControleurParties.class,
			ModeleAfficherParties.class,
			ModeleControleurPartie.class})
@WebMvcTest(ControleurParties.class)
public class ControleurPartiesTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ListeParties listePartiesMock;

	@MockBean
	private ListeJoueurs listeJoueursMock;

	@MockBean
	private Partie partieMock;

	@MockBean
	private ServiceJwt serviceJwtMock;

	@Test
	public void creerPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(AJOUTER_JOUEUR,
			1, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 0, "", "");
		when(listePartiesMock.creerPartie()).thenReturn(1);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(any(JoueurConnecte.class))).thenReturn(messageTest);
		when(serviceJwtMock.creerToken(any(JoueurConnecte.class))).thenReturn("aa");

		MvcResult resultat = mockMvc.perform(post("/0/creerPartie")
										.content("Vincent").contentType("application/json"))
										.andDo(print())
										.andExpect(status().isCreated())
										.andExpect(header().string("Authorization", "Bearer aa"))
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void ajouterJoueurMauvaisePartieFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "La partie n'existe pas");
		when(listePartiesMock.getPartie(1)).thenReturn(null);

		MvcResult resultat = mockMvc.perform(post("/1/ajouterJoueur")
										.content("Vincent").contentType("application/json"))
										.andExpect(status().isNotFound())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void ajouterJoueurMauvaiseMethode() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Méthode GET non autorisée");
		when(listePartiesMock.getPartie(1)).thenReturn(null);

		MvcResult resultat = mockMvc.perform(get("/1/ajouterJoueur")
										.content("Vincent").contentType("application/json"))
										.andExpect(status().isMethodNotAllowed())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}

	@Test
	public void ajouterJoueurJoueurInvalideFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Nom non valide");
		when(listePartiesMock.creerPartie()).thenReturn(1);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);

		MvcResult resultat = mockMvc.perform(post("/1/ajouterJoueur")
										.content("&é_ç").contentType("application/json"))
										.andExpect(status().isForbidden())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void ajouterJoueurTropdeJoueursFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			1, 0, "", "", 0, "", "Partie Pleine");
		when(listePartiesMock.creerPartie()).thenReturn(1);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(any(JoueurConnecte.class))).thenReturn(messageTest);

		MvcResult resultat = mockMvc.perform(post("/1/ajouterJoueur")
										.content("Vincent").contentType("application/json"))
										.andExpect(status().isForbidden())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void demarrerPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(DEBUT_NOUVEAU_TOUR,
			0, 0, "", "",  1, "", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.commencerPartie(1)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		MvcResult resultat = mockMvc.perform(post("/1/1/demarrerPartie")
										.contentType("application/json")
										.header("Authorization", "Bearer aa"))
										.andDo(print())
										.andExpect(status().isOk())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void demarrerPartieMauvaisePartieFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "La partie n'existe pas");
		when(listePartiesMock.getPartie(1)).thenReturn(null);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		MvcResult resultat = mockMvc.perform(post("/1/1/demarrerPartie")
										.contentType("application/json")
										.header("Authorization", "Bearer aa"))
										.andDo(print())
										.andExpect(status().isNotFound())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);

	}

	@Test
	public void demarrerPartieDemarreeFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "", 0, "", "Partie déjà commencée");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.commencerPartie(1)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		MvcResult resultat = mockMvc.perform(post("/1/1/demarrerPartie")
										.contentType("application/json")
										.header("Authorization", "Bearer aa"))
										.andExpect(status().isForbidden())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(Charset.defaultCharset()), false);
	}

	@Test
	public void listerPartiesDisposTest() throws Exception {
		List<PartieDispo> resultatList = new ArrayList<>();
		PartieDispo joueur1 = new PartieDispo(1, Arrays.asList("Vincent","Kate"));
		PartieDispo joueur2 = new PartieDispo(2, Arrays.asList("Benoit","Emie"));
		resultatList.add(joueur1);
		resultatList.add(joueur2);

		when(listePartiesMock.listerPartiesDispos()).thenReturn(resultatList);

		MvcResult resultat = mockMvc.perform(get("/0/listerPartiesDispos")
										.contentType("application/json"))
										.andDo(print())
										.andExpect(status().isOk())
										.andReturn();
	}

	@Test
	public void mauvaisTypeAccepteTest() throws Exception {
		MvcResult resultat = mockMvc.perform(get("/0/listerPartiesDispos")
										.contentType("application/json")
										.accept("text/plain"))
										.andDo(print())
										.andExpect(status().isNotAcceptable())
										.andReturn();

		assertEquals("Type accepté : application/json\n", resultat.getResponse().getContentAsString());
	}

	@Test
	public void quitterPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(RESULTAT_ACTION,
			0, 0, "", "",  0, "", "");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.quitterPartie(1)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		MvcResult resultat = mockMvc.perform(delete("/1/1/quitterPartie")
										.contentType("application/json")
										.header("Authorization", "Bearer aa"))
										.andDo(print())
										.andExpect(status().isOk())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void quitterPartiePasAssezJoueursFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "",  0, "", "Nombre de joueurs insuffisant");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.quitterPartie(1)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",1));

		MvcResult resultat = mockMvc.perform(delete("/1/1/quitterPartie")
										.contentType("application/json")
										.header("Authorization", "Bearer aa"))
										.andDo(print())
										.andExpect(status().isForbidden())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void quitterPartieMauvaisePartieFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(ERREUR,
			0, 0, "", "",  0, "", "La partie n'existe pas");
		when(listePartiesMock.getPartie(2)).thenReturn(null);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(1,"Vincent",2));

		MvcResult resultat = mockMvc.perform(delete("/2/1/quitterPartie")
										.contentType("application/json")
										.header("Authorization", "Bearer aa"))
										.andDo(print())
										.andExpect(status().isNotFound())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
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

