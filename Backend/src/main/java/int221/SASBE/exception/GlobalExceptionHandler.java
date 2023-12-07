package int221.SASBE.exception;

import com.sun.net.httpserver.Authenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

//
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseStatusException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(ex.getStatusCode().value(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(ex.getStatusCode().value()).body(er);
    }
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
//        String title = "sample title";
//        ErrorResponse er = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), title, request.getDescription(false));
//        for (ObjectError field : ex.getBindingResult().getAllErrors()) {
//            String fieldName = field.getCode();
//            if (field instanceof FieldError) fieldName = ((FieldError) field).getField();
//            er.addValidateError(fieldName, field.getDefaultMessage());
//
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
//    }
//
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<ErrorResponse> handlerForbiddenException(ResponseStatusException ex, WebRequest request) {
//        String title = "NOT PERMISSION";
//        ErrorResponse er = new ErrorResponse(HttpStatus.FORBIDDEN.value(), title, request.getDescription(false));
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(er);
//    }
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
//    public ResponseEntity<ErrorResponse> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex, WebRequest request) {
//        String title = "NOT PERMISSION";
//        ErrorResponse er = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), title, request.getDescription(false));
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
//    }
//

}