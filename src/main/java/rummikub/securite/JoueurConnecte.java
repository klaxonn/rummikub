package rummikub.securite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rummikub.core.jeu.Joueur;

/**
 * Repésentation d'un joueur connecté.
 * Un joueur connecté est specifique à une partie
 */
@Data
@EqualsAndHashCode (callSuper = false)
public class JoueurConnecte extends Joueur {
    private int id;
    private int idPartie;
    private String nom;
    private String adresseIP;

	/**
	 * Construit un joueur connecté.
	 *
	 * @param nom le nom du joueur
	 * @throws IllegalArgumentException si le nom n'est pas valide
	 */
	public JoueurConnecte(String nom) {
		super(nom);
		id = 0;
		idPartie = 0;
		this.nom = nom;
	}

	/**
	 * Construit un joueur connecté.
	 *
	 * @param id l'id du joueur
	 * @param nom le nom du joueur
	 * @param idPartie l'id de la partie dont fait partie le joueur
	 * @throws IllegalArgumentException si le nom n'est pas valide
	 */
	public JoueurConnecte(int id, String nom, int idPartie, String adresseIP) {
		super(nom);
		this.id = id;
		this.idPartie = idPartie;
		this.nom = nom;
		this.adresseIP = adresseIP;
	}
}
