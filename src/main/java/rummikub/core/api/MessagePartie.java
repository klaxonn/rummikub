package rummikub.core.api;

public class MessagePartie {
    private TypeMessage typeMessage;
    private String nomJoueur;
	private String jeuJoueur;
    private String plateau;
	private String message;


    public enum TypeMessage {
		DEBUT_PARTIE,
        RESULTAT_ACTION,
        DEBUT_NOUVEAU_TOUR,
		FIN_DE_PARTIE,
		ERREUR
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

    public String getNomJoueur() {
        return nomJoueur;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public String getJeuJoueur() {
        return jeuJoueur;
    }

    public void setJeuJoueur(String jeuJoueur) {
        this.jeuJoueur = jeuJoueur;
    }

    public String getPlateau() {
        return plateau;
    }

    public void setPlateau(String plateau) {
        this.plateau = plateau;
    }
}

