package rummikub.securite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rummikub.core.jeu.Joueur;


@Data
@EqualsAndHashCode (callSuper = false)
public class JoueurConnecte extends Joueur {
    private int id;
    private int idPartie;
    private String nom;
    private boolean desactive = false;

	public JoueurConnecte(String nom) {
		super(nom);
		id = 0;
		idPartie = 0;
		this.nom = nom;
	}

	public JoueurConnecte(int id, String nom, int idPartie) {
		super(nom);
		this.id = id;
		this.idPartie = idPartie;
		this.nom = nom;
	}

	public boolean isDesactive() {
		return desactive;
	}

	public void desactive() {
		desactive = true;
	}
}
