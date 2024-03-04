package databaseconnection.exceptions;

public class HotelNotFoundException extends Exception {
    public HotelNotFoundException(String message, Long id,Throwable cause) {
        super(String.format(message + "%d", id),cause);
    }
}
