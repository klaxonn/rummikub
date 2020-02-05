package rummikub.controleurs;

import rummikub.core.api.Partie;
import rummikub.core.api.MessagePartie;
import rummikub.core.jeu.Joueur;
import rummikub.securite.ServiceJwt;
import rummikub.joueurs.JoueurConnecte;
import rummikub.joueurs.RepertoireJoueurConnecte;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.junit.jupiter.api.Disabled;

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
	private RepertoireJoueurConnecte repertoireJoueurConnecte;

	@MockBean
	private Partie partieMock;

	@MockBean
	private ServiceJwt serviceJwtMock;

	@Test
	public void creerPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.AJOUTER_JOUEUR,
			1, 1, "Vincent", "10bleu 11bleu 12bleu 13bleu", 0, "", "");
		when(listePartiesMock.creerPartie()).thenReturn(1);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(any(Joueur.class))).thenReturn(messageTest);
		when(partieMock.ajouterJoueur(any(Joueur.class))).thenReturn(messageTest);
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
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
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
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
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
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
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
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			1, 0, "", "", 0, "", "Partie Pleine");
		when(listePartiesMock.creerPartie()).thenReturn(1);
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.ajouterJoueur(any(Joueur.class))).thenReturn(messageTest);

		MvcResult resultat = mockMvc.perform(post("/1/ajouterJoueur")
										.content("Vincent").contentType("application/json"))
										.andExpect(status().isForbidden())
										.andReturn();

		String resultatTest = asJsonString(messageTest);
		JSONAssert.assertEquals(resultatTest, resultat.getResponse().getContentAsString(), false);
	}

	@Test
	public void demarrerPartieTest() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.DEBUT_NOUVEAU_TOUR,
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
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
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
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
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
	public void demarrerMauvaisJoueurFail() throws Exception {
		MessagePartie messageTest = new MessagePartie(MessagePartie.TypeMessage.ERREUR,
			0, 0, "", "", 0, "", "Vous ne pouvez pas démarrer la partie");
		when(listePartiesMock.getPartie(1)).thenReturn(partieMock);
		when(partieMock.commencerPartie(2)).thenReturn(messageTest);
		when(serviceJwtMock.parseToken("aa")).thenReturn(new JoueurConnecte(2,"Katya",1));

		MvcResult resultat = mockMvc.perform(post("/1/2/demarrerPartie")
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

		MvcResult resultat = mockMvc.perform(get("/0/listerPartiesDispos"))
										.andDo(print())
										.andExpect(status().isOk())
										.andReturn();
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

