package databaseconnection.controlleradvices;

import databaseconnection.dto.ExceptionDto;
import databaseconnection.exceptions.HotelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlers {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionDto> invalidEndpointRequestException(NoHandlerFoundException noHandlerFoundException) {
        return new ResponseEntity<>(
                new ExceptionDto(noHandlerFoundException.getClass().
                        getSimpleName()+" : "+noHandlerFoundException.getMessage()),HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ExceptionDto> HotelNotFoundException(HotelNotFoundException hotelNotFoundException) {
        return new ResponseEntity<>(
                new ExceptionDto(hotelNotFoundException.getClass().
                        getSimpleName()+" : "+hotelNotFoundException.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> runtimeException(RuntimeException runtimeException) {
        return new ResponseEntity<>(
                new ExceptionDto(runtimeException.getClass().
                        getSimpleName()+" : "+runtimeException.getMessage()),HttpStatus.NOT_FOUND);    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> methodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        return new ResponseEntity<>(
                new ExceptionDto(methodArgumentNotValidException.getClass().
                        getSimpleName()+" : "+methodArgumentNotValidException.getMessage()),HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> exception(Exception exception) {
        return new ResponseEntity<>(
                new ExceptionDto(exception.getClass().
                        getSimpleName()+" : "+exception.getMessage()),HttpStatus.NOT_FOUND);
    }
}
