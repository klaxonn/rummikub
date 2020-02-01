package rummikub.controleurs;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import javax.validation.ConstraintViolationException;
import rummikub.core.api.MessagePartie;

@ControllerAdvice
@RestController
class ErreursControleurs{

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<EntityModel> argumentManquant(HttpMessageNotReadableException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		message.setMessageErreur("Argument manquant ou de mauvais type");
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(java.lang.NullPointerException.class)
	ResponseEntity<EntityModel> argumentManquant(java.lang.NullPointerException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		message.setMessageErreur("Argument incorrect");
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	ResponseEntity<EntityModel> methodePasAuthorisee(HttpRequestMethodNotSupportedException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		String messageErreur = "Méthode " + ex.getMethod() + " non autorisée";
		message.setMessageErreur(messageErreur);
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<EntityModel> mauvaiseTailleTableau(ConstraintViolationException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		message.setMessageErreur(ex.getMessage());
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ControleurErreurException.class)
	ResponseEntity<EntityModel> erreurControleur(ControleurErreurException ex) {
		MessagePartie message = ex.getMessagePartie();
		if(message.getTypeMessage() == null) {
			message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		}
		EntityModel<MessagePartie> reponseAjout = ex.getCreateurModele().toModel(message);
		return new ResponseEntity<EntityModel>(reponseAjout, ex.getStatut());
	}
}
