package rummikub.controleurs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ErreursControleurs{

  @ResponseBody
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String argumentIllegal(IllegalArgumentException ex) {
    return ex.getMessage() +"\n";
  }

  @ResponseBody
  @ExceptionHandler(IndexOutOfBoundsException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND )
  String argumentIllegal(IndexOutOfBoundsException ex) {
    return ex.getMessage() +"\n";
  }

  @ResponseBody
  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  String echecOperation(UnsupportedOperationException ex) {
    return ex.getMessage() +"\n";
  }
}
