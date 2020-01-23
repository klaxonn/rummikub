package rummikub.salon;

import lombok.Data;

/**
* Représentation d'un message envoyé pour le salon.
*/
@Data
public class MessageChat {
    private TypeMessage typeMessage;
    private String message;
    private String joueur;

    /**
	 * Constructeur par défaut.
	 */
    public MessageChat() {
	}

    /**
	 * Construit un message.
	 *
	 * @param typeMessage le type de message
	 * @param joueur le nom du joueur
	 * @param message le message
	 */
	public MessageChat(MessageChat.TypeMessage typeMessage, String joueur, String message){
        this.typeMessage = typeMessage;
        this.message = message;
        this.joueur = joueur;
	}

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
}

