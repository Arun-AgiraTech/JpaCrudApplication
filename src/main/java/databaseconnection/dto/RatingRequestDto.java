package databaseconnection.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RatingRequestDto {
    @NotNull
    @Size(min = 2, max = 10, message = "Should be between 2 and 10 characters")
    @Pattern(regexp = "^[a-zA-Z]*$",message = "Name must be of characters only")
    private String userName;
    @NotNull
    @Size(min = 1, max = 1, message = "Should be of 1 digit length")
    @Pattern(regexp = "^[1-5]$", message = "invalid rating format")
    private String star;
    @NotNull
    @Size(min = 2, max = 50, message = "Should be between 2 and 50 characters")
    private String review;
    private HotelRequestDto hotelRequestDto;
}
