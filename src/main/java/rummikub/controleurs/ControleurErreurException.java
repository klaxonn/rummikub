package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;

public class ControleurErreurException extends RuntimeException {

	private final MessagePartie message;
	private final RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> createurModele;
	private final HttpStatus statut;

	public ControleurErreurException(MessagePartie message,
	  RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>>  createurModele,
	  HttpStatus statut) {
		super(message.getMessageErreur());
		this.message = message;
		this.createurModele = createurModele;
		this.statut = statut;
	}

	public MessagePartie getMessagePartie() {
		return message;
	}

	public RepresentationModelAssembler<MessagePartie, EntityModel<MessagePartie>> getCreateurModele() {
		return createurModele;
	}

	public HttpStatus getStatut() {
		return statut;
	}
}
