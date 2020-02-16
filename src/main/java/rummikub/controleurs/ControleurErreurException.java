package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;

/**
 * Exception traitant des erreurs concernant spécifiquement le jeu.
 */
public class ControleurErreurException extends RuntimeException {

	private final MessagePartie message;
	private final RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> createurModele;
	private final HttpStatus statut;

	/**
	 * Crée une exception.
	 *
	 * @param message le message contenant les informations de l'erreur
	 * @param createurModele le modèle à utiliser pour les liens HATEAOS
	 * @param statut le statut d'erreur à renvoyer
	 */
	public ControleurErreurException(MessagePartie message,
	  RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>>  createurModele,
	  HttpStatus statut) {
		super(message.getMessageErreur());
		this.message = message;
		this.createurModele = createurModele;
		this.statut = statut;
	}

	/**
	 * Retourne le message.
	 *
	 * @return le message
	 */
	public MessagePartie getMessagePartie() {
		return message;
	}

	/**
	 * Retourne le modèle.
	 *
	 * @return le modèle
	 */
	public RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> getCreateurModele() {
		return createurModele;
	}

	/**
	 * Retourne le statut d'erreur.
	 *
	 * @return le statut
	 */
	public HttpStatus getStatut() {
		return statut;
	}
}
