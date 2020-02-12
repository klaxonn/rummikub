package rummikub.securite;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rummikub.core.jeu.Joueur;

/**
 * Repésentation d'un joueur connecté.
 * Un joueur connecté est specifique à une partie et peut-être désactivé.
 */
@Data
@EqualsAndHashCode (callSuper = false)
public class JoueurConnecte extends Joueur {
    private int id;
    private int idPartie;
    private String nom;
    private String adresseIP;
    private boolean desactive = false;

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

	/**
	 * Détermine si le joueur est toujours membre de la partie.
	 *
	 * @return <code>true</code> si c'est le cas
	 */
	public boolean isDesactive() {
		return desactive;
	}

	/**
	 * Désactive le joueur.
	 * Il n'est plus membre de la partie.
	 */
	public void desactive() {
		desactive = true;
	}
}
