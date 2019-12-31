package rummikub.web;

public class MessageChat {
    private TypeMessage typeMessage;
    private String message;
    private String joueur;

    public enum TypeMessage {
        MESSAGE_CHAT,
        CONNEXION,
		DECONNEXION,
		JOINDRE_PARTIE,
		CREER_PARTIE,
		DEMARRER_PARTIE
    }

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(TypeMessage typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJoueur() {
        return joueur;
    }

    public void setJoueur(String joueur) {
        this.joueur = joueur;
    }
}

