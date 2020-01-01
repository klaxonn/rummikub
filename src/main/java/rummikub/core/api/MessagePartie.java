package rummikub.core.api;

/**
* Représentation d'un message envoyé par Partie.
*/
public class MessagePartie {
    private TypeMessage typeMessage;
    private String nomJoueur;
	private String jeuJoueur;
    private String plateau;
	private String messageErreur;


	/**
	 * Types de messages possibles.
	 */
    public enum TypeMessage {
		DEBUT_PARTIE,
        RESULTAT_ACTION,
        DEBUT_NOUVEAU_TOUR,
		FIN_DE_PARTIE,
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
	 * Obtenir le message d'erreur.
	 * Chaine vide s'il n'y a pas d'erreur.
	 *
	 * @return le message
	 */
    public String getMessageErreur() {
        return messageErreur;
    }

	/**
	 * Définir le message d'erreur.
	 *
	 * @param messageErreur le message
	 */
    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

	/**
	 * Obtenir le nom du joueur.
	 *
	 * @return le nom
	 */
    public String getNomJoueur() {
        return nomJoueur;
    }

	/**
	 * Définir le nom du joueur.
	 *
	 * @param nomJoueur le nom
	 */
    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

	/**
	 * Obtenir la représentation textuelle des jetons du joueur.
	 *
	 * @return le jeu
	 */
    public String getJeuJoueur() {
        return jeuJoueur;
    }

	/**
	 * Définir le jeu (ensemble des jetons) du joueur.
	 *
	 * @param jeuJoueur le jeu
	 */
    public void setJeuJoueur(String jeuJoueur) {
        this.jeuJoueur = jeuJoueur;
    }

	/**
	 * Obtenir la représentation textuelle du plateau.
	 *
	 * @return le plateau
	 */
    public String getPlateau() {
        return plateau;
    }

	/**
	 * Définir le plateau.
	 *
	 * @param plateau le plateau
	 */
    public void setPlateau(String plateau) {
        this.plateau = plateau;
    }
}

