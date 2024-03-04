package databaseconnection.dto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class HotelRequestDto {
    @NotNull
    @Size(min = 2, max = 30, message = "name must be between 2 and 10 characters")
    @Pattern( regexp = "^[a-zA-Z]*$",message = "Name must be of characters only")
    private String name;
    @Size(min = 4, max = 30, message = "Address must be between 4 and 10 characters")
    private String address;
    @NotNull
    @Size(min = 13, max = 13, message = "number should be exactly 13 digits including +country code")
    @Pattern(regexp = "^\\+?[0-9\\s-]+$", message = "Invalid phone number format")
    private String contact;
    @Valid
    @NotEmpty(message = "Rating list cannot be empty")
    private List<RatingRequestDto> rating;
    private List<SearchRequestDto> searchRequestDto;
    private GlobalOperator globalOperator;
    public enum GlobalOperator{
        AND,OR;
    }
}
