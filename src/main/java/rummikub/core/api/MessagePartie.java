package rummikub.core.api;

import lombok.Data;

/**
* Représentation d'un message envoyé par Partie.
*/
@Data
public class MessagePartie {
    private TypeMessage typeMessage;
    private int idPartie;
    private int idJoueur;
	private String nomJoueur;
	private String token;
	private String jeuJoueur;
    private int idJoueurCourant;
    private String plateau;
	private String messageErreur;

	/**
	 * Types de messages possibles.
	 */
    public enum TypeMessage {
		AJOUTER_JOUEUR,
		AFFICHER_PARTIE,
        RESULTAT_ACTION,
        DEBUT_NOUVEAU_TOUR,
		FIN_DE_PARTIE,
		ERREUR
    }

	/**
	 * Constructeur par défaut.
	 */
    public MessagePartie() {
        this.idPartie = 0;
        this.idJoueur = 0;
		this.nomJoueur = "";
		this.jeuJoueur = "";
		this.idJoueurCourant = 0;
        this.plateau = "";
        this.messageErreur = "";
        this.token = "";
	}

    /**
	 * Construit un message.
	 *
	 * @param typeMessage le type de message
	 * @param idPartie l'id de la partie
	 * @param idJoueur l'id du joueur
	 * @param nomJoueur le nom du joueur
	 * @param jeuJoueur la représentation textuelle du jeu du joueur
	 * @param idJoueurCourant l'id du joueur courant
	 * @param plateau la représentation textuelle du plateau
	 * @param messageErreur le message d'erreur
	 */
	public MessagePartie(MessagePartie.TypeMessage typeMessage, int idPartie, int idJoueur, String nomJoueur, String jeuJoueur,
						int idJoueurCourant, String plateau, String messageErreur){
        this.typeMessage = typeMessage;
		this.nomJoueur = nomJoueur;
		this.jeuJoueur = jeuJoueur;
		this.idJoueurCourant = idJoueurCourant;
        this.plateau = plateau;
        this.messageErreur = messageErreur;
        this.idJoueur = idJoueur;
        this.idPartie = idPartie;
        this.token = "";
	}
}

