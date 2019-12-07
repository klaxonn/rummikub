package Rummikub.web;

public class Message {
    private TypeMessage typeMessage;
    private String message;
    private String joueur;

    public enum TypeMessage {
        CHAT,
        JOIN,
        LEAVE
    }

    public TypeMessage getType() {
        return typeMessage;
    }

    public void setType(TypeMessage typeMessage) {
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

