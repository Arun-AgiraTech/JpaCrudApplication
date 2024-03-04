package databaseconnection.controller;

import databaseconnection.dto.HotelRequestDto;
import databaseconnection.dto.HotelResponseDto;
import databaseconnection.dto.RatingRequestDto;
import databaseconnection.exceptions.HotelNotFoundException;
import databaseconnection.model.Hotel;
import databaseconnection.model.Rating;
import databaseconnection.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotel")
@Validated
public class HotelController {
    HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDto> getSingleHotel(@PathVariable long id) throws HotelNotFoundException {
        if (hotelService.getSingleHotel(id) == null)
            throw new HotelNotFoundException("Invalid Hotel ID - ", id, null);
        Hotel hotel = hotelService.getSingleHotel(id);
        HotelResponseDto hotelResponseDto = new HotelResponseDto();
        hotelResponseDto.setId(hotel.getId());
        hotelResponseDto.setName(hotel.getName());
        hotelResponseDto.setAddress(hotel.getAddress());
        hotelResponseDto.setContact(hotel.getContact());
        hotelResponseDto.setRating(hotel.getRating());
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<HotelResponseDto>> sortHotels(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sort", defaultValue = "name", required = false) String sort,
            @RequestParam(value = "type", defaultValue = "asc", required = false) String type) {
        List<HotelResponseDto> hotelResponseDtoList = hotelService.sortHotels(pageNumber, pageSize, sort, type);
        return new ResponseEntity<>(hotelResponseDtoList, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<HotelResponseDto>> getAllHotels(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize) {
        List<HotelResponseDto> hotelResponseDtoList = hotelService.getAllHotels(pageNumber, pageSize);
        return new ResponseEntity<>(hotelResponseDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createHotel(@Valid @RequestBody HotelRequestDto hotelRequestDto, BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            map.put(ResponseEntity.badRequest().body("Validations error : " +
                    bindingResult.getAllErrors()).toString(), "VALIDATION ERROR");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<RatingRequestDto> ratingdtos = hotelRequestDto.getRating();
        List<Rating> ratings = ratingdtos.stream().map(ratingdto -> {
            Rating rating = new Rating();
            rating.setName(ratingdto.getUserName());
            rating.setStar(ratingdto.getStar());
            rating.setReview(ratingdto.getReview());
            return rating;
        }).collect(Collectors.toList());
        Hotel hotel = Hotel.builder()
                .name(hotelRequestDto.getName())
                .contact(hotelRequestDto.getContact())
                .address(hotelRequestDto.getAddress())
                .rating(ratings).build();
        hotelService.createHotel(hotel);
        HttpStatus httpStatus = HttpStatus.CREATED;
        String message = "Status";
        map.put(message, "HOTEL CREATED SUCCESSFULLY");
        return new ResponseEntity<>(map, httpStatus);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateHotel(@Valid @RequestBody HotelRequestDto hotelRequestDto, @PathVariable long id, BindingResult bindingResult) throws HotelNotFoundException {
        Map<String, String> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            map.put(ResponseEntity.badRequest().body("Validations error : " +
                    bindingResult.getAllErrors()).toString(), "VALIDATION ERROR");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (hotelService.getSingleHotel(id) == null) {
            throw new HotelNotFoundException("\"Hotel not found :(\"", id, null);
        }
        List<RatingRequestDto> ratingDtos = hotelRequestDto.getRating();
        List<Rating> ratings =
                ratingDtos.stream().map(ratingdto -> {
                    Rating rating = new Rating();
                    rating.setName(ratingdto.getUserName());
                    rating.setStar(ratingdto.getStar());
                    rating.setReview(ratingdto.getReview());
                    return rating;
                }).collect(Collectors.toList());
        Hotel hotel = Hotel.builder()
                .name(hotelRequestDto.getName())
                .contact(hotelRequestDto.getContact())
                .address(hotelRequestDto.getAddress())
                .rating(ratings).build();
        hotelService.updateHotel(id, hotel);
        String message = "status";
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        map.put(message, "UPDATED");
        return new ResponseEntity<>(map, httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteHotel(@PathVariable Long id) {
        Map<String, String> map = new HashMap<>();
        String message = "Status";
        HttpStatus httpStatus = HttpStatus.OK;
        if (hotelService.deleteHotel(id) == null) {
            httpStatus = HttpStatus.NOT_FOUND;
            map.put(message, "HOTEL NOT FOUND :(");
            return new ResponseEntity<>(map, httpStatus);
        }
        hotelService.deleteHotel(id);
        map.put(message, "HOTEL DELETED SUCCESSFULLY");
        httpStatus = HttpStatus.ACCEPTED;
        return new ResponseEntity<>(map, httpStatus);
    }

    @GetMapping("searchhotels/{name}")
    public ResponseEntity<List<HotelResponseDto>> searchHotelsByName(@PathVariable String name) {
        List<Hotel> hotels = hotelService.findByNameContaining(name);
        List<HotelResponseDto> hotelResponseDtoList = hotels.stream().map(hotel -> {
            HotelResponseDto hotelResponseDto = new HotelResponseDto();
            hotelResponseDto.setId(hotel.getId());
            hotelResponseDto.setName(hotel.getName());
            hotelResponseDto.setAddress(hotel.getAddress());
            hotelResponseDto.setContact(hotel.getContact());
            hotelResponseDto.setRating(hotel.getRating());
            return hotelResponseDto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(hotelResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("search/{name}")
    public ResponseEntity<List<HotelResponseDto>> searchHotelBName(@PathVariable String name) {
        List<Hotel> hotels = hotelService.findHotelByName(name);
        List<HotelResponseDto> hotelResponseDtoList = hotels.stream().map(hotel -> {
            HotelResponseDto hotelResponseDto = new HotelResponseDto();
            hotelResponseDto.setId(hotel.getId());
            hotelResponseDto.setName(hotel.getName());
            hotelResponseDto.setAddress(hotel.getAddress());
            hotelResponseDto.setContact(hotel.getContact());
            hotelResponseDto.setRating(hotel.getRating());
            return hotelResponseDto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(hotelResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("ratings")
    public ResponseEntity<List<Rating>> showAllRatings() {
        List<Rating> ratings = hotelService.findAllRatings();
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @GetMapping("rating/{id}")
    public ResponseEntity<List<Rating>> showRatingsByHotelId(@PathVariable Long id) throws HotelNotFoundException {
        List<Rating> ratings = hotelService.findRatings(id);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @GetMapping("ratingdelete/{id}")
    public ResponseEntity<List<Rating>> deleteRating(@PathVariable Long id) throws HotelNotFoundException {
        List<Rating> ratings = hotelService.deleteRatingsByHotelID(id);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @GetMapping("deleteall")
    public ResponseEntity<Map<List<HotelResponseDto>, List<Rating>>> deleteAllRecords() {
        Map<List<HotelResponseDto>, List<Rating>> allRecords = hotelService.deleteAllRecords();
        return new ResponseEntity<>(allRecords, HttpStatus.OK);
    }
}
