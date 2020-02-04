package rummikub.joueurs;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class JoueurConnecte {
    @Id
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
