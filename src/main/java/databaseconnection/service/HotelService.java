package databaseconnection.service;

import databaseconnection.dto.HotelResponseDto;
import databaseconnection.model.Hotel;
import databaseconnection.model.Rating;
import databaseconnection.repository.HotelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
    public Hotel getSingleHotel(Long id){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        return optionalHotel.orElse(null);
    }
    public List<HotelResponseDto> getAllHotels(Integer pageNumber, Integer pageSize){
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Hotel> hotels = this.hotelRepository.findAll(pageRequest);
        List<Hotel> hotelList = hotels.getContent();
        List<HotelResponseDto> hotelResponseDtoList = hotelList.stream().map(hotel -> {
            HotelResponseDto hotelResponseDto = new HotelResponseDto();
            hotelResponseDto.setId(hotel.getId());
            hotelResponseDto.setName(hotel.getName());
            hotelResponseDto.setAddress(hotel.getAddress());
            hotelResponseDto.setContact(hotel.getContact());
            hotelResponseDto.setRating(hotel.getRating());
            return hotelResponseDto;
        }).collect(Collectors.toList());
        return hotelResponseDtoList;
    }
    public Hotel createHotel(Hotel hotel){
        return hotelRepository.save(hotel);
    }
    public Hotel updateHotel(Long id,Hotel hotel){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (!optionalHotel.isPresent()){
            return null;
        }
        Hotel newHotel = optionalHotel.get();
        newHotel.setAddress(hotel.getAddress());
        newHotel.setName(hotel.getName());
        newHotel.setContact(hotel.getContact());
        newHotel.setRating(hotel.getRating());
        return hotelRepository.save(newHotel);
    }
    public Hotel deleteHotel(Long id){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (!optionalHotel.isPresent()) {
            return null;
        }
        Hotel hotel = optionalHotel.get();
        List<Rating> ratings = hotelRepository.findRatingByHotelId(id);
        if(ratings!=null) hotelRepository.deleteRatingsByHotelId(id);
        hotelRepository.delete(hotel);
        return hotel;
    }
    public List<Hotel> findHotelByName(String name){
        return hotelRepository.findHotelByName(name);
    }
    public List<Hotel> findByNameContaining(String name){
        return hotelRepository.findByNameContaining(name);
    }
    public List<Rating> findRatings(Long id){
        return hotelRepository.findRatingByHotelId(id);
    }
    public List<Rating> deleteRatingsByHotelID(Long id){
        List<Rating> ratings = hotelRepository.findRatingByHotelId(id);
        hotelRepository.deleteRatingsByHotelId(id);
        return ratings;
    }
    public List<Rating> findAllRatings(){
        List<Rating> ratings = hotelRepository.findAllRatings();
        return ratings;
    }
    public Map<List<HotelResponseDto>,List<Rating>> deleteAllRecords(){
        List<HotelResponseDto> hotels = getAllHotels(0,30);
        List<Rating> ratings = findAllRatings();
        Map<List<HotelResponseDto>,List<Rating>> map = new HashMap();
        map.put(hotels,ratings);
        hotelRepository.deleteAllHotels();
        hotelRepository.deleteAllRatings();
        return map;
    }
    public List<HotelResponseDto> sortHotels(Integer pageNumber,Integer pageSize,String sortBy,String type){
        Pageable pageable  = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        if(sortBy.equals("star")||sortBy.equals("userName")||sortBy.equals("review")||sortBy.equals("rid")) {
            if(sortBy.equals("userName")) {
                sortBy = "rating" + "." + "name";
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
            } else if(sortBy.equals("rid")) {
                sortBy = "rating" + "." + "id";
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
            } else {
                sortBy = "rating" + "." + sortBy;
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
            }
        }
        if(type.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            if(type.equals("desc")) {
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
            }
        }
        Page<Hotel> hotels = this.hotelRepository.findAll(pageable);
        List<Hotel> hotelList = hotels.getContent();
        List<HotelResponseDto> hotelResponseDtoList = hotelList.stream().map(hotel -> {
            HotelResponseDto hotelResponseDto = new HotelResponseDto();
            hotelResponseDto.setId(hotel.getId());
            hotelResponseDto.setName(hotel.getName());
            hotelResponseDto.setAddress(hotel.getAddress());
            hotelResponseDto.setContact(hotel.getContact());
            hotelResponseDto.setRating(hotel.getRating());
            return hotelResponseDto;
        }).collect(Collectors.toList());
        return hotelResponseDtoList;
    }
}
