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
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String argumentManquant(HttpMessageNotReadableException ex) {
		return "Argument manquant ou de mauvais type\n";
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String mauvaiseTailleTableau(ConstraintViolationException ex) {
		return ex.getMessage() +"\n";
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
