package rummikub.controleurs;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import javax.validation.ConstraintViolationException;
import rummikub.core.api.MessagePartie;

@ControllerAdvice
class ErreursControleurs{

	@ResponseBody
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<EntityModel> argumentManquant(HttpMessageNotReadableException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		message.setMessageErreur("Argument manquant ou de mauvais type");
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<EntityModel> mauvaiseTailleTableau(ConstraintViolationException ex) {
		MessagePartie message = new MessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		message.setMessageErreur(ex.getMessage());
		EntityModel<MessagePartie> reponseAjout = new EntityModel<>(message);
		return new ResponseEntity<EntityModel>(reponseAjout, HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler(ControleurErreurException.class)
	ResponseEntity<EntityModel> erreurControleur(ControleurErreurException ex) {
		MessagePartie message = ex.getMessagePartie();
		message.setTypeMessage(MessagePartie.TypeMessage.ERREUR);
		EntityModel<MessagePartie> reponseAjout = ex.getCreateurModele().toModel(message);
		return new ResponseEntity<EntityModel>(reponseAjout, ex.getStatut());
	}
}
