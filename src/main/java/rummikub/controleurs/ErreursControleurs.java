package rummikub.controleurs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import javax.validation.ConstraintViolationException;

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
  @ExceptionHandler(IndexOutOfBoundsException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String mauvaisIndex(IndexOutOfBoundsException ex) {
    return ex.getMessage() +"\n";
  }

  @ResponseBody
  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  String echecOperation(UnsupportedOperationException ex) {
    return ex.getMessage() +"\n";
  }
}
