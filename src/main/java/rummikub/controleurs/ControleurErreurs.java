package rummikub.controleurs;

import rummikub.core.api.MessagePartie;
import static rummikub.core.api.MessagePartie.TypeMessage.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import javax.validation.ConstraintViolationException;
import javax.servlet.http.HttpServletResponse;

/**
 * Controleur qui s'occupe des erreurs pouvant survenir.
 */
@ControllerAdvice
@RestController
class ErreursControleurs {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<EntityModel> argumentManquant(HttpMessageNotReadableException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(ERREUR);
		message.setMessageErreur("Argument manquant ou de mauvais type");
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	String typeNonAccepte(HttpMediaTypeNotAcceptableException ex, HttpServletResponse reponse) {
		reponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		return "Type accepté : " + MediaType.APPLICATION_JSON_VALUE + "\n";
	}

	@ExceptionHandler(NullPointerException.class)
	ResponseEntity<EntityModel> argumentIncorrect(NullPointerException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(ERREUR);
		message.setMessageErreur("Argument incorrect");
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	ResponseEntity<EntityModel> methodePasAuthorisee(HttpRequestMethodNotSupportedException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(ERREUR);
		String messageErreur = "Méthode " + ex.getMethod() + " non autorisée";
		message.setMessageErreur(messageErreur);
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	ResponseEntity<EntityModel> methodePasAuthorisee(HttpMediaTypeNotSupportedException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(ERREUR);
		String messageErreur = "Type " + ex.getContentType() + " non autorisé";
		message.setMessageErreur(messageErreur);
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}


	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<EntityModel> mauvaiseTailleTableau(ConstraintViolationException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(ERREUR);
		message.setMessageErreur(ex.getMessage());
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ControleurErreurException.class)
	ResponseEntity<EntityModel> erreurControleur(ControleurErreurException ex) {
		MessagePartie message = ex.getMessagePartie();
		if(message.getTypeMessage() == null) {
			message.setTypeMessage(ERREUR);
		}
		EntityModel<MessagePartie> reponseAjout = ex.getCreateurModele().toModel(message);
		return new ResponseEntity<EntityModel>(reponseAjout, ex.getStatut());
	}
}
