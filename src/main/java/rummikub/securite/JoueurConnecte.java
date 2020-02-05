package rummikub.securite;

import lombok.Data;

@Data
public class JoueurConnecte {
    private int id;
    private int idPartie;
    private String nom;

	public JoueurConnecte() {
		id = 0;
		idPartie = 0;
		nom = "";
	}

	public JoueurConnecte(int id, String nom, int idPartie) {
		this.id = id;
		this.idPartie = idPartie;
		this.nom = nom;
	}
}
