package databaseconnection.dto;

import databaseconnection.model.Rating;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelResponseDto {
    private Long id;
    private String name;
    private String address;
    private String contact;
    private List<Rating> rating;
}
