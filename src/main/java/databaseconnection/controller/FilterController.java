package databaseconnection.controller;

import databaseconnection.dto.HotelRequestDto;
import databaseconnection.model.Hotel;
import databaseconnection.repository.HotelRepository;
import databaseconnection.service.FilterSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filter")
public class FilterController {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private FilterSpecificationService<Hotel> filterSpecificationService;
    @Autowired
    private FilterSpecificationService<Hotel> hotelSpecificationService;
    @GetMapping("/specification")
    public List<Hotel> getHotels(@RequestBody HotelRequestDto hotelRequestDto){
        Specification<Hotel> searchSpecification = filterSpecificationService.getSearchSpecification(hotelRequestDto.getSearchRequestDto(),hotelRequestDto.getGlobalOperator());
        return hotelSpecificationService.findSpecification(searchSpecification);
    }
}
