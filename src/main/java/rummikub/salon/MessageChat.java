package rummikub.salon;

/**
* Représentation d'un message envoyé pour le salon.
*/
public class MessageChat {
    private TypeMessage typeMessage;
    private String message;
    private String joueur;

	/**
	 * Types de messages possibles.
	 */
    public enum TypeMessage {
        MESSAGE_CHAT,
        CONNEXION,
		DECONNEXION,
		JOINDRE_PARTIE,
		CREER_PARTIE,
		DEMARRER_PARTIE,
		ERREUR
    }

	/**
	 * Obtenir le type du message.
	 *
	 * @return le type du message
	 */

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }

	/**
	 * Définir le type du message.
	 *
	 * @param typeMessage le type de message
	 */
    public void setTypeMessage(TypeMessage typeMessage) {
        this.typeMessage = typeMessage;
    }

	/**
	 * Obtenir le message.
	 *
	 * @return le message
	 */
    public String getMessage() {
        return message;
    }

	/**
	 * Définir le message.
	 *
	 * @param message le message
	 */
    public void setMessage(String message) {
        this.message = message;
    }

	/**
	 * Obtenir le nom du joueur.
	 *
	 * @return le nom
	 */
    public String getJoueur() {
        return joueur;
    }

	/**
	 * Définir le nom du joueur.
	 *
	 * @param joueur le nom
	 */
    public void setJoueur(String joueur) {
        this.joueur = joueur;
    }
}

