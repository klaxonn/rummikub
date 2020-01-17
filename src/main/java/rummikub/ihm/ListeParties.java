package rummikub.ihm;

import rummikub.core.api.Partie;
import rummikub.core.api.FabriquePartie;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("listeParties")
public class ListeParties {

	private Map<Integer, Partie> listeParties;

	public ListeParties() {
		listeParties = new HashMap<>();
	}

	public Partie creerPartie(List<String> listeNomsJoueurs) {
		Partie partie = FabriquePartie.creerNouvellePartie(listeNomsJoueurs);
		int indexNouvellePartie = listeParties.size() + 1;
		listeParties.put(indexNouvellePartie, partie);
		return partie;
	}

	public String listerPartiesDispos() {
		return "";
	}

	public Partie getPartie(int id) {
		return listeParties.get(id);
	}
}
