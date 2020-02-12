package rummikub.controleurs;

import java.util.List;
import lombok.Data;

/**
 * Représente une partie qui n'a pas encore commencée.
 */
@Data
public class PartieDispo {

	private int id;
	private List<String> listeNomsJoueurs;

	/**
	 * Crée une partie disponible.
	 *
	 * @param id l'id de la partie
	 * @param listeNomsJoueurs la liste des noms des joueurs de la partie
	 */
	public PartieDispo(int id, List<String> listeNomsJoueurs) {
		this.id = id;
		this.listeNomsJoueurs = listeNomsJoueurs;
	}
}
