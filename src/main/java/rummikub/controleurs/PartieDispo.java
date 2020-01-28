package rummikub.controleurs;

import java.util.List;
import lombok.Data;

@Data
public class PartieDispo {

	private int id;
	private List<String> listeNomsJoueurs;

	public PartieDispo(int id, List<String> listeNomsJoueurs) {
		this.id = id;
		this.listeNomsJoueurs = listeNomsJoueurs;
	}
}
